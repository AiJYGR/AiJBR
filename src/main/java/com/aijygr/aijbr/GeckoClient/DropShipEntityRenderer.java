package com.aijygr.aijbr.GeckoClient;

import com.aijygr.aijbr.Entity.DropShip;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DropShipEntityRenderer extends GeoEntityRenderer<DropShip> {
    public DropShipEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new DropShipEntityModel());
    }

    @Override
    public void render(DropShip animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
