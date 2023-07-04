package net.mine_diver.fabrifine.compat.stationapi.mixin;

import net.mine_diver.fabrifine.compat.stationapi.BakedModelRendererImplCompat;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.render.RendererAccess;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ArsenicItemRenderer.class)
public class ArsenicItemRendererMixin {
    @Redirect(
            method = "renderModel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;start()V",
                    ordinal = 0
            )
    )
    private void fabrifine_deferStart(Tessellator instance) {
        ((BakedModelRendererImplCompat) RendererAccess.INSTANCE.getRenderer().bakedModelRenderer()).deferItemStart();
    }
}
