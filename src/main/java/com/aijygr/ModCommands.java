package com.aijygr;

import com.aijygr.AiJBP.AiJBackpack;
import com.aijygr.AiJBP.SyncConfigJSON.BP.SyncBP;
import com.aijygr.AiJBP.SyncConfigJSON.Tag.SyncTag;
import com.aijygr.AiJGame.AiJBRPlayer;
import com.aijygr.AiJGame.Game;
import com.aijygr.AiJGame.GameInitEvent;
import com.aijygr.AiJGame.GameStartEvent;

import com.aijygr.Screen.Scr;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
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
                return source.hasPermission(0);
            }).then(Commands.literal("scr").executes((command) -> {
//                Minecraft.getInstance().tell(() -> {
//                    Minecraft.getInstance().setScreen(new Scr(Component.translatable("title.singleplayer")));
//                });
                Minecraft.getInstance().setScreen(new Scr(Component.translatable("title.singleplayer")));
                //Player player = command.getSource().getPlayer();
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
                return source.hasPermission(0);
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
            Game.tryBroadcastMessage(player,"\n","msg.aijbr.yellow","msg.server","Start to SYNC JSON...");
            Game.isReloaded = false;
            try{
                SyncTag.reload(source.getServer());
                SyncBP.reload(source.getServer());
                Game.isReloaded = true;
            }catch(Exception e){
                Game.tryBroadcastMessage(player,"msg.aijbr.red","msg.server","Reload failed:");
                Game.tryBroadcastMessage(player,e.getMessage(),"\nPlease check the JSON file.");
                Game.tryBroadcastMessage(player,"msg.aijbr.red","msg.aijbr.err.command_executed_failed");
            }
            //Game.tryBroadcastMessage(player,"msg.aijbr.bold"," SYNC SUCCESSFULLY.");
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

    private static class PlayerJoinCommand {
        private int PlayerJoin(ServerPlayer player) {
            if(!Game.isInitialized){
                Game.tryPlayerMessage(player,"msg.aijbr.red","msg.aijbr.err.command_game_not_initialized");
                return 1;
            }
            if(player!=null){
                for(int i = 1; i <= ModConfig.Server.Config.TEAM.TEAMNUM.get();i++)
                {
                    if(AiJBRPlayer.joinTeam(player,i))
                    {
                        Game.tryPlayerMessage(player,"msg.aijbr.green","msg.aijbr.info.command_player_join_team_p1",AiJBRPlayer.getTeamName(i),"msg.aijbr.info.command_player_join_team_p2");
                        return 0;
                    }
                }
            }
            Game.tryPlayerMessage(player,"msg.aijbr.red","msg.aijbr.err.command_player_join_team_failed");
            return 1;
        }
        private int PlayerJoin(ServerPlayer player, int team) {
            if(!Game.isInitialized){
                Game.tryPlayerMessage(player,"msg.aijbr.red","msg.aijbr.err.command_game_not_initialized");
                return 1;
            }
            if(player!=null){
                if(AiJBRPlayer.joinTeam(player,team)){
                    Game.tryPlayerMessage(player,"msg.aijbr.green","msg.aijbr.info.command_player_join_team_p1",AiJBRPlayer.getTeamName(team),"msg.aijbr.info.command_player_join_team_p2");
                    return 0;
                }
            }
            Game.tryPlayerMessage(player,"msg.aijbr.red","msg.aijbr.err.command_player_join_team_failed_p1",AiJBRPlayer.getTeamName(team),"msg.aijbr.err.command_player_join_team_failed_p2");
            return 1;
        }
        public PlayerJoinCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal("AiJBR").requires((source) -> {
                return source.hasPermission(0);
            }).then(Commands.literal("join").executes((command)->{
                return PlayerJoin(command.getSource().getPlayer());
            }).then(Commands.argument("team_num", IntegerArgumentType.integer()).executes((command) -> {
                return PlayerJoin(command.getSource().getPlayer(),IntegerArgumentType.getInteger(command,"team_num"));
            }))));
        }
    }

    public static class PlayerLeaveCommand {
        private int PlayerLeave(ServerPlayer player) {
            if(!Game.isInitialized){
                Game.tryPlayerMessage(player,"msg.aijbr.red","msg.aijbr.err.command_game_not_initialized");
                return 1;
            }
            if(AiJBRPlayer.leaveTeam(player))
            {
                Game.tryPlayerMessage(player,"msg.aijbr.green","msg.aijbr.info.command_player_leave_team");
                return 0;
            }
            else{
                Game.tryPlayerMessage(player,"msg.aijbr.red","msg.aijbr.err.command_player_leave_team_failed");
                return 1;
            }
        }
        public PlayerLeaveCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal("AiJBR").requires((source) -> {
                return source.hasPermission(0);
            }).then(Commands.literal("leave").executes((command)->{
                return PlayerLeave(command.getSource().getPlayer());
            })));
        }
    }

    public static class SVCommand {
        private int PlayerLeave(ServerPlayer player) {
            if(!Game.isInitialized){
                Game.tryPlayerMessage(player,"msg.aijbr.red","msg.aijbr.err.command_game_not_initialized");
                return 1;
            }
            if(AiJBRPlayer.leaveTeam(player))
            {
                Game.tryPlayerMessage(player,"msg.aijbr.green","msg.aijbr.info.command_player_leave_team");
                return 0;
            }
            else{
                Game.tryPlayerMessage(player,"msg.aijbr.red","msg.aijbr.err.command_player_leave_team_failed");
                return 1;
            }
        }
        public SVCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal("AiJBR").requires((source) -> {
                return source.hasPermission(3);
            }).then(Commands.literal("SV").executes((command)->{
                StringBuilder str = new StringBuilder();
                str.append(AiJBRPlayer.getPlayers(command.getSource().getServer())).append("\n");
                str.append(AiJBRPlayer.getTeams(command.getSource().getServer()));
                Game.tryPlayerMessage(command.getSource().getPlayer(),str.toString());
                return 0;
            })));
        }
    }

    @SubscribeEvent
    public static void onServerCommandsRegister(RegisterCommandsEvent event)
    {
        new StartCommand(event.getDispatcher());
        new InitCommand(event.getDispatcher());
        new ReloadCommand(event.getDispatcher());
        new PlayerJoinCommand(event.getDispatcher());
        new PlayerLeaveCommand(event.getDispatcher());
        new SVCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onClientCommandsRegister(RegisterClientCommandsEvent event){
        new ScrCommand(event.getDispatcher());
        new SyncBPCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }
}