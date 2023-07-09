package me.modmuss50.optifabric.compat.stationrendererapi.mixin;

import me.modmuss50.optifabric.compat.stationrendererapi.TextureManagerOF;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.awt.*;

@Mixin(TextureManager.class)
public abstract class TextureManagerMixin implements TextureManagerOF {
    @Dynamic
    @Shadow
    abstract void setTextureDimension(int id, Dimension dim);

    @Override
    @Unique
    public void optifabric_setTextureDimension(int id, Dimension dim) {
        setTextureDimension(id, dim);
    }
}
