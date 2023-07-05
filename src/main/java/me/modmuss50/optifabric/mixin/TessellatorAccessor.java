package me.modmuss50.optifabric.mixin;

import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.nio.IntBuffer;

@Mixin(Tessellator.class)
public interface TessellatorAccessor {
    @Accessor
    IntBuffer getIntBuffer();
}
