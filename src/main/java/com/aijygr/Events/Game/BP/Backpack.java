package com.aijygr.Events.Game.BP;

import com.aijygr.Events.Game.Game;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.entity.player.Player;

@Mod.EventBusSubscriber() //####################################
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

        public String getTagName() {
            return tag.name();
        }
    }


    public static String s = "";
    public static Inventory inventory;

    @SubscribeEvent     //##############################
    public static void onPlayerTick(PlayerTickEvent event)
    {
        Player player = event.player;
        if(player != null && event.side == LogicalSide.CLIENT && event.phase == Phase.START)
        {
            if (Minecraft.getInstance().player == null || !player.getUUID().equals(Minecraft.getInstance().player.getUUID()))
                return;

            String inv = player.getInventory().getSelected().getDisplayName().getString();
            //Main.LOGGER.info("s:"+s);
            if(!s.equals(inv))
            {
                System.out.println("inv:"+inv+player.level().getGameTime());
                s = inv;
            }
            boolean c = player.getInventory().equals(inventory);
            if(!c)
                System.out.println(Game.gametime+" isChanged:"+c);
            inventory = player.getInventory();
            if(Game.gametime%20 == 0)
            {
                //SlotTests.IsValidSlot(player,player.getInventory().getSelected(),new BackpackSlotAttribute((short)0,(short)0,""));
            }


        }
    }
}
