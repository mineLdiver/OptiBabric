package me.modmuss50.optifabric.compat.arsenic.mixin;

import me.modmuss50.optifabric.api.mixin.InterceptingMixin;
import net.minecraft.client.render.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TextRenderer.class)
@InterceptingMixin("net/modificationstation/stationapi/mixin/resourceloader/client/TextRendererMixin")
class TextRendererMixin {}
