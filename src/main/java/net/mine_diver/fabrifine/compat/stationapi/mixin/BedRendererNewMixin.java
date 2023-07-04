package net.mine_diver.fabrifine.compat.stationapi.mixin;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BlockRenderer.class)
@InterceptingMixin("net/modificationstation/stationapi/mixin/arsenic/client/block/BedRendererMixin")
public class BedRendererNewMixin {
    @Inject(
            method = "renderBed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getTextureForSide(Lnet/minecraft/level/BlockView;IIII)I",
                    ordinal = 0,
                    shift = At.Shift.BY,
                    by = 2
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void fabrifine_bed_captureTexture1(
            BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            Tessellator var5, int var6, int var7, boolean var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20, float var21, float var22, float var23, float var24, float var25, int texture1
    ) {
        stationapi_bed_captureTexture1(i, j, k, par4, cir, var5, var6, var7, var8 ? 1 : 0, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19, var20, var21, var22, var23, var24, var25, texture1);
    }

    @Shim
    private native void stationapi_bed_captureTexture1(
            BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            Tessellator var5, int var6, int var7, int var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20, float var21, float var22, float var23, float var24, float var25, int texture1
    );

    @PlacatingSurrogate
    private void stationapi_bed_captureTexture1(
            BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            Tessellator var5, int var6, int var7, boolean var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20, float var21, float var22, float var23, float var24, float var25, int texture1
    ) {}

    @Inject(
            method = "renderBed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getTextureForSide(Lnet/minecraft/level/BlockView;IIII)I",
                    ordinal = 1,
                    shift = At.Shift.BY,
                    by = 2
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void fabrifine_bed_captureTexture2(
            BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            Tessellator var5, int var6, int var7, boolean var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20, float var21, float var22, float var23, float var24, float var25, int var26, int texture2
    ) {
        stationapi_bed_captureTexture2(i, j, k, par4, cir, var5, var6, var7, var8 ? 1 : 0, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19, var20, var21, var22, var23, var24, var25, var26, texture2);
    }

    @Shim
    private native void stationapi_bed_captureTexture2(
            BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            Tessellator var5, int var6, int var7, int var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20, float var21, float var22, float var23, float var24, float var25, float var26, int texture2
    );

    @PlacatingSurrogate
    private void stationapi_bed_captureTexture2(
            BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            Tessellator var5, int var6, int var7, boolean var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20, float var21, float var22, float var23, float var24, float var25, int var26, int texture2
    ) {}

    @ModifyVariable(
            method = "renderBed",
            index = 29,
            at = @At(
                    value = "STORE",
                    ordinal = 1
            )
    )
    private double fabrifine_bed_modTexture2Y(double y) {
        return stationapi_bed_modTexture2Y((int) y);
    }

    @Shim
    private native int stationapi_bed_modTexture2Y(int y);

    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(doubleValue = 16)
    )
    private double fabrifine_bed_modTexture2Height(double height) {
        return stationapi_bed_modTexture2Height((int) height);
    }

    @Shim
    private native int stationapi_bed_modTexture2Height(int height);
}