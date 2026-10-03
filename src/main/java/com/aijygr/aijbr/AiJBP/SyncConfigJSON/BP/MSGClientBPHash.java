package com.aijygr.aijbr.AiJBP.SyncConfigJSON.BP;

import com.aijygr.aijbr.Network.ClientPackageHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGClientBPHash {
    private final String str;
    public MSGClientBPHash(String str) { this.str = str; }
    public static MSGClientBPHash decoder(FriendlyByteBuf buf) {
        return new MSGClientBPHash(buf.readUtf());
    }
    public void encoder(FriendlyByteBuf buf) {
        buf.writeUtf(this.str, SyncBP.PMAXLENGTH);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
               ClientPackageHandler.MSGClientBPHash(this.str);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}