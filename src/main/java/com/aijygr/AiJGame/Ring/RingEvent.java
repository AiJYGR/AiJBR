package com.aijygr.AiJGame.Ring;


import net.minecraftforge.eventbus.api.Event;
import net.minecraft.world.level.Level;

public class RingEvent extends Event
{
    private Level level;
    public RingEvent(Level level)
    {
        this.level = level ;
    }
}
