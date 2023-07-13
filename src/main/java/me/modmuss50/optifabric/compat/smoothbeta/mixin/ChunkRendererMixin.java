package me.modmuss50.optifabric.compat.smoothbeta.mixin;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;
import net.minecraft.class_66;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.WorldPopulationRegion;
import net.minecraft.tileentity.TileEntityBase;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashSet;

@Mixin(class_66.class)
@InterceptingMixin("net/mine_diver/smoothbeta/mixin/client/multidraw/ChunkRendererMixin")
public class ChunkRendererMixin {
    @Shadow public int field_231;
    @Shadow public int field_232;
    @Shadow public int field_233;
    @Dynamic
    private static Tessellator tesselator = Tessellator.INSTANCE;

    @Inject(
            method = "updateRenderer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;start()V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void optifabric_startRenderingTerrain(
            @Coerce Object updateListener, CallbackInfo ci,
            int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, boolean[] tempSkipRenderPass, Object lightCache, HashSet<TileEntityBase> hashset, int one, WorldPopulationRegion chunkcache, BlockRenderer renderblocks, Tessellator tessellator, int renderPass
    ) {
        tesselator = tessellator;
        smoothbeta_startRenderingTerrain(
                ci,
                xMin, yMin, zMin, xMax, yMax, zMax, hashset, one, chunkcache, renderblocks, renderPass
        );
        tesselator.setOffset(-this.field_231, -this.field_232, -this.field_233);
    }

    @PlacatingSurrogate
    private void smoothbeta_startRenderingTerrain(CallbackInfo ci) {}

    @Shim
    private native void smoothbeta_startRenderingTerrain(
            CallbackInfo ci,
            int var1, int var2, int var3, int var4, int var5, int var6, HashSet<TileEntityBase> var7, int var8, WorldPopulationRegion var9, BlockRenderer var10, int var11
    );

    @Inject(
            method = "updateRenderer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;draw()V",
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void optifabric_stopRenderingTerrain(
            @Coerce Object updateListener, CallbackInfo ci,
            int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, boolean[] tempSkipRenderPass, Object lightCache, HashSet<TileEntityBase> hashset, int one, WorldPopulationRegion chunkcache, BlockRenderer renderblocks, Tessellator tessellator
    ) {
        tesselator = tessellator;
        smoothbeta_stopRenderingTerrain(ci);
        tessellator.setOffset(0, 0, 0);
    }

    @Shim
    private native void smoothbeta_stopRenderingTerrain(CallbackInfo ci);
}
