package net.mine_diver.fabrifine.compat.stationapi.mixin;

import net.mine_diver.fabrifine.compat.stationapi.BakedModelRendererImplCompat;
import net.minecraft.client.render.Tessellator;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.BakedModelRendererImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.objectweb.asm.Opcodes.GETFIELD;

@Mixin(BakedModelRendererImpl.class)
public class BakedModelRendererImplMixin implements BakedModelRendererImplCompat {
    @Redirect(
            method = "*",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/modificationstation/stationapi/impl/client/arsenic/renderer/render/BakedModelRendererImpl;tessellator:Lnet/minecraft/client/render/Tessellator;",
                    opcode = GETFIELD
            )
    )
    private Tessellator optifabric_getTessellator(BakedModelRendererImpl instance) {
        return Tessellator.INSTANCE;
    }

    @Unique
    private boolean optifabric_deferredStart;

    @Redirect(
            method = "renderGuiItemModel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;start()V"
            )
    )
    private void fabrifine_deferTessellatorStart(Tessellator instance) {
        deferItemStart();
    }

    @Inject(
            method = "renderBakedItemModel",
            at = @At("HEAD")
    )
    private void fabrifine_startDeferred(BakedModel model, ItemInstance stack, float brightness, CallbackInfo ci) {
        if (optifabric_deferredStart) {
            optifabric_deferredStart = false;
            Tessellator.INSTANCE.start();
        }
    }

    @Override
    public void deferItemStart() {
        optifabric_deferredStart = true;
    }
}
