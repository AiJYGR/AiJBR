package com.aijygr.Events.Game;

import com.aijygr.Config;
import com.aijygr.Events.Game.Ring.GameInitEvent;
import com.aijygr.Events.Game.Ring.RingGeneration;
import com.aijygr.Main;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;

import static com.aijygr.Events.Game.Ring.RingGeneration.Generate;

@Mod.EventBusSubscriber()
public class GameInitialization {


    private static ServerPlayer player;

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
        Main.LOGGER.info("Successfully read ring attributes.");
    }



    private static void tryPlayerMessage(ServerPlayer player, String message) {
        if(player!=null){
            player.sendSystemMessage(Component.translatable(message));
            Main.LOGGER.info("[AiJBR]PlayerMessage:{}", message);
        }
        else {
            Main.LOGGER.info("[AiJBR]tryPlayerMessage:{}", message);
        }
    }
    //initialization
    @SubscribeEvent
    public static void onGameInit(GameInitEvent event) {
        if(event.getLevel().isClientSide()) return;
        Game.r_ring_size.clear();
        Game.r_waiting_tick.clear();
        Game.r_basic_damage.clear();
        Game.r_damage_per_block.clear();
        Game.r_moving_tick.clear();
        Game.r_generation_modes.clear();

        Game.isInitialized = false;
        ServerPlayer player = event.getPlayer();
        WorldBorder worldBorder =event.getLevel().getWorldBorder();
        Game.isGameStart = false;
        Game.sv_next_x = 0;
        Game.sv_next_z = 0;
        Game.sv_curr_x = 0;
        Game.sv_curr_z = 0;

        Game.sv_r_x = Game.sv_curr_x;
        Game.sv_r_z = Game.sv_curr_z;
        Game.damage_tickingtime = Config.ServerConfig.DAMAGE_TICKING_TIME.get();

        //List<GenerationMode>   <---   List<String>
        var list_str = Config.ServerConfig.GENERATIONMODES.get();
        var LIST_ENUM = RingGeneration.GenerationMode.values();
        Game.r_generation_modes.clear();
        for(int i = 0; i < list_str.size(); i++) {
            System.out.println(i);
            for(int j = 0 ; j < LIST_ENUM.length; j++ ) {
                if(LIST_ENUM[j].name().equalsIgnoreCase(list_str.get(i))) {
                    Game.r_generation_modes.add(LIST_ENUM[j]);
                }
            }
        }


        var ring_initial = Config.ServerConfig.RING_INITIAL_ATTRUBUTES.get();
        Game.r_initial_ringsize = ring_initial.get(0); //InitialRingSize, WaitingTick
        Game.sv_curr_size = Game.r_initial_ringsize;
        Game.r_initial_waitingtick = ring_initial.get(1);
        System.out.println(Game.r_initial_waitingtick+" "+Game.r_initial_ringsize);
        worldBorder.setSize(Game.sv_curr_size);
        worldBorder.setCenter(Game.sv_curr_x,Game.sv_curr_z);

        var ring_initial_attributes = Config.ServerConfig.RING_ATTRIBUTES.get();

        //Read RingAttributes
        try {
            readRingAttributesConfig(ring_initial_attributes);
        }catch(Exception e){
            String str = ("§4[AiJBR]§r RingAttributesInitialization failed. "+e.getMessage()+" *Please check the TOML file.");
            tryPlayerMessage(player, str);
            String str2 = "§4[AiJBR]§r Trying to use Default RingAttributesConfig.";
            tryPlayerMessage(player, str2);
            var default_ring_initial_attributes = Config.ServerConfig.Default.RING_ATTRIBUTES;
            try{
                readRingAttributesConfig(default_ring_initial_attributes);
                String str3 = "§4[AiJBR]§r Default RingAttributesConfig Loaded.";
                tryPlayerMessage(player, str3);
            }
            catch (Exception e1){
                String str4 = ("§4[AiJBR]§r §cHey AiJYGR, what are you doing? "+e1.getMessage());
                String str5 = "§4[AiJBR]§r Initialization failed";
                tryPlayerMessage(player, str4);
                tryPlayerMessage(player, str5);
                return ;
            }
        }
        //tryPlayerMessage(player,(Game.totalrounds)+"rounds read.");

        //RingGeneration.Generate(256,256,256,64, Game.generation_modes.get(0));
        Game.sv_next_size = Game.r_initial_ringsize;
        Game.sv_damage_per_block = Game.r_damage_per_block.get(0);
        Game.sv_basicdamage = Game.r_basic_damage.get(0);

        for(var it = Game.r_generation_modes.stream().iterator();it.hasNext();)
        {
            System.out.println(it.next().name());
        }

        tryPlayerMessage(player,"aijbr.command.executed");
        Game.isInitialized = true;
    }
}
