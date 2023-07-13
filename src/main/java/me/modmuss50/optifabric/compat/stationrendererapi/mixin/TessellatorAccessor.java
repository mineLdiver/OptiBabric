package me.modmuss50.optifabric.compat.stationrendererapi.mixin;

import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@Mixin(Tessellator.class)
public interface TessellatorAccessor {
    @Accessor("byteBuffer")
    ByteBuffer optifabric_getByteBuffer();

    @Accessor("intBuffer")
    IntBuffer optifabric_getIntBuffer();

    @Accessor("floatBuffer")
    FloatBuffer optifabric_getFloatBuffer();
}
