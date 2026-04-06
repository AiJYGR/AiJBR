package com.aijygr.Screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


public class RingIndicatorGUI {

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.hideGui)
            return;
        if(minecraft.player.isCreative() && minecraft.player.isCrouching())
        {
            System.out.println("crouching");
            Minecraft.getInstance().setScreen(new Scr(Component.literal("123")));
        }

    }
}
