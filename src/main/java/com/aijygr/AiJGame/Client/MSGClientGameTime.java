package com.aijygr.AiJGame.Client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGClientGameTime {
    private final int round;
    private final int roundtick;
    private final boolean isShrinking;

    public MSGClientGameTime(int round, int roundtick, boolean isShrinking) {
        this.round = round;
        this.roundtick = roundtick;
        this.isShrinking = isShrinking;
    }
    public MSGClientGameTime(FriendlyByteBuf buf) {
        this.round = buf.readInt();
        this.roundtick = buf.readInt();
        this.isShrinking = buf.readBoolean();
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(round);
        buf.writeInt(roundtick);
        buf.writeBoolean(isShrinking);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientGame.setClientTime(round,roundtick,isShrinking);
        });
        ctx.get().setPacketHandled(true);
    }
}