package me.modmuss50.optifabric.compat.arsenic.mixin;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRenderer.class)
@InterceptingMixin("net/modificationstation/stationapi/mixin/arsenic/client/block/FluidRendererMixin")
class FluidRendererMixin {
    @Shim
    private native void stationapi_fluid_captureTexture1(
            BlockBase block, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            Tessellator var5, int var6, float var7, float var8, float var9, int var10, int var11, boolean[] var12, int var13, float var14, float var15, float var16, float var17, double var18, double var20, Material var22, int var23, float var24, float var25, float var26, float var27, int texture
    );

    @PlacatingSurrogate
    private void stationapi_fluid_captureTexture1(
            BlockBase block, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            Tessellator var5, int var6, float var7, float var8, float var9, boolean var10, boolean var11, boolean[] var12, boolean var13, float var14, float var15, float var16, float var17, double var18, double var20, Material var22, int var23, float var24, float var25, float var26, float var27, int texture
    ) {
        stationapi_fluid_captureTexture1(block, j, k, par4, cir, var5, var6, var7, var8, var9, var10 ? 1 : 0, var11 ? 1 : 0, var12, var13 ? 1 : 0, var14, var15, var16, var17, var18, var20, var22, var23, var24, var25, var26, var27, texture);
    }

    @Shim
    private native void stationapi_fluid_calculateAtlasSizeIndependentUV(
            BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            Tessellator var5, int var6, float var7, float var8, float var9, int var10, int var11, boolean[] var12, int var13, float var14, float var15, float var16, float var17, double var18, double var20, Material var22, int var23, float var24, float var25, float var26, float var27, int var28, float var29, int var30, int var31, double var32, double var34,
            float var36, float var37
    );

    @PlacatingSurrogate
    private void stationapi_fluid_calculateAtlasSizeIndependentUV(
            BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            Tessellator var5, int var6, float var7, float var8, float var9, boolean var10, boolean var11, boolean[] var12, boolean var13, float var14, float var15, float var16, float var17, double var18, double var20, Material var22, int var23, float var24, float var25, float var26, float var27, int var28, float var29, int var30, int var31, double var32, double var34,
            float var36, float var37
    ) {
        stationapi_fluid_calculateAtlasSizeIndependentUV(i, j, k, par4, cir, var5, var6, var7, var8, var9, var10 ? 1 : 0, var11 ? 1 : 0, var12, var13 ? 1 : 0, var14, var15, var16, var17, var18, var20, var22, var23, var24, var25, var26, var27, var28, var29, var30, var31, var32, var34, var36, var37);
    }

    @Shim
    private native void stationapi_fluid_captureTexture2(
            BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            Tessellator var5, int var6, float var7, float var8, float var9, int var10, int var11, boolean[] var12, int var13, float var14, float var15, float var16, float var17, double var18, double var20, Material var22, int var23, float var24, float var25, float var26, float var27, int var28, int var29, int var30, int var31, int texture
    );

    @PlacatingSurrogate
    private void stationapi_fluid_captureTexture2(
            BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            Tessellator var5, int var6, float var7, float var8, float var9, boolean var10, boolean var11, boolean[] var12, boolean var13, float var14, float var15, float var16, float var17, double var18, double var20, Material var22, int var23, float var24, float var25, float var26, float var27, int var28, int var29, int var30, int var31, int texture
    ) {
        stationapi_fluid_captureTexture2(i, j, k, par4, cir, var5, var6, var7, var8, var9, var10 ? 1 : 0, var11 ? 1 : 0, var12, var13 ? 1 : 0, var14, var15, var16, var17, var18, var20, var22, var23, var24, var25, var26, var27, var28, var29, var30, var31, texture);
    }
}
