package me.modmuss50.optifabric.patcher.fixes;

import me.modmuss50.optifabric.mod.OptifabricSetup;
import me.modmuss50.optifabric.util.RemappingUtils;

import java.util.*;

public class OptifineFixer {

	public static final OptifineFixer INSTANCE = new OptifineFixer();

	private final Map<String, List<ClassFixer>> classFixes = new HashMap<>();
	private final Set<String> skippedClass = new HashSet<>();

	private OptifineFixer() {
		if (OptifabricSetup.isPresent("smoothbeta")) {
//			registerFix("class_471", new SmoothWorldRendererFix());
			skipClass("class_471");
		}
	}

	private void registerFix(String className, ClassFixer classFixer) {
		classFixes.computeIfAbsent(RemappingUtils.getClassName(className), s -> new ArrayList<>()).add(classFixer);
	}

	@SuppressWarnings("SameParameterValue") //Might be useful in future
	private void skipClass(String className) {
		skippedClass.add(RemappingUtils.getClassName(className));
	}

	public boolean shouldSkip(String className) {
		return skippedClass.contains(className);
	}

	public List<ClassFixer> getFixers(String className) {
		return classFixes.getOrDefault(className, Collections.emptyList());
	}
}