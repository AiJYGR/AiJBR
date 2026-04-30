package com.aijygr.Effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;

public class Flying extends MobEffect {
    public Flying(){
        super(MobEffectCategory.NEUTRAL,0xFFFFFF);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        entity.resetFallDistance();
        //entity.setSwimming(true);
        //entity.setPose(Pose.SWIMMING);
    }


    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
