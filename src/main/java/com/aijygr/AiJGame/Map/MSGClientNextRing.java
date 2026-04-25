package com.aijygr.AiJGame.Map;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGClientNextRing {
    private final int x;
    private final int z;
    private final double size;

    public MSGClientNextRing(int x,int z,double size) {
        this.x = x;
        this.z = z;
        this.size = size;
    }
    public MSGClientNextRing(FriendlyByteBuf buf) {
        this.x = buf.readInt();
        this.z = buf.readInt();
        this.size = buf.readDouble();
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(z);
        buf.writeDouble(size);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Map.setClientRing(x, z, size);
        });
        ctx.get().setPacketHandled(true);
    }
}