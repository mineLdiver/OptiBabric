package me.modmuss50.optifabric.compat.stationrendererapi.mixin;

import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Tessellator.class)
interface TessellatorOF {
    @Accessor(
            value = "renderingChunk",
            remap = false
    )
    boolean optifabric_isRenderingChunk();
}
