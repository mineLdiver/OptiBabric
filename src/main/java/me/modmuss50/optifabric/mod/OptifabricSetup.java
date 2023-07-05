package me.modmuss50.optifabric.mod;

import com.chocohead.mm.api.ClassTinkerers;
import me.modmuss50.optifabric.patcher.MethodComparison;
import me.modmuss50.optifabric.patcher.metadata.OptifineContainer;
import me.modmuss50.optifabric.patcher.metadata.OptifineIcon;
import me.modmuss50.optifabric.util.Edition;
import me.modmuss50.optifabric.util.MixinUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.fabricmc.loader.launch.common.FabricLauncherBase;
import net.fabricmc.mapping.tree.ClassDef;
import net.fabricmc.mapping.tree.FieldDef;
import net.fabricmc.mapping.tree.MethodDef;
import net.fabricmc.mapping.tree.TinyTree;
import net.fabricmc.tinyremapper.IMappingProvider;
import net.fabricmc.tinyremapper.OutputConsumerPath;
import net.fabricmc.tinyremapper.TinyRemapper;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.Mixins;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class OptifabricSetup implements Runnable {

    public static final Logger LOGGER = LogManager.getLogger("OptiFabric");
    public static final File WORK_DIR = FabricLoader.getInstance().getGameDir().resolve(".optifabric").toFile();
    static {
        if (!WORK_DIR.exists() && ! WORK_DIR.mkdirs()) throw new RuntimeException("Couldn't create " + WORK_DIR + "!");
    }
    public static Edition EDITION;
    public static String VERSION;

    @Override
    public void run() {
        File ofFile = getRemappedOptifine();
        File mcFile = getLaunchMinecraftJar().toFile();

        try {
            ClassTinkerers.addURL(ofFile.toURI().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        try {
            //noinspection unchecked
            ((List<ModContainer>) (List<? extends ModContainer>) FabricLoaderImpl.InitHelper.get().getModsInternal()).add(new OptifineContainer(ofFile.toPath(), Version.parse(VERSION)));
        } catch (VersionParsingException e) {
            throw new RuntimeException(e);
        }

        try (ZipFile ofZip = new ZipFile(ofFile); ZipFile mcZip = new ZipFile(mcFile)) {
            Enumeration<? extends ZipEntry> entries = ofZip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.endsWith(".class") && mcZip.getEntry(name) != null) {
                    String className = name.substring(0, name.length() - ".class".length());
                    ClassNode ofNode = new ClassNode();
                    new ClassReader(IOUtils.toByteArray(ofZip.getInputStream(entry))).accept(ofNode, ClassReader.EXPAND_FRAMES);
                    ClassTinkerers.addReplacement(className, mcNode -> {
                        ofNode.fields.forEach(ofField -> {
                            Optional<FieldNode> mcFieldNode = mcNode.fields.stream().filter(fieldNode -> ofField.name.equals(fieldNode.name)).findFirst();
                            boolean fieldPresent = mcFieldNode.isPresent();
                            if (fieldPresent) {
                                FieldNode mcField = mcFieldNode.get();
                                if (mcField.desc.equals(ofField.desc) && mcField.access == ofField.access) return;
                            }
                            boolean skipCheck = false;
                            if (fieldPresent) {
                                for (int i = 0; i < mcNode.fields.size(); i++)
                                    if (ofField.name.equals(mcNode.fields.get(i).name)) {
                                        mcNode.fields.set(i, ofField);
                                        return;
                                    }
                                LOGGER.warn("Couldn't find overwrite target field \"L" + ofNode.name + ";" + ofField.name + ":" + ofField.desc + "\"! Injecting instead.");
                                skipCheck = true;
                            }
                            if (skipCheck || mcNode.fields.stream().noneMatch(fieldNode -> ofField.name.equals(fieldNode.name))) {
                                mcNode.fields.add(ofField);
                                MixinUtils.addFieldInfo(mcNode, ofField);
                            }
                        });
                        for (MethodNode ofMethod : ofNode.methods) {
                            Optional<MethodNode> mcMethodOptional = mcNode.methods.stream().filter(mcMethod -> ofMethod.name.equals(mcMethod.name) && ofMethod.desc.equals(mcMethod.desc)).findFirst();
                            if (mcMethodOptional.isPresent()) {
                                MethodNode mcMethod = mcMethodOptional.get();
                                if (!new MethodComparison(mcMethod, ofMethod).effectivelyEqual) {
                                    mcNode.methods.remove(mcMethod);
                                    mcNode.methods.add(ofMethod);
                                }
                            } else {
                                mcNode.methods.add(ofMethod);
                                MixinUtils.addMethodInfo(mcNode, ofMethod);
                            }
                        }
                    });
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (isPresent("station-renderer-arsenic"))
            Mixins.addConfiguration("optifabric.compat.arsenic.mixins.json");
        if (isPresent("glsl"))
            Mixins.addConfiguration("optifabric.compat.glsl.mixins.json");
        if (isPresent("modmenu"))
            Mixins.addConfiguration("optifabric.compat.modmenu.mixins.json");
    }

    private static boolean isPresent(String modID) {
        return FabricLoader.getInstance().isModLoaded(modID);
    }

    private static File getRemappedOptifine() {
        File ofFile = findOptifine();
        File remappedOfFile = new File(WORK_DIR, "optifine-remapped.jar");
        if (!remappedOfFile.exists() || hasChanged(ofFile)) try {
            remappedOfFile.createNewFile();
            remap(ofFile, getLibs(getMinecraftJar()), remappedOfFile, out -> {
                try (ZipFile ofZip = new ZipFile(ofFile)) {
                    Enumeration<? extends ZipEntry> entries = ofZip.entries();
                    while (entries.hasMoreElements()) {
                        String name = entries.nextElement().getName();
                        if (name.endsWith(".class")) {
                            String className = name.substring(0, name.length() - ".class".length());
                            out.acceptClass(className, "net/optifine/" + className);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                createMappings("client", FabricLoader.getInstance().getMappingResolver().getCurrentRuntimeNamespace()).load(out);
            });
            addIcon(remappedOfFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return remappedOfFile;
    }

    private static void addIcon(File zipFile) {
        File tempFile;
        try {
            tempFile = File.createTempFile(zipFile.getName(), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tempFile.delete();

        boolean renameOk = zipFile.renameTo(tempFile);
        if (!renameOk)
            throw new RuntimeException("Could not rename the file " + zipFile.getAbsolutePath() + " to " + tempFile.getAbsolutePath());
        byte[] buf = new byte[1024];

        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile))) {
            try (ZipInputStream zin = new ZipInputStream(new FileInputStream(tempFile))) {
                ZipEntry entry = zin.getNextEntry();
                while (entry != null) {
                    String name = entry.getName();
                    out.putNextEntry(new ZipEntry(name));
                    int len;
                    while ((len = zin.read(buf)) > 0)
                        out.write(buf, 0, len);
                    entry = zin.getNextEntry();
                }
            }
            try (InputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(OptifineIcon.DATA))) {
                out.putNextEntry(new ZipEntry("assets/optifine/icon.png"));
                int len;
                while ((len = in.read(buf)) > 0)
                    out.write(buf, 0, len);
                out.closeEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tempFile.delete();
    }

    private static File findOptifine() {
        for (File file : Objects.requireNonNull(FabricLoader.getInstance().getGameDir().resolve("mods").toFile().listFiles((dir, name) -> new File(dir, name).isFile() && (name.endsWith(".zip") || name.endsWith(".jar"))))) {
            String name = file.getName();
            try (ZipFile zipFile = new ZipFile(file)) {
                ZipEntry entry = zipFile.getEntry("Config.class");
                if (entry != null) {
                    LOGGER.info("Found OptiFine in: " + name);
                    InputStream configStream = zipFile.getInputStream(entry);
                    ClassNode configNode = new ClassNode();
                    ClassReader configReader = new ClassReader(IOUtils.toByteArray(configStream));
                    configReader.accept(configNode, 0);
                    Edition.EditionAndVersion editionAndVersion = Edition.fromVersion((String) StreamSupport.stream(configNode.methods.stream().filter(methodNode -> "getVersion".equals(methodNode.name)).findFirst().orElseThrow(NullPointerException::new).instructions.spliterator(), false).filter(abstractInsnNode -> AbstractInsnNode.LDC_INSN == abstractInsnNode.getType()).map(abstractInsnNode -> (LdcInsnNode) abstractInsnNode).findFirst().orElseThrow(NullPointerException::new).cst);
                    EDITION = editionAndVersion.edition();
                    VERSION = editionAndVersion.version();
                    LOGGER.info("Edition detected: " + EDITION.name());
                    LOGGER.info("Version detected: " + VERSION);
                    return file;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Couldn't find OptiFine in mods folder!");
    }

    private static void remap(File input, Path[] libraries, File output, IMappingProvider mappings) throws IOException {
        remap(input.toPath(), libraries, output.toPath(), mappings);
    }

    private static void remap(Path input, Path[] libraries, Path output, IMappingProvider mappings) throws IOException {
        Files.deleteIfExists(output);

        TinyRemapper remapper = TinyRemapper.newRemapper().withMappings(mappings).renameInvalidLocals(FabricLoader.getInstance().isDevelopmentEnvironment()).rebuildSourceFilenames(true).fixPackageAccess(true).build();

        try (OutputConsumerPath outputConsumer = new OutputConsumerPath.Builder(output).assumeArchive(true).build()) {
            outputConsumer.addNonClassFiles(input);
            remapper.readInputs(input);
            remapper.readClassPath(libraries);

            remapper.apply(outputConsumer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to remap jar", e);
        } finally {
            remapper.finish();
        }
    }

    private static IMappingProvider createMappings(String from, String to) {
        //noinspection deprecation
        TinyTree normalMappings = FabricLauncherBase.getLauncher().getMappingConfiguration().getMappings();

        //In prod
        return out -> {
            for (ClassDef classDef : normalMappings.getClasses()) {
                String rawName;
                try {
                    rawName = classDef.getRawName(from);
                    if (rawName.isEmpty()) continue; // we don't need definitions from other namespaces, so skipping the "namespace--" loop is necessary
                } catch (ArrayIndexOutOfBoundsException ignored) {}
                String className = classDef.getName(from);
                out.acceptClass(className, classDef.getName(to));

                for (FieldDef field : classDef.getFields()) {
                    out.acceptField(new IMappingProvider.Member(className, field.getName(from), field.getDescriptor(from)), field.getName(to));
                }

                for (MethodDef method : classDef.getMethods()) {
                    out.acceptMethod(new IMappingProvider.Member(className, method.getName(from), method.getDescriptor(from)), method.getName(to));
                }
            }
        };
    }

    private static Path[] getLibs(Path minecraftJar) {
        //noinspection deprecation
        Path[] libs = FabricLauncherBase.getLauncher().getLoadTimeDependencies().stream().map(url -> {
            try {
                return Paths.get(url.toURI());
            } catch (URISyntaxException e) {
                throw new RuntimeException("Failed to convert " + url + " to path", e);
            }
        }).filter(Files::exists).toArray(Path[]::new);

        out: if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            Path launchJar = getLaunchMinecraftJar();

            for (int i = 0, end = libs.length; i < end; i++) {
                Path lib = libs[i];

                if (launchJar.equals(lib)) {
                    libs[i] = minecraftJar;
                    break out;
                }
            }

            //Can't find the launch jar apparently, remapping will go wrong if it is left in
            throw new IllegalStateException("Unable to find Minecraft jar (at " + launchJar + ") in classpath: " + Arrays.toString(libs));
        }

        return libs;
    }

    public static Path getMinecraftJar() {
        String givenJar = System.getProperty("optifabric.mc-jar");
        if (givenJar != null) {
            File givenJarFile = new File(givenJar);

            if (givenJarFile.exists()) {
                return givenJarFile.toPath();
            } else {
                System.err.println("Supplied Minecraft jar at " + givenJar + " doesn't exist, falling back");
            }
        }

        Path minecraftJar = getLaunchMinecraftJar();

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            Path officialNames = minecraftJar.resolveSibling(String.format("minecraft-%s-client.jar", "b1.7.3"));

            if (Files.notExists(officialNames)) {
                Path parent = minecraftJar.getParent().resolveSibling(String.format("minecraft-%s-client.jar", "b1.7.3"));

                if (Files.notExists(parent)) {
                    Path alternativeParent = parent.resolveSibling("minecraft-client.jar");

                    if (Files.notExists(alternativeParent)) {
                        throw new AssertionError("Unable to find Minecraft dev jar! Tried " + officialNames + ", " + parent + " and " + alternativeParent
                                + "\nPlease supply it explicitly with -Doptifabric.mc-jar");
                    }

                    parent = alternativeParent;
                }

                officialNames = parent;
            }

            minecraftJar = officialNames;
        }

        return minecraftJar;
    }

    public static Path getLaunchMinecraftJar() {
        ModContainer mod = FabricLoader.getInstance().getModContainer("minecraft").orElseThrow(() -> new IllegalStateException("No Minecraft?"));
        URI uri = mod.getRootPaths().get(0).toUri();
        assert "jar".equals(uri.getScheme());

        String path = uri.getSchemeSpecificPart();
        int split = path.lastIndexOf("!/");

        if (path.substring(0, split).indexOf(' ') > 0 && path.startsWith("file:///")) {//This is meant to be a URI...
            Path out = Paths.get(path.substring(8, split));
            if (Files.exists(out)) return out;
        }

        try {
            return Paths.get(new URI(path.substring(0, split)));
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to find Minecraft jar from " + uri + " (calculated " + path.substring(0, split) + ')', e);
        }
    }

    public static boolean hasChanged(File optifine) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try (InputStream stream = Files.newInputStream(optifine.toPath()); DigestInputStream digest = new DigestInputStream(stream, md)) {
            while (true) if (digest.read() == -1) break;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] md5 = md.digest();
        File md5File = new File(WORK_DIR, "optifine.md5");
        if (md5File.exists()) {
            try (InputStream md5Stream = Files.newInputStream(md5File.toPath())) {
                byte[] md5Existing = IOUtils.toByteArray(md5Stream);
                return !Arrays.equals(md5, md5Existing);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                if (!md5File.createNewFile()) throw new RuntimeException("Couldn't create " + md5File + "!");
                try (OutputStream stream = Files.newOutputStream(md5File.toPath())) {
                    stream.write(md5);
                    stream.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
    }
}
