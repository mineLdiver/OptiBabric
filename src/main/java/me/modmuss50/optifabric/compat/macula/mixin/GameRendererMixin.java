package me.modmuss50.optifabric.compat.macula.mixin;

import me.modmuss50.optifabric.api.mixin.InterceptingMixin;
import me.modmuss50.optifabric.api.mixin.Shim;
import net.minecraft.sortme.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
@InterceptingMixin("net/mine_diver/macula/mixin/GameRendererMixin")
class GameRendererMixin {
    @Inject(
            method = "delta",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WorldRenderer;renderAllSortedRenderers(ID)I",
                    ordinal = 0
            )
    )
    private void optifabric_injectWaterBegin1(float partialTicks, long n, CallbackInfo ci) {
        injectWaterBegin1(partialTicks, n, ci);
    }

    @Shim
    private native void injectWaterBegin1(float l, long par2, CallbackInfo ci);

    @Inject(
            method = "delta",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WorldRenderer;renderAllSortedRenderers(ID)I",
                    shift = At.Shift.AFTER,
                    ordinal = 0
            )
    )
    private void optifabric_injectWaterEnd1(float partialTicks, long n, CallbackInfo ci) {
        injectWaterEnd1(partialTicks, n, ci);
    }

    @Shim
    private native void injectWaterEnd1(float l, long par2, CallbackInfo ci);

    @Inject(
            method = "delta",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WorldRenderer;renderAllSortedRenderers(ID)I",
                    ordinal = 1
            )
    )
    private void optifabric_injectBeginWater3(float partialTicks, long n, CallbackInfo ci) {
        injectBeginWater3(partialTicks, n, ci);
    }

    @Shim
    private native void injectBeginWater3(float l, long par2, CallbackInfo ci);

    @Inject(
            method = "delta",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WorldRenderer;renderAllSortedRenderers(ID)I",
                    shift = At.Shift.AFTER,
                    ordinal = 1
            )
    )
    private void optifabric_injectEndWater3(float partialTicks, long n, CallbackInfo ci) {
        injectEndWater3(partialTicks, n, ci);
    }

    @Shim
    private native void injectEndWater3(float l, long par2, CallbackInfo ci);
}
