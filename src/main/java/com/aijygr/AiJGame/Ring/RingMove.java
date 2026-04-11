package com.aijygr.AiJGame.Ring;

import com.aijygr.AiJGame.Game;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class RingMove {
    public static void PhaseChange(){
        System.out.println("PHASE CHANGE");
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
        }
        else{//开始缩圈
            Game.isRingClosing = true;
            if(Game.sv_round == 0)
            {
                Game.sv_roundticktotal = Game.r_initial_waitingtick;
                Game.sv_roundtick = Game.sv_roundticktotal;
                Game.sv_damage_per_block=Game.r_damage_per_block.get(0);
                Game.sv_basicdamage=Game.r_basic_damage.get(0);
                return;
            }
            else{
                int i = Game.sv_round - 1 ;
                Game.sv_damage_per_block=Game.r_damage_per_block.get(i*2+1);
                Game.sv_basicdamage=Game.r_basic_damage.get(i*2+1);
                Game.sv_roundticktotal = Game.r_moving_tick.get(i);
                Game.sv_roundtick = Game.sv_roundticktotal;
            }
        }
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if ((event.phase == TickEvent.Phase.START) && (event.level.dimension().equals(Level.OVERWORLD))
                && (!event.level.isClientSide())){
            double i = 0.0d;
            if(Game.isGameStart && Game.isInitialized){
                Game.sv_roundtick--;
                if(Game.sv_roundtick<=-1){
                    PhaseChange();
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
                    //System.out.println("closing");
                    WorldBorder worldborder = event.level.getWorldBorder();
                    if(Game.sv_r_size < 0.01)
                        Game.sv_r_size = 0.01;
                    worldborder.setSize(Game.sv_r_size);
                    worldborder.setCenter(Game.sv_r_x, Game.sv_r_z);
                }
            }
            //LOG
//            if(Game.gametime%10==1&&Game.isInitialized)
//            {
//                String str =  String.format("\ni=%3.2f\n%d: Round %d %d  %b closing:%b\n" +
//                                "cur(%3d,%3d):%3.2f  next(%3d,%3d):%3.2f\n" +
//                                "SV(%3.2f,%3.2f):%3.2f\n"+
//                                "dpb:%3.2f  dmg:%3.2f\n",
//                        i,
//                        Game.gametime,Game.sv_round,Game.sv_roundtick,Game.isGameStart,Game.isRingClosing,
//                        Game.sv_curr_x,Game.sv_curr_z,Game.sv_curr_size,
//                        Game.sv_next_x,Game.sv_next_z,Game.sv_next_size,
//                        Game.sv_r_x, Game.sv_r_z,Game.sv_r_size,
//                        Game.sv_damage_per_block, Game.sv_basicdamage
//                );
//                System.out.println(str);
//            }
        }
    }
}
