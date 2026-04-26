package com.aijygr.AiJGame.BR;

import com.aijygr.AiJGame.AiJDropShip;
import com.aijygr.AiJGame.Game;
import com.aijygr.LIB;
import com.aijygr.ModConfig;
import com.aijygr.AiJGame.Ring.RingGeneration;
import com.aijygr.ModEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber()
public class GameInitialization {
    //private static ServerPlayer player;

    private static void readRingAttributesConfig(List<? extends String> ringattributes) throws Exception {
        Game.totalrounds = 0;
        for (var it = ringattributes.iterator(); it.hasNext(); Game.totalrounds++) {
            String attribute = it.next();
            String[] parts = attribute.split(",");//RingSize,   WaitingTick,BasicDamage,DamagePerBlock,  ClosingTick,BasicDamage,DamagePerBlock
            if (parts.length == 7) {
                try {
                    Game.r_ring_size.add(Double.parseDouble(parts[0].trim()));
                    Game.r_waiting_tick.add(Integer.parseInt(parts[1].trim()));
                    Game.r_basic_damage.add(Double.parseDouble(parts[2].trim()));
                    Game.r_damage_per_block.add(Double.parseDouble(parts[3].trim()));
                    Game.r_moving_tick.add(Integer.parseInt(parts[4].trim()));
                    Game.r_basic_damage.add(Double.parseDouble(parts[5].trim()));
                    Game.r_damage_per_block.add(Double.parseDouble(parts[6].trim()));
                } catch (Exception e) {
                    throw new Exception("At Round:" + (Game.totalrounds+1) + "  " + e.getMessage());
                }
            } else throw new Exception("At Round:"+ (Game.totalrounds+1) +"  Inputs must be 7.");
        }
        if(Game.totalrounds == 0 ){
            throw new Exception(" Input is empty.");
        }
    }

    public static void GameInit(ModEvents.GameInitEvent event) {
        if(event.getLevel().isClientSide())
            return;
        ServerPlayer player = event.getPlayer();

        //Reset SV
        LIB.tryBroadcastMessage(player," ");
        LIB.tryBroadcastMessage(player, "msg.aijbr.yellow","Starting INIT.");
        Game.isInitialized = false;
        Game.isGameStart = false;
        Game.r_ring_size.clear();
        Game.r_waiting_tick.clear();
        Game.r_basic_damage.clear();
        Game.r_damage_per_block.clear();
        Game.r_moving_tick.clear();
        Game.r_generation_modes.clear();

        WorldBorder worldBorder = event.getLevel().getWorldBorder();
        Game.sv_next_x = 0;
        Game.sv_next_z = 0;
        Game.sv_curr_x = 0;
        Game.sv_curr_z = 0;
        Game.sv_r_x = Game.sv_curr_x;
        Game.sv_r_z = Game.sv_curr_z;
        Game.damage_tickingtime = ModConfig.Server.Config.RING.DAMAGE_TICKING_TIME.get();

        Game.BRGameTime = 0;
        Game.gameendteamcondition = 1;

        AiJDropShip.dropshipPlayerlist.clear();
        AiJDropShip.isTick = false;

        //Read RingAttributes CFG
        var ring_initial = ModConfig.Server.Config.RING.RING_INITIAL_ATTRUBUTES.get();
        Game.r_initial_ringsize = ring_initial.get(0); //InitialRingSize, WaitingTick
        Game.sv_curr_size = Game.r_initial_ringsize;
        Game.r_initial_waitingtick = ring_initial.get(1);
        worldBorder.setSize(Game.sv_curr_size);
        worldBorder.setCenter(Game.sv_curr_x,Game.sv_curr_z);
        var ring_initial_attributes = ModConfig.Server.Config.RING.RING_ATTRIBUTES.get();
        try {
            readRingAttributesConfig(ring_initial_attributes);
            LIB.tryBroadcastMessage(player, "msg.aijbr.green","Successfully loaded RingAttributes, "+(Game.totalrounds)+" rounds read.");
        }catch(Exception e){
            String str = ("RingAttributesInitialization failed. "+e.getMessage()+" *Please check the TOML file.");
            LIB.tryBroadcastMessage(player,"msg.aijbr.red", str);
            boolean usingDefaultsWhenFailed = false;//Don't use default config.
            if(usingDefaultsWhenFailed){
                String str2 = "Trying to use Default RingAttributesConfig.";
                LIB.tryBroadcastMessage(player,"msg.aijbr.yellow", str2);
                var default_ring_initial_attributes = ModConfig.Server.Default.RING.RING_ATTRIBUTES;
                try{
                    readRingAttributesConfig(default_ring_initial_attributes);
                    String str3 = "Default RingAttributesConfig Loaded. "+Game.totalrounds+" rounds read.";
                    LIB.tryBroadcastMessage(player, "msg.aijbr.yellow",str3);
                }
                catch (Exception e1){
                    String str4 = ("Hey AiJYGR, what are you doing? "+e1.getMessage());
                    String str5 = "Initialization failed";
                    LIB.tryBroadcastMessage(player, "msg.aijbr.red",str4);
                    LIB.tryBroadcastMessage(player,"msg.aijbr.red",str5);
                    return ;
                }
            }
            else
                return;
        }

        //Set Initial Attributes
        Game.sv_next_size = Game.r_initial_ringsize;
        Game.sv_damage_per_block = Game.r_damage_per_block.get(0);
        Game.sv_basicdamage = Game.r_basic_damage.get(0);

        //Read Generation Mode CFG
        //List<GenerationMode>   <---   List<String>
        var list_str = ModConfig.Server.Config.RING.GENERATIONMODES.get();
        var LIST_ENUM = RingGeneration.GenerationMode.values();
        Game.r_generation_modes.clear();
        for(int i = 0; i < list_str.size(); i++) {
            for(int j = 0 ; j < LIST_ENUM.length; j++ ) {
                if(LIST_ENUM[j].name().equalsIgnoreCase(list_str.get(i).trim())) {
                    Game.r_generation_modes.add(LIST_ENUM[j]);
                }
            }
        }
        if(Game.totalrounds != Game.r_generation_modes.size()) {
            StringBuilder str = new StringBuilder();
            for(var it = Game.r_generation_modes.stream().iterator();it.hasNext();)
            {
                str.append(it.next()).append(",");
            }
            LIB.tryBroadcastMessage(player, "msg.aijbr.red","Failed to load GenerationModes.\nGet: "+str+" only "+Game.r_generation_modes.size()+" values read.");
            return ;
        }
        else
            LIB.tryBroadcastMessage(player,"msg.aijbr.green","Successfully loaded GenerationModes, "+Game.r_generation_modes.size()+" values read.");

        //Set GameRule
        ServerLevel level = event.getLevel();
        GameRules rules = level.getGameRules();
        rules.getRule(GameRules.RULE_DAYLIGHT).set(false,event.getLevel().getServer());
        rules.getRule(GameRules.RULE_NATURAL_REGENERATION).set(false,event.getLevel().getServer());
        rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false,event.getLevel().getServer());
        rules.getRule(GameRules.RULE_DOINSOMNIA).set(false,event.getLevel().getServer());
        rules.getRule(GameRules.RULE_SHOWDEATHMESSAGES).set(true,event.getLevel().getServer());
        rules.getRule(GameRules.RULE_COMMANDBLOCKOUTPUT).set(true,event.getLevel().getServer());
        rules.getRule(GameRules.RULE_RANDOMTICKING).set(0,event.getLevel().getServer());

        //Set Player
        LIB.PLAYERS(event.getLevel().getServer().getPlayerList().getPlayers(),(svplayer)->{
            svplayer.setNoGravity(false);
            svplayer.removeAllEffects();
            svplayer.setGameMode(GameType.SURVIVAL);
            player.addEffect(new MobEffectInstance(MobEffects.SATURATION, MobEffectInstance.INFINITE_DURATION, 0, false, false));
            LIB.TPTop(svplayer,0,0);
        });

        //结束指令
        Game.isInitialized = true;
        LIB.tryBroadcastMessage(player,"msg.aijbr.bold","msg.aijbr.info.command_executed");
    }
}
