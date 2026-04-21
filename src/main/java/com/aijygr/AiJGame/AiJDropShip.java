package com.aijygr.AiJGame;


import com.aijygr.Entity.DropShip;
import com.aijygr.LIB;
import com.aijygr.Main;
import com.aijygr.ModConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class AiJDropShip {
    public static final UUID DROPSHIPUUID = LIB.UUID(Main.MODUUIDP1,Main.MODUUIDP2,1,0);
    public static List<UUID> playerlist = new ArrayList<>();
    public static long dropship_forceejecttick = 2000;
    public static long dropship_allowejecttick = 100;
    public static boolean canleave = false;
    public static boolean isTick = false;
    public static List<Double> generate(){
        double x1,z1,x2,z2;
        final double Range = Game.r_initial_ringsize / 2;
        final double r1 = 0.9, r2 = 0.7;
        if(Math.random()<0.5){
            x1 = -1.0;
            z1 = (Math.random()*2-1)*r1;
            x2 = 1.0;
            z2 = (Math.random()*2-1)*r2;
        }
        else{
            x1 = (Math.random()*2-1)*r1;
            z1 = -1.0;
            x2 = (Math.random()*2-1)*r2;
            z2 = 1.0;
        }
        if(Math.random()<0.5){
            double temp = x1;
            x1 = x2;
            x2 = temp;
            temp = z1;
            z1 = z2;
            z2 = temp;
        }//swap(x1,x2) (z1,z2)
        x1*=Range;
        z1*=Range;
        x2*=Range;
        z2*=Range;
        System.out.println("x1: "+x1+" z1: "+z1);
        System.out.println("x2: "+x2+" z2: "+z2);
        System.out.println("Range: " + Range);
        System.out.println("Speed: "+ ModConfig.Server.Config.DROPSHIP.SPEED.get());
        return List.of(x1,z1,x2,z2);
    }

    public static void tick(MinecraftServer server, DropShip dropship){
        if(!isTick) return;
        if(playerlist.isEmpty()){
            dropship.setUUID(UUID.randomUUID());
            System.out.println(dropship.getUUID());
        }
        if(Game.BRGameTime>=dropship_allowejecttick && !canleave)
        {
            canleave = true;
            System.out.println("Canleave "+canleave);
        }
        if(Game.BRGameTime == dropship_forceejecttick && !playerlist.isEmpty()){
            System.out.println("ForceEject");
            LIB.PLAYERS(server,playerlist,player -> {
                System.out.println(player.getName().getString());
            });
            List<UUID> templist = new ArrayList<>(playerlist);
            dropship.ejectAllPassengers(server,templist);
        }
    }
    public static void playerGetOn(MinecraftServer server, UUID uuid, DropShip dropship){
        if(!playerlist.contains(uuid)){
            ServerPlayer player = server.getPlayerList().getPlayer(uuid);
            if (player != null) {
                playerlist.add(uuid);
                //player.setNoGravity(true);
                player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, MobEffectInstance.INFINITE_DURATION, 1, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, MobEffectInstance.INFINITE_DURATION, 5, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, MobEffectInstance.INFINITE_DURATION, 5, false, false));

                player.startRiding(dropship,true);
            }
        }
    }
    public static void playerLeave(ServerPlayer player){
        if (player != null) {
            playerlist.remove(player.getUUID());
            player.setNoGravity(false);
            player.removeEffect(MobEffects.INVISIBILITY);
            player.removeEffect(MobEffects.REGENERATION);
            player.removeEffect(MobEffects.DAMAGE_RESISTANCE);
        }
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if(LIB.SVLV1TK(event)){
            LIB.PLAYERS(event.level.getServer(),playerlist,(player)->{
                //player.teleportTo();
            });
        }
    }
}
