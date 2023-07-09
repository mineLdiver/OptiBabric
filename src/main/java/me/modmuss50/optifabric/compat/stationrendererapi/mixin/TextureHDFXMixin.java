package me.modmuss50.optifabric.compat.stationrendererapi.mixin;

import net.minecraft.client.render.TextureBinder;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.binder.StaticReferenceProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(
        targets = {
                "net.optifine.TextureHDCompassFX",
                "net.optifine.TextureHDFlamesFX",
                "net.optifine.TextureHDLavaFlowFX",
                "net.optifine.TextureHDLavaFX",
                "net.optifine.TextureHDPortalFX",
                "net.optifine.TextureHDWatchFX",
                "net.optifine.TextureHDWaterFlowFX",
                "net.optifine.TextureHDWatchFX",
                "net.optifine.TextureHDWaterFX",
        }
)
class TextureHDFXMixin extends TextureBinder implements StaticReferenceProvider {
    TextureHDFXMixin(int i) {
        super(i);
    }

    @Override
    public Atlas.Sprite getStaticReference() {
        return (switch (renderMode) {
            case 0 -> Atlases.getTerrain();
            case 1 -> Atlases.getGuiItems();
            default -> throw new IllegalStateException("Unexpected value: " + renderMode);
        }).getTexture(index);
    }
}
