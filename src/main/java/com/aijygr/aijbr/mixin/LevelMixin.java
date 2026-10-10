package com.aijygr.aijbr.mixin;

import com.aijygr.aijbr.AiJGameUtils.MapResetter.BlockStateAndNBT;
import com.aijygr.aijbr.AiJGameUtils.MapResetter.MapResetter;
import com.aijygr.aijbr.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class LevelMixin {
    ///onSetBlock的钩子
    @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z", at = @At("HEAD") ,remap = true)
    private void onSetBlock(BlockPos pPos, BlockState pState, int pFlags, int pRecursionLeft, CallbackInfoReturnable<Boolean> cir) {
        if(!ModConfig.Server.Config.DEV.ENABLE_MAPRESETTER.get())
            return;
        if((Object)this instanceof ServerLevel level){
            BlockState oldState = level.getBlockState(pPos);
            if (oldState.equals(pState))
            {
                return;
            }
            BlockEntity blockEntity = level.getBlockEntity(pPos);
            CompoundTag oldNBT = null;
            CompoundTag newNBT = null;
            if(blockEntity != null)
            {
                oldNBT = blockEntity.serializeNBT();
            }
            //System.out.printf("setblock %s -> %s at %s\n",oldState.toString(),pState.toString(),pPos);
            MapResetter.getInstance(level).recordOriginalState(pPos, BlockStateAndNBT.create(oldState,oldNBT),BlockStateAndNBT.create(pState, newNBT));
        }
    }
}
