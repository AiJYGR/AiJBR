package com.aijygr.AiJTAG;

import com.aijygr.Main;
import com.google.gson.JsonParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.aijygr.AiJTAG.AiJTAGSync.HASH;

public class MSGClientTagJSON {
    private final String str;
    public MSGClientTagJSON(String str) { this.str = str; }
    public MSGClientTagJSON(FriendlyByteBuf buf) {
        this.str = buf.readUtf(AiJTAGSync.PMAXLENGTH);
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.str, AiJTAGSync.PMAXLENGTH);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            AiJTAGSync.json = JsonParser.parseString(str).getAsJsonObject();
            String hash = HASH(str);
            AiJTAGSync.saveLocalCache(str,hash);
            Main.LOGGER.info("[AiJTAG][Client] Successfully synced json and saved local cache.");
        });
        ctx.get().setPacketHandled(true);
    }
}