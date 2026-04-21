package com.aijygr.Entity;

import com.aijygr.AiJGame.AiJDropShip;
import com.aijygr.LIB;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
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
import java.util.UUID;


public class DropShip extends Entity implements GeoEntity{

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

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
            double horizontalDistance = Math.sqrt(motion1.x * motion1.x + motion1.z * motion1.z);
            float pitch = (float) (Math.atan2(-motion1.y, horizontalDistance) * (180.0 / Math.PI));
            this.setXRot(pitch);
        }
        this.move(MoverType.SELF, motion1);
//        Vec3 motion2 = this.getDeltaMovement();
//        if (!motion1.equals(motion2)) {
//            this.setDeltaMovement(Vec3.ZERO);
//        }

        if (this.uuid.equals(AiJDropShip.DROPSHIPUUID) && !this.level().isClientSide)
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
            passenger.startRiding(this,true);
        }
    }

    public void ejectAllPassengers(MinecraftServer server, List<UUID> passengers) {
        LIB.PLAYERS(server,passengers,player -> {
            player.stopRiding();
        });
    }

    @Override
    protected void defineSynchedData() {

    }
    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {

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