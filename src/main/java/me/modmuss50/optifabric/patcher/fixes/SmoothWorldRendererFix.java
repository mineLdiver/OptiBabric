package me.modmuss50.optifabric.patcher.fixes;

import me.modmuss50.optifabric.util.RemappingUtils;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class SmoothWorldRendererFix implements ClassFixer {
    @Override
    public void fix(ClassNode optifine, ClassNode minecraft) {
        String method_1542Desc = "(IIID)I"; // int (int, int, int, double)
        String method_1542 = RemappingUtils.getMethodName("class_471", "method_1542", method_1542Desc); // WorldRenderer, method_1542
        method_1542Desc = RemappingUtils.mapMethodDescriptor(method_1542Desc);
        String method_1540Desc = "(ID)V"; // (int, double)
        String method_1540 = RemappingUtils.getMethodName("class_471", "method_1540", method_1540Desc); // WorldRenderer, method_1540
        method_1540Desc = RemappingUtils.mapMethodDescriptor(method_1540Desc);

        MethodNode method_1542Mc = null;
        MethodNode method_1540Mc = null;
        for (MethodNode method : minecraft.methods) {
            if (method_1542.equals(method.name) && method_1542Desc.equals(method.desc))
                method_1542Mc = method;
            else if (method_1540.equals(method.name) && method_1540Desc.equals(method.desc))
                method_1540Mc = method;
        }
        if (method_1542Mc == null || method_1540Mc == null)
            throw new RuntimeException();

        int method_1542OfIndex = -1;
        int method_1540OfIndex = -1;
        for (int i = 0; i < optifine.methods.size(); i++) {
            MethodNode method = optifine.methods.get(i);
            if (method_1542.equals(method.name) && method_1542Desc.equals(method.desc))
                method_1542OfIndex = i;
            else if (method_1540.equals(method.name) && method_1540Desc.equals(method.desc))
                method_1540OfIndex = i;
        }
        if (method_1542OfIndex < 0 || method_1540OfIndex < 0)
            throw new RuntimeException();

        optifine.methods.set(method_1542OfIndex, method_1542Mc);
        optifine.methods.set(method_1540OfIndex, method_1540Mc);
    }
}
