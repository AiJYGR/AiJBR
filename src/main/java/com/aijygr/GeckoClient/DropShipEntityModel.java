package com.aijygr.GeckoClient;

import com.aijygr.Entity.DropShip;
import com.aijygr.Main;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class DropShipEntityModel extends DefaultedEntityGeoModel<DropShip> {
    public DropShipEntityModel() {
        super(ResourceLocation.fromNamespaceAndPath(Main.MODID,"dropship"));
    }
}
