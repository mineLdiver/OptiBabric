package me.modmuss50.optifabric.compat.stationrendererapi.mixin;

import me.modmuss50.optifabric.compat.stationrendererapi.TessellatorOF;
import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Tessellator.class)
public class TessellatorMixin implements TessellatorOF {
    @Dynamic
    @Shadow
    private boolean renderingChunk;

    @Override
    @Unique
    public boolean optifabric_isRenderingChunk() {
        return renderingChunk;
    }
}
