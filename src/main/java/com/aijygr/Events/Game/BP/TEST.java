package com.aijygr.Events.Game.BP;

import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import com.aijygr.Config;

import net.minecraft.world.entity.player.Player;

@Mod.EventBusSubscriber() //####################################
public class TEST extends Event
{

    public static String s = "";
    @SubscribeEvent     //##############################
    public static void onPlayerTick(PlayerTickEvent event)
    {
        Player player = event.player;
        if(player != null && event.side == LogicalSide.CLIENT && event.phase == Phase.START)
        {
            String inv = player.getInventory().getSelected().getDisplayName().getString();
            //Main.LOGGER.info("s:"+s);
            if(!s.equals(inv))
            {
                System.out.println("inv:"+inv+player.level().getGameTime());
                s = inv;
            }
            //System.out.println(Config.CFGDOUBLE.get());
        }
    }
}
