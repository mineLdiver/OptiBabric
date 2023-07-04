package net.mine_diver.fabrifine.compat.stationapi.mixin;

import net.mine_diver.fabrifine.compat.stationapi.BakedModelRendererImplCompat;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.render.RendererAccess;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicOverlayRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ArsenicOverlayRenderer.class)
public class ArsenicOverlayRendererMixin {
    @Redirect(
            method = "renderItem(F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;start()V",
                    ordinal = 1
            )
    )
    private void fabrifine_deferStart(Tessellator instance) {
        ((BakedModelRendererImplCompat) RendererAccess.INSTANCE.getRenderer().bakedModelRenderer()).deferItemStart();
    }

    @Redirect(
            method = "renderItem3D",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;start()V",
                    ordinal = 6
            )
    )
    private void fabrifine_deferStart3D(Tessellator instance) {
        ((BakedModelRendererImplCompat) RendererAccess.INSTANCE.getRenderer().bakedModelRenderer()).deferItemStart();
    }
}
