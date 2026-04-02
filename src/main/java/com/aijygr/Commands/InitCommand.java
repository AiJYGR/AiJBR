package com.aijygr.Commands;

import com.aijygr.Events.Game.Ring.GameInitEvent;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;

public class InitCommand {
    public InitCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("AiJBR").requires((source) -> {
            return source.hasPermission(3);
         }).then(Commands.literal("init").executes((command) -> {
            ServerPlayer player = command.getSource().getPlayer();
            MinecraftServer server = command.getSource().getServer();
            MinecraftForge.EVENT_BUS.post(new GameInitEvent(server.overworld(),player));
            return 0;
        })));
    }
}
