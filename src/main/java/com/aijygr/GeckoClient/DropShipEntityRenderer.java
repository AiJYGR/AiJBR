package com.aijygr.GeckoClient;

import com.aijygr.Entity.DropShip;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DropShipEntityRenderer extends GeoEntityRenderer<DropShip> {
    public DropShipEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new DropShipEntityModel());
    }

    @Override
    protected void applyRotations(DropShip animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);

        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - rotationYaw));

        float lerpPitch = Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot());

        poseStack.mulPose(Axis.XP.rotationDegrees(lerpPitch));
    }
}
