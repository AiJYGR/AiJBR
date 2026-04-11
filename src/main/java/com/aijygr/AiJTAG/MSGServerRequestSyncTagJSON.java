package com.aijygr.AiJTAG;

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
        this.str = buf.readUtf(AiJTAGSync.PMAXLENGTH);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.str, AiJTAGSync.PMAXLENGTH);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(str.equals("!")){
                ServerPlayer player = ctx.get().getSender();
                ModMessages.ServerSendToPlayer(new MSGClientTagJSON(AiJTAGSync.rawjson), player);
                Main.LOGGER.info("[AiJTAG][Server] Sync request received, send json file.");
            }
            else if (str.equals("=")||str.equals("+")){
                ServerPlayer player = ctx.get().getSender();
                Main.LOGGER.info("[AiJTAG][Server]"+str+player.getName().getString()+" has synced json file.");
            }
        });
        ctx.get().setPacketHandled(true);
    }
}