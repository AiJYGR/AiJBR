package com.aijygr;

import com.aijygr.AiJGame.Game;
import com.sun.jna.platform.win32.WinDef;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class LIB {
    public static void tryPlayerMessage(Player player, String message) {
        if(player!=null)
            player.sendSystemMessage(Component.translatable(message));
            //player.sendSystemMessage(Component.translatable("msg.aijbr").append(Component.translatable(message)));
        Main.LOGGER.info("[AiJBR]tryPlayerMessage:{}", message);
    }

    public static void tryPlayerMessage(Player player, String... messages) {
        messages = Arrays.copyOf(messages, messages.length);
        MutableComponent component = MutableComponent.create(ComponentContents.EMPTY);
        StringBuilder str = new StringBuilder();
        for (String message : messages) {
            component.append(Component.translatable(message));
            str.append(message).append(" ");
        }
        if(player!=null)
            player.sendSystemMessage(component);
        else
            Main.LOGGER.info("[AiJBR]tryPlayerMessage:{}", str);
    }

    public static void tryBroadcastMessage(Player player, String... messages) {
        messages = Arrays.copyOf(messages, messages.length);
        MutableComponent component = MutableComponent.create(ComponentContents.EMPTY);
        StringBuilder str = new StringBuilder();
        for (String message : messages) {
            component.append(Component.translatable(message));
            str.append(message).append(" ");
        }
        if(player!=null)
            player.getServer().getPlayerList().broadcastSystemMessage(component,false);
        else
            Main.LOGGER.info("[AiJBR]tryBroadcastMessage:{}", str);
    }

    public static void TPwithForceLoad(Entity entity, Vec3 vec3) {
        entity.getServer().getLevel(Level.OVERWORLD).setChunkForced((int)Math.ceil(vec3.x/16),(int)Math.ceil(vec3.z/16),true);
        entity.setPos(vec3.x, vec3.y, vec3.z);
    }

    public static Entity SMwithForceLoad(MinecraftServer server, EntityType entitytype, Vec3 vec3) {
        BlockPos pos = new BlockPos((int)Math.ceil(vec3.x),(int)Math.ceil(vec3.y),(int)Math.ceil(vec3.z));
        ServerLevel level = server.getLevel(Level.OVERWORLD);
        level.setChunkForced((int)Math.ceil(vec3.x/16),(int)Math.ceil(vec3.z/16),true);
        return entitytype.spawn(level,pos, MobSpawnType.COMMAND);
    }

    public static int PLAYERS(MinecraftServer server, List<UUID> uuids, Consumer<ServerPlayer> action) {
        List<UUID> templist = new ArrayList<>(uuids);
        int i = 0;
        for (UUID uuid : templist) {
            ServerPlayer player = server.getPlayerList().getPlayer(uuid);
            if (player != null) {
                action.accept(player);
                i++;
            }
        }
        return i;
    }
    public static int PLAYERS(List<ServerPlayer> players, Consumer<ServerPlayer> action){
        int i = 0;
        for(ServerPlayer player : players){
            if(player != null){
                action.accept(player);
            }
        }
        return i;
    }

    private static boolean allowBRLOG(){
        return ModConfig.Server.Config.ALLOW_BRLOG.get().get();
    };
    private static long memTime = Long.MIN_VALUE;
    public static void BRLOG(String string){
        if(allowBRLOG()){
            if(memTime != Game.BRGameTime)
            {
                memTime = Game.BRGameTime;
                System.out.println("\n#AiJBR "+Game.BRGameTime+": ");
            }
//            ArrayList<String> strings = new ArrayList<>(List.of(string.split("\n")));
//            for(String str : strings){
//                System.out.println(str);
//            }
            System.out.println(string);
        }
    }
    public static List<String> UUIDtoNames(MinecraftServer server, List<UUID> uuids) {
        List<String> list = new ArrayList<>();
        for (UUID uuid : uuids) {
            list.add(server.getPlayerList().getPlayer(uuid).getName().getString());
        }
        return list;
    }

    public static boolean SVLV1TK(TickEvent.LevelTickEvent event){
        return event.level.dimension().equals(Level.OVERWORLD)
                && event.phase == TickEvent.Phase.END
                && !event.side.isClient()
                && !event.level.isClientSide;
    }
    public static boolean SVLV1TK(TickEvent.PlayerTickEvent event){
        return event.phase == TickEvent.Phase.END
                && !event.side.isClient();
    }

    public static UUID UUID(int part1, int part2, int part3, int part4) {
        int[] a = {part1,part2,part3,part4};
        return UUIDUtil.uuidFromIntArray(a);
    }

    public static class VecIntXZ {
        public int x;
        public int z;
        public VecIntXZ(int x, int z) {
            this.x = x;
            this.z = z;
        }
        public VecIntXZ brlog(){
            LIB.BRLOG("("+this.x+","+this.z+")");
            return this;
        }
    }
}
