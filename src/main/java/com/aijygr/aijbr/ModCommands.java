package com.aijygr.aijbr;

import com.aijygr.aijbr.AiJBP.AiJBackpack;
import com.aijygr.aijbr.AiJBP.SyncConfigJSON.BP.SyncBP;
import com.aijygr.aijbr.AiJBP.SyncConfigJSON.Tag.SyncTag;
import com.aijygr.aijbr.AiJGame.AiJBRPlayer;
import com.aijygr.aijbr.AiJGame.Game;
import com.aijygr.aijbr.AiJGameUtils.ItemCleaner;
import com.aijygr.aijbr.AiJGameUtils.MapResetter.MapResetter;
import com.aijygr.aijbr.Screen.Scr;

import com.ibm.icu.impl.Pair;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.server.command.ConfigCommand;


@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCommands
{
    private static class AiJBR{
        IModInfo info = ModList.get().getModContainerById(Main.MODID).get().getModInfo();
        final String LICENSE = info.getOwningFile().getLicense();
        final String VERSION = info.getVersion().toString();
        final String NAME = info.getDisplayName();
        final String json = """
{
"text": "\\n","extra":
    [
        {"text": "%s ", "color": "#ff5555", "clickEvent": {"action": "open_url", "value": "https://github.com/AiJYGR/AiJBR"}, "hoverEvent": {"action": "show_text", "contents": {"text": "Github Repository", "color": "#00c897"}}},
        {"text": " Ver ", "color": "#1bbbbb", "clickEvent": {"action": "copy_to_clipboard", "value": ""}, "hoverEvent": { "action": "show_text", "contents": "" }},
        {"text": "%s\\n", "color": "#0eeeee"},
        {"text": "License: ", "color": "#1bbbbb"},
        {"text": "%s\\n", "color": "#0eeeee"},
        {"text": "made by ", "color": "#1bbbbb"},
        {
            "text": "AiJYGR\\n",
            "bold": false,
            "color": "#FFC060",
            "clickEvent": {"action": "open_url", "value": "https://space.bilibili.com/1788766018"},
            "hoverEvent": {"action": "show_text", "contents": {"text": "Plz DM me if you meet bugs!", "color": "#00c897"}}
        },
        {"text": "Thanks to everyone who helped me with this!\\n", "color": "#eeeeee"},
        {"text": "But no one has helped me so far QwQ", "color": "#DDDDDD", "obfuscated": true}
    ]
}""".formatted(NAME, VERSION, LICENSE);
        public AiJBR(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(
                    Commands.literal(Main.MOD_DISPLAY_NAME).requires((source) -> {
                                return source.hasPermission(0);})
                            .executes((command)->{
                                //commmand.getSource().getPlayer().sendSystemMessage(Component.Serializer.fromJson(json));
                                LocalPlayer player = Minecraft.getInstance().player;
                                Component component = Component.Serializer.fromJson(json);
                                if (player != null && component != null) {
                                    player.displayClientMessage(component,false);
                                }
                                //command.getSource().sendSuccess(()->{return Component.literal("");},false);
                                return 1;
                            })
            );
        }
    }

    private static class ScrCommand {
        public ScrCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal(Main.MOD_DISPLAY_NAME).then(Commands.literal("scr").requires((source) -> {
                return source.hasPermission(0);
            }).executes((command) -> {
//                Minecraft.getInstance().tell(() -> {
//                    Minecraft.getInstance().setScreen(new Scr(Component.translatable("title.singleplayer")));
//                });
                Minecraft.getInstance().setScreen(new Scr(Component.translatable("title.singleplayer")));
                //Player player = command.getSource().getPlayer();
                return 1;
            })));
        }
    }
    private static class SyncBPCommand{
        private void SYNC(){
            AiJBackpack.clientsync();
        }
        public SyncBPCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal(Main.MOD_DISPLAY_NAME).then(Commands.literal("sync").requires((source) -> {
                return source.hasPermission(0);
            }).executes((command) -> {
                SYNC();
                LIB.tryPlayerMessage(command.getSource().getPlayer(),"msg.aijbr.yellow","Refreshed your backpack.");
                return 1;
            })));
        }
    }

    private static class ReloadCommand {
        private void reload(CommandSourceStack source) {

            if(source.isPlayer()){
                ServerPlayer player = source.getPlayer();
                LIB.tryBroadcastMessage(player,"\n","msg.aijbr.yellow",player.getName().getString(),"msg.aijbr.info.player_starting_reload");
                LIB.tryBroadcastMessage(player,"msg.aijbr.yellow","msg.server","Start to SYNC JSON...");
            }
            else{
                LIB.tryBroadcastMessage(source.getServer(),"\n","msg.aijbr.yellow","SV","msg.aijbr.info.player_starting_reload");
                LIB.tryBroadcastMessage(source.getServer(),"msg.aijbr.yellow","msg.server","Start to SYNC JSON...");
            }

            Game.isReloaded = false;
            try{
                SyncTag.reload(source.getServer());
                SyncBP.reload(source.getServer());
                Game.isReloaded = true;
            }catch(Exception e){
//                LIB.tryBroadcastMessage(player,"msg.aijbr.red","msg.server","Reload failed:");
//                LIB.tryBroadcastMessage(player,e.getMessage(),"\nPlease check the JSON file.");
//                LIB.tryBroadcastMessage(player,"msg.aijbr.red","msg.aijbr.err.command_executed_failed");
                LIB.tryBroadcastMessage(source.getServer(),"msg.aijbr.red","msg.server","Reload failed:");
                LIB.tryBroadcastMessage(source.getServer(),e.getMessage(),"\nPlease check the JSON file.");
                LIB.tryBroadcastMessage(source.getServer(),"msg.aijbr.red","msg.aijbr.err.command_executed_failed");
            }
            //Game.tryBroadcastMessage(player,"msg.aijbr.bold"," SYNC SUCCESSFULLY.");
        }
        public ReloadCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal("reload").requires((source) -> {
                return source.hasPermission(3);
            }).then(Commands.literal(Main.MOD_DISPLAY_NAME).executes((command) -> {
                reload(command.getSource());
                return 1;
            })));
            dispatcher.register(Commands.literal(Main.MOD_DISPLAY_NAME)
                    .then(Commands.literal("reload").requires(source -> source.hasPermission(3))
                            .executes((command) -> {
                                reload(command.getSource());
                                return 1;
                            })));
        }
    }

    private static class StartCommand {
        public StartCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal(Main.MOD_DISPLAY_NAME).then(Commands.literal("start")
                    .requires((source) -> {return source.hasPermission(2);})
                    .executes((command) -> {
                ServerPlayer player = command.getSource().getPlayer();
                MinecraftServer server = command.getSource().getServer();
                MinecraftForge.EVENT_BUS.post(new ModEvents.GameStartEvent(server.overworld(),player));
                return 1;
            })));
        }
    }

    private static class InitCommand {
        public InitCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal(Main.MOD_DISPLAY_NAME)
                    .then(Commands.literal("init").requires((source) -> {return source.hasPermission(3);})
                        .executes((command) -> {
                ServerPlayer player = command.getSource().getPlayer();
                MinecraftServer server = command.getSource().getServer();
                MinecraftForge.EVENT_BUS.post(new ModEvents.GameInitEvent(server.overworld(),player));
                return 1;
            })));
        }
    }

    private static class PlayerJoinCommand {
        private int PlayerJoin(ServerPlayer player) {
            if(!Game.isInitialized){
                LIB.tryPlayerMessage(player,"msg.aijbr.red","msg.aijbr.err.command_game_not_initialized");
                return 1;
            }
            if(player!=null){
                for(int i = 1; i <= ModConfig.Server.Config.TEAM.TEAMNUM.get();i++)
                {
                    if(AiJBRPlayer.joinTeam(player,i))
                    {
                        LIB.tryPlayerMessage(player,"msg.aijbr.green","msg.aijbr.info.command_player_join_team_p1",AiJBRPlayer.toTeamName(i),"msg.aijbr.info.command_player_join_team_p2");
                        return 0;
                    }
                }
            }
            LIB.tryPlayerMessage(player,"msg.aijbr.red","msg.aijbr.err.command_player_join_team_failed");
            return 1;
        }
        private int PlayerJoin(ServerPlayer player, int team) {
            if(!Game.isInitialized){
                LIB.tryPlayerMessage(player,"msg.aijbr.red","msg.aijbr.err.command_game_not_initialized");
                return 0;
            }
            if(player!=null){
                if(AiJBRPlayer.joinTeam(player,team)){
                    LIB.tryPlayerMessage(player,"msg.aijbr.green","msg.aijbr.info.command_player_join_team_p1",AiJBRPlayer.toTeamName(team),"msg.aijbr.info.command_player_join_team_p2");
                    return 1;
                }
            }
            LIB.tryPlayerMessage(player,"msg.aijbr.red","msg.aijbr.err.command_player_join_team_failed_p1",AiJBRPlayer.toTeamName(team),"msg.aijbr.err.command_player_join_team_failed_p2");
            return 0;
        }
        public PlayerJoinCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal(Main.MOD_DISPLAY_NAME)
                    .then(Commands.literal("join").requires((source) -> {return source.hasPermission(0);})
                            .executes((command)->{
                return PlayerJoin(command.getSource().getPlayer());
            }).then(Commands.argument("team_num", IntegerArgumentType.integer()).executes((command) -> {
                return PlayerJoin(command.getSource().getPlayer(),IntegerArgumentType.getInteger(command,"team_num"));
            }))));
        }
    }

    public static class PlayerLeaveCommand {
        private int PlayerLeave(ServerPlayer player) {
            if(!Game.isInitialized){
                LIB.tryPlayerMessage(player,"msg.aijbr.red","msg.aijbr.err.command_game_not_initialized");
                return 0;
            }
            if(AiJBRPlayer.leaveTeam(player))
            {
                LIB.tryPlayerMessage(player,"msg.aijbr.green","msg.aijbr.info.command_player_leave_team");
            }
            else{
                LIB.tryPlayerMessage(player,"msg.aijbr.red","msg.aijbr.err.command_player_leave_team_failed");
            }
            return 1;
        }
        public PlayerLeaveCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal(Main.MOD_DISPLAY_NAME)
                    .then(Commands.literal("leave").requires((source) -> {return source.hasPermission(0);})
                            .executes((command)->{
                return PlayerLeave(command.getSource().getPlayer());
            })));
        }
    }

    public static class SVCommand {
        public SVCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal(Main.MOD_DISPLAY_NAME)
                    .then(Commands.literal("SV").requires((source) -> {return source.hasPermission(2);})
                    .executes((command)->{
                        String str = "TeamList:" + AiJBRPlayer.getTeamsNames(command.getSource().getServer()) + "\n" +
                                "PlayerList:" + LIB.UUIDtoNames(command.getSource().getServer(), AiJBRPlayer.getPlayers(command.getSource().getServer()));
                        command.getSource().sendSuccess(()->Component.literal(str), false);
                        //LIB.tryPlayerMessage(command.getSource().getPlayer(),str.toString());
                        return AiJBRPlayer.getAliveTeamsCount(command.getSource().getServer());
            })));
        }
    }

    public static class RefillCommand {
        public static long refill(){
            Game.refillTick = Game.gametime;
            return Game.refillTick;
        }
        public RefillCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal(Main.MOD_DISPLAY_NAME)
                    .then(Commands.literal("refill").requires((source) -> {return source.hasPermission(3);})
                    .executes((command)->{
                        LIB.tryPlayerMessage(command.getSource().getPlayer(),String.format("RefillTick = %d",refill()));
                        return 1;
                    })));
        }
    }
    public static class CleanItemsCommand {
        public static int cleanitems(long time,MinecraftServer server){
            return ItemCleaner.cleanitems(server);
        }
        public CleanItemsCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal(Main.MOD_DISPLAY_NAME)
                    .then(Commands.literal("cleanitems").requires((source) -> {return source.hasPermission(3);})
                            .executes((command)->{
                                long time = command.getSource().getLevel().getGameTime();
                                LIB.tryPlayerMessage(command.getSource().getPlayer(),String.format("CleanItemsTick = %d  Count = %d",time,cleanitems(time,command.getSource().getServer())));

                                return 1;
                            })));
        }
    }

    public static class MapResetCommand {
        public static Pair<Integer,Integer> resetMap(ServerLevel serverLevel){
            return MapResetter.getInstance(serverLevel).resetMap(serverLevel);
        }
        public static int clearData(ServerLevel serverLevel){
            return MapResetter.getInstance(serverLevel).clearData(serverLevel);
        }
        public MapResetCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
            dispatcher.register(Commands.literal(Main.MOD_DISPLAY_NAME)
                    .then(Commands.literal("mapresetter")
                    .then(Commands.literal("reset").requires((source) -> {return source.hasPermission(3);})
                            .executes((command)->{
                                var result =resetMap(command.getSource().getLevel());
                                String str = String.format("BlocksReset:%d BlockEntities:%d",result.first,result.second);
                                try{
                                    LIB.tryPlayerMessage(command.getSource().getPlayerOrException(),str);
                                }
                                catch(Exception e){
                                    System.out.println("[Server]"+str);
                                }
                                return 1;
                            }))
                    .then(Commands.literal("cleardata").requires((source) -> {return source.hasPermission(3);})
                            .executes((command)->{
                                int result = clearData(command.getSource().getLevel());
                                String str = String.format("DataCleared:%d",result);
                                try{
                                    LIB.tryPlayerMessage(command.getSource().getPlayerOrException(),str);
                                }
                                catch(Exception e){
                                    System.out.println("[Server]"+str);
                                }
                                return 1;
                            }))
            ));
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
        new RefillCommand(event.getDispatcher());
        new CleanItemsCommand(event.getDispatcher());
        new MapResetCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onClientCommandsRegister(RegisterClientCommandsEvent event){
        //new ScrCommand(event.getDispatcher());
        new AiJBR(event.getDispatcher());
        new SyncBPCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }
}