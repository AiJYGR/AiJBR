package com.aijygr.Effects;

import com.aijygr.Reg;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Flying extends MobEffect {
    public Flying(){
        super(MobEffectCategory.NEUTRAL,0xFFFFFF);
//        this.addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(),
//                "11451419-0000-0000-0000-0000000001F3",
//                -0.8D,
//                AttributeModifier.Operation.MULTIPLY_TOTAL);
//        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
//                "11451419-0000-0000-0000-0000000001F4",
//                2.0D,
//                AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    private static final double MAXFALLSPEED = -0.5 ;
    private static final double DEFAULTFALLSPEED = -0.2 ;
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if(entity.onGround())
            entity.removeEffect(this);
        entity.resetFallDistance();
    }
    private static int getsymbol(float v){
        if(v >= 0.0001)
            return 1;
        else if(v <= -0.0001)
            return -1;
        return 0;
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.END)
            return;
        Player player = event.player;
        if(!player.hasEffect(Reg.FLYING_EFFECT.get()))
            return;
        Vec3 motion = player.getDeltaMovement();
        double v = MAXFALLSPEED;
        if(player.isShiftKeyDown())
            v = DEFAULTFALLSPEED;
        if(motion.y < v)
            player.setDeltaMovement(motion.x, v, motion.z);
        //0.04
        player.moveRelative(0.04f,new Vec3(getsymbol(player.xxa),0,getsymbol(player.zza)));
    }


    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
