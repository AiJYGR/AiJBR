package com.aijygr.Entity;

import com.aijygr.AiJGame.Game;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class DropShip extends Entity implements GeoEntity{

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    public DropShip(EntityType<? extends Entity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Override
    public void tick() {
        super.tick();

        Vec3 motion1 = this.getDeltaMovement();
        StringBuilder str = new StringBuilder();
        str.append(Game.gametime).append(":");
        str.append("(").append(motion1.x).append(",").append(motion1.y).append(",").append(motion1.z).append(")  ");
        if (motion1.horizontalDistanceSqr() > 1.0E-7D) {
            float yaw = (float) (Math.atan2(motion1.z, motion1.x) * (180.0 / Math.PI)) - 90.0F;
            this.setYRot(yaw);
            this.setYHeadRot(yaw);
            str.append("(").append(yaw).append(",");
            double horizontalDistance = Math.sqrt(motion1.x * motion1.x + motion1.z * motion1.z);
            float pitch = (float) (Math.atan2(-motion1.y, horizontalDistance) * (180.0 / Math.PI));
            this.setXRot(pitch);
            str.append(pitch).append(")");
        }
        this.move(MoverType.SELF, motion1);
        if(Game.gametime%10==0&& !str.isEmpty() && motion1.length() > 0.00001)
            System.out.println(str);
        Vec3 motion2 = this.getDeltaMovement();
        if (!motion1.equals(motion2)) {
            this.setDeltaMovement(Vec3.ZERO);
        }

    }
    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {

    }
    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {

    }


    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

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