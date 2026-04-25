package com.aijygr.plugin;

import com.aijygr.Main;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.IClientPlugin;
import journeymap.client.api.display.PolygonOverlay;
import journeymap.client.api.event.ClientEvent;
import journeymap.client.api.model.MapPolygon;
import journeymap.client.api.model.ShapeProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

@journeymap.client.api.ClientPlugin
public class AiJBRMap implements IClientPlugin {

    private static IClientAPI jmAPI;
    @Override
    public void initialize(IClientAPI api) {
        jmAPI = api;
    }

    @Override
    public void onEvent(ClientEvent event) {

    }
    @Override
    public String getModId() {
        return Main.MODID;
    }

    private static final double m = 0.00000001;
    public static void drawZone(ResourceKey<Level> dimension, double x,double z, double r,int color,String id) {
        if (jmAPI == null)
            return;
        List<BlockPos> points = new ArrayList<>();

        int px = (int)Math.ceil( x+r + m);
        int nx = (int)Math.floor( x-r - m);
        int pz = (int)Math.ceil( z+r + m);
        int nz = (int)Math.floor( z-r - m);

        points.add(new BlockPos(px, 64, pz));
        points.add(new BlockPos(nx, 64, pz));
        points.add(new BlockPos(nx, 64, nz));
        points.add(new BlockPos(px, 64, nz));

        MapPolygon square = new MapPolygon(points);

        ShapeProperties properties = new ShapeProperties()
                .setStrokeColor(color)
                .setStrokeOpacity(0.8f)
                .setStrokeWidth(3)
                .setFillOpacity(0.0f);

        PolygonOverlay overlay = new PolygonOverlay(
                Main.MODID,
                id,
                dimension,
                properties,
                square
        );

        try {
            jmAPI.show(overlay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
