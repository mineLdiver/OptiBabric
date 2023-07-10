package me.modmuss50.optifabric.compat.bnb.mixin;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;
import net.minecraft.sortme.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
@InterceptingMixin("paulevs/bnb/mixin/client/GameRendererMixin")
class GameRendererMixin {
    @Inject(
            method = "method_1842",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glFogf(IF)V",
                    ordinal = 4,
                    shift = At.Shift.AFTER
            )
    )
    private void optifabric_changeNetherFog(int i, float par2, CallbackInfo info) {
        bnb_changeNetherFog(i, par2, info);
    }

    @Shim
    private native void bnb_changeNetherFog(int i, float par2, CallbackInfo info);
}
