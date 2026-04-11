package com.aijygr.AiJGame;


import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class GameStartEvent extends Event
{
    private ServerLevel level;
    private Player player;
    public GameStartEvent(ServerLevel level,Player player)
    {
        this.level = level;
        this.player = player;
    }
    public ServerLevel getLevel()
    {
        return this.level;
    }
    public Player getPlayer()
    {
        return this.player;
    }
}
