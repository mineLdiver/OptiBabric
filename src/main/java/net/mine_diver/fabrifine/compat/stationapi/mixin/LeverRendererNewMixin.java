package net.mine_diver.fabrifine.compat.stationapi.mixin;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BlockRenderer.class)
@InterceptingMixin("net/modificationstation/stationapi/mixin/arsenic/client/block/LeverRendererMixin")
public class LeverRendererNewMixin {
    @Inject(
            method = "renderLever",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/render/block/BlockRenderer;textureOverride:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 2,
                    shift = At.Shift.BY,
                    by = 3
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void fabrifine_lever_captureTexture(
            BlockBase block, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            int var5, int var6, boolean var7, Tessellator var8, boolean var9, float var10, float var11, float var12, float var13, int texture
    ) {
        stationapi_lever_captureTexture(block, j, k, par4, cir, var5, var6, var7 ? 1 : 0, var8, var9 ? 1 : 0, var10, var11, var12, var13, texture);
    }

    @Shim
    private native void stationapi_lever_captureTexture(
            BlockBase block, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            int var5, int var6, int var7, Tessellator var8, int var9, float var10, float var11, float var12, float var13, int texture
    );

    @PlacatingSurrogate
    private void stationapi_lever_captureTexture(
            BlockBase block, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            int var5, int var6, boolean var7, Tessellator var8, boolean var9, float var10, float var11, float var12, float var13, int texture
    ) {}
}
