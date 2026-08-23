package com.aijygr.aijbr.AiJBP.SyncConfigJSON.Tag;

import com.aijygr.aijbr.Network.ClientPackageHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGClientTagHash {
    private final String str;
    public MSGClientTagHash(String str) { this.str = str; }
    public static MSGClientTagHash decoder(FriendlyByteBuf buf) {
        return new MSGClientTagHash(buf.readUtf());
    }
    public void encoder(FriendlyByteBuf buf) {
        buf.writeUtf(this.str, SyncTag.PMAXLENGTH);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ClientPackageHandler.MSGClientTagHash(str);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}