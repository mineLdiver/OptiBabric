package me.modmuss50.optifabric.compat.arsenic.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.modmuss50.optifabric.api.mixin.InterceptingMixin;
import me.modmuss50.optifabric.api.mixin.Shim;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.BlockRenderer;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRenderer.class)
@InterceptingMixin("net/modificationstation/stationapi/mixin/arsenic/client/block/BedRendererMixin")
class BedRendererMixin {
    @Inject(
            method = "renderBed",
            at = @At("HEAD")
    )
    private void optifabric_bed_captureAtlas(
            BlockBase block, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            @Share("atlas") LocalRef<Atlas> atlas
    ) {
        atlas.set(block.getAtlas());
    }

    @Inject(
            method = "renderBed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getTextureForSide(Lnet/minecraft/level/BlockView;IIII)I",
                    ordinal = 0,
                    shift = At.Shift.BY,
                    by = 2
            )
    )
    private void optifabric_bed_captureTexture1(
            BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            @Local(index = 26) int texture1,
            @Share("atlas") LocalRef<Atlas> atlas, @Share("texture") LocalRef<Sprite> texture
    ) {
        texture.set(atlas.get().getTexture(texture1).getSprite());
    }

    @ModifyVariable(
            method = "renderBed",
            index = 29,
            at = @At(
                    value = "STORE",
                    ordinal = 1
            )
    )
    private double optifabric_bed_modTexture2Y(
            double y,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return stationapi_bed_modTexture2Y((int) y, texture);
    }

    @Shim
    private native int stationapi_bed_modTexture2Y(
            int y,
            LocalRef<Sprite> texture
    );

    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(doubleValue = 16)
    )
    private double optifabric_bed_modTexture2Height(
            double height,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return stationapi_bed_modTexture2Height((int) height, texture);
    }

    @Shim
    private native int stationapi_bed_modTexture2Height(
            int height,
            LocalRef<Sprite> texture
    );
}
