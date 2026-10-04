package com.aijygr.aijbr.ItemCleaner;

import com.aijygr.aijbr.Main;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class ItemCleaner {
    public static final String TAG = "ItemCleaner";
    public static long time = -1;

    public static int cleanitems(long time, MinecraftServer server)
    {
        updatetime(time);
        int i = 0;
        for(ServerLevel level : server.getAllLevels()){
            var items = level.getEntities(EntityTypeTest.forClass(ItemEntity.class), item -> true);
            for(Entity item : items){
                if(item instanceof ItemEntity){
                    item.remove(Entity.RemovalReason.KILLED);
                    i++;
                }
            }
        }
        return i;
    }

    public static void updatetime(long time)
    {
        ItemCleaner.time = time;
    }

    @SubscribeEvent
    public static void onItemJoin(EntityJoinLevelEvent event) {
        if(event.getLevel().isClientSide())
            return;
        if(event.getEntity() instanceof ItemEntity item){
            CompoundTag tag = item.getPersistentData();
            if(!tag.contains(TAG))
            {
                tag.putLong(TAG,time);
            }
            else if (tag.getLong(TAG) != time && time != -1)
            {
                System.out.printf("[ItemCleaner]移除了一个过期的item:%s %d", ForgeRegistries.ITEMS.getKey(item.getItem().getItem()),tag.getLong(TAG));
                item.discard();
                event.setCanceled(true);
            }
        }
    }
}
