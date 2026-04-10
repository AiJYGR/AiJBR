package com.aijygr.AiJTAG;

import com.aijygr.Main;
import com.google.common.hash.Hashing;
import com.google.gson.JsonParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

import static com.aijygr.AiJTAG.Tagger.HASH;

public class MSGClientTagJSON {
    private final String str;
    public MSGClientTagJSON(String str) { this.str = str; }
    public MSGClientTagJSON(FriendlyByteBuf buf) {
        this.str = buf.readUtf(Tagger.PMAXLENGTH);
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.str, Tagger.PMAXLENGTH);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Tagger.json = JsonParser.parseString(str).getAsJsonObject();
            String hash = HASH(str);
            Tagger.saveLocalCache(str,hash);
            Main.LOGGER.info("[AiJTAG]: Client: received json and saved local cache.");
        });
        ctx.get().setPacketHandled(true);
    }
}