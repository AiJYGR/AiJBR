package com.aijygr.Screen;

import com.aijygr.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = "aijbr", value = Dist.CLIENT)
public class HealthGUI {
    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if(!ModConfig.Client.Config.SHOWACCURATEHEALTH.get().get())
            return;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null
                || minecraft.options.hideGui
                || minecraft.player.isCreative()
                || minecraft.player.isSpectator())
            return;


        float health = minecraft.player.getHealth();
        float maxHealth = minecraft.player.getMaxHealth();

        String healthstr = String.format("%.2f / %.0f", health, maxHealth);
        String percentagestr = String.format("%.2f%%", (health/maxHealth)*100);

        int width = event.getWindow().getGuiScaledWidth();
        int height = event.getWindow().getGuiScaledHeight();
        var graphics = event.getGuiGraphics();
        var font = minecraft.font;

        int x = width / 2 + 55;
        int y = height - 50;

        graphics.drawString(font, percentagestr, x - font.width(percentagestr)/2 , y, 0xFF5555,false);
        graphics.drawString(font, healthstr, x - font.width(healthstr)/2 , y+10, 0xFF5555,false);

    }
}
