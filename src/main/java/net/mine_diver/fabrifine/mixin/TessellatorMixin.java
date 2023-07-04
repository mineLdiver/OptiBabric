package net.mine_diver.fabrifine.mixin;

import net.mine_diver.fabrifine.patcher.OptifineTessellator;
import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Tessellator.class)
public class TessellatorMixin implements OptifineTessellator {
    @Dynamic
    private boolean renderingChunk;

    @Override
    public boolean isRenderingChunk() {
        return renderingChunk;
    }
}
