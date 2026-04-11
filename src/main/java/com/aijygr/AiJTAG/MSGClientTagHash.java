package com.aijygr.AiJTAG;

import com.aijygr.Main;
import com.aijygr.ModMessages;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGClientTagHash {
    private final String str;
    public MSGClientTagHash(String str) { this.str = str; }
    public MSGClientTagHash(FriendlyByteBuf buf) {
        this.str = buf.readUtf(AiJTAGSync.PMAXLENGTH);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.str, AiJTAGSync.PMAXLENGTH);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(AiJTAGSync.clienthash.isEmpty()){
                AiJTAGSync.loadLocalCache();
            }
            Main.LOGGER.info("[AiJTAG][Client]: Server tag hash received = "+ str);
            Main.LOGGER.info("[AiJTAG][Client]: Client Hash = "+ AiJTAGSync.clienthash);
            if(AiJTAGSync.clienthash.equals(this.str)){
                Main.LOGGER.info("[AiJTAG][Client]: EQUAL.");
                ModMessages.PlayerSendToServer(new MSGServerRequestSyncTagJSON("="));
            }
            else{
                Main.LOGGER.info("[AiJTAG][Client]: DIFFERENT.");
                ModMessages.PlayerSendToServer(new MSGServerRequestSyncTagJSON("!"));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}