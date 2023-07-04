package me.modmuss50.optifabric.compat;

import me.modmuss50.optifabric.util.MixinUtils.Mixin;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.injection.Surrogate;
import org.spongepowered.asm.mixin.transformer.ClassInfo.Method;
import org.spongepowered.asm.util.Annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Iterator;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Retention(CLASS)
@Target(PARAMETER)
public @interface LoudCoerce {
	class CoercionApplicator {
		@Target(METHOD)
		@Retention(CLASS)
		private @interface CoercedInPlace {
		}

		public static void preApply(IMixinInfo mixinInfo, ClassNode targetClass) {
			Mixin thisMixin = Mixin.create(mixinInfo);

			on: for (MethodNode method : thisMixin.getClassNode().methods) {
				if (Annotations.getVisible(method, Surrogate.class) != null) {
					String coercedDesc = InterceptingMixinPlugin.coerceDesc(method);
					if (coercedDesc == null) continue; //Perfectly fine

					for (Method realMethod : thisMixin.getMethods()) {
						if (realMethod.getOriginalName().equals(method.name) && !method.desc.equals(realMethod.getOriginalDesc())) {
							Annotations.setInvisible(method, CoercedInPlace.class);
							method.name = realMethod.getName(); //Mangle name to whatever Mixin is using for the real injection
							method.desc = coercedDesc;

							targetClass.methods.add(method);
							continue on;
						}
					}

					throw new IllegalStateException("Cannot find original Mixin method for surrogate " + method.name + method.desc + " in " + thisMixin);	
				}
			}
		}

		public static void postApply(ClassNode targetClass) {
			for (Iterator<MethodNode> it = targetClass.methods.iterator(); it.hasNext();) {
				MethodNode method = it.next();

				if (Annotations.getInvisible(method, CoercedInPlace.class) != null) {
					it.remove();
				} else if (Annotations.getVisible(method, Surrogate.class) != null) {
					String coercedDesc = InterceptingMixinPlugin.coerceDesc(method);
					if (coercedDesc != null) method.desc = coercedDesc;
				}
			}
		}
	}

	String value();

	boolean remap() default true;
}