package com.aijygr.AiJBP.SyncConfigJSON.Tag;

import com.aijygr.Main;
import com.aijygr.ModMessages;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGServerRequestSyncTagJSON {
    private final String str;
    public MSGServerRequestSyncTagJSON(String str) { this.str = str; }
    public MSGServerRequestSyncTagJSON(FriendlyByteBuf buf) {
        // 给 256KB 的宽限，防止大型配置包溢出 GEMINI
        this.str = buf.readUtf(SyncTag.PMAXLENGTH);
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.str, SyncTag.PMAXLENGTH);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(str.equals("!")){
                ServerPlayer player = ctx.get().getSender();
                ModMessages.ServerSendToPlayer(new MSGClientTagJSON(SyncTag.rawjson), player);
                Main.LOGGER.info("[AiJBR][MSGServerRequestSyncTagJSON] SyncBP request received, send json file.");
            }
            else if (str.equals("=")||str.equals("+") ){
                ServerPlayer player = ctx.get().getSender();
                Main.LOGGER.info("[AiJBR][MSGServerRequestSyncTagJSON]"+str+player.getName().getString()+" has synced json file.");
            }
        });
        ctx.get().setPacketHandled(true);
    }
}