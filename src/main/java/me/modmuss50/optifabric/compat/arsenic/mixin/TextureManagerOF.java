package me.modmuss50.optifabric.compat.arsenic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(targets = "net.minecraft.client.texture.TextureManager")
interface TextureManagerOF {
    @SuppressWarnings("MixinAnnotationTarget")
    @Accessor(
            value = "hdTexturesInstalled",
            remap = false
    )
    void optifabric_setHdTexturesInstalled(boolean hdTexturesInstalled);
}
