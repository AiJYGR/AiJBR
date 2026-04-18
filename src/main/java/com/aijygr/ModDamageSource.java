package com.aijygr;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;

public class ModDamageSource {
    public static DamageSource getRingDamageSource(Level level) {
        return new DamageSource(
                level.registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(Reg.AIJBR_RING_DAMAGE)
        );
    }
}
