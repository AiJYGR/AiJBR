package com.aijygr.GeckoClient;

import com.aijygr.Entity.DropShip;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DropShipEntityRenderer extends GeoEntityRenderer<DropShip> {
    public DropShipEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new DropShipEntityModel());
    }
}
