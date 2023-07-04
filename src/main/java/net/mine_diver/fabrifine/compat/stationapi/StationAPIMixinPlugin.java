package net.mine_diver.fabrifine.compat.stationapi;

import me.modmuss50.optifabric.compat.InterceptingMixinPlugin;
import me.modmuss50.optifabric.util.RemappingUtils;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.objectweb.asm.Opcodes.*;

public class StationAPIMixinPlugin extends InterceptingMixinPlugin {
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        switch (mixinInfo.getName()) {
            case "BedRendererNewMixin" -> {
                String renderBedDesc = "(Lnet/minecraft/class_17;III)Z"; // boolean (Block, int, int, int)
                String renderBed = RemappingUtils.getMethodName("class_13", "method_81", renderBedDesc); //BlockRenderer, renderBed
                renderBedDesc = RemappingUtils.mapMethodDescriptor(renderBedDesc);
                for (MethodNode method : targetClass.methods) {
                    if (renderBed.equals(method.name) && renderBedDesc.equals(method.desc)) {
                        InsnList extra1 = new InsnList();
                        LabelNode fakeStart1 = new LabelNode();
                        LabelNode skip1 = new LabelNode();

                        extra1.add(new JumpInsnNode(GOTO, skip1));
                        extra1.add(fakeStart1);
                        extra1.add(new InsnNode(ICONST_0));
                        extra1.add(new VarInsnNode(ISTORE, 29));
                        extra1.add(new InsnNode(ICONST_0));
                        extra1.add(new VarInsnNode(ISTORE, 29));
                        extra1.add(skip1);
                        method.instructions.insertBefore(method.instructions.getFirst(), extra1);
                        method.localVariables.add(new LocalVariableNode("fakeTexture2Y", Type.INT_TYPE.getDescriptor(), null, fakeStart1, skip1, 29));

                        InsnList extra2 = new InsnList();
                        LabelNode skip2 = new LabelNode();

                        extra2.add(new JumpInsnNode(GOTO, skip2));
                        extra2.add(new LdcInsnNode(16));
//                        extra2.add(new MethodInsnNode(INVOKESTATIC, "net/mine_diver/fabrifine/fake/Consumers", "consume", Type.getMethodDescriptor(Type.VOID_TYPE, Type.INT_TYPE)));
                        extra2.add(new InsnNode(POP));
                        extra2.add(skip2);
                        method.instructions.insertBefore(method.instructions.getLast(), extra2);
                    }
                }
                ClassWriter writer = new ClassWriter(0);
                targetClass.accept(writer);
                Path debug = FabricLoader.getInstance().getGameDir().resolve("BlockRenderer.class");
                if (Files.notExists(debug)) {
                    try {
                        Files.createFile(debug);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                try (OutputStream out = Files.newOutputStream(debug)) {
                    out.write(writer.toByteArray());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        super.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
    }
}
