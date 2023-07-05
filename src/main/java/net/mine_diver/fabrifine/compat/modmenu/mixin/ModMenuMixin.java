package net.mine_diver.fabrifine.compat.modmenu.mixin;

import com.google.common.collect.ImmutableMap;
import io.github.prospector.modmenu.ModMenu;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.menu.VideoSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Function;

@Mixin(ModMenu.class)
public class ModMenuMixin {
    @Inject(
            method = "onInitializeClient",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void optifabric_addOptifineConfigFactory(CallbackInfo ci, ImmutableMap.Builder<String, Function<ScreenBase, ? extends ScreenBase>> factories) {
        //noinspection deprecation
        factories.put("optifine", parent -> new VideoSettings(parent, ((Minecraft) FabricLoader.getInstance().getGameInstance()).options));
    }
}
