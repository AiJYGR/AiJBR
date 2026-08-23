package com.aijygr.aijbr.AiJBP.SyncConfigJSON.BP;

import com.aijygr.aijbr.LIB;
import com.aijygr.aijbr.Main;
import com.aijygr.aijbr.Network.ClientPackageHandler;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.aijygr.aijbr.AiJBP.SyncConfigJSON.Tag.SyncTag.HASH;

public class MSGClientBPJSON {
    private final String str;
    public MSGClientBPJSON(String str) { this.str = str; }
    public static MSGClientBPJSON decoder(FriendlyByteBuf buf) {
        return new MSGClientBPJSON(buf.readUtf(SyncBP.PMAXLENGTH));
    }
    public void encoder(FriendlyByteBuf buf) {
        buf.writeUtf(this.str, SyncBP.PMAXLENGTH);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ClientPackageHandler.MSGClientBPJson(this.str);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}