package com.aijygr.AiJBP.SYNC.Tag;

import com.aijygr.Main;
import com.google.gson.JsonParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.aijygr.AiJBP.SYNC.Tag.SyncTag.HASH;

public class MSGClientTagJSON {
    private final String str;
    public MSGClientTagJSON(String str) { this.str = str; }
    public MSGClientTagJSON(FriendlyByteBuf buf) {
        this.str = buf.readUtf(SyncTag.PMAXLENGTH);
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.str, SyncTag.PMAXLENGTH);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            SyncTag.json = JsonParser.parseString(str).getAsJsonObject();
            String hash = HASH(str);
            SyncTag.saveLocalCache(str,hash);
            Main.LOGGER.info("[AiJBR][MSGClientTagJSON] Successfully synced json and saved local cache.");
        });
        ctx.get().setPacketHandled(true);
    }
}