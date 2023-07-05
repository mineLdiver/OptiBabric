package net.mine_diver.fabrifine.compat.stationapi.mixin;

import net.minecraft.class_257;
import net.minecraft.client.options.GameOptions;
import net.minecraft.level.LightType;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.source.LevelSource;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Arrays;

import static org.objectweb.asm.Opcodes.GETFIELD;

@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {
    @Unique
    private static final byte[] OPTIFABRIC$EMPTY_LIGHT = new byte[0];

    @Unique
    private boolean optifabric_flattened;

    @Inject(
            method = "updateWaterOpacity",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/level/chunk/Chunk;field_958:Lnet/minecraft/class_257;",
                    opcode = GETFIELD
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void optifabric_checkForFlattenedChunk(CallbackInfo ci, int opacity, LevelSource cp, int x, int z, Chunk c) {
        optifabric_flattened = c instanceof FlattenedChunk;
        if (optifabric_flattened)
            for (ChunkSection section : ((FlattenedChunk) c).sections)
                if (section != null)
                    Arrays.fill(section.getLightArray(LightType.field_2757).data, (byte) 0);
    }

    @Redirect(
            method = "updateWaterOpacity",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/class_257;field_2103:[B",
                    opcode = GETFIELD
            )
    )
    private byte[] optifabric_fixNullLight(class_257 instance) {
        byte[] light = optifabric_flattened ? OPTIFABRIC$EMPTY_LIGHT : instance.field_2103;
        optifabric_flattened = false;
        return light;
    }
}
