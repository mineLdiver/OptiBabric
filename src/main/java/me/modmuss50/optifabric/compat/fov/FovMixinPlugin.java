package me.modmuss50.optifabric.compat.fov;

import me.modmuss50.optifabric.api.mixin.InterceptingMixinPlugin;
import me.modmuss50.optifabric.api.util.RemappingUtils;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import static org.objectweb.asm.Opcodes.*;

public class FovMixinPlugin extends InterceptingMixinPlugin {
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        switch (mixinInfo.getName()) {
            case "OptionMixin" -> {
                String field_1113 = RemappingUtils.mapFieldName("class_271", "field_1113", "[Lnet/minecraft/class_271;"); // Option, field_1113
                String option = RemappingUtils.getClassName("class_271");
                for (MethodNode method : targetClass.methods) {
                    if ("<clinit>".equals(method.name)) {
                        InsnList extra = new InsnList();
                        LabelNode skip = new LabelNode();

                        extra.add(new JumpInsnNode(GOTO, skip));
                        extra.add(new InsnNode(ACONST_NULL));
                        extra.add(new FieldInsnNode(PUTSTATIC, option, field_1113, "[L" + option + ";"));
                        extra.add(skip);
                        method.instructions.insertBefore(method.instructions.getLast(), extra);
                    }
                }
            }
        }
        super.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
    }
}
