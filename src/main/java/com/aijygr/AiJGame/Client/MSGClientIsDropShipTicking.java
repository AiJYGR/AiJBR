package com.aijygr.AiJGame.Client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGClientIsDropShipTicking {
    private final boolean bool;

    public MSGClientIsDropShipTicking(boolean bool) {
        this.bool = bool;
    }
    public MSGClientIsDropShipTicking(FriendlyByteBuf buf) {
        this.bool = buf.readBoolean();
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(bool);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientGame.setIsDropShipTicking(bool);
        });
        ctx.get().setPacketHandled(true);
    }
}