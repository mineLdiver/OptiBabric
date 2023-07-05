package me.modmuss50.optifabric.compat.arsenic.mixin;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRenderer.class)
@InterceptingMixin("net/modificationstation/stationapi/mixin/arsenic/client/block/LeverRendererMixin")
public class LeverRendererNewMixin {
    @Shim
    private native void stationapi_lever_captureTexture(
            BlockBase block, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            int var5, int var6, int var7, Tessellator var8, int var9, float var10, float var11, float var12, float var13, int texture
    );

    @PlacatingSurrogate
    private void stationapi_lever_captureTexture(
            BlockBase block, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            int var5, int var6, boolean var7, Tessellator var8, boolean var9, float var10, float var11, float var12, float var13, int texture
    ) {
        stationapi_lever_captureTexture(block, j, k, par4, cir, var5, var6, var7 ? 1 : 0, var8, var9 ? 1 : 0, var10, var11, var12, var13, texture);
    }
}
