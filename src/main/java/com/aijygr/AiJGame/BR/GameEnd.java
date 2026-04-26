package com.aijygr.AiJGame.BR;

import com.aijygr.AiJGame.AiJBRPlayer;
import com.aijygr.AiJGame.Game;
import com.aijygr.LIB;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class GameEnd {
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if(event.phase == TickEvent.Phase.END)
        {
            MinecraftServer server = event.getServer();
            if(Game.isGameStart && AiJBRPlayer.getAliveTeamsCount(server) <= Game.gameendteamcondition) {
                Game.isGameStart = false;
                System.out.println("Game End");
            }
        }
    }

}
