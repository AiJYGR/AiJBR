package com.aijygr;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.Level;

public class DamageSource {
    public static net.minecraft.world.damagesource.DamageSource getRingDamageSource(Level level) {
        return new net.minecraft.world.damagesource.DamageSource(
                level.registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(Reg.AIJBR_RING_DAMAGE)
        );
    }
}
