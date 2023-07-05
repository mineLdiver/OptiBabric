package net.mine_diver.fabrifine.util;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class MixinHelper {

    private static final Field CLASSINFO_FIELDS;
    private static final Method CLASSINFO_ADDMETHOD;
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
}
