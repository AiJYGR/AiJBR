package com.aijygr.Events;

import com.aijygr.Main;

import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigUpdate {
    // @SubscribeEvent
    // public static void onLoading(ModConfigEvent.Loading event) {
    //     Main.LOGGER.info("!!! CONFIG LOADING: " + event.getConfig().getFileName());
    // }

    // @SubscribeEvent
    // public static void onReload(ModConfigEvent.Reloading event) {
    //     Main.LOGGER.info("!!! CONFIG RELOADING: " + event.getConfig().getFileName());
    // }
}
