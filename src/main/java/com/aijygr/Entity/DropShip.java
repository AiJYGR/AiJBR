package com.aijygr.Entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DropShip extends Entity implements GeoEntity{

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    public DropShip(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {

    }
    // See the Data and Networking article for information about these methods.
    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }
    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "wdnmd", 5, a -> {
            return a.setAndContinue(RawAnimation.begin().thenLoop("spin"));
        }));
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}