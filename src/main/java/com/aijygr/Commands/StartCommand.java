package com.aijygr.Commands;

import com.aijygr.Events.Game.Ring.GameStartEvent;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;

public class StartCommand {
    public StartCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("AiJBR").requires((source) -> {
            return source.hasPermission(3);//权限等级
         }).then(Commands.literal("start").executes((command) -> {
            //指令实现代码
            ServerPlayer player = command.getSource().getPlayer();
            MinecraftServer server = command.getSource().getServer();
            MinecraftForge.EVENT_BUS.post(new GameStartEvent(server.overworld(),player));//向EVENT_BUS传递一个事件
            return 0;
        })));

        dispatcher.register(
            Commands.literal("SB").requires((r)->{return true;})
            .executes((cmd)->{System.out.println("execute:SB");return 1;})
        
        );
    }
}
