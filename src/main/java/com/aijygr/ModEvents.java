package com.aijygr;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class ModEvents {
    public static class GameInitEvent extends Event
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
    public static class GameStartEvent extends Event
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
}
