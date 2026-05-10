package com.aijygr.Entity;

import com.aijygr.AiJGame.AiJDropShip;
import com.aijygr.LIB;
import com.aijygr.Reg;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class DropShip extends Entity implements GeoEntity{

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected static final RawAnimation SPIN_ANIM = RawAnimation.begin().thenLoop("spin");

    public DropShip(EntityType<? extends Entity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public void setMotion(double x, double y, double z){
        this.setDeltaMovement(x,y,z);
    }
    public void setMotion(Vec3 v){
        this.setDeltaMovement(v);
    }

    public Vec3 getMotion(){
        return this.getDeltaMovement();
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 motion1 = this.getDeltaMovement();
        if (motion1.horizontalDistanceSqr() > 1.0E-7D) {
            float yaw = (float) (Math.atan2(motion1.z, motion1.x) * (180.0 / Math.PI)) - 90.0F;
            this.setYRot(yaw);
            this.setYHeadRot(yaw);
            this.setYBodyRot(yaw);
            double horizontalDistance = Math.sqrt(motion1.x * motion1.x + motion1.z * motion1.z);
            float pitch = (float) (Math.atan2(-motion1.y, horizontalDistance) * (180.0 / Math.PI));
            this.setXRot(pitch);
        }
        if(this.getISTICKING())
            this.move(MoverType.SELF, motion1);
        else
            this.move(MoverType.SELF, Vec3.ZERO);
        //服务端DropShip逻辑
        if (this.isAiJDropShip() && !this.level().isClientSide)
        {
            Vec3 pos = this.position();
            MinecraftServer sv = this.level().getServer();
            AiJDropShip.tick(sv,this);
        }
    }


    @Override
    public boolean isNoGravity() {
        return true;
    }
    @Override
    protected boolean canRide(Entity pVehicle){
        return true;
    }
    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().size() <= 100;
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (AiJDropShip.canleave) {
            if(passenger instanceof ServerPlayer player) {
                AiJDropShip.playerLeave(player);
            }
        }
        else{
            if(!this.level().isClientSide){
                passenger.startRiding(this,true);
            }
        }
    }

    public void ejectAllPassengers(MinecraftServer server, List<UUID> passengers) {
        LIB.PLAYERS(server,passengers,player -> {
            player.stopRiding();
            player.addEffect(new MobEffectInstance(Reg.FLYING_EFFECT.get(), MobEffectInstance.INFINITE_DURATION, 0, false, true));
        });
    }
    private static final EntityDataAccessor<String> DATA_NAME = SynchedEntityData.defineId(DropShip.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> DATA_ISTICKING = SynchedEntityData.defineId(DropShip.class, EntityDataSerializers.BOOLEAN);
    //private static final EntityDataAccessor<Boolean> DATA_CANLEAVE = SynchedEntityData.defineId(DropShip.class, EntityDataSerializers.BOOLEAN);
    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_NAME, "");
        this.entityData.define(DATA_ISTICKING, false);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putString("NAME", this.entityData.get(DATA_NAME));
        tag.putBoolean("ISTICKING", this.entityData.get(DATA_ISTICKING));
    }
    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.entityData.set(DATA_NAME, tag.getString("NAME"));
        this.entityData.set(DATA_ISTICKING, tag.getBoolean("ISTICKING"));
    }
    public String getNAME(){
        return this.entityData.get(DATA_NAME);
    }
    public void setNAMEAiJDropShip(){
        setNAME(AiJDropShip.DROPSHIPTAG);
    }
    public boolean isAiJDropShip(){
        return getNAME().equals(AiJDropShip.DROPSHIPTAG);
    }
    public void setNAME(String name){
        this.entityData.set(DATA_NAME, name);
    }
    public boolean getISTICKING() {
        return this.entityData.get(DATA_ISTICKING);
    }
    public void setISTICKING(boolean isticking) {
        this.entityData.set(DATA_ISTICKING, isticking);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "wdnmd", 5, aState -> {
            return aState.setAndContinue(SPIN_ANIM);
        }));
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}