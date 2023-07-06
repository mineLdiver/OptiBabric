package me.modmuss50.optifabric.compat.arsenic.mixin;

import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Tessellator.class)
public interface TessellatorOF {
    @Accessor(
            value = "renderingChunk",
            remap = false
    )
    boolean optifabric_isRenderingChunk();
}
