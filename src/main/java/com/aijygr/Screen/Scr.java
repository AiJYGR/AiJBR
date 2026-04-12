package com.aijygr.Screen;

import com.aijygr.ModConfig;
import com.aijygr.Main;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.Builder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class Scr extends Screen {

    public Scr(Component title) {
        super(title);
    }

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Main.MODID,"textures/gui/test.png");

    private Button button;


    @Override
    protected void init() {
        this.addRenderableWidget(Button.builder(Component.translatable("CLICK"), (btn) -> {
            System.out.println("nb");
        }).bounds(this.width / 2 - 50, this.height / 2, 100, 20).build());      // 在init添加按钮

        
        button = new Builder(Component.translatable("CLICK"), (btn) -> {
            this.minecraft.player.sendSystemMessage(title);
            
        }).pos(this.width / 2 - 50, this.height / 2 + 40).size(100, 20).build();
        this.addRenderableWidget(button);

    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {

        this.renderBackground(graphics);        // 背景

        final int PWIDTH = 206, PHEIGHT = 101;
        graphics.blit(TEXTURE, (this.width - PWIDTH) / 2 , (this.height - PHEIGHT) / 2 , 0, 0, PWIDTH, PHEIGHT, PWIDTH, PHEIGHT);      //图片

        super.render(graphics, mouseX, mouseY, partialTick);  

        String str = ModConfig.CFGDOUBLE.get().toString();
        

        graphics.drawString(this.font, str, this.width / 2 , this.height / 2 - 20, 0xf0f0f0, true);       //   文字

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
