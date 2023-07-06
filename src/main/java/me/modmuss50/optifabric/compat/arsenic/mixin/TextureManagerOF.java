package me.modmuss50.optifabric.compat.arsenic.mixin;

import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TextureManager.class)
interface TextureManagerOF {
    @Accessor(
            value = "hdTexturesInstalled",
            remap = false
    )
    void optifabric_setHdTexturesInstalled(boolean hdTexturesInstalled);
}
