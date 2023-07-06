package me.modmuss50.optifabric.compat.stationrendererapi.mixin;

import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.awt.*;

@Mixin(TextureManager.class)
interface TextureManagerOF {
    @Invoker(
            value = "setTextureDimension",
            remap = false
    )
    void optifabric_setTextureDimension(int id, Dimension dim);
}
