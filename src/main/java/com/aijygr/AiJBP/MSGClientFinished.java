package com.aijygr.AiJBP;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGClientFinished {
    public MSGClientFinished() {
    }
    public MSGClientFinished(FriendlyByteBuf buf) {
    }
    public void encode(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            AiJBackpack.setAvaliable();
            System.out.println("AVALIABLE!");
        });
        ctx.get().setPacketHandled(true);
    }
}