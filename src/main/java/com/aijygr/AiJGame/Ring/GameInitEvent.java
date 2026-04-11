package com.aijygr.AiJGame.Ring;


import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Event;

public class GameInitEvent extends Event
{
    private ServerLevel level;
    private ServerPlayer player;
    public GameInitEvent(ServerLevel level, ServerPlayer player)
    {
        this.level = level;
        this.player = player;
    }
    public ServerLevel getLevel()
    {
        return this.level;
    }
    public ServerPlayer getPlayer()
    {
        return this.player;
    }
}
