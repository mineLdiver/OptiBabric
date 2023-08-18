package me.modmuss50.optifabric.compat.macula.mixin;

import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.menu.VideoSettings;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.options.Option;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(VideoSettings.class)
class VideoSettingsScreenMixin extends ScreenBase {
    @Inject(
            method = "init",
            at = @At("RETURN"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void optifabric_macula_readjustButtons(
            CallbackInfo ci,
            TranslationStorage stringtranslate, int i, Option[] aenumoptions, int j, int y, int x
    ) {
        int shadersButtonId = "macula:shaders".hashCode();
        //noinspection unchecked
        for (Button button : (List<Button>) buttons)
            if (button.id == shadersButtonId)
                button.y = y + 21;
            else if (button.id == 200) {
                button.width = 150;
                button.x = x;
            }
    }
}
