package com.aijygr.AiJGame;

import com.aijygr.AiJGame.Client.MSGClientPlayerInfo;
import com.aijygr.Item.Lock;
import com.aijygr.LIB;
import com.aijygr.ModConfig;
import com.aijygr.ModEvents;
import com.aijygr.ModMessages;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class AiJBRPlayer {
    private static int toTeamColor(int i){
        return switch (i){
            case 1 -> 0xff0000;
            case 2 -> 0x0000ff;
            case 3 -> 0xffff00;
            case 4 -> 0x00ff00;
            case 5 -> 0xff7f00;
            case 6 -> 0x7f00ff;
            case 7 -> 0x00ffff;
            case 8 -> 0x007fff;
            case 9 -> 0xff00ff;

            case 10 -> 0xffffff;
            case 11 -> 0x000000;
            case 12 -> 0x7f7f7f;

            case 13 -> 0xff7f7f;
            case 14 -> 0x7f7fff;
            case 15 -> 0xffff7f;
            case 16 -> 0x7fff7f;
            case 17 -> 0xffc07f;
            case 18 -> 0xc07fff;
            case 19 -> 0x7fffff;
            case 20 -> 0x7fc0ff;
            case 21 -> 0xff7fff;

            case 22 -> 0x7f0000;
            case 23 -> 0x00007f;
            case 24 -> 0x7f7f00;
            case 25 -> 0x007f00;
            case 26 -> 0x7f3f00;
            case 27 -> 0x3f007f;
            case 28 -> 0x007f7f;
            case 29 -> 0x003f7f;
            case 30 -> 0x7f007f;

            case 31 -> 0xc0c0c0;
            case 32 -> 0x3f3f3f;
            default -> 0x114514;
        };
    }
    public static String toTeamName(int i){
        return String.format("%02d",i);
    }
    public static String toTeamID(int i){
        return "AiJBR"+ toTeamName(i);
    }

    public static boolean joinTeam(ServerPlayer player, int index) {
        Scoreboard scoreboard = player.getServer().getScoreboard();
        if (scoreboard != null) {
            PlayerTeam team = scoreboard.getPlayerTeam(toTeamID(index));
            if (team != null && team.getPlayers().size() < ModConfig.Server.Config.TEAM.TEAMSIZE.get()) {
                scoreboard.removePlayerFromTeam(player.getName().getString());
                scoreboard.addPlayerToTeam(player.getName().getString(), team);
                return true;
            }
        }
        return false;
    }
    public static boolean leaveTeam(ServerPlayer player) {
        Scoreboard scoreboard = player.getServer().getScoreboard();
        if(scoreboard != null) {
            return scoreboard.removePlayerFromTeam(player.getName().getString());
        }
        return false;
    }

    public static List<String> getTeamsNames(MinecraftServer server){
        List<String> list = new ArrayList<>();
        for(PlayerTeam team : server.getScoreboard().getPlayerTeams())
        {
            if(!team.getPlayers().isEmpty() && team.getName().contains("AiJBR"))
            {
                list.add(team.getName());
            }
        }
        return list;
    }
    public static List<UUID> getPlayers(MinecraftServer server){
        List<UUID> list = new ArrayList<>();
        for(String teamname : getTeamsNames(server)){
            PlayerTeam team = server.getScoreboard().getPlayerTeam(teamname);
            if(team != null)
                for(String playername : team.getPlayers())
                    for (ServerPlayer player : server.getPlayerList().getPlayers())
                        if(player.getName().getString().equals(playername))
                        {
                            list.add(player.getUUID());
                            break;
                        }
        }
        return list;
    }

    public static void initTeams(int team_num, int team_size, Scoreboard scoreboard) {
        for(int i=1;i<=team_num;i++){
            int color;
            if(team_size == 1)
                color = 0xf0f0f0;
            else
                color = toTeamColor(i);
            String teamname = toTeamName(i);
            String teamid = toTeamID(i);
            if(scoreboard.getPlayerTeam(teamid)!=null) {
                scoreboard.removePlayerTeam(scoreboard.getPlayerTeam(teamid));
            }
            PlayerTeam team = scoreboard.addPlayerTeam(teamid);
            Component component = Component
                    .literal(teamname)
                    .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))
                    );

            team.setAllowFriendlyFire(ModConfig.Server.Config.TEAM.ALLOWFRIENDLYFIRE.get().get());
            team.setCollisionRule(ModConfig.Server.Config.TEAM.COLLISION_RULE.get());
            team.setDeathMessageVisibility(ModConfig.Server.Config.TEAM.DEATHMSG_VISIBILITY.get());
            team.setDisplayName(component);
            team.setNameTagVisibility(ModConfig.Server.Config.TEAM.NAMETAG_VISIBILITY.get());
            team.setPlayerPrefix(Component.empty());
            team.setPlayerSuffix(Component.empty());
            team.setSeeFriendlyInvisibles(ModConfig.Server.Config.TEAM.SEEFRIENTLYINVISIBLES.get().get());
        }
    }

    @SubscribeEvent
    public static void onPlayerFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            event.setDamageMultiplier(ModConfig.Server.Config.PLAYER.FALLDAMAGEMUTIPIER.get().floatValue());
        }
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        BlockState blockstate = event.getState();
        Player player = event.getEntity();
        if(!ModConfig.Server.Config.PLAYER.SURVIVALBREAK.get().get()){
            if(ModConfig.Server.Config.PLAYER.SURVIVALBREAKGLASS.get().get()){
                if(blockstate.is(Tags.Blocks.GLASS)||blockstate.is(Tags.Blocks.GLASS_PANES)){
                    event.setNewSpeed(event.getOriginalSpeed());
                    return;
                }
            }
            event.setNewSpeed(-1);
        }
    }



    private static void resetPlayerAttributes(List<ServerPlayer> players){
        for (ServerPlayer player : players) {
            player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(ModConfig.Server.Config.PLAYER.MOVEMENTSPEED.get());
            player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(ModConfig.Server.Config.PLAYER.MAXHEALTH.get());
            player.setHealth(ModConfig.Server.Config.PLAYER.MAXHEALTH.get());
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event){
        if(event.getEntity() instanceof ServerPlayer player)
            resetPlayerAttributes(List.of(player));
    }

    @SubscribeEvent
    public static void onGameInit(ModEvents.GameInitEvent event) {
        resetPlayerAttributes(event.getLevel().getServer().getPlayerList().getPlayers());
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        Game.isReloaded = false;
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {

        if(event.getEntity() instanceof ServerPlayer player) {
            Inventory inventory = player.getInventory();
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack stack = inventory.getItem(i);
                if (!stack.isEmpty() && stack.getItem() instanceof Lock)
                    inventory.setItem(i, ItemStack.EMPTY);
            }

            UUID uuid = player.getUUID();
            if(Game.playerlist.containsKey(uuid)){
                Game.playerlist.put(uuid, Game.PlayerStatus.DEAD);
            }
            MinecraftServer server = event.getEntity().getServer();
            updateAndBroadcastPlayerInfo(server);
        }


    }

    public static List<String> getAlivePlayers(MinecraftServer server){
        List<UUID> list = new ArrayList<>();
        for(UUID uuid : Game.playerlist.keySet()){
            if(Game.playerlist.get(uuid)!= Game.PlayerStatus.DEAD){
                list.add(uuid);
            }
        }
        return LIB.UUIDtoNames(server,list);
    }

    public static int getAlivePlayersCount(MinecraftServer server){
        return getAlivePlayers(server).size();
    }

    public static int teamcount = 0;
    private static void updateAliveTeams(MinecraftServer server){
        teamcount = 0;
        for(String teamname : Game.teamlist.keySet()){
            PlayerTeam team = server.getScoreboard().getPlayerTeam(teamname);
            if (team != null) {
                boolean flag = false;
                for(String str : team.getPlayers()){
                    UUID uuid = LIB.playerNametoUUID(server,str);
                    if(uuid==null)
                        uuid = UUID.fromString(str);
                    if(Game.playerlist.containsKey(uuid)){
                        if(Game.playerlist.get(uuid)!= Game.PlayerStatus.DEAD){
                            flag = true;
                            break;
                        }
                    }
                }
                if(flag){
                    teamcount++;
                    System.out.println(teamname+"ALIVE");
                    Game.teamlist.put(teamname, Game.TeamStatus.ALIVE);
                }
                else
                {
                    System.out.println(teamname+"DEAD");
                    Game.teamlist.put(teamname, Game.TeamStatus.DEAD);
                }

            }
        }
    }

    public static int getAliveTeamsCount(MinecraftServer server){
        return teamcount;
    }

    public static void updateAndBroadcastPlayerInfo(MinecraftServer server){
        updateAliveTeams(server);
        ModMessages.ServerSendToAll(new MSGClientPlayerInfo(getAlivePlayersCount(server), getAliveTeamsCount(server)));
    }
}
