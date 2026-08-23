package com.aijygr.aijbr.AiJGame.Client;

import com.aijygr.aijbr.Network.ClientPackageHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
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
    public static MSGClientRingInfo decoder(FriendlyByteBuf buf) {
        return new MSGClientRingInfo(buf.readInt(), buf.readInt(), buf.readDouble(), buf.readUtf());
    }
    public void encoder(FriendlyByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(z);
        buf.writeDouble(size);
        buf.writeUtf(generationmode);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ClientPackageHandler.MSGClientRingInfo(this.x, this.z, this.size, this.generationmode);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}