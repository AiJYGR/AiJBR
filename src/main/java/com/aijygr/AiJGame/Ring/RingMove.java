package com.aijygr.AiJGame.Ring;

import com.aijygr.AiJGame.Game;
import com.aijygr.AiJGame.Client.MSGClientGameInfo;
import com.aijygr.AiJGame.Client.MSGClientRingInfo;
import com.aijygr.LIB;
import com.aijygr.ModMessages;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class RingMove {
    public static void PhaseChange(){
        LIB.BRLOG("PHASE CHANGE");
        if(Game.isRingClosing){//进入下一阶段 刷圈&等待缩圈
            Game.isRingClosing = false;
            Game.sv_round++;
            if(Game.sv_round>Game.totalrounds)
            {
                Game.sv_roundtick = Integer.MAX_VALUE;
                Game.sv_roundticktotal = Integer.MAX_VALUE;
                return;
            }
            int i = Game.sv_round -1 ;
            Game.sv_curr_size = Game.sv_next_size;
            Game.sv_r_size = Game.sv_curr_size;
            Game.sv_curr_x = Game.sv_next_x;
            Game.sv_curr_z = Game.sv_next_z;
            Game.sv_next_size = Game.r_ring_size.get(i);
            var vec2 = RingGeneration.Generate(
                    Game.sv_curr_x,Game.sv_curr_z,
                    Game.sv_curr_size, Game.sv_next_size,
                    Game.r_generation_modes.get(i));
            Game.sv_next_x = vec2.x;
            Game.sv_next_z = vec2.z;

            Game.sv_roundticktotal = Game.r_waiting_tick.get(i);
            Game.sv_roundtick = Game.sv_roundticktotal;
            Game.sv_damage_per_block=Game.r_damage_per_block.get(i*2);
            Game.sv_basicdamage=Game.r_basic_damage.get(i*2);
            ModMessages.ServerSendToAll(new MSGClientRingInfo(Game.sv_next_x,Game.sv_next_z,Game.sv_next_size,Game.r_generation_modes.get(i).name()));
            ModMessages.ServerSendToAll(new MSGClientGameInfo(Game.sv_round,Game.sv_roundtick));
        }
        else{//开始缩圈
            Game.isRingClosing = true;
            if(Game.sv_round == 0)
            {
                Game.sv_roundticktotal = Game.r_initial_waitingtick;
                Game.sv_roundtick = Game.sv_roundticktotal;
                Game.sv_damage_per_block=Game.r_damage_per_block.get(0);
                Game.sv_basicdamage=Game.r_basic_damage.get(0);
            }
            else{
                int i = Game.sv_round - 1 ;
                Game.sv_damage_per_block=Game.r_damage_per_block.get(i*2+1);
                Game.sv_basicdamage=Game.r_basic_damage.get(i*2+1);
                Game.sv_roundticktotal = Game.r_moving_tick.get(i);
                Game.sv_roundtick = Game.sv_roundticktotal;
            }
            ModMessages.ServerSendToAll(new MSGClientGameInfo(Game.sv_round,Game.sv_roundtick));
        }
    }
    public static void PhaseChange(MinecraftServer server,String message){
        server.getPlayerList().broadcastSystemMessage(Component.translatable(message),false);
        PhaseChange();
    }

    public static void LogRingStatus(){
        String str =  String.format("""
                        Rnd:%d RndTick:%d  isClosing:%b
                        cur(%3d,%3d,%3.2f)  next(%3d,%3d,%3.2f)
                        sv(%3.2f,%3.2f,%3.2f)
                        DPB:%3.2f  DMG:%3.2f""",
                Game.sv_round,Game.sv_roundtick,Game.isRingClosing,
                Game.sv_curr_x,Game.sv_curr_z,Game.sv_curr_size,
                Game.sv_next_x,Game.sv_next_z,Game.sv_next_size,
                Game.sv_r_x, Game.sv_r_z,Game.sv_r_size,
                Game.sv_damage_per_block, Game.sv_basicdamage
        );
        LIB.BRLOG(str);
    }

    public static void setWorldBorder(WorldBorder worldborder){
        setWorldBorder(worldborder,Game.sv_r_x,Game.sv_r_z,Game.sv_r_size);
    }
    public static void setWorldBorder(WorldBorder worldborder,double x, double z, double size){
        if(size<0.02)
            size = 0.02;
        worldborder.setSize(size);
        worldborder.setCenter(x, z);
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (LIB.SV1TK(event))
        {
            double i;
            WorldBorder worldborder = event.level.getWorldBorder();

            if(Game.isGameStart && Game.isInitialized){
                //LOG
                //if(Game.gametime%100==0)
                //    LogRingStatus();
                Game.sv_roundtick--;
                if(Game.sv_roundtick<0){
                    PhaseChange();
                    setWorldBorder(worldborder);
                }
                if(Game.isRingClosing&&Game.sv_round==0){
                    Game.sv_r_size = Game.R;
                    worldborder.setSize(Game.sv_r_size);
                    return;
                }
                if(Game.isRingClosing&&Game.sv_round!=0){
                    if(Game.sv_roundtick==0){
                        Game.sv_r_size = Game.sv_next_size;
                        Game.sv_r_x = Game.sv_next_x;
                        Game.sv_r_z = Game.sv_next_z;
                    }
                    else{
                        i = (double) Game.sv_roundtick / (double) Game.sv_roundticktotal;
                        i = 1 - i;
                        Game.sv_r_size = (Game.sv_next_size-Game.sv_curr_size)*i+Game.sv_curr_size;
                        Game.sv_r_x = (Game.sv_next_x-Game.sv_curr_x)*i+Game.sv_curr_x;
                        Game.sv_r_z = (Game.sv_next_z-Game.sv_curr_z)*i+Game.sv_curr_z;
                    }
                    setWorldBorder(worldborder);
                }
            }
        }
    }
}
