package com.aijygr.aijbr.AiJBP.SyncConfigJSON.BP;

import com.aijygr.aijbr.Main;
import com.aijygr.aijbr.Network.ModMessages;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGServerRequestSyncBPJSON {
    private final String str;
    public MSGServerRequestSyncBPJSON(String str) { this.str = str; }
    public static MSGServerRequestSyncBPJSON decoder(FriendlyByteBuf buf) {
        return  new MSGServerRequestSyncBPJSON(buf.readUtf());
    }

    public void encoder(FriendlyByteBuf buf) {
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