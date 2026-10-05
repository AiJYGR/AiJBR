package com.aijygr.aijbr.AiJGameUtils.MapResetter;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

public class BlockStateAndNBT {
    private final BlockState state;
    private final CompoundTag nbt;

    ///同时存储BlockState和nbt的数据结构
    public BlockStateAndNBT(@NotNull BlockState state, @Nullable CompoundTag nbt) {
        this.state = state;
        this.nbt = nbt;
    }

    public static BlockStateAndNBT create(BlockState state, CompoundTag nbt) {
        return new BlockStateAndNBT(state,nbt);
    }

    public BlockState getState() {
        return state;
    }
    public CompoundTag getNbt() {
        return nbt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BlockStateAndNBT blockStateAndNBT)) return false;
        return Objects.equals(state, blockStateAndNBT.state) && Objects.equals(nbt, blockStateAndNBT.nbt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, nbt);
    }
}
