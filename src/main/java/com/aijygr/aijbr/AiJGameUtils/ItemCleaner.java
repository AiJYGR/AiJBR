package com.aijygr.aijbr.AiJGameUtils;

import com.aijygr.aijbr.AiJGame.Game;
import com.aijygr.aijbr.Main;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class ItemCleaner {
    public static final String TAG = "ItemCleaner";
    public static long time = -1;

    public static int cleanitems(MinecraftServer server)
    {
        updatetime(Game.gametime);
        int i = 0;
        for(ServerLevel level : server.getAllLevels()){
            var entities = level.getAllEntities();
            for(Entity entity : entities){
                if(isTarget(entity)){
                    entity.remove(Entity.RemovalReason.KILLED);
                    i++;
                }
                //entity.getType().equals(Reg.DROPSHIP.get());
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
        Entity entity = event.getEntity();
        if(isTarget(event.getEntity())){
            CompoundTag tag = entity.getPersistentData();
            if(!tag.contains(TAG))
            {
                tag.putLong(TAG,time);
            }
            else if (tag.getLong(TAG) != time && time != -1)
            {
                System.out.printf("[ItemCleaner]移除了一个过期的entity:%s %d", ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()),tag.getLong(TAG));
                entity.discard();
                event.setCanceled(true);
            }
        }
    }

    private static final List<String> targetList = List.of("minecraft:item","aijbr:dropship");
    public static boolean isTarget(Entity entity){
        if(entity instanceof ItemEntity)
            return true;
        ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        if(id == null)
            return false;
        return targetList.contains(id.toString());
    }
}
