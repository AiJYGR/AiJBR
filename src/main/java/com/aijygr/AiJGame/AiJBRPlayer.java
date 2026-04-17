package com.aijygr.AiJGame;

import com.aijygr.ModConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AiJBRPlayer {
    public static int getTeamColor(int i){
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
    public static String getTeamName(int i){
        return String.format("%02d",i);
    }
    public static String getTeamID(int i){
        return "AiJBR"+getTeamName(i);
    }
    public static boolean joinTeam(ServerPlayer player, int index) {
        Scoreboard scoreboard = player.getServer().getScoreboard();
        if (scoreboard != null) {
            PlayerTeam team = scoreboard.getPlayerTeam(getTeamID(index));
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

    public static void initTeams(int team_num, int team_size, Scoreboard scoreboard) {

        for(int i=1;i<=team_num;i++){
            int color;
            if(team_size == 1)
                color = 0xf0f0f0;
            else
                color = getTeamColor(i);
            String teamname = getTeamName(i);
            String teamid = getTeamID(i);
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
}
