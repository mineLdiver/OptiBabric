package me.modmuss50.optifabric.api.util;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.fabricmc.tinyremapper.IMappingProvider.Member;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemappingUtils {

	private static final MappingResolver RESOLVER = FabricLoader.getInstance().getMappingResolver();
	private static final String INTERMEDIARY = "intermediary";
	private static final Pattern CLASS_FINDER = Pattern.compile("Lnet/minecraft/([^;]+);");

	public static boolean hasClassName(String className) {
		if (!INTERMEDIARY.equals(RESOLVER.getCurrentRuntimeNamespace())) {
			className = fromIntermediaryDot(className);
		} else {
			className = "net.minecraft.".concat(className);
		}

		return !className.equals(RESOLVER.unmapClassName("official", className));
	}

	public static String getClassName(String className) {
		return fromIntermediaryDot(className).replace('.', '/');
	}

	private static String fromIntermediaryDot(String className) {
		return RESOLVER.mapClassName(INTERMEDIARY, "net.minecraft." + className);
	}

	public static Member mapMethod(String owner, String name, String desc) {
		return new Member(getClassName(owner), getMethodName(owner, name, desc), mapMethodDescriptor(desc));
	}

	public static String getMethodName(String owner, String methodName, String desc) {
		return RESOLVER.mapMethodName(INTERMEDIARY, "net.minecraft." + owner, methodName, desc);
	}

	public static String mapMethodDescriptor(String desc) {
		StringBuilder buf = new StringBuilder();

		Matcher matcher = CLASS_FINDER.matcher(desc);
		while (matcher.find()) {
			matcher.appendReplacement(buf, Matcher.quoteReplacement('L' + getClassName(matcher.group(1)) + ';'));
		}

		return matcher.appendTail(buf).toString();
	}

	public static String mapFieldName(String owner, String name, String desc) {
		return RESOLVER.mapFieldName(INTERMEDIARY, "net.minecraft.".concat(owner), name, desc);
	}
}