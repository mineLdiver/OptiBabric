package me.modmuss50.optifabric.compat.arsenic.mixin;

import me.modmuss50.optifabric.compat.arsenic.BakedModelRendererImplCompat;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.render.RendererAccess;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ArsenicItemRenderer.class)
class ArsenicItemRendererMixin {
    @Redirect(
            method = "renderModel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;start()V",
                    ordinal = 0
            )
    )
    private void optifabric_deferStart(Tessellator instance) {
        ((BakedModelRendererImplCompat) RendererAccess.INSTANCE.getRenderer().bakedModelRenderer()).optifabric_deferItemStart();
    }
}
