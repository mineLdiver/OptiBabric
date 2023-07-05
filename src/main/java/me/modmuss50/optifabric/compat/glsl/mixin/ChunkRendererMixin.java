package me.modmuss50.optifabric.compat.glsl.mixin;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;
import net.minecraft.block.BlockBase;
import net.minecraft.class_66;
import net.minecraft.client.render.block.BlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_66.class)
@InterceptingMixin("net/mine_diver/glsl/mixin/Mixinclass_66")
public class ChunkRendererMixin {
    @Redirect(
            method = "updateRenderer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/block/BlockRenderer;render(Lnet/minecraft/block/BlockBase;III)Z"
            )
    )
    private boolean optifabric_onRenderBlockByRenderType(BlockRenderer tileRenderer, BlockBase var24, int var17, int var15, int var16) {
        return onRenderBlockByRenderType(tileRenderer, var24, var17, var15, var16);
    }

    @Shim
    private native boolean onRenderBlockByRenderType(BlockRenderer tileRenderer, BlockBase var24, int var17, int var15, int var16);
}
