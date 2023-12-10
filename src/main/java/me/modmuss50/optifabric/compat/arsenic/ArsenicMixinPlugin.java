package me.modmuss50.optifabric.compat.arsenic;

import me.modmuss50.optifabric.api.mixin.InterceptingMixinPlugin;
import me.modmuss50.optifabric.api.util.RemappingUtils;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import static org.objectweb.asm.Opcodes.*;

public class ArsenicMixinPlugin extends InterceptingMixinPlugin {
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        switch (mixinInfo.getName()) {
            case "BedRendererMixin" -> {
                String renderBedDesc = "(Lnet/minecraft/class_17;III)Z"; // boolean (Block, int, int, int)
                String renderBed = RemappingUtils.getMethodName("class_13", "method_81", renderBedDesc); // BlockRenderer, renderBed
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
                        extra2.add(new InsnNode(POP));
                        extra2.add(skip2);
                        method.instructions.insertBefore(method.instructions.getLast(), extra2);
                    }
                }
            }
            case "TextRendererMixin" -> {
                String initDesc = "(Lnet/minecraft/class_322;Ljava/lang/String;Lnet/minecraft/class_76;)V"; // void (GameOptions, String, TextureManager)
                String init = RemappingUtils.getMethodName("class_34", "<init>", initDesc); // TextRenderer, <init>
                initDesc = RemappingUtils.mapMethodDescriptor(initDesc);
                for (MethodNode method : targetClass.methods) {
                    if (init.equals(method.name) && initDesc.equals(method.desc)) {
                        InsnList extra = new InsnList();
                        LabelNode skip = new LabelNode();

                        String tessellator = RemappingUtils.getClassName("class_67");

                        String instanceDesc = "Lnet/minecraft/class_67;";
                        String instance = RemappingUtils.mapFieldName("class_67", "field_2054", instanceDesc);

                        extra.add(new JumpInsnNode(GOTO, skip));
                        extra.add(new FieldInsnNode(GETSTATIC, tessellator, instance, "L" + tessellator + ";"));
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
