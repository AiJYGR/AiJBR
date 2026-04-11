package com.aijygr.AiJBP;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SV {

    @SubscribeEvent
    public static void onItemPickup(PlayerEvent.ItemPickupEvent event) {

    }
}
