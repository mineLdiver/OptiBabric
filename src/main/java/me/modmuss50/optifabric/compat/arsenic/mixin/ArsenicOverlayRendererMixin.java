package me.modmuss50.optifabric.compat.arsenic.mixin;

import me.modmuss50.optifabric.compat.arsenic.BakedModelRendererImplCompat;
import net.minecraft.client.render.Tessellator;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.render.RendererAccess;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicOverlayRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static org.objectweb.asm.Opcodes.GETFIELD;

@Mixin(ArsenicOverlayRenderer.class)
class ArsenicOverlayRendererMixin {
    @Redirect(
            method = "renderItem(F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;start()V",
                    ordinal = 1
            )
    )
    private void optifabric_deferStart(Tessellator instance) {
        ((BakedModelRendererImplCompat) RendererAccess.INSTANCE.getRenderer().bakedModelRenderer()).optifabric_deferItemStart();
    }

    @Redirect(
            method = "renderItem3D",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;start()V",
                    ordinal = 6
            )
    )
    private void optifabric_deferStart3D(Tessellator instance) {
        ((BakedModelRendererImplCompat) RendererAccess.INSTANCE.getRenderer().bakedModelRenderer()).optifabric_deferItemStart();
    }

    @Redirect(
            method = "renderItem(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;Lnet/modificationstation/stationapi/api/client/render/model/json/ModelTransformation$Mode;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemInstance;count:I",
                    opcode = GETFIELD
            )
    )
    private int optifabric_renderEmptyStack(ItemInstance instance) {
        return 1;
    }
}
