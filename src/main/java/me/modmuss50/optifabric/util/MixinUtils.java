package me.modmuss50.optifabric.util;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo.Method;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MixinUtils {
    private static final Field CLASSINFO_FIELDS;
	private static final java.lang.reflect.Method CLASSINFO_ADDMETHOD;

	static {
		try {
			CLASSINFO_FIELDS = ClassInfo.class.getDeclaredField("fields");
			CLASSINFO_FIELDS.setAccessible(true);
			CLASSINFO_ADDMETHOD = ClassInfo.class.getDeclaredMethod("addMethod", MethodNode.class, boolean.class);
			CLASSINFO_ADDMETHOD.setAccessible(true);
		} catch (NoSuchMethodException | NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

    public static void addFieldInfo(ClassNode classNode, FieldNode fieldNode) {
        try {
            ClassInfo info = ClassInfo.forName(classNode.name);
            //noinspection unchecked
            ((Set<ClassInfo.Field>) CLASSINFO_FIELDS.get(info)).add(info.new Field(fieldNode));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addMethodInfo(ClassNode classNode, MethodNode methodNode) {
        try {
            CLASSINFO_ADDMETHOD.invoke(ClassInfo.forName(classNode.name), methodNode, false);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static class MixinInfo {
		private final IMixinConfig config;
		private final java.lang.reflect.Method getMixins;

		MixinInfo(IMixinConfig config) {
			this.config = config;
			getMixins = MethodUtils.getMatchingMethod(config.getClass(), "getMixinsFor", String.class);
			getMixins.setAccessible(true);
		}

		public boolean hasMixinFor(String target) {
			return config.getTargets().contains(target);
		}

		@SuppressWarnings("unchecked")
		public List<IMixinInfo> getMixinsFor(String target) {
			if (hasMixinFor(target)) {
				try {
					return (List<IMixinInfo>) getMixins.invoke(config, target);
				} catch (ReflectiveOperationException e) {
					throw new RuntimeException("Error getting " + target + " mixins from " + config, e);
				}
			} else {
				return Collections.emptyList();
			}
		}

		@Override
		public String toString() {
			return config.getName();
		}
	}

	@SuppressWarnings("unchecked") //It is but so is reflection in general
	private static List<IMixinConfig> goFish() throws ReflectiveOperationException {
		Object transformer = MixinEnvironment.getCurrentEnvironment().getActiveTransformer();
		if (transformer == null) throw new IllegalStateException("No active transformer?");

		Object processor = FieldUtils.readDeclaredField(transformer, "processor", true);
		assert processor != null; //Shouldn't manage to get it null

		Object configs = FieldUtils.readDeclaredField(processor, "configs", true);
		assert configs != null; //Shouldn't manage to be null either

		return (List<IMixinConfig>) configs;
	}

	private static final List<MixinInfo> MIXINS;
	static {
		try {
			MIXINS = goFish().stream().map(MixinInfo::new).collect(Collectors.toList());
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Error fishing for mixins", e);
		}
	}

	public static class Mixin implements Comparable<Mixin> {
		private final IMixinInfo mixin;
		private final ClassInfo info;
		private ClassNode node;

		public static Mixin create(IMixinInfo mixin) {
			return new Mixin(mixin);
		}

		Mixin(IMixinInfo mixin) {
			this.mixin = mixin;
			info = getInfo(mixin);
			assert info.isMixin();
		}

		private static ClassInfo getInfo(IMixinInfo mixin) {
			ClassInfo out = ClassInfo.fromCache(mixin.getClassName());
			if (out != null) return out;

			try {
				return (ClassInfo) FieldUtils.readDeclaredField(mixin, "info", true);
			} catch (ReflectiveOperationException e) {
				throw new RuntimeException("Error getting class info from " + mixin, e);
			}
		}

		public String getName() {
			return mixin.getClassRef();
		}

		public ClassNode getClassNode() {
			if (node == null) {
				node = mixin.getClassNode(ClassReader.SKIP_CODE);
			}
			
			return node;
		}

		public Set<ClassInfo.Method> getMethods() {
			return info.getMethods();
		}

		public boolean hasMethod(String name, String descriptor) {
			return getMethod(name, descriptor) != null;
		}

		public ClassInfo.Method getMethod(String name, String descriptor) {
			return info.findMethod(name, descriptor, ClassInfo.INCLUDE_ALL | ClassInfo.INCLUDE_INITIALISERS);
		}

		@Override
		@SuppressWarnings("unchecked") //The underlying type is comparable even if the interface isn't
		public int compareTo(Mixin other) {
			return ((Comparable<IMixinInfo>) mixin).compareTo(other.mixin);
		}

		@Override
		public String toString() {
			return getName();
		}
	}

	public static List<Mixin> getMixinsFor(String target) {
		return MIXINS.stream().filter(info -> info.hasMixinFor(target)).flatMap(info -> info.getMixinsFor(target).stream()).map(Mixin::new).sorted().collect(Collectors.toList());
	}

	public static void completeClassInfo(ClassInfo info, Iterable<MethodNode> methods) {
		Set<String> known = info.getMethods().stream().map(method -> method.getName().concat(method.getDesc())).collect(Collectors.toSet());
		List<Method> extra = new ArrayList<>();

		for (MethodNode method : methods) {
			if (!known.contains(method.name.concat(method.desc)) && method.name.charAt(0) != '<') {
				extra.add(info.new Method(method));
			}
		}

		if (!extra.isEmpty()) {
			try {
				@SuppressWarnings("unchecked") //We need to add to this...
				Set<Method> allMethods = (Set<Method>) FieldUtils.readDeclaredField(info, "methods", true);
				allMethods.addAll(extra);
			} catch (ReflectiveOperationException e) {
				throw new RuntimeException("Unable to add extra " + extra.size() + " methods to " + info + "'s class info", e);
			}
		}
	}
}