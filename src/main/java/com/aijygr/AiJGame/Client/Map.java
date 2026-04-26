package com.aijygr.AiJGame.Client;

import com.aijygr.plugin.AiJBRMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class Map {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        ClientLevel level = Minecraft.getInstance().level;
        if (event.phase == TickEvent.Phase.END && level != null) {
            var border = level.getWorldBorder();
            if (net.minecraftforge.fml.ModList.get().isLoaded("journeymap")) {
                AiJBRMap.drawZone(level.dimension(), ClientGame.next_x, ClientGame.next_z, ClientGame.next_size/2, 0x22f022,"next");
                AiJBRMap.drawZone(level.dimension(), border.getCenterX(),border.getCenterZ(), border.getSize()/2,0x2222f0,"curr");
            }
        }
    }
}
