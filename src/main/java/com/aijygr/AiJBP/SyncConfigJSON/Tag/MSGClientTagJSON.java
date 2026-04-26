package com.aijygr.AiJBP.SyncConfigJSON.Tag;

import com.aijygr.AiJGame.Client.ClientGame;
import com.aijygr.LIB;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.aijygr.AiJBP.SyncConfigJSON.Tag.SyncTag.HASH;

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
            ClientGame.isTagSynced = true;
            LIB.tryPlayerMessage(Minecraft.getInstance().player,"msg.aijbr.green","[MSGClient TagJSON] Success.");
        });
        ctx.get().setPacketHandled(true);
    }
}