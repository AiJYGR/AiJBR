package com.aijygr.aijbr.AiJBP;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGClientExecSync {
    public MSGClientExecSync() {
    }
    public MSGClientExecSync(FriendlyByteBuf buf) {
    }
    public void encode(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            AiJBackpack.clientsync();
        });
        ctx.get().setPacketHandled(true);
    }
}