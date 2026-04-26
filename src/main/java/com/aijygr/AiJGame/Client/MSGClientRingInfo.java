package com.aijygr.AiJGame.Client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGClientRingInfo {
    private final int x;
    private final int z;
    private final double size;
    private final String generationmode;

    public MSGClientRingInfo(int x, int z, double size, String generationmode) {
        this.x = x;
        this.z = z;
        this.size = size;
        this.generationmode = generationmode;
    }
    public MSGClientRingInfo(FriendlyByteBuf buf) {
        this.x = buf.readInt();
        this.z = buf.readInt();
        this.size = buf.readDouble();
        this.generationmode = buf.readUtf();
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(z);
        buf.writeDouble(size);
        buf.writeUtf(generationmode);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientGame.setClientRing(x, z, size,generationmode);
        });
        ctx.get().setPacketHandled(true);
    }
}