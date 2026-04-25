package com.aijygr.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldBorder.class)
public class WorldBorderMixin {
    @Inject(method = "isWithinBounds(Lnet/minecraft/world/phys/AABB;)Z", at = @At("HEAD"), cancellable = true, remap = true)
    private void onIsWithinBoundsAABB(AABB pBox, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "isWithinBounds(DD)Z", at = @At("HEAD"), cancellable = true, remap = true)
    private void onIsWithinBoundsDD(double pX, double pZ, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "isWithinBounds(DDD)Z", at = @At("HEAD"), cancellable = true, remap = true)
    private void onIsWithinBoundsDDD(double pX, double pZ, double pOffset, CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(false);
    }

    @Inject(method = "isWithinBounds(Lnet/minecraft/world/level/ChunkPos;)Z",at = @At("HEAD"),cancellable = true, remap = true)
    private void onIsWithinBoundsChunkPos(ChunkPos p, CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(true);
    }

    @Inject(method = "isWithinBounds(Lnet/minecraft/core/BlockPos;)Z",at = @At("HEAD"),cancellable = true ,remap = true)
    private void onIsWithinBoundsBlockPos(BlockPos p, CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(true);
    }
}
