package com.aijygr.Events.Game;

import com.aijygr.Events.Game.Ring.GameStartEvent;
import com.aijygr.Events.Game.Ring.RingGeneration;
import com.aijygr.Events.Game.Ring.RingMove;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class GameStart {

    @SubscribeEvent
    public static void onGameStart(GameStartEvent event) {
        if(!Game.isInitialized){
            event.getPlayer().sendSystemMessage(Component.translatable("[AiJBR][WARN] Game not initialized!"));
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
        event.getPlayer().sendSystemMessage(Component.translatable("aijbr.gamestart"));
    }
}
