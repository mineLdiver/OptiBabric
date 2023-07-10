package me.modmuss50.optifabric.compat.arsenic.mixin;

import net.minecraft.sortme.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Redirect(
            method = "method_1844",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/optifine/Config;getIconWidthTerrain()I"
            )
    )
    private int optifabric_disableOptiFineHDItems() {
        return 16;
    }
}
