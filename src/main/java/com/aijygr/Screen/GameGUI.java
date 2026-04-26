package com.aijygr.Screen;

import com.aijygr.AiJGame.Client.ClientGame;
import com.aijygr.LIB;
import com.aijygr.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = "aijbr", value = Dist.CLIENT)
public class GameGUI {

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if(!ModConfig.Client.Config.SHOWCLIENTGAMEINFO.get().get())
            return;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null
                || minecraft.options.hideGui)
            return;

        int width = event.getWindow().getGuiScaledWidth();
        int height = event.getWindow().getGuiScaledHeight();
        var graphics = event.getGuiGraphics();
        var font = minecraft.font;

        //队伍数量 存活人数
        int x = 10;
        int y = 10;
        MutableComponent component = LIB.makeComponent("gui.aijbr.game_team_num",String.format(" %d", ClientGame.teams));
        int textWidth = font.width(component);
        int textHeight = font.lineHeight;
        graphics.fill(x-3,  y-3, x+textWidth+2, y+textHeight+2, 0x80000000);
        graphics.drawString(font, component, x , y, 0xE0E0E0,false);

        x+=(textWidth+10);
        component = LIB.makeComponent("gui.aijbr.game_player_num",String.format(" %d", ClientGame.players));
        textWidth = font.width(component);
        graphics.fill(x-3,  y-3, x+textWidth+2, y+textHeight+2, 0x80000000);
        graphics.drawString(font, component, x , y, 0xE0E0E0,false);

        //游戏信息 缩圈倒计时等

        x = width;
        y = height/2 - 30;
        component = LIB.makeComponent("gui.aijbr.game_round",String.format(" %d", ClientGame.round));
        textWidth = font.width(component);
        x-=(textWidth+10);
        graphics.fill(x-3,  y-3, x+textWidth+2, y+textHeight+2, 0x80000000);
        graphics.drawString(font, component, x , y, 0xE0E0E0,false);

        x = width;
        y += textHeight+10;
        component = LIB.makeComponent("gui.aijbr.game_roundtick",String.format(" %d", ClientGame.getSecond()));
        textWidth = font.width(component);
        x-=(textWidth+10);
        graphics.fill(x-3,  y-3, x+textWidth+2, y+textHeight+2, 0x80000000);
        graphics.drawString(font, component, x , y, 0xE0E0E0,false);

    }

}
