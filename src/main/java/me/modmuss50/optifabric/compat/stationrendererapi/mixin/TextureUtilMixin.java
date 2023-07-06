package me.modmuss50.optifabric.compat.stationrendererapi.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.client.texture.NativeImage;
import net.modificationstation.stationapi.api.client.texture.TextureUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(
        value = TextureUtil.class,
        remap = false
)
class TextureUtilMixin {
    @Inject(
            method = "prepareImage(Lnet/modificationstation/stationapi/api/client/texture/NativeImage$InternalFormat;IIII)V",
            at = @At("HEAD")
    )
    private static void optifabric_informOptifineOfDimensions(NativeImage.InternalFormat internalFormat, int id, int maxLevel, int width, int height, CallbackInfo ci) {
        //noinspection deprecation
        ((TextureManagerOF) ((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager).optifabric_setTextureDimension(id, new Dimension(width, height));
    }
}
