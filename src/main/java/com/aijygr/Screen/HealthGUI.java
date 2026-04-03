package com.aijygr.Screen;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "aijbr", value = Dist.CLIENT)
public class HealthGUI {
    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.hideGui)
            return;

        float health = minecraft.player.getHealth();
        float maxHealth = minecraft.player.getMaxHealth();

        String healthText = String.format("%.2f / %.0f", health, maxHealth);

        int width = event.getWindow().getGuiScaledWidth();
        int height = event.getWindow().getGuiScaledHeight();
        var guiGraphics = event.getGuiGraphics();
        var font = minecraft.font;

        int x = width / 2 + 40;
        int y = height - 50;

        guiGraphics.drawString(font, healthText, x, y, 0xFF5555, false);
    }
}
