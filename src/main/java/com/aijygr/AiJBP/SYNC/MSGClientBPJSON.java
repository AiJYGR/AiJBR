package com.aijygr.AiJBP.SYNC;

import com.aijygr.Main;
import com.google.gson.JsonParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.aijygr.AiJBP.SYNC.SyncTag.HASH;

public class MSGClientBPJSON {
    private final String str;
    public MSGClientBPJSON(String str) { this.str = str; }
    public MSGClientBPJSON(FriendlyByteBuf buf) {
        this.str = buf.readUtf(SyncBP.PMAXLENGTH);
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.str, SyncBP.PMAXLENGTH);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            SyncBP.json = JsonParser.parseString(str).getAsJsonObject();
            String hash = HASH(str);
            SyncBP.saveLocalCache(str,hash);
            Main.LOGGER.info("[AiJBR][MSGClientBPJSON] Successfully synced json and saved local cache.");
        });
        ctx.get().setPacketHandled(true);
    }
}