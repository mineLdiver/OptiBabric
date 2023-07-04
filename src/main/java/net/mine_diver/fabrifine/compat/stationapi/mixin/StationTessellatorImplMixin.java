package net.mine_diver.fabrifine.compat.stationapi.mixin;

import net.mine_diver.fabrifine.patcher.OptifineTessellator;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Matrix4f;
import net.modificationstation.stationapi.api.util.math.Vec3f;
import net.modificationstation.stationapi.api.util.math.Vector4f;
import net.modificationstation.stationapi.impl.client.render.StationTessellatorImpl;
import net.modificationstation.stationapi.mixin.render.client.TessellatorAccessor;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StationTessellatorImpl.class)
class StationTessellatorImplMixin {
    @Shadow @Final private Tessellator self;

//    @Redirect(
//            method = "quad",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Ljava/lang/System;arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V",
//                    remap = false
//            )
//    )
//    private void fabrifine_quad_fixArraycopy(Object src, int srcPos, Object dest, int destPos, int length) {
//        ((TessellatorAccessor) self).getIntBuffer().put((int[]) src, srcPos, length);
//    }

    @Shadow @Final private TessellatorAccessor access;

    @Shadow @Final private Vector4f damageUV;

    @Mutable
    @Unique
    private @Final OptifineTessellator fabrifine_selfOF;

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void fabrifine_init(Tessellator tessellator, CallbackInfo ci) {
        fabrifine_selfOF = (OptifineTessellator) self;
    }

    /**
     * @author mine_diver
     * @reason temporary workaround
     */
    @Overwrite
    public void quad(BakedQuad quad, float x, float y, float z, int colour0, int colour1, int colour2, int colour3, float normalX, float normalY, float normalZ, boolean spreadUV) {
        int[] data = quad.getVertexData();
        int[] colors = new int[] { colour0, colour1, colour2, colour3 };
        if (!fabrifine_selfOF.isRenderingChunk())
            self.setNormal(normalX, normalY, normalZ);
        Direction facing = quad.getFace();
        Matrix4f texture = Matrix4f.translateTmp((float) access.getXOffset(), (float) access.getYOffset(), (float) access.getZOffset());
        texture.invert();
        for (int i = 0; i < 32; i+=8) {
            float u, v;
            if (spreadUV) {
                damageUV.set(Float.intBitsToFloat(data[i]), Float.intBitsToFloat(data[i + 1]), Float.intBitsToFloat(data[i + 2]), 1.0F);
                damageUV.transform(texture);
                damageUV.rotate(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
                damageUV.rotate(Vec3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
                damageUV.rotate(facing.getRotationQuaternion());
                u = Float.floatToRawIntBits(-damageUV.getX());
                v = Float.floatToRawIntBits(-damageUV.getY());
            } else {
                u = Float.intBitsToFloat(data[i + 3]);
                v = Float.intBitsToFloat(data[i + 4]);
            }
            self.colour(colors[i / 8]);
            self.vertex(
                    x + Float.intBitsToFloat(data[i]),
                    y + Float.intBitsToFloat(data[i + 1]),
                    z + Float.intBitsToFloat(data[i + 2]),
                    u, v
            );
        }
    }
}
