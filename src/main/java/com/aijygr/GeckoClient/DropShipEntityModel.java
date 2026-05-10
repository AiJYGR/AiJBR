package com.aijygr.GeckoClient;

import com.aijygr.Entity.DropShip;
import com.aijygr.Main;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class DropShipEntityModel extends DefaultedEntityGeoModel<DropShip> {
    public DropShipEntityModel() {
        super(ResourceLocation.fromNamespaceAndPath(Main.MODID,"dropship"));
    }

    @Override
    public void setCustomAnimations(DropShip animatable, long instanceId, AnimationState<DropShip> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone root = getAnimationProcessor().getBone("MAIN");
        if (root != null) {
            float yaw = animatable.getYRot();
            // 将角度转为弧度应用给骨骼
            // 如果渲染器已经转过了，这里就不要再转，否则会转两次
            root.setRotY((-180.0f-yaw) * ((float)Math.PI / 180.0f));
        }
    }
}
