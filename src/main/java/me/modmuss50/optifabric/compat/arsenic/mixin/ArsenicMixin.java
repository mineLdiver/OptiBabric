package me.modmuss50.optifabric.compat.arsenic.mixin;

import net.modificationstation.stationapi.api.client.event.resource.TexturePackLoadedEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.impl.client.arsenic.Arsenic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Arsenic.class)
class ArsenicMixin {
    @Inject(
            method = "registerTextures",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void optifabric_disableArsenicBinders(TextureRegisterEvent event, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(
            method = "beforeTexturePackApplied",
            at = @At("RETURN"),
            remap = false
    )
    private static void optifabric_informOptifineOfClearedBinders(TexturePackLoadedEvent.Before event, CallbackInfo ci) {
        ((TextureManagerOF) event.textureManager).optifabric_setHdTexturesInstalled(false);
    }
}
