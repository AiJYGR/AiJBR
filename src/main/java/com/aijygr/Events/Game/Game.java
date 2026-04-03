package com.aijygr.Events.Game;

import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber()
public class Game {
    public static long gametime;
    public static short damage_tickingtime = 30;
    public static float basic_damage = 0.5f;
    public static float damage_per_block =  0.01f;
    public static int next_x = 0;
    public static int next_z = 0;
    public static int next_size = 0;
    public static int curr_x = 0;
    public static int curr_z = 0;
    public static int curr_size = 0;
    public static int r_initial_waitingtick = 0;
    public static int r_initial_ringsize = 0;
    public static ArrayList<Integer> r_waiting_tick =new ArrayList<>();
    public static ArrayList<Integer> r_moving_tick = new ArrayList<>();
    public static ArrayList<Double> r_basic_damage= new ArrayList<>();
    public static ArrayList<Double> r_damage_per_block = new ArrayList<>();
    public static ArrayList<Double> r_ring_size = new ArrayList<>();
    public static int round = -1;
    public static int totalrounds = 0;

    public static class VecIntXZ {
        public int x;
        public int z;
        public VecIntXZ(int x, int z) {
            this.x = x;
            this.z = z;
        }
    }


    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if((event.phase == TickEvent.Phase.START) && (event.level.dimension().equals(Level.OVERWORLD))
                && (!event.level.isClientSide()))
        {
            gametime = event.level.getGameTime();
        }
    }

    public static long getGametime(){
        return gametime;
    }
    public static short getDamage_tickingtime() {
        return damage_tickingtime;
    }
    public static float getBasic_damage() {
        return basic_damage;
    }
    public static float getDamage_per_block() {
        return damage_per_block;
    }
}
