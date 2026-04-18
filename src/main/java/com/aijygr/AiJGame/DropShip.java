package com.aijygr.AiJGame;


import com.aijygr.LIB;
import com.aijygr.Main;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class DropShip {
    public static List<UUID> playerlist = new ArrayList<>();

    public static void tick(Vec3 pos){

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
