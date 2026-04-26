package com.aijygr.AiJGame.Client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGClientGameInfo {
    private final int round;
    private final int roundtick;

    public MSGClientGameInfo(int round,int roundtick) {
        this.round = round;
        this.roundtick = roundtick;
    }
    public MSGClientGameInfo(FriendlyByteBuf buf) {
        this.round = buf.readInt();
        this.roundtick = buf.readInt();
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(round);
        buf.writeInt(roundtick);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientGame.setClientRound(round,roundtick);
        });
        ctx.get().setPacketHandled(true);
    }
}