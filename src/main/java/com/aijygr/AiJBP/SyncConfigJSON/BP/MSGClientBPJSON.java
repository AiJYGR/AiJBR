package com.aijygr.AiJBP.SyncConfigJSON.BP;

import com.aijygr.LIB;
import com.aijygr.Main;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.aijygr.AiJBP.SyncConfigJSON.Tag.SyncTag.HASH;

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
            try{
                SyncBP.json = JsonParser.parseString(str).getAsJsonObject();
                String hash = HASH(str);
                SyncBP.saveLocalCache(str,hash);
                Reload.ReloadBP();
                LIB.tryPlayerMessage(Minecraft.getInstance().player,"msg.aijbr.green","[MSGClient BPJSON] Success.");
            }catch(Exception e){
                Main.LOGGER.error("[MSGClientBPJSON]:{}", e.getMessage());
            }

        });
        ctx.get().setPacketHandled(true);
    }
}