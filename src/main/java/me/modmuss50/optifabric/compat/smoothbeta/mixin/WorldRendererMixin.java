package me.modmuss50.optifabric.compat.smoothbeta.mixin;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import net.minecraft.class_66;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldRenderer.class)
@InterceptingMixin("net/mine_diver/smoothbeta/mixin/client/multidraw/WorldRendererMixin")
public abstract class WorldRendererMixin {
//    @Shadow
//    private List field_1793;
//    @Shadow
//    private RenderList[] field_1794;
//
//    @Inject(
//            method = "<init>",
//            at = @At("RETURN")
//    )
//    private void optifabric_initRenderRegions(Minecraft minecraft, TextureManager renderengine, CallbackInfo ci) {
//        field_1793 = new ArrayList();
//        field_1794 = new RenderList[] {
//                smoothbeta_injectRenderRegion(),
//                smoothbeta_injectRenderRegion(),
//                smoothbeta_injectRenderRegion(),
//                smoothbeta_injectRenderRegion()
//        };
//    }
//
//    @Shim
//    private native @Coerce RenderList smoothbeta_injectRenderRegion();

    @Shadow protected abstract int method_1542(int i, int j, int k, double d);

    @Shadow private class_66[] field_1808;

    public int renderAllSortedRenderers(int i, double d) {
        return this.method_1542(0, this.field_1808.length, i, d);
    }
}
