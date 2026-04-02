package com.aijygr.Events;

import com.aijygr.Main;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    // @SubscribeEvent
    // public static void onLoading(ModConfigEvent.Loading event) {
    //     Main.LOGGER.info("!!! CONFIG LOADING: " + event.getConfig().getFileName());
    // }

    // @SubscribeEvent
    // public static void onReload(ModConfigEvent.Reloading event) {
    //     Main.LOGGER.info("!!! CONFIG RELOADING: " + event.getConfig().getFileName());
    // }
}
