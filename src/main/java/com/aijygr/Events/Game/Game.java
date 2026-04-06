package com.aijygr.Events.Game;

import com.aijygr.Events.Game.Ring.RingGeneration;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber()
public class Game {
    public static long gametime;
    public static int damage_tickingtime = 30;
    public static int sv_next_x = 0;
    public static int sv_next_z = 0;
    public static double sv_next_size = 0;
    public static int sv_curr_x = 0;
    public static int sv_curr_z = 0;
    public static double sv_curr_size = 0;
    public static double sv_r_x = 0;
    public static double sv_r_z = 0;
    public static double sv_r_size = 0;
    public static double sv_damage_per_block = 0.00001;
    public static double sv_basicdamage = 0;
    public static int r_initial_waitingtick = 0;
    public static double r_initial_ringsize = 0;
    public static boolean isInitialized = false;
    public static List<Integer> r_waiting_tick =new ArrayList<>();
    public static List<Integer> r_moving_tick = new ArrayList<>();
    public static List<Double> r_basic_damage= new ArrayList<>();
    public static List<Double> r_damage_per_block = new ArrayList<>();
    public static List<Double> r_ring_size = new ArrayList<>();
    public static List<RingGeneration.GenerationMode> r_generation_modes = new ArrayList<>();
    public static RingGeneration.WeightedMode r_weighted_mode = RingGeneration.WeightedMode.MUL2;
    public static int sv_round = -1;
    public static int sv_roundtick = 0;
    public static int totalrounds = 0;
    public static boolean isGameStart = false;
    public static boolean isRingClosing = false;
    public static int sv_roundticktotal = 0;

    public static class VecIntXZ {
        public int x;
        public int z;
        public VecIntXZ(int x, int z) {
            this.x = x;
            this.z = z;
        }
        public VecIntXZ sout(){
            System.out.println("("+this.x+","+this.z+")");
            return this;
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
    public static int getDamage_tickingtime() {
        return damage_tickingtime;
    }
}
