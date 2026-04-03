package com.aijygr.Commands;

import com.aijygr.Screen.Scr;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ScrCommand {
    public ScrCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("AiJBR").requires((source) -> {
            return source.hasPermission(1);
        }).then(Commands.literal("scr").executes((command) -> {

            Minecraft.getInstance().tell(() -> {
                Minecraft.getInstance().setScreen(new Scr(Component.translatable("fuck")));
            });

            return 0;
        })));
    }
}
