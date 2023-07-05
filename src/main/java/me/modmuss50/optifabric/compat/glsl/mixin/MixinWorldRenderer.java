package me.modmuss50.optifabric.compat.glsl.mixin;

import me.modmuss50.optifabric.compat.glsl.WorldRendererCompat;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WorldRenderer.class)
abstract class MixinWorldRenderer implements WorldRendererCompat {
    @Shadow public abstract int renderAllSortedRenderers(int par1, double par2);

    private int optifabric_num;

    @Override
    public int optifabric_getNum() {
        return optifabric_num;
    }

    @Unique(silent = true)
    public void method_1540(int i, double d) {
        optifabric_num = renderAllSortedRenderers(i, d);
    }
}
