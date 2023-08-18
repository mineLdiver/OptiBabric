package me.modmuss50.optifabric.compat.macula.smooth;

import me.modmuss50.optifabric.api.mixin.InterceptingMixinPlugin;
import me.modmuss50.optifabric.api.util.RemappingUtils;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import static org.objectweb.asm.Opcodes.*;

public class MaculaSmoothMixinPlugin extends InterceptingMixinPlugin {
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        switch (mixinInfo.getName()) {
            case "ChunkRendererMixin" -> {
                String method_296Desc = "()V"; // ()
                String method_296 = RemappingUtils.getMethodName("class_66", "method_296", method_296Desc); // ChunkRenderer, method_296
                method_296Desc = RemappingUtils.mapMethodDescriptor(method_296Desc);
                for (MethodNode method : targetClass.methods) {
                    if (method_296.equals(method.name) && method_296Desc.equals(method.desc)) {
                        InsnList extra = new InsnList();
                        LabelNode skip = new LabelNode();
                        String renderDesc = "(Lnet/minecraft/class_17;III)Z"; // boolean (Block, int, int, int)
                        String render = RemappingUtils.getMethodName("class_13", "method_57", renderDesc); // BlockRenderer, render
                        renderDesc = RemappingUtils.mapMethodDescriptor(renderDesc);
                        String blockRenderer = RemappingUtils.getClassName("class_13");

                        extra.add(new JumpInsnNode(GOTO, skip));
                        extra.add(new InsnNode(ACONST_NULL));
                        extra.add(new InsnNode(ACONST_NULL));
                        extra.add(new InsnNode(ICONST_0));
                        extra.add(new InsnNode(ICONST_0));
                        extra.add(new InsnNode(ICONST_0));
                        extra.add(new MethodInsnNode(INVOKEVIRTUAL, blockRenderer, render, renderDesc));
                        extra.add(new InsnNode(POP));
                        extra.add(skip);
                        method.instructions.insertBefore(method.instructions.getLast(), extra);
                    }
                }
            }
        }
        super.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
    }
}
