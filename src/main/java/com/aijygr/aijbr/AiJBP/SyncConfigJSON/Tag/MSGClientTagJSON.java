package com.aijygr.aijbr.AiJBP.SyncConfigJSON.Tag;

import com.aijygr.aijbr.Network.ClientPackageHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGClientTagJSON {
    private final String str;
    public MSGClientTagJSON(String str) { this.str = str; }
    public static MSGClientTagJSON decoder(FriendlyByteBuf buf) {
        return new MSGClientTagJSON(buf.readUtf());
    }
    public void encoder(FriendlyByteBuf buf) {
        buf.writeUtf(this.str, SyncTag.PMAXLENGTH);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ClientPackageHandler.MSGClientTagJson(this.str);
            });
        });
        ctx.get().setPacketHandled(true);
    }


}