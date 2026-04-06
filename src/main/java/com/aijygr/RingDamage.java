package com.aijygr;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;

public class RingDamage {
    public static DamageSource getRingDamageSource(Level level) {
        // 从世界注册表中获取 Holder
        return new DamageSource(
                level.registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(Reg.AIJBR_RING_DAMAGE)
        );
    }
}
