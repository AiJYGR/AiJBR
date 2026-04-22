package com.aijygr.Screen;

import com.aijygr.AiJGame.Game;
import com.aijygr.AiJGame.Ring.RingDamage;
import com.aijygr.LIB;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class RingIndicatorGUI {

    public static double distance = 0;
    public static String str = "RI";
    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.hideGui)
            return;
        else
        {
            var guiGraphics = event.getGuiGraphics();
            var font = minecraft.font;
            int width = event.getWindow().getGuiScaledWidth();
            int height = event.getWindow().getGuiScaledHeight();
            guiGraphics.drawString(font, str, width/2 , height/2 + 40, 0x11FF11, false);
            guiGraphics.renderItem(Items.IRON_BLOCK.getDefaultInstance()  ,width/2 +50, height / 2 + 50);
        }
    }
    private enum OOBState{
        INSIDE(0),
        X(1),Z(3),nX(-1), nZ(-3),
        XZ(4), // = X+Z
        nXZ(2),
        nXnZ(-4),
        XnZ(-2);
        final int value;
        OOBState(int value) {
            this.value = value;
        }
        public int toInt() {
            return value;
        }
        public static OOBState make(int value) {
            for (OOBState state : OOBState.values()) {
                if (state.value == value) {
                    return state;
                }
            }
            return INSIDE;
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(LIB.CL1TK(event)){
            if(event.player instanceof LocalPlayer player){
                double playerPosX = player.position().x;
                double playerPosZ = player.position().z;
                double ringPosX = Game.sv_next_x;
                double ringPosZ = Game.sv_next_z;
                double ringSize = Game.sv_next_size;

                OOBState oob  = OOBState.INSIDE;
                double r = ringSize / 2;
                final double f = RingDamage.PLAYER_HITBOXFIX;
                double distance = 0;
                double dx =    playerPosX + f - ringPosX - r;
                double dnx = - playerPosX + f + ringPosX - r;
                double dz =    playerPosZ + f - ringPosZ - r;
                double dnz = - playerPosZ + f + ringPosZ - r;
                if(dx > 0)
                {
                    distance = Math.max(distance,dx);
                    oob = OOBState.X;
                }
                else if(dnx > 0)
                {
                    distance = Math.max(distance,dnx);
                    oob = OOBState.nX;
                }

                if(dz > 0)
                {
                    distance = Math.max(distance,dz);
                    oob = OOBState.make(oob.toInt() + OOBState.Z.toInt());
                }
                else if(dnz > 0)
                {
                    distance = Math.max(distance,dnz);
                    oob = OOBState.make(oob.toInt() + OOBState.nZ.toInt());
                }
                String str = switch (oob){
                    case INSIDE -> "Ｏ";
                    case X -> "⬅";
                    case Z -> "⬆";
                    case nX -> "➡";
                    case nZ -> "⬇";
                    case XZ -> "↖️";
                    case XnZ -> "↙️";
                    case nXnZ -> "↗️";
                    case nXZ -> "↘️";
                    default -> "0";
                };
                RingIndicatorGUI.str = str;
                RingIndicatorGUI.distance = distance;

            }
        }
    }
}
