package me.modmuss50.optifabric.compat.arsenic.mixin;

import me.modmuss50.optifabric.compat.arsenic.BakedModelRendererImplCompat;
import net.minecraft.client.render.Tessellator;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
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
class BakedModelRendererImplMixin implements BakedModelRendererImplCompat {
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
    private void optifabric_deferTessellatorStart(Tessellator instance) {
        optifabric_deferItemStart();
    }

    @Inject(
            method = "renderBakedItemModel",
            at = @At("HEAD")
    )
    private void optifabric_startDeferred(BakedModel model, ItemInstance stack, float brightness, CallbackInfo ci) {
        if (optifabric_deferredStart) {
            optifabric_deferredStart = false;
            Tessellator.INSTANCE.start();
        }
    }

    @Override
    public void optifabric_deferItemStart() {
        optifabric_deferredStart = true;
    }

    @Redirect(
            method = "renderItem(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;Lnet/modificationstation/stationapi/api/client/render/model/json/ModelTransformation$Mode;Lnet/minecraft/level/Level;FI)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemInstance;count:I",
                    opcode = GETFIELD
            )
    )
    private int optifabric_renderEmptyStack(ItemInstance instance) {
        return 1;
    }

    @Inject(
            method = "renderDamage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/modificationstation/stationapi/impl/client/arsenic/renderer/render/BakedModelRendererImpl;render(Lnet/minecraft/level/BlockView;Lnet/modificationstation/stationapi/api/client/render/model/BakedModel;Lnet/modificationstation/stationapi/api/block/BlockState;Lnet/minecraft/util/maths/TilePos;ZLjava/util/Random;J)Z"
            )
    )
    private void optifabric_deferredStartDamage(BlockState state, TilePos pos, BlockView world, float progress, CallbackInfo ci) {
        Tessellator.INSTANCE.start();
        Tessellator.INSTANCE.disableColour();
    }
}
