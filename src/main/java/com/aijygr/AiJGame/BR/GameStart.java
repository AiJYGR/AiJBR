package com.aijygr.AiJGame.BR;

import com.aijygr.*;
import com.aijygr.AiJGame.AiJBRPlayer;
import com.aijygr.AiJGame.AiJDropShip;
import com.aijygr.AiJGame.Game;
import com.aijygr.AiJGame.Ring.RingMove;
import com.aijygr.Entity.DropShip;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber()
public class GameStart {

    @SubscribeEvent
    public static void onGameStart(ModEvents.GameStartEvent event) {
        //检查是否初始化/重载
        if(!(Game.isInitialized&&Game.isReloaded)){
            if(!Game.isInitialized){
                LIB.tryPlayerMessage(event.getPlayer(), "msg.aijbr.red","msg.aijbr.err.command_game_not_initialized");
            }
            if(!Game.isReloaded){
                LIB.tryPlayerMessage(event.getPlayer(),"msg.aijbr.red","msg.aijbr.err.command_game_not_reloaded");
            }
            return;
        }

        //获取玩家和队伍列表
        MinecraftServer server = event.getLevel().getServer();
        for(String str : AiJBRPlayer.getTeamsNames(server))
            Game.teamlist.put(str, Game.TeamStatus.ALIVE);
        for(UUID uuid : AiJBRPlayer.getPlayers(server))
            Game.playerlist.put(uuid, Game.PlayerStatus.ALIVE);

        //检查队伍数量
        AiJBRPlayer.updateAndBroadcastPlayerInfo(server);
        if(AiJBRPlayer.getAliveTeamsCount(server)==0)
        {
            LIB.tryPlayerMessage(event.getPlayer(),"msg.aijbr.red","msg.aijbr.err.command_no_enough_teams");
            return;
        }
        Game.gameendteamcondition = 1;
        if(AiJBRPlayer.getAliveTeamsCount(server)==1)
        {
            LIB.tryPlayerMessage(event.getPlayer(),"msg.aijbr.yellow","msg.aijbr.warn.command_disabled_game_end_check");
            Game.gameendteamcondition = 0;
        }

        //开启服务端参数
        Game.BRGameTime = 0;
        Game.sv_round = 0;
        Game.sv_roundticktotal = Game.r_initial_waitingtick+1;
        Game.sv_roundtick = Game.sv_roundticktotal;
        Game.isRingClosing = false;

        //生成DropShip
        double altitude = ModConfig.Server.Config.DROPSHIP.HEIGHT.get();
        if(LIB.SMwithForceLoad(server, Reg.DROPSHIP.get(), new Vec3(0,altitude,0)) instanceof DropShip dropship)
        {
            AiJDropShip.isTick = true;
            dropship.setUUID(AiJDropShip.DROPSHIPUUID);
            Vec3 pos = new Vec3(dropship.getX(), dropship.getY(), dropship.getZ());

            List<Double> d = AiJDropShip.generate();
            double v = ModConfig.Server.Config.DROPSHIP.SPEED.get();
            Vec3 pos1 = new Vec3(d.get(0),altitude,d.get(1));
            Vec3 pos2 = new Vec3(d.get(2),altitude,d.get(3));
            Vec3 facing = pos2.subtract(pos1).normalize();
            Vec3 motion = facing.scale(v);
            Vec3 startingpoint = pos1.subtract(motion.scale(Game.TRAVELTICKS));

            LIB.TPwithForceLoad(dropship,startingpoint);

            for(UUID uuid : Game.playerlist.keySet()){
                AiJDropShip.playerGetOn(server, uuid, dropship);
            }
            dropship.setMotion(motion);
            double distance = pos1.distanceTo(pos2);
            AiJDropShip.dropship_allowejecttick = (int)Math.ceil( distance/v*0.05 + Game.TRAVELTICKS);
            AiJDropShip.dropship_forceejecttick = (int)Math.ceil( distance/v*0.95 + Game.TRAVELTICKS);
        }
        else{
            LIB.tryBroadcastMessage(event.getPlayer(),"msg.aijbr.red","msg.aijbr.err");
            return;
        }

        RingMove.PhaseChange();

        //结束指令
        Game.isGameStart  = true;
        LIB.tryBroadcastMessage(event.getPlayer(), "msg.aijbr.yellow","msg.aijbr.info.command_game_started");
        LIB.tryBroadcastMessage(event.getPlayer(), "msg.aijbr.bold","msg.aijbr.info.command_executed");
    }
}
