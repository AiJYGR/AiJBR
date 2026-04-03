package com.aijygr.Events.Game;


import com.aijygr.Config;
import com.aijygr.Events.Game.Ring.GameInitEvent;
import com.aijygr.Main;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class GameInitialization {

    private static boolean isGameStart = false;
    public static long gametime = -1;



    //initialization
    @SubscribeEvent
    public static void onGameInit(GameInitEvent event) {
        Player player = event.getPlayer();
        WorldBorder worldBorder =event.getLevel().getWorldBorder();
        isGameStart = false;
        Game.next_x = 0;
        Game.next_z = 0;
        Game.curr_x = 0;
        Game.curr_z = 0;

        var ring_initial = Config.SERVERCFG_RING_INITIAL_ATTRUBUTES.get();
        Game.r_initial_ringsize = ring_initial.get(0); //InitialRingSize, WaitingTick
        Game.curr_size = Game.r_initial_ringsize;
        Game.r_initial_waitingtick = ring_initial.get(1);
        worldBorder.setSize(Game.curr_size);
        worldBorder.setCenter(0,0);

        var ring_initial_attributes = Config.SERVERCFG_RING_PHASE_ATTRIBUTES.get();
        Game.totalrounds = 0;
        for(var it = ring_initial_attributes.iterator() ; it.hasNext(); Game.totalrounds++){
            String attribute = it.next();
            String[] parts = attribute.split(",");//RingSize,   WaitingTick,BasicDamage,DamagePerBlock,  ClosingTick,BasicDamage,DamagePerBlock
            if(parts.length >= 7){
                try{
                    Game.r_ring_size.add(Double.parseDouble(parts[0].trim()));
                    Game.r_waiting_tick.add(Integer.parseInt(parts[1].trim()));
                    Game.r_basic_damage.add(Double.parseDouble(parts[2].trim()));
                    Game.r_damage_per_block.add(Double.parseDouble(parts[3].trim()));
                    Game.r_moving_tick.add(Integer.parseInt(parts[4].trim()));
                    Game.r_basic_damage.add(Double.parseDouble(parts[5].trim()));
                    Game.r_damage_per_block.add(Double.parseDouble(parts[6].trim()));
                }
                catch(Exception e){
                    System.out.println(parts[0]);
                    Main.LOGGER.warn("[AiJBR]Ring Attribute Initialization Failed: At Round "+Game.totalrounds+", Please check the Server TOML file.\n"+e.getLocalizedMessage());
                }
            }
        }

        for(var it = Game.r_ring_size.iterator() ; it.hasNext() ; )
        {
            System.out.println(it.next());
        }


        if (player != null)
            player.sendSystemMessage(Component.translatable("aijbr.command.executed"));
    }
}
