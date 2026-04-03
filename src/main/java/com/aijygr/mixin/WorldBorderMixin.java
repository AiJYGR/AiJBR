package com.aijygr.mixin;

import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldBorder.class)
public class WorldBorderMixin {

    @Inject(method = "isWithinBounds(Lnet/minecraft/world/phys/AABB;)Z", at = @At("HEAD"), cancellable = true)
    private void onIsWithinBoundsAABB(AABB pBox, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false); // 强制认为在边界内
    }

    @Inject(method = "isWithinBounds(DD)Z", at = @At("HEAD"), cancellable = true)
    private void onIsWithinBoundsDD(double pX, double pZ, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false); // 强制认为在边界内
    }

    @Inject(method = "isWithinBounds(DDD)Z", at = @At("HEAD"), cancellable = true)
    private void onIsWithinBoundsDDD(double pX, double pZ, double pOffset, CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(false);
    }




}
