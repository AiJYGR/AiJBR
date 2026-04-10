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
        this.str = buf.readUtf(Tagger.PMAXLENGTH);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.str, Tagger.PMAXLENGTH);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(str.equals("!")){
                ServerPlayer player = ctx.get().getSender();
                ModMessages.ServerSendToPlayer(new MSGClientTagJSON(Tagger.rawjson), player);
                Main.LOGGER.info("[AiJTAG]: Server: send json.");
            }
        });
        ctx.get().setPacketHandled(true);
    }
}