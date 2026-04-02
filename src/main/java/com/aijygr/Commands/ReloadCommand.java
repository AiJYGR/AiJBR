package com.aijygr.Commands;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ReloadCommand {

    public void Reload(CommandSourceStack source) {
        source.sendSuccess(()->Component.translatable("123"), true);
    }

    public ReloadCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("reload").requires((source) -> {
            return source.hasPermission(3);
        }).then(Commands.literal("AiJBR").executes((command) -> {
            this.Reload(command.getSource());
            return 0;
        })));

        dispatcher.register(Commands.literal("AiJBR")
                .then(Commands.literal("reload").requires(s -> s.hasPermission(3))
                        .executes((command) -> {
                            this.Reload(command.getSource());
                            return 0;
                        })));
    }
}
