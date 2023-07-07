package me.modmuss50.optifabric.compat.stationrendererapi.mixin;

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

// TODO: come up with a better quad() method
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
//    private void optifabric_quad_fixArraycopy(Object src, int srcPos, Object dest, int destPos, int length) {
//        ((TessellatorAccessor) self).getIntBuffer().put((int[]) src, srcPos, length);
//    }

    @Shadow @Final private TessellatorAccessor access;

    @Shadow @Final private Vector4f damageUV;

    @Mutable
    @Unique
    private @Final TessellatorOF optifabric_selfOF;

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void optifabric_init(Tessellator tessellator, CallbackInfo ci) {
        optifabric_selfOF = (TessellatorOF) self;
    }

    /**
     * @author mine_diver
     * @reason temporary workaround
     */
    @Overwrite
    public void quad(BakedQuad quad, float x, float y, float z, int colour0, int colour1, int colour2, int colour3, float normalX, float normalY, float normalZ, boolean spreadUV) {
        int[] data = quad.getVertexData();
        int[] colors = new int[] { colour0, colour1, colour2, colour3 };
        if (!optifabric_selfOF.optifabric_isRenderingChunk())
            self.setNormal(normalX, normalY, normalZ);
        Direction facing = quad.getFace();
        Matrix4f texture = Matrix4f.translateTmp((float) access.getXOffset(), (float) access.getYOffset(), (float) access.getZOffset());
        texture.invert();
        for (int i = 0; i < 32; i+=8) {
            float vx = x + Float.intBitsToFloat(data[i]), vy = y + Float.intBitsToFloat(data[i + 1]), vz = z + Float.intBitsToFloat(data[i + 2]);
            float u, v;
            if (spreadUV) {
                damageUV.set((float) (vx + access.getXOffset()), (float) (vy + access.getYOffset()), (float) (vz + access.getZOffset()), 1.0F);
                damageUV.transform(texture);
                damageUV.rotate(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
                damageUV.rotate(Vec3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
                damageUV.rotate(facing.getRotationQuaternion());
                u = -damageUV.getX();
                v = -damageUV.getY();
            } else {
                u = Float.intBitsToFloat(data[i + 3]);
                v = Float.intBitsToFloat(data[i + 4]);
            }
            self.colour(colors[i / 8]);
            self.vertex(vx, vy, vz, u, v);
        }
    }
}
