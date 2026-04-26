package com.aijygr.AiJGame.Client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGClientPlayerInfo {
    private final int players;
    private final int teams;

    public MSGClientPlayerInfo(int players, int teams) {
        this.players = players;
        this.teams = teams;
    }
    public MSGClientPlayerInfo(FriendlyByteBuf buf) {
        this.players = buf.readInt();
        this.teams = buf.readInt();
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(players);
        buf.writeInt(teams);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientGame.setClientGame(players, teams);
        });
        ctx.get().setPacketHandled(true);
    }
}