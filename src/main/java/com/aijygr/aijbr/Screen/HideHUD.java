package com.aijygr.aijbr.Screen;

import com.aijygr.aijbr.Main;
import com.aijygr.aijbr.ModConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class HideHUD {
    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Pre event)
    {
        if(ModConfig.Client.Config.HIDEHEALTHBAR.get().get())
            if (event.getOverlay().id().equals(VanillaGuiOverlay.PLAYER_HEALTH.id()))
                event.setCanceled(true);
        if(ModConfig.Client.Config.HIDEFOODLEVEL.get().get())
            if (event.getOverlay().id().equals(VanillaGuiOverlay.FOOD_LEVEL.id()))
                event.setCanceled(true);
    }
}
