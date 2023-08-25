package me.modmuss50.optifabric.compat.fov.mixin;

import me.modmuss50.optifabric.api.mixin.InterceptingMixin;
import me.modmuss50.optifabric.api.mixin.Shim;
import net.minecraft.client.options.Option;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Option.class)
@InterceptingMixin("com/vendoau/fov/mixin/OptionMixin")
public class OptionMixin {
    @Mutable
    @Dynamic
    @Shadow
    private static @Final Option[] $VALUES;

    @Mutable
    @Shadow @Final private static Option[] field_1113;

    @Inject(
            method = "<clinit>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/options/Option;$VALUES:[Lnet/minecraft/client/options/Option;",
                    opcode = Opcodes.PUTSTATIC,
                    shift = At.Shift.AFTER
            )
    )
    private static void optifabric_addFovOptionToValues(CallbackInfo ci) {
        field_1113 = $VALUES;
        addCustomOption(ci);
        $VALUES = field_1113;
    }

    @Shim
    private static native void addCustomOption(CallbackInfo ci);
}
