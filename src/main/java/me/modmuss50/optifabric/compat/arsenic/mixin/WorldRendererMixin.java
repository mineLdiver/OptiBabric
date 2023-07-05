package me.modmuss50.optifabric.compat.arsenic.mixin;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.hit.HitResult;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.render.model.VanillaBakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
class WorldRendererMixin {
    @Shadow private Level level;

    @Redirect(
            method = "method_1547",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;start()V"
            )
    )
    private void optifabric_startOnlyOnVanilla(Tessellator instance, PlayerBase arg, HitResult arg2, int i, ItemInstance arg3, float f) {
        if (StationRenderAPI.getBakedModelManager().getBlockModels().getModel(level.getBlockState(arg2.x, arg2.y, arg2.z)) instanceof VanillaBakedModel)
            instance.start();
    }
}
