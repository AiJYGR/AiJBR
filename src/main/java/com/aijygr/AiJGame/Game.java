package com.aijygr.AiJGame;

import com.aijygr.AiJGame.BR.GameInitialization;
import com.aijygr.AiJGame.Ring.RingGeneration;
import com.aijygr.LIB;
import com.aijygr.ModConfig;
import com.aijygr.ModEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

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
    public static boolean isReloaded = false;
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
    public static boolean enableAiJBP;
    public static final double R = 6000000.0d;
    public static List<UUID> playerlist = new ArrayList<>();
    public static List<String> teams = new ArrayList<>();



    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if(LIB.SVLV1TK(event))
        {
            gametime = event.level.getGameTime();
        }
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        Game.isReloaded = false;
        Game.isInitialized = false;
        Game.isGameStart = false;
        Game.playerlist = new ArrayList<>();
    }

    @SubscribeEvent
    public static void onGameInit(ModEvents.GameInitEvent event) {
        GameInitialization.GameInit(event);
        AiJBRPlayer.initTeams(ModConfig.Server.Config.TEAM.TEAMNUM.get(), ModConfig.Server.Config.TEAM.TEAMSIZE.get(),event.getLevel().getScoreboard());
    }
}
