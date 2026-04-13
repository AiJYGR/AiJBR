package com.aijygr;

import com.aijygr.AiJBP.AiJBackpack;
import com.aijygr.AiJBP.SyncConfigJSON.BP.SyncBP;
import com.aijygr.AiJBP.SyncConfigJSON.Tag.SyncTag;
import com.aijygr.AiJGame.Game;
import com.aijygr.AiJGame.GameInitEvent;
import com.aijygr.AiJGame.GameStartEvent;

import com.aijygr.Screen.Scr;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
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

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCommands
{
    private static class ScrCommand {
        public ScrCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal("AiJBR").requires((source) -> {
                return source.hasPermission(1);
            }).then(Commands.literal("scr").executes((command) -> {
//                Minecraft.getInstance().tell(() -> {
//                    Minecraft.getInstance().setScreen(new Scr(Component.translatable("title.singleplayer")));
//                });
                Minecraft.getInstance().setScreen(new Scr(Component.translatable("title.singleplayer")));
                Player player = command.getSource().getPlayer();
                return 0;
            })));
        }
    }

    private static class SyncBPCommand{
        private void SYNC(){
            AiJBackpack.clientsync();
        }
        public SyncBPCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal("AiJBR").requires((source) -> {
                return source.hasPermission(1);
            }).then(Commands.literal("sync").executes((command) -> {
                SYNC();
                Game.tryPlayerMessage(command.getSource().getPlayer(),"msg.aijbr.yellow","Refreshed your backpack.");
                return 0;
            })));
        }
    }


    private static class ReloadCommand {
        private void Reload(CommandSourceStack source) {
            ServerPlayer player = source.getPlayer();
            Game.tryBroadcastMessage(player,"\n","msg.aijbr.green","msg.server"," Starting Reload...");
            try{
                SyncTag.reload(source.getServer());
                SyncBP.reload(source.getServer());
                Game.isReloaded = true;
            }catch(Exception e){
                Game.tryBroadcastMessage(player,"\n","msg.aijbr.red","msg.server","Reload failed:");
                Game.tryBroadcastMessage(player,e.getMessage(),"\nPlease check the JSON file.");
                Game.tryBroadcastMessage(player,"msg.aijbr.red","msg.aijbr.err.command_executed_failed");
                return;
            }
            Game.tryBroadcastMessage(player,"msg.aijbr.bold","msg.aijbr.info.command_executed");
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

    private static class StartCommand {
        public StartCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal("AiJBR").requires((source) -> {
                return source.hasPermission(3);//权限等级
            }).then(Commands.literal("start").executes((command) -> {
                ServerPlayer player = command.getSource().getPlayer();
                MinecraftServer server = command.getSource().getServer();
                MinecraftForge.EVENT_BUS.post(new GameStartEvent(server.overworld(),player));//向EVENT_BUS传递一个事件
                return 0;
            })));
            dispatcher.register(
                    Commands.literal("AiJBR").requires((r)->{return true;})
                            .executes((commmand)->{
                                Objects.requireNonNull(commmand.getSource().getPlayer()).sendSystemMessage(Component.literal(
                                        "[AiJBR]\n"+ "Auth: AiJYGR"));
                                return 1;
                            })
            );
        }
    }

    private static class InitCommand {
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
        new SyncBPCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }
}