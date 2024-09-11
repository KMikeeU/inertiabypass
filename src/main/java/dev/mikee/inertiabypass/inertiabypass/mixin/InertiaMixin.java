package dev.mikee.inertiabypass.inertiabypass.mixin;

import me.diffusehyperion.inertiaanticheat.client.InertiaAntiCheatClient;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

@Mixin(InertiaAntiCheatClient.class)
public class InertiaMixin {
    @Inject(method = "setupModDataList()V", at = @At("HEAD"), remap = false, cancellable = true)
    public void setupModDataListInject (CallbackInfo ci) {
        try {
            File modDirectory = FabricLoader.getInstance().getGameDir().resolve("mods").toFile();
            for (File modFile : Objects.requireNonNull(modDirectory.listFiles())) {
                if (
                    modFile.isDirectory()
                    || !modFile.getAbsolutePath().endsWith(".jar")
                    || modFile.getName().contains("inertiabypass")
                ) continue;

                InertiaAntiCheatClient.allModData.add(Files.readAllBytes(modFile.toPath()));
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        ci.cancel();
    }
}
