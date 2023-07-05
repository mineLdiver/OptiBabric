package me.modmuss50.optifabric.mixin;

import me.modmuss50.optifabric.patcher.OptifineTessellator;
import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Tessellator.class)
class TessellatorMixin implements OptifineTessellator {
    @Dynamic
    private boolean renderingChunk;

    @Override
    public boolean isRenderingChunk() {
        return renderingChunk;
    }
}
