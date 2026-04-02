package com.aijygr.Events.Game.Ring;


import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class GameInitEvent extends Event
{
    private ServerLevel level;
    private Player player;
    public GameInitEvent(ServerLevel level,Player player)
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
