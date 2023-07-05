package me.modmuss50.optifabric.compat.glsl.mixin;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;
import me.modmuss50.optifabric.compat.glsl.WorldRendererCompat;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.sortme.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
@InterceptingMixin("net/mine_diver/glsl/mixin/MixinGameRenderer")
public class GameRendererMixin {
    @Redirect(
            method = "delta",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WorldRenderer;renderAllSortedRenderers(ID)I"
            )
    )
    private int optifabric_beginWater(WorldRenderer worldRenderer, int i, double var1) {
        beginWater(worldRenderer, i, var1);
        return ((WorldRendererCompat) worldRenderer).optifabric_getNum();
    }

    @Shim
    private native void beginWater(WorldRenderer worldRenderer, int i, double var1);
}
