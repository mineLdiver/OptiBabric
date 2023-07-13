package me.modmuss50.optifabric.compat.smoothbeta.mixin;

import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.nio.IntBuffer;

@Mixin(Tessellator.class)
public class TessellatorMixin {
    @Redirect(
            method = "addVertex",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/nio/IntBuffer;put(I)Ljava/nio/IntBuffer;",
                    ordinal = 18
            )
    )
    private IntBuffer optifabric_preventExtraByteFromOptifine(IntBuffer instance, int i) {
        return instance;
    }
}
