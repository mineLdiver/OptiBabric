package me.modmuss50.optifabric.compat.macula.mixin;

import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.ByteBuffer;

@Mixin(Tessellator.class)
class TessellatorMixin {
    @Shadow private int drawingMode;

    @Shadow private static boolean useTriangles;

    @Shadow private int vertexAmount;

    @Dynamic
    @Shadow(remap = false)
    public ByteBuffer shadersBuffer;

    @Dynamic
    @Shadow(remap = false)
    public short[] shadersData;

    @Inject(
            method = "addVertex",
            at = @At("HEAD")
    )
    private void optifabric_macula_uploadBlockId(double d, double d1, double d2, CallbackInfo ci) {
        if (drawingMode == 7 && useTriangles && (vertexAmount + 1) % 4 == 0) {
            shadersBuffer.putShort(shadersData[0]).putShort(shadersData[1]);
            shadersBuffer.putShort(shadersData[0]).putShort(shadersData[1]);
        }
        shadersBuffer.putShort(shadersData[0]).putShort(shadersData[1]);
    }

    @Inject(
            method = "clear",
            at = @At("RETURN")
    )
    private void optifabric_macula_alwaysClearShadersBuffer(CallbackInfo ci) {
        shadersBuffer.clear();
    }
}
