package me.modmuss50.optifabric.compat.macula.multithreaded.mixin;

import me.modmuss50.optifabric.api.mixin.InterceptingMixin;
import me.modmuss50.optifabric.api.mixin.PlacatingSurrogate;
import me.modmuss50.optifabric.api.mixin.Shim;
import net.minecraft.block.BlockBase;
import net.minecraft.class_66;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.WorldPopulationRegion;
import net.minecraft.tileentity.TileEntityBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashSet;

@Mixin(class_66.class)
@InterceptingMixin("net/mine_diver/macula/mixin/ChunkBuilderMixin")
class ChunkRendererMixin {
    @Inject(
            method = "updateRenderer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/block/BlockRenderer;render(Lnet/minecraft/block/BlockBase;III)Z"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void optifabric_onRenderBlockByRenderType(
            @Coerce Object updateListener, CallbackInfo ci,
            int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, boolean[] tempSkipRenderPass, Object lightCache, HashSet<TileEntityBase> hashset, int one, WorldPopulationRegion chunkcache, BlockRenderer renderblocks, Tessellator tessellator, int renderPass, boolean flag, boolean hasRenderedBlocks, boolean hasGlList, int y, int z, int x, int i3, BlockBase block
    ) {
        onRenderBlockByRenderType(ci, xMin, yMin, zMin, xMax, yMax, zMax, hashset, 1, chunkcache, renderblocks, renderPass, flag ? 1 : 0, hasRenderedBlocks ? 1 : 0, hasGlList ? 1 : 0, y, z, x, i3, block);
    }

    @Shim
    private native void onRenderBlockByRenderType(
            CallbackInfo ci,
            int var1, int var2, int var3, int var4, int var5, int var6, HashSet<TileEntityBase> var7, int var8, WorldPopulationRegion var9, BlockRenderer var10, int var11, int var12, int var13, int var14, int var15, int var16, int var17, int var18, BlockBase var19
    );

    @PlacatingSurrogate
    private void onRenderBlockByRenderType(CallbackInfo ci) {}
}
