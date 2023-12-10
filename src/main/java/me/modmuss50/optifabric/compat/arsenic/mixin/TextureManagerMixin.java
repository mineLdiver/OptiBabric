package me.modmuss50.optifabric.compat.arsenic.mixin;

import me.modmuss50.optifabric.api.mixin.InterceptingMixin;
import me.modmuss50.optifabric.api.mixin.PlacatingSurrogate;
import me.modmuss50.optifabric.api.mixin.Shim;
import me.modmuss50.optifabric.compat.arsenic.ArsenicTextureManagerCompat;
import net.minecraft.client.render.TextureBinder;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.awt.*;

@Mixin(TextureManager.class)
@InterceptingMixin("net/modificationstation/stationapi/mixin/arsenic/client/TextureManagerMixin")
class TextureManagerMixin {
    @Inject(
            method = "tick",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=3553",
                    ordinal = 0
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void optifabric_tick_captureCurrentBinderOffsets(CallbackInfo ci, int i, TextureBinder texturefx, int tid, Dimension dim, int tileWidth, int tileHeight, boolean customOk, boolean fastColor, int ix, int iy) {
        stationapi_tick_captureCurrentBinderOffsets(ci, i, texturefx, ix, iy);
    }

    @Shim
    private native void stationapi_tick_captureCurrentBinderOffsets(
            CallbackInfo ci,
            int var1, TextureBinder var2, int var3, int var4
    );

    @PlacatingSurrogate
    private void stationapi_tick_captureCurrentBinderOffsets(
            CallbackInfo ci,
            int var1, TextureBinder var2, int tid, Dimension dim
    ) {}

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void optifabric_forceLegacyAtlases(CallbackInfo ci) {
        ArsenicTextureManagerCompat.forceCompatibility = true;
    }

    @Inject(
            method = "tick",
            at = @At("RETURN")
    )
    private void optifabric_hideLegacyAtlases(CallbackInfo ci) {
        ArsenicTextureManagerCompat.forceCompatibility = false;
    }
}
