package com.aijygr;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Event;

public class ModEvents {
    public static class GameInitEvent extends Event
    {
        private ServerLevel level;
        private ServerPlayer player;
        public GameInitEvent(ServerLevel level,ServerPlayer player)
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
        private ServerPlayer player;
        public GameStartEvent(ServerLevel level,ServerPlayer player)
        {
            this.level = level;
            this.player = player;
        }
        public ServerLevel getLevel() {
            return this.level;
        }
        public ServerPlayer getPlayer()
        {
            return this.player;
        }
    }
    public static class GameEndEvent extends Event
    {
        private MinecraftServer server;
        private String extra;
        public GameEndEvent(MinecraftServer server)
        {
            this.server = server;
            this.extra = "";
        }
        public GameEndEvent(MinecraftServer server,String extra)
        {
            this.server = server;
            this.extra = extra;
        }
        public MinecraftServer getServer(){
            return this.server;
        }
        public String getExtra(){
            return this.extra;
        }
    }
}
