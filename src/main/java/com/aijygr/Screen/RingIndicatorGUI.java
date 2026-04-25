package com.aijygr.Screen;

import com.aijygr.AiJGame.Game;
import com.aijygr.AiJGame.Ring.RingDamage;
import com.aijygr.Main;
import com.aijygr.ModConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class RingIndicatorGUI {

    public static void drawRotatedTexture(GuiGraphics graphics, ResourceLocation texture, int x, int y, int width, int height, float angle) {
        var poseStack = graphics.pose();
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        poseStack.pushPose();
        poseStack.translate(centerX, centerY, 0);
        poseStack.mulPose(Axis.ZP.rotation(angle));
        poseStack.translate((float) -width/2, (float) -height/2, 0);
        graphics.blit(texture, 0, 0, 0, 0, width, height, width, height);
        poseStack.popPose();
    }

    public static double distance = 0;
    public static float angle = 0;
    private static final ResourceLocation PT = ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/gui/pt.png");
    private static final ResourceLocation PT_BG_0 = ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/gui/pt_bg_0.png");
    private static final ResourceLocation PT_BG_1 = ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/gui/pt_bg_1.png");
    private static final int TEXTURE_SIZE = 12;

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if(!ModConfig.Client.Config.SHOWRINGINDICATOR.get().get())
            return;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null
                || minecraft.options.hideGui
                || minecraft.player.isCreative()
                || minecraft.player.isSpectator())
            return;

        cal(minecraft.player);

        var graphics = event.getGuiGraphics();
        var font = minecraft.font;
        int width = event.getWindow().getGuiScaledWidth();
        int height = event.getWindow().getGuiScaledHeight();
        int x = width / 2 + 10;
        int y = height - 50;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if(distance <= 0){
            graphics.blit(PT_BG_1,x-TEXTURE_SIZE/2,y-TEXTURE_SIZE/2,0,0,TEXTURE_SIZE,TEXTURE_SIZE,TEXTURE_SIZE,TEXTURE_SIZE);
            String str = "0.0";
            graphics.drawString(font, str, x - font.width(str)/2 , y+10,  0x47A036,false);
        }
        else{
            drawRotatedTexture(graphics, PT, x-TEXTURE_SIZE/2, y-TEXTURE_SIZE/2,TEXTURE_SIZE,TEXTURE_SIZE, angle);
            graphics.blit(PT_BG_0,x-TEXTURE_SIZE/2,y-TEXTURE_SIZE/2,0,0,TEXTURE_SIZE,TEXTURE_SIZE,TEXTURE_SIZE,TEXTURE_SIZE);
            String str = (distance >= 10 ? String.format("%.0f", distance) : String.format("%.1f", distance));
            graphics.drawString(font, str, x - font.width(str)/2 , y+10, 0xFF5555,false);
        }

        RenderSystem.disableBlend();
    }

    private static void cal(LocalPlayer player){
        double playerPosX = player.position().x;
        double playerPosZ = player.position().z;
        double ringPosX = Game.sv_next_x;
        double ringPosZ = Game.sv_next_z;
        double ringSize = Game.sv_next_size;
        final double f = RingDamage.PLAYER_HITBOXFIX;

        double r = ringSize / 2;
        double dx =    playerPosX + f - ringPosX - r;
        double dnx = - playerPosX + f + ringPosX - r;
        double dz =    playerPosZ + f - ringPosZ - r;
        double dnz = - playerPosZ + f + ringPosZ - r;

        double vx = 0;
        double vz = 0;

        if (dx > 0) vx -= dx;
        if (dnx > 0) vx += dnx;

        if (dz > 0) vz -= dz;
        if (dnz > 0) vz += dnz;

        if(vx==0 && vz==0) {
            RingIndicatorGUI.distance = 0;
        } else {
            RingIndicatorGUI.distance = Math.sqrt(vx*vx + vz*vz);
            double angle = Math.atan2(vz,vx);
            float yaw = player.getYRot();
            double degree = Math.toDegrees(angle);
            float playerdegree = (float) (degree - (yaw - 90) + 180);
            RingIndicatorGUI.angle = (float) Math.toRadians(playerdegree);
        }
    }
    //20帧的计算有点卡顿 所以改为每帧都算一遍
    /*@SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(LIB.CL1TK(event)){
            if(event.player instanceof LocalPlayer player) {
                cal(player);
            }
        }
    }*/
}
