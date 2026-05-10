package com.aijygr.AiJGame.BR;

import com.aijygr.AiJGame.AiJBRPlayer;
import com.aijygr.AiJGame.Game;
import com.aijygr.LIB;
import com.aijygr.ModEvents;
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
                onGameEnd(new ModEvents.GameEndEvent(event.getServer()));
            }
        }
    }
    @SubscribeEvent
    public static void onGameEnd(ModEvents.GameEndEvent event) {
        Game.isGameStart = false;
        MinecraftServer server = event.getServer();
        Game.sv_damage_per_block = 0.00001;
        Game.sv_basicdamage = 0.0;
        LIB.schedule(server,20,()->{LIB.killItemEntities(server);});
        LIB.tryBroadcastMessage(server,"\n","msg.aijbr.bold","msg.aijbr.info.gameover");
    }
}
