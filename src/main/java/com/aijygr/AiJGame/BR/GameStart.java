package com.aijygr.AiJGame.BR;

import com.aijygr.AiJGame.AiJBRPlayer;
import com.aijygr.AiJGame.Game;
import com.aijygr.AiJGame.Ring.RingMove;
import com.aijygr.LIB;
import com.aijygr.ModEvents;
import com.aijygr.Reg;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class GameStart {

    @SubscribeEvent
    public static void onGameStart(ModEvents.GameStartEvent event) {
        if(AiJBRPlayer.getTeamsNames(event.getLevel().getServer()).size()<=1)
        {
            LIB.tryPlayerMessage(event.getPlayer(),"msg.aijbr.red","msg.aijbr.err.command_no_enough_teams");
            return;
        }
        if(!(Game.isInitialized&&Game.isReloaded)){
            if(!Game.isInitialized){
                LIB.tryPlayerMessage(event.getPlayer(), "msg.aijbr.red","msg.aijbr.err.command_game_not_initialized");
            }
            if(!Game.isReloaded){
                LIB.tryPlayerMessage(event.getPlayer(),"msg.aijbr.red","msg.aijbr.err.command_game_not_reloaded");
            }
            return;
        }
        Game.isGameStart  = true;
        Game.sv_round = 0;
        Game.sv_roundticktotal = Game.r_initial_waitingtick+1;
        Game.sv_roundtick = Game.sv_roundticktotal;
        Game.isRingClosing = false;

        MinecraftServer server = event.getLevel().getServer();
        Entity dropship = LIB.SMwithForceLoad(server, Reg.DROPSHIP.get(), Vec3.ZERO);
        Vec3 pos = new Vec3(dropship.getX(), dropship.getY(), dropship.getZ());

        Game.teams.addAll(AiJBRPlayer.getTeamsNames(server));
        Game.playerlist = AiJBRPlayer.getPlayers(server);
        LIB.PLAYERS(server, Game.playerlist , player -> {
            player.teleportTo(pos.x, pos.y, pos.z);
        });

        RingMove.PhaseChange(event.getLevel().getServer(),"msg.aijbr.yellow");


        LIB.tryBroadcastMessage(event.getPlayer(), "msg.aijbr.yellow","msg.aijbr.info.command_game_started");
        LIB.tryBroadcastMessage(event.getPlayer(), "msg.aijbr.bold","msg.aijbr.info.command_executed");
    }
}
