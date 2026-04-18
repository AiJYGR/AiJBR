package com.aijygr.AiJGame.BR;

import com.aijygr.AiJGame.AiJBRPlayer;
import com.aijygr.AiJGame.Game;
import com.aijygr.AiJGame.GameStartEvent;
import com.aijygr.AiJGame.Ring.RingMove;
import com.aijygr.Reg;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class GameStart {

    @SubscribeEvent
    public static void onGameStart(GameStartEvent event) {
        if(AiJBRPlayer.getTeams(event.getLevel().getServer()).size()<=1)
        {
            Game.tryPlayerMessage(event.getPlayer(),"msg.aijbr.red","msg.aijbr.err.command_no_enough_teams");
            return;
        }
        if(!(Game.isInitialized&&Game.isReloaded)){
            if(!Game.isInitialized){
                Game.tryPlayerMessage(event.getPlayer(), "msg.aijbr.red","msg.aijbr.err.command_game_not_initialized");
            }
            if(!Game.isReloaded){
                Game.tryPlayerMessage(event.getPlayer(),"msg.aijbr.red","msg.aijbr.err.command_game_not_reloaded");
            }
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

        Game.SMwithForceLoad(event.getLevel().getServer(),Reg.DROPSHIP.get(), Vec3.ZERO);



        RingMove.PhaseChange(event.getLevel().getServer(),"msg.aijbr.yellow");

        Game.tryBroadcastMessage(event.getPlayer(), "msg.aijbr.yellow","msg.aijbr.info.command_game_started");
        Game.tryBroadcastMessage(event.getPlayer(), "msg.aijbr.bold","msg.aijbr.info.command_executed");
    }
}
