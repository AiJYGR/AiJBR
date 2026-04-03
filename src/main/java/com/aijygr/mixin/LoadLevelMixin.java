package com.aijygr.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class LoadLevelMixin {
    @Inject(method = "loadLevel",at = @At("HEAD"))
    public void onLoadLevel(CallbackInfo ci)
    {
        System.out.println("Loading level~~");
    }
}
