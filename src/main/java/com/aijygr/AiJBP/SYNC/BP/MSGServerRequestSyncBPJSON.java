package com.aijygr.AiJBP.SYNC.BP;

import com.aijygr.Main;
import com.aijygr.ModMessages;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGServerRequestSyncBPJSON {
    private final String str;
    public MSGServerRequestSyncBPJSON(String str) { this.str = str; }
    public MSGServerRequestSyncBPJSON(FriendlyByteBuf buf) {
        // 给 256KB 的宽限，防止大型配置包溢出 GEMINI
        this.str = buf.readUtf(SyncBP.PMAXLENGTH);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.str, SyncBP.PMAXLENGTH);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(str.equals("!")){
                ServerPlayer player = ctx.get().getSender();
                ModMessages.ServerSendToPlayer(new MSGClientBPJSON(SyncBP.rawjson), player);
                Main.LOGGER.info("[AiJBR][MSGServerRequestSyncBPJSON] SyncBP request received, send json file.");
            }
            else if (str.equals("=")||str.equals("+")){
                ServerPlayer player = ctx.get().getSender();
                Main.LOGGER.info("[AiJBR][MSGServerRequestSyncBPJSON]"+str+player.getName().getString()+" has synced json file.");
            }
        });
        ctx.get().setPacketHandled(true);
    }
}