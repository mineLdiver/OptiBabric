package net.mine_diver.fabrifine.compat.glsl.mixin;

import net.mine_diver.fabrifine.compat.glsl.WorldRendererCompat;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer implements WorldRendererCompat {
    @Shadow public abstract int renderAllSortedRenderers(int par1, double par2);

    private int fabrifine_num;

    @Override
    public int fabrifine_getNum() {
        return fabrifine_num;
    }

    @Unique(silent = true)
    public void method_1540(int i, double d) {
        fabrifine_num = renderAllSortedRenderers(i, d);
    }
}
