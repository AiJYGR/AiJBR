package com.aijygr.AiJGame.BR;

import com.aijygr.Reg;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Damaged {
    @SubscribeEvent
    public static void onDamage(LivingDamageEvent event) {

        if(event.getEntity() instanceof Player player){
            if(player.level().isClientSide){ return ;}
            DamageSource damagesource = event.getSource();
            if(damagesource.is(Reg.AIJBR_RING_DAMAGE)){
                player.sendSystemMessage(Component.literal(String.format("%s:%4.3f",player.getName().getString(), event.getAmount())));
            }
        }
    }
}
