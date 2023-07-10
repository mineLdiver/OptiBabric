package me.modmuss50.optifabric.compat.bnb;

import me.modmuss50.optifabric.compat.InterceptingMixinPlugin;
import me.modmuss50.optifabric.util.RemappingUtils;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import static org.objectweb.asm.Opcodes.*;

public class BetterNetherBetaMixinPlugin extends InterceptingMixinPlugin {
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        switch (mixinInfo.getName()) {
            case "GameRendererMixin" -> {
                String method_1842Desc = "(IF)V"; // (int, float)
                String method_1842 = RemappingUtils.getMethodName("class_555", "method_1842", method_1842Desc); // GameRenderer, method_1842
                method_1842Desc = RemappingUtils.mapMethodDescriptor(method_1842Desc);
                for (MethodNode method : targetClass.methods) {
                    if (method_1842.equals(method.name) && method_1842Desc.equals(method.desc)) {
                        InsnList extra = new InsnList();
                        LabelNode skip = new LabelNode();

                        extra.add(new JumpInsnNode(GOTO, skip));
                        for (int i = 0; i < 3; i++) {
                            extra.add(new InsnNode(ICONST_0));
                            extra.add(new LdcInsnNode(0F));
                            extra.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(GL11.class), "glFogf", Type.getMethodDescriptor(Type.VOID_TYPE, Type.INT_TYPE, Type.FLOAT_TYPE)));
                        }
                        extra.add(skip);
                        method.instructions.insertBefore(method.instructions.getLast(), extra);
                    }
                }
            }
        }
        super.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
    }
}
