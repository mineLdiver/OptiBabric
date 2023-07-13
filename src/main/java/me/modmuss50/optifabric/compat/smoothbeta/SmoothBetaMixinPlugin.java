package me.modmuss50.optifabric.compat.smoothbeta;

import me.modmuss50.optifabric.compat.InterceptingMixinPlugin;
import me.modmuss50.optifabric.util.RemappingUtils;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;

import static org.objectweb.asm.Opcodes.*;

public class SmoothBetaMixinPlugin extends InterceptingMixinPlugin {
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        switch (mixinInfo.getName()) {
//            case "WorldRendererMixin" -> {
//                String ctorDesc = "(Lnet/minecraft/client.Minecraft;Lnet/minecraft/class_76;)V"; // (Minecraft, TextureManager)
//                String ctor = "<init>";
//                ctorDesc = RemappingUtils.mapMethodDescriptor(ctorDesc);
//                for (MethodNode method : targetClass.methods) {
//                    if (ctor.equals(method.name) && ctorDesc.equals(method.desc)) {
//                        InsnList extra = new InsnList();
//                        LabelNode skip = new LabelNode();
//                        String renderList = RemappingUtils.getClassName("class_472");
//
//                        extra.add(new JumpInsnNode(GOTO, skip));
//                        extra.add(new TypeInsnNode(NEW, renderList));
//                        extra.add(new InsnNode(DUP));
//                        extra.add(new MethodInsnNode(INVOKESPECIAL, renderList, "<init>", "()V"));
//                        extra.add(skip);
//                        method.instructions.insertBefore(method.instructions.getFirst(), extra);
//                    }
//                }
//            }
            case "ChunkRendererMixin" -> {
                String method_296Desc = "()V"; // ()
                String method_296 = RemappingUtils.getMethodName("class_66", "method_296", method_296Desc); // ChunkRenderer, method_296
                method_296Desc = RemappingUtils.mapMethodDescriptor(method_296Desc);
                for (MethodNode method : targetClass.methods) {
                    if (method_296.equals(method.name) && method_296Desc.equals(method.desc)) {
                        InsnList extra = new InsnList();
                        LabelNode skip = new LabelNode();
                        String startDesc = "()V"; // ()
                        String start = RemappingUtils.getMethodName("class_67", "method_1695", startDesc); // Tessellator, start
                        startDesc = RemappingUtils.mapMethodDescriptor(startDesc);
                        String drawDesc = "()V"; // ()
                        String draw = RemappingUtils.getMethodName("class_67", "method_1685", startDesc); // Tessellator, draw
                        drawDesc = RemappingUtils.mapMethodDescriptor(drawDesc);
                        String tessellator = RemappingUtils.getClassName("class_67");

                        extra.add(new JumpInsnNode(GOTO, skip));
                        extra.add(new InsnNode(ACONST_NULL));
                        extra.add(new MethodInsnNode(INVOKEVIRTUAL, tessellator, start, startDesc));
                        extra.add(new InsnNode(ACONST_NULL));
                        extra.add(new MethodInsnNode(INVOKEVIRTUAL, tessellator, draw, drawDesc));
                        extra.add(skip);
                        method.instructions.insertBefore(method.instructions.getFirst(), extra);
                    }
                }
            }
        }
        super.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        switch (mixinInfo.getName()) {
            case "StationTessellatorImplMixin" -> {
                for (MethodNode method : targetClass.methods) {
                    if (method.visibleAnnotations != null) {
                        String mixinMerged = Type.getDescriptor(MixinMerged.class);
                        for (AnnotationNode annotation : method.visibleAnnotations) {
                            if (mixinMerged.equals(annotation.desc) && annotation.values != null) {
                                for (int i = 0; i < annotation.values.size(); i+=2) {
                                    String key = (String) annotation.values.get(i);
                                    if (
                                            "mixin".equals(key) && "net.mine_diver.smoothbeta.mixin.client.multidraw.compat.arsenic.StationTessellatorImplMixin".equals(annotation.values.get(i + 1)) &&
                                                    method.name.startsWith("handler$") && method.name.endsWith("$smoothbeta$renderTerrain")
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
        }
        super.postApply(targetClassName, targetClass, mixinClassName, mixinInfo);
    }
}
