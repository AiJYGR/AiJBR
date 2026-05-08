package com.aijygr.AiJGame.BR;

import com.aijygr.*;
import com.aijygr.AiJGame.AiJBRPlayer;
import com.aijygr.AiJGame.AiJDropShip;
import com.aijygr.AiJGame.Game;
import com.aijygr.AiJGame.Ring.RingMove;
import com.aijygr.Entity.DropShip;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public class GameStart {

    ///{@link Game#onGameStart(ModEvents.GameStartEvent)}
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

        if(Game.isGameStart){
            LIB.tryPlayerMessage(event.getPlayer(),"msg.aijbr.red","msg.aijbr.err.command_game_started");
            return;
        }

        //获取玩家和队伍列表
        MinecraftServer server = event.getLevel().getServer();
        for(String str : AiJBRPlayer.getTeamsNames(server))
            Game.teamlist.put(str, Game.TeamStatus.ALIVE);
        for(UUID uuid : AiJBRPlayer.getPlayers(server))
            Game.playerlist.put(uuid, Game.PlayerStatus.ALIVE);

        for(ServerPlayer player : server.getPlayerList().getPlayers()){
            if(Game.playerlist.containsKey(player.getUUID())){
                AiJBRPlayer.resetPlayerAttributes(player);
            }
            else
            {
                AiJBRPlayer.setSpectator(player);
            }
        }

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
        //以下片段摘自GameInitialization 2026 05 02
        {
            WorldBorder worldBorder = event.getLevel().getWorldBorder();
            Game.sv_next_x = 0;
            Game.sv_next_z = 0;
            Game.sv_curr_x = 0;
            Game.sv_curr_z = 0;
            Game.sv_r_x = Game.sv_curr_x;
            Game.sv_r_z = Game.sv_curr_z;
            Game.damage_tickingtime = ModConfig.Server.Config.RING.DAMAGE_TICKING_TIME.get();

            Game.BRGameTime = 0;



            Game.sv_curr_size = Game.r_initial_ringsize;
            worldBorder.setSize(Game.sv_curr_size);
            worldBorder.setCenter(Game.sv_curr_x,Game.sv_curr_z);
            Game.sv_next_size = Game.r_initial_ringsize;

            Game.sv_damage_per_block = Game.r_damage_per_block.get(0);
            Game.sv_basicdamage = Game.r_basic_damage.get(0);
        }
        //清理掉落物 清空背包
        LIB.killItemEntities(server);
        LIB.clearPlayersInv(server);
        //生成DropShip
        for (ServerLevel level : server.getAllLevels()) {
            level.getEntities().getAll().forEach(entity -> {
                if (entity.getType() == Reg.DROPSHIP.get()) {
                    UUID uuid = UUID.randomUUID();
                    entity.setUUID(uuid);
                    System.out.println("Dropship " + entity.getId() + " UUID reset: " + uuid);
                }
            });
        }
        double altitude = ModConfig.Server.Config.DROPSHIP.HEIGHT.get();
        AiJDropShip.dropshipPlayerlist.clear();
        AiJDropShip.canleave = false;
        if(LIB.SMwithForceLoad(server, Reg.DROPSHIP.get(), new Vec3(0,altitude,0)) instanceof DropShip dropship)
        {

            dropship.setUUID(AiJDropShip.DROPSHIPUUID);
            Vec3 pos = new Vec3(dropship.getX(), dropship.getY(), dropship.getZ());

            List<Double> d = AiJDropShip.generate();
            double v = ModConfig.Server.Config.DROPSHIP.SPEED.get();
            Vec3 pos1 = new Vec3(d.get(0),altitude,d.get(1));
            Vec3 pos2 = new Vec3(d.get(2),altitude,d.get(3));
            Vec3 facing = pos2.subtract(pos1).normalize();
            Vec3 motion = facing.scale(v);
            Game.travelTick = ModConfig.Server.Config.DROPSHIP.PREWAITINGTICK.get();
            Game.shouldTravel = ModConfig.Server.Config.DROPSHIP.SHOULDFLYTOBATTLEFIELD.get().get();
            Vec3 startingpoint;
            if(Game.shouldTravel) {
                AiJDropShip.setandSendClientIsDropShipTickking(true);
                startingpoint = pos1.subtract(motion.scale(Game.travelTick));
            }
            else
            {
                AiJDropShip.setandSendClientIsDropShipTickking(false);
                startingpoint = pos1;
            }

            LIB.TPwithForceLoad(dropship,startingpoint);

            for(UUID uuid : Game.playerlist.keySet()){
                AiJDropShip.playerGetOn(server, uuid, dropship);
            }
            dropship.setMotion(motion);
            double distance = pos1.distanceTo(pos2);
            AiJDropShip.dropship_allowejecttick = (int)Math.ceil( distance/v*0.05 + Game.travelTick);
            AiJDropShip.dropship_forceejecttick = (int)Math.ceil( distance/v*0.95 + Game.travelTick);
        }
        else{
            LIB.tryBroadcastMessage(event.getPlayer(),"msg.aijbr.red","msg.aijbr.err");
            return;
        }

        RingMove.PhaseChange();

        //结束指令

        ModCommands.RefillCommand.refill();

        Game.isGameStart  = true;
        LIB.tryBroadcastMessage(event.getPlayer(), "msg.aijbr.yellow","msg.aijbr.info.command_game_started");
        LIB.tryBroadcastMessage(event.getPlayer(), "msg.aijbr.bold","msg.aijbr.info.command_executed");
    }
}
