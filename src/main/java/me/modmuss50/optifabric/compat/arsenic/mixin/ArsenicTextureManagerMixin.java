package me.modmuss50.optifabric.compat.arsenic.mixin;

import me.modmuss50.optifabric.compat.arsenic.ArsenicTextureManagerCompat;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArsenicTextureManager.class)
class ArsenicTextureManagerMixin {
    @SuppressWarnings("CancellableInjectionUsage")
    @Inject(
            method = "getTextureId",
            at = @At("HEAD"),
            cancellable = true
    )
    private void optifabric_forceLegacyAtlases(String par1, CallbackInfoReturnable<Integer> cir, CallbackInfo ci) {
        if (ArsenicTextureManagerCompat.forceCompatibility)
            ci.cancel();
    }
}
