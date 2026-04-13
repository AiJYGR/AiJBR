package com.aijygr.AiJBP.SyncConfigJSON.BP;

import com.aijygr.Main;
import com.aijygr.ModMessages;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGClientBPHash {
    private final String str;
    public MSGClientBPHash(String str) { this.str = str; }
    public MSGClientBPHash(FriendlyByteBuf buf) {
        this.str = buf.readUtf(SyncBP.PMAXLENGTH);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.str, SyncBP.PMAXLENGTH);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(SyncBP.clienthash.isEmpty()){
                SyncBP.loadLocalCache();
            }
            Main.LOGGER.info("[AiJBR][MSGClientBPHash]: Server tag hash received = "+ str);
            Main.LOGGER.info("[AiJBR][MSGClientBPHash]: Client Hash = "+ SyncBP.clienthash);
            if(SyncBP.clienthash.equals(this.str)){
                Main.LOGGER.info("[AiJBR][MSGClientBPHash]: EQUAL.");
                ModMessages.PlayerSendToServer(new MSGServerRequestSyncBPJSON("="));
                Reload.ReloadBP();
            }
            else{
                Main.LOGGER.info("[AiJBR][MSGClientBPHash]: DIFFERENT.");
                ModMessages.PlayerSendToServer(new MSGServerRequestSyncBPJSON("!"));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}