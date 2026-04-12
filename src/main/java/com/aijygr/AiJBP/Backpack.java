package com.aijygr.AiJBP;

import com.aijygr.AiJGame.Game;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)//####################################
public class Backpack extends Event
{
    public enum SlotTag{
        MAINWPN,
        SUBWPN,
        MELEE,
        NADES,
        SUPPLIES,
        BACKPACK,
        ARMOR,
        DEFAULT
    }
    public static class BackpackSlotAttribute{
        SlotTag tag;
        short permissionLevel;
        short slot;
        public BackpackSlotAttribute(short slot, short permissionLevel, SlotTag tag) {
            this.tag = tag;
            this.permissionLevel = permissionLevel;
            this.slot = slot;
        }
        public BackpackSlotAttribute(short slot, short permissionLevel, String tag) {
            this.tag = SlotTag.valueOf(tag);
            this.permissionLevel = permissionLevel;
            this.slot = slot;
        }
        public BackpackSlotAttribute(short slot, String tag) {
            this.tag = SlotTag.valueOf(tag);
            this.slot = slot;
            this.permissionLevel = Short.MAX_VALUE;
        }
        public BackpackSlotAttribute(short slot) {
            this.tag = SlotTag.DEFAULT;
            this.slot = slot;
            this.permissionLevel = Short.MAX_VALUE;
        }
        public BackpackSlotAttribute() {
            this.tag = SlotTag.DEFAULT;
            this.slot = 0;
            this.permissionLevel = Short.MAX_VALUE;
        }
        public String getTagName() {
            return tag.name();
        }
    }
    public static List<BackpackSlotAttribute> backpack = new ArrayList<>();
    public static String s = "";
    public static Inventory inventory;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent event)
    {
        Player player = event.player;
        if(player != null && event.side == LogicalSide.CLIENT && event.phase == Phase.START)
        {
            if (Minecraft.getInstance().player == null || !player.getUUID().equals(Minecraft.getInstance().player.getUUID()))
                return;
            Inventory inventory = player.getInventory();

            if(Game.gametime%200 == 0)
            {
                System.out.println("\n"+Game.gametime);
                int i = 0;
                List<String> tags = Tagger.GetItemTag(inventory.getItem(0));
                if(tags != null)
                {
                    StringBuilder s = new StringBuilder();
                    for(String tag : tags)
                        s.append(tag).append(" ");
                    System.out.println("TAG:"+s);
                }
                InventoryLock.lock(12);
            }
            if(Game.gametime%200 == 100)
            {
                InventoryLock.unlock(12);
                if(Game.isReloaded){
                    for(BackpackSlotAttribute backpackSlotAttribute : backpack){
                        System.out.println(backpackSlotAttribute.tag.name());
                    }
                }
            }
        }
    }



}
