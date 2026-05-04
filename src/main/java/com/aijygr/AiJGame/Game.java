package com.aijygr.AiJGame;

import com.aijygr.*;
import com.aijygr.AiJGame.BR.GameInitialization;
import com.aijygr.AiJGame.BR.GameStart;
import com.aijygr.AiJGame.Client.MSGClientGameTime;
import com.aijygr.AiJGame.Client.MSGClientPlayerInfo;
import com.aijygr.AiJGame.Client.MSGClientRingInfo;
import com.aijygr.AiJGame.Ring.RingGeneration;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.List;

@Mod.EventBusSubscriber()
public class Game {
    public static long gametime;
    public static long BRGameTime = 0;
    public static int damage_tickingtime = 30;
    public static int sv_curr_x = 0;
    public static int sv_curr_z = 0;
    public static double sv_curr_size = 0;
    public static int sv_next_x = 0;
    public static int sv_next_z = 0;
    public static double sv_next_size = 0;
    //curr next:只在回合更新的时候刷新
    public static double sv_r_x = 0;
    public static double sv_r_z = 0;
    public static double sv_r_size = 0;
    //实时刷新
    public static double sv_damage_per_block = 0.00001;
    public static double sv_basicdamage = 0;
    public static int r_initial_waitingtick = 0;
    public static double r_initial_ringsize = 0;
    public static boolean isInitialized = false;
    public static boolean isReloaded = false;
    public static int gameendteamcondition = 1;
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
    //public static boolean enableAiJBP;
    public static final double R = 6000000.0d;
    public static final int TRAVELTICKS = 300;

    /// 这个名字只是用惯了，和游戏刻tick没什么关系。
    /// 大概含义就是服务器存储的一个数值，如果和箱子的这个数值不同就需要刷新一下箱子的战利品表
    public static int refillTick = 0;
    public static Map<String,TeamStatus> teamlist = new HashMap<>();
    public static Map<UUID,PlayerStatus> playerlist = new HashMap<>();

    public enum PlayerStatus{
        ALIVE,
        //DBNO,
        DEAD
    }
    public enum TeamStatus{
        ALIVE,
        DEAD
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if(LIB.SV1TK(event))
        {
            gametime = event.level.getGameTime();
            if(Game.isGameStart)
            {
                BRGameTime++;
                sv_roundtick--;
            }

        }
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        Game.isReloaded = false;
        Game.isInitialized = false;
        Game.isGameStart = false;
        Game.playerlist = new HashMap<>();
        Game.sv_damage_per_block = 0.00001;
        Game.sv_basicdamage = 0;
    }

    @SubscribeEvent
    public static void onGameInit(ModEvents.GameInitEvent event) {
        GameInitialization.GameInit(event);
        AiJBRPlayer.initTeams(ModConfig.Server.Config.TEAM.TEAMNUM.get(), ModConfig.Server.Config.TEAM.TEAMSIZE.get(),event.getLevel().getScoreboard());
        AiJBRPlayer.onGameInit(event);
        ModMessages.ServerSendToAll(new MSGClientGameTime(0,0,false));
        ModMessages.ServerSendToAll(new MSGClientPlayerInfo(0,0));
        ModMessages.ServerSendToAll(new MSGClientRingInfo(0,0,0,""));
    }
    @SubscribeEvent
    public static void onGameStart(ModEvents.GameStartEvent event) {
        ModMessages.ServerSendToAll(new MSGClientRingInfo(0,0,0,""));
        GameStart.onGameStart(event);
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        //Set GameRule
        MinecraftServer server = event.getServer();
        GameRules rules = server.getGameRules();
        //Spawns
        rules.getRule(GameRules.RULE_DOINSOMNIA).set(false,server);
        rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false,server);
        rules.getRule(GameRules.RULE_DO_PATROL_SPAWNING).set(false,server);
        rules.getRule(GameRules.RULE_DO_TRADER_SPAWNING).set(false,server);
        rules.getRule(GameRules.RULE_DO_WARDEN_SPAWNING).set(false,server);

        rules.getRule(GameRules.RULE_DAYLIGHT).set(false,server);
        rules.getRule(GameRules.RULE_NATURAL_REGENERATION).set(false,server);

        rules.getRule(GameRules.RULE_SHOWDEATHMESSAGES).set(true,server);
        //rules.getRule(GameRules.RULE_COMMANDBLOCKOUTPUT).set(true,server);
        rules.getRule(GameRules.RULE_RANDOMTICKING).set(0,server);

        rules.getRule(GameRules.RULE_SPAWN_RADIUS).set(0,server);
        rules.getRule(GameRules.RULE_DO_IMMEDIATE_RESPAWN).set(false,server);


        server.getLevel(ServerLevel.OVERWORLD).getWorldBorder().setDamagePerBlock(0.0);
        server.getLevel(ServerLevel.OVERWORLD).getWorldBorder().setWarningBlocks(1);
    }
}
