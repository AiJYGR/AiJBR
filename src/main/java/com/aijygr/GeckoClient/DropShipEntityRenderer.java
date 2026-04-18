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
    public void render(DropShip animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        //神奇的GeckoLib 解决模型不匹配朝向
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
        float pitch = Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot());
        poseStack.mulPose(Axis.XP.rotationDegrees((float) - pitch));
        super.render(animatable, 0, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}
