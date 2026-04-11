package com.aijygr.AiJBP.SYNC;

import com.aijygr.Main;
import com.aijygr.ModMessages;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGClientTagHash {
    private final String str;
    public MSGClientTagHash(String str) { this.str = str; }
    public MSGClientTagHash(FriendlyByteBuf buf) {
        this.str = buf.readUtf(SyncTag.PMAXLENGTH);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.str, SyncTag.PMAXLENGTH);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(SyncTag.clienthash.isEmpty()){
                SyncTag.loadLocalCache();
            }
            Main.LOGGER.info("[AiJBR][MSGClientTagHash]: Server tag hash received = "+ str);
            Main.LOGGER.info("[AiJBR][MSGClientTagHash]: Client Hash = "+ SyncTag.clienthash);
            if(SyncTag.clienthash.equals(this.str)){
                Main.LOGGER.info("[AiJBR][MSGClientTagHash]: EQUAL.");
                ModMessages.PlayerSendToServer(new MSGServerRequestSyncTagJSON("="));
            }
            else{
                Main.LOGGER.info("[AiJBR][MSGClientTagHash]: DIFFERENT.");
                ModMessages.PlayerSendToServer(new MSGServerRequestSyncTagJSON("!"));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}