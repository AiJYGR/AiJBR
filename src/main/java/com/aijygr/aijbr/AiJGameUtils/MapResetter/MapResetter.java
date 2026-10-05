package com.aijygr.aijbr.AiJGameUtils.MapResetter;

import com.aijygr.aijbr.Main;
import com.aijygr.aijbr.ModConfig;
import com.ibm.icu.impl.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MapResetter extends SavedData {

    private static final String DATA = Main.MODID+"_map_resetter_data";
    private static final String POS = "Pos";
    private static final String STATE = "State";
    private static final String NBT = "NBT";
    private final Map<BlockPos, BlockStateAndNBT> data = new HashMap<>();

    ///单例实例Map
    public static MapResetter getInstance(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                MapResetter::load,
                MapResetter::new,
                DATA
        );
    }

    public MapResetter() {}

    ///存进电脑用List
    public static MapResetter load(CompoundTag tag) {
        MapResetter savedData = new MapResetter();
        ListTag list = tag.getList(DATA, CompoundTag.TAG_COMPOUND);
        for (Tag value : list) {
            CompoundTag entry = (CompoundTag) value;
            BlockPos pos = NbtUtils.readBlockPos(entry.getCompound(POS));
            BlockState state = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), entry.getCompound(STATE));
            CompoundTag nbt = entry.getCompound(NBT);
            savedData.data.put(pos, BlockStateAndNBT.create(state,nbt));
        }
        return savedData;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag)
    {
        ListTag list = new ListTag();
        for (Map.Entry<BlockPos, BlockStateAndNBT> entry : data.entrySet()) {
            CompoundTag tagEntry = new CompoundTag();
            BlockPos pos = entry.getKey().immutable();
            BlockStateAndNBT data = entry.getValue();
            CompoundTag nbt = data.getNbt();
            BlockState state = data.getState();
            tagEntry.put(POS, NbtUtils.writeBlockPos(pos));
            tagEntry.put(STATE, NbtUtils.writeBlockState(state));
            if(nbt!=null)
                tagEntry.put(NBT, nbt);
            list.add(tagEntry);
        }
        tag.put(DATA, list);
        tag.putString("Version", ModList.get().getModContainerById(Main.MODID).get().getModInfo().getVersion().toString());
        return tag;
    }


    public boolean recordOriginalState(BlockPos blockPos, BlockStateAndNBT oldStateAndNBT, BlockStateAndNBT newStateAndNBT) {
        BlockPos pos = blockPos.immutable();
        if (!data.containsKey(pos))
        {
            if (isResetTarget(oldStateAndNBT,newStateAndNBT)) {
                data.put(pos, oldStateAndNBT);
                this.setDirty();
                return true;
            }
        }
        return false;
    }


    private final String AIR = "minecraft:air";
    private boolean isResetTarget(BlockStateAndNBT oldStateAndNBT,BlockStateAndNBT newStateAndNBT) {
        var list = ModConfig.Server.Config.DEV.MAPRESETTER_EXCLUSIONS.get();
        boolean o = list.contains(ForgeRegistries.BLOCKS.getKey(oldStateAndNBT.getState().getBlock()).toString());
        boolean n = list.contains(ForgeRegistries.BLOCKS.getKey(newStateAndNBT.getState().getBlock()).toString());
        return (!o)&&(!n);
    }

    public Pair<Integer,Integer> resetMap(ServerLevel level) {
        int i=0,j=0;
        for (Map.Entry<BlockPos, BlockStateAndNBT> entry : data.entrySet()) {
            level.setBlock(entry.getKey(), entry.getValue().getState(), 50);
            if(entry.getValue().getNbt()!=null){
                BlockEntity blockEntity = level.getBlockEntity(entry.getKey());
                if(blockEntity!=null)
                {
                    blockEntity.load(entry.getValue().getNbt());
                    blockEntity.setChanged();
                    j++;
                }
                else
                    Main.LOGGER.warn("A BlockEntity is null, failed to load NBT");
            }

            i++;
        }
        data.clear();
        this.setDirty();
        return Pair.of(i,j);
    }
    public int clearData(ServerLevel level) {
        int i = data.size();
        data.clear();
        return i;
    }
}
