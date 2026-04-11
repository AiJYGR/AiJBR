package com.aijygr.AiJGame;

import com.aijygr.AiJGame.Ring.GameStartEvent;
import com.aijygr.AiJGame.Ring.RingMove;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class GameStart {

    @SubscribeEvent
    public static void onGameStart(GameStartEvent event) {
        if(!Game.isInitialized){
            Game.tryPlayerMessage(event.getPlayer(), "msg.aijbr.red","msg.aijbr.err.command_game_not_initialized");
            return;
        }
        Game.isGameStart  = true;
        Game.sv_round = 0;
        Game.sv_roundticktotal = Game.r_initial_waitingtick+1;
        Game.sv_roundtick = Game.sv_roundticktotal;
        //var vec2 = RingGeneration.Generate(Game.sv_curr_x,Game.sv_curr_z,Game.sv_curr_size,Game.sv_curr_size,Game.r_generation_modes.get(0));
        //Game.sv_next_x = vec2.x;
        //Game.sv_next_z = vec2.z;
        Game.isRingClosing = false;
        RingMove.PhaseChange();

        Game.tryPlayerMessage(event.getPlayer(), "msg.aijbr.yellow","msg.aijbr.info.command_game_started");
        Game.tryPlayerMessage(event.getPlayer(), "msg.aijbr.bold","msg.aijbr.info.command_executed");
    }
}
