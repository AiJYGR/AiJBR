package com.aijygr.AiJGame.Client;

import com.aijygr.AiJGame.AiJDropShip;
import com.aijygr.Reg;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientGame {
    public static boolean isBPSynced = false;
    public static boolean isTagSynced = false;

    public static int next_x;
    public static int next_z;
    public static double next_size;
    public static String generationMode;
    public static int round;
    public static int roundtick;
    public static boolean isShrinking;
    public static int players;
    public static int teams;
    public static void setClientRing(int x, int z, double size, String mode) {
        next_x = x;
        next_z = z;
        next_size = size;
        generationMode = mode;
    }
    public static void setClientTime(int round, int roundtick, boolean isShrinking){
        ClientGame.round = round;
        ClientGame.roundtick = roundtick;
        ClientGame.isShrinking = isShrinking;
        if(round>0)
            if(isShrinking)
                Minecraft.getInstance().player.playNotifySound(Reg.RING_CLOSE_SOUND.get(), SoundSource.AMBIENT,1.0f,1.0f);
            else
                Minecraft.getInstance().player.playNotifySound(Reg.RING_PHASE_SOUND.get(), SoundSource.AMBIENT,1.0f,1.0f);

    }
    public static int getSecond(){
        if(roundtick<=20){
            return 0;
        }
        else return (int)Math.floor((double) roundtick / 20.0);
    }
    public static void setClientPlayer(int players, int teams){
        ClientGame.players = players;
        ClientGame.teams = teams;
    }

    public static void setIsDropShipTicking(boolean bool){
        AiJDropShip.setIsDropShipTickking(bool);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if(Minecraft.getInstance().isPaused())
            return;
        if(event.phase == TickEvent.Phase.START && event.side.isClient()) {
            ClientGame.roundtick--;
        }
    }
}
