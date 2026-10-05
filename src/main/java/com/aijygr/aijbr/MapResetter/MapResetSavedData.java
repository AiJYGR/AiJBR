package com.aijygr.aijbr.MapResetter;

import com.aijygr.aijbr.Main;
import com.aijygr.aijbr.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MapResetSavedData extends SavedData {

    private static final String DATA = Main.MODID+"_map_resetter_data";
    private static final String POS = "Pos";
    private static final String STATE = "State";
    private final Map<BlockPos, BlockState> data = new HashMap<>();

    ///单例实例Map
    public static MapResetSavedData getInstance(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                MapResetSavedData::load,
                MapResetSavedData::new,
                DATA
        );
    }

    public MapResetSavedData() {}

    ///存进电脑用List
    public static MapResetSavedData load(CompoundTag tag) {
        MapResetSavedData savedData = new MapResetSavedData();
        ListTag list = tag.getList(DATA, CompoundTag.TAG_COMPOUND);
        for (Tag value : list) {
            CompoundTag entry = (CompoundTag) value;
            BlockPos pos = NbtUtils.readBlockPos(entry.getCompound(POS));
            BlockState state = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), entry.getCompound(STATE));
            savedData.data.put(pos, state);
        }
        return savedData;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag)
    {
        ListTag list = new ListTag();
        for (Map.Entry<BlockPos, BlockState> entry : data.entrySet()) {
            CompoundTag tagEntry = new CompoundTag();
            tagEntry.put(POS, NbtUtils.writeBlockPos(entry.getKey()));
            tagEntry.put(STATE, NbtUtils.writeBlockState(entry.getValue()));
            list.add(tagEntry);
        }
        tag.put(DATA, list);
        tag.putString("Version", ModList.get().getModContainerById(Main.MODID).get().getModInfo().getVersion().toString());
        return tag;
    }


    public boolean recordOriginalState(BlockPos blockPos, BlockState oldState, BlockState newState) {
        BlockPos pos = blockPos.immutable();
        if (!data.containsKey(pos))
        {
            if (isResetTarget(oldState,newState)) {
                data.put(pos, oldState);
                this.setDirty();
                return true;
            }
        }
        return false;
    }


    private final String AIR = "minecraft:air";
    private boolean isResetTarget(BlockState oldState,BlockState newState){
        var list = ModConfig.Server.Config.DEV.MAPRESETTER_EXCLUSIONS.get();
        boolean o = list.contains(ForgeRegistries.BLOCKS.getKey(oldState.getBlock()).toString());
        boolean n = list.contains(ForgeRegistries.BLOCKS.getKey(newState.getBlock()).toString());
        return (!o)&&(!n);
    }

    public int resetMap(ServerLevel level) {
        int i=0;
        for (Map.Entry<BlockPos, BlockState> entry : data.entrySet()) {
            level.setBlock(entry.getKey(), entry.getValue(), 50);
            i++;
        }
        data.clear();
        this.setDirty();
        return i;
    }
}
