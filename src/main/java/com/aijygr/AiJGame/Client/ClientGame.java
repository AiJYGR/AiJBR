package com.aijygr.AiJGame.Client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientGame {
    public static int next_x;
    public static int next_z;
    public static double next_size;
    public static String generationMode;
    public static void setClientRing(int x, int z, double size, String mode) {
        next_x = x;
        next_z = z;
        next_size = size;
        generationMode = mode;
    }
    public static int round;
    public static int roundtick;
    public static void setClientRound(int round,int roundtick){
        ClientGame.round = round;
        ClientGame.roundtick = roundtick;
    }
    public static int getSecond(){
        if(roundtick<=0){
            return 0;
        }
        else return (int)Math.ceil((double) roundtick / 20.0);
    }

    public static int players;
    public static int teams;
    public static void setClientGame(int players, int teams){
        ClientGame.players = players;
        ClientGame.teams = teams;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.START && event.side.isClient()) {
            ClientGame.roundtick--;
        }
    }
}
