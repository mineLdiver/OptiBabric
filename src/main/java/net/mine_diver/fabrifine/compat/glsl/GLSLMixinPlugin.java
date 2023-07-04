package net.mine_diver.fabrifine.compat.glsl;

import me.modmuss50.optifabric.compat.InterceptingMixinPlugin;
import me.modmuss50.optifabric.util.RemappingUtils;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;

import static org.objectweb.asm.Opcodes.*;

public class GLSLMixinPlugin extends InterceptingMixinPlugin {
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        switch (mixinInfo.getName()) {
            case "GameRendererMixin" -> {
                String deltaDesc = "(FJ)V"; // (float, long)
                String delta = RemappingUtils.getMethodName("class_555", "method_1841", deltaDesc); // GameRenderer, delta
                deltaDesc = RemappingUtils.mapMethodDescriptor(deltaDesc);
                for (MethodNode method : targetClass.methods) {
                    if (delta.equals(method.name) && deltaDesc.equals(method.desc)) {
                        InsnList extra = new InsnList();
                        LabelNode skip = new LabelNode();
                        String method_1540Desc = "(ID)V"; // (int, double)
                        String method_1540 = RemappingUtils.getMethodName("class_471", "method_1540", method_1540Desc); // WorldRenderer, method_1540
                        method_1540Desc = RemappingUtils.mapMethodDescriptor(method_1540Desc);
                        String worldRenderer = RemappingUtils.getClassName("class_471");

                        extra.add(new JumpInsnNode(GOTO, skip));
                        extra.add(new InsnNode(ACONST_NULL));
                        extra.add(new InsnNode(ICONST_0));
                        extra.add(new LdcInsnNode(0D));
                        extra.add(new MethodInsnNode(INVOKEVIRTUAL, worldRenderer, method_1540, method_1540Desc));
                        extra.add(skip);
                        method.instructions.insertBefore(method.instructions.getLast(), extra);
                    }
                }
            }
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

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        super.postApply(targetClassName, targetClass, mixinClassName, mixinInfo);
        switch (mixinInfo.getName()) {
            case "TessellatorMixin" -> {
                for (MethodNode method : targetClass.methods) {
                    if (method.visibleAnnotations != null) {
                        String mixinMerged = Type.getDescriptor(MixinMerged.class);
                        for (AnnotationNode annotation : method.visibleAnnotations) {
                            if (mixinMerged.equals(annotation.desc) && annotation.values != null) {
                                for (int i = 0; i < annotation.values.size(); i+=2) {
                                    String key = (String) annotation.values.get(i);
                                    if (
                                            "mixin".equals(key) && "net.mine_diver.glsl.mixin.MixinTessellator".equals(annotation.values.get(i + 1)) &&
                                                    method.name.startsWith("handler$") && method.name.endsWith("$onAddVertex")
                                    ) {
                                        method.instructions.clear();
                                        method.maxStack = 0;
                                        method.maxLocals = 0;
                                        method.instructions.add(new InsnNode(RETURN));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            case "BlockRendererMixin" -> {
                for (MethodNode method : targetClass.methods) {
                    if (method.visibleAnnotations != null) {
                        String mixinMerged = Type.getDescriptor(MixinMerged.class);
                        for (AnnotationNode annotation : method.visibleAnnotations) {
                            if (mixinMerged.equals(annotation.desc) && annotation.values != null) {
                                for (int i = 0; i < annotation.values.size(); i+=2) {
                                    String key = (String) annotation.values.get(i);
                                    if ("mixin".equals(key) && "net.mine_diver.glsl.mixin.MixinTileRenderer".equals(annotation.values.get(i + 1))) {
                                        method.instructions.clear();
                                        method.maxStack = 0;
                                        method.maxLocals = 0;
                                        method.instructions.add(new InsnNode(RETURN));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
