package com.aijygr;

import com.aijygr.Events.Game.Ring.GameInitEvent;
import com.aijygr.Events.Game.Ring.GameStartEvent;

import com.aijygr.Screen.Scr;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Commands
{
    private static class ScrCommand {
        public ScrCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(net.minecraft.commands.Commands.literal("AiJBR").requires((source) -> {
                return source.hasPermission(1);
            }).then(net.minecraft.commands.Commands.literal("scr").executes((command) -> {
//                Minecraft.getInstance().tell(() -> {
//                    Minecraft.getInstance().setScreen(new Scr(Component.translatable("title.singleplayer")));
//                });
                Minecraft.getInstance().setScreen(new Scr(Component.translatable("title.singleplayer")));
                Player player = command.getSource().getPlayer();

                return 0;
            })));
        }
    }

    private static class ReloadCommand {
        private void Reload(CommandSourceStack source) {
            source.getServer().sendSystemMessage(Component.literal("TMM"));
            source.getServer().getPlayerList().broadcastSystemMessage(Component.translatable("msg.aijbr.command.reload"),false);
        }
        public ReloadCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(net.minecraft.commands.Commands.literal("reload").requires((source) -> {
                return source.hasPermission(3);
            }).then(net.minecraft.commands.Commands.literal("AiJBR").executes((command) -> {
                this.Reload(command.getSource());
                return 0;
            })));

            dispatcher.register(net.minecraft.commands.Commands.literal("AiJBR")
                    .then(net.minecraft.commands.Commands.literal("reload").requires(s -> s.hasPermission(3))
                            .executes((command) -> {
                                this.Reload(command.getSource());
                                return 0;
                            })));
        }
    }

    private static class StartCommand {
        public StartCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(net.minecraft.commands.Commands.literal("AiJBR").requires((source) -> {
                return source.hasPermission(3);//权限等级
            }).then(net.minecraft.commands.Commands.literal("start").executes((command) -> {
                //指令实现代码
                ServerPlayer player = command.getSource().getPlayer();
                MinecraftServer server = command.getSource().getServer();
                MinecraftForge.EVENT_BUS.post(new GameStartEvent(server.overworld(),player));//向EVENT_BUS传递一个事件
                return 0;
            })));

            dispatcher.register(
                    net.minecraft.commands.Commands.literal("AiJBR").requires((r)->{return true;})
                            .executes((cmd)->{
                                cmd.getSource().getPlayer().sendSystemMessage(Component.literal(
                                        "[AiJBR]\n"+ "Auth: AiJYGR"));
                                return 1;
                            })
            );
        }
    }

    private static class InitCommand {
        public InitCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(net.minecraft.commands.Commands.literal("AiJBR").requires((source) -> {
                return source.hasPermission(3);
            }).then(net.minecraft.commands.Commands.literal("init").executes((command) -> {
                ServerPlayer player = command.getSource().getPlayer();
                MinecraftServer server = command.getSource().getServer();
                MinecraftForge.EVENT_BUS.post(new GameInitEvent(server.overworld(),player));
                return 0;
            })));
        }
    }

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event)
    {
        new StartCommand(event.getDispatcher());
        new InitCommand(event.getDispatcher());
        new ReloadCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onClientCommandsRegister(RegisterClientCommandsEvent event){
        new ScrCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }
}