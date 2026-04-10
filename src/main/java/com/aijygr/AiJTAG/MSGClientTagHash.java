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
        this.str = buf.readUtf(Tagger.PMAXLENGTH);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.str, Tagger.PMAXLENGTH);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(Tagger.clienthash.isEmpty()){
                Tagger.loadLocalCache();
            }
            Main.LOGGER.info("[AiJTAG]: Server tag hash received = "+ str);
            Main.LOGGER.info("[AiJTAG]: Client Hash: "+ Tagger.clienthash);
            if(Tagger.clienthash.equals(this.str)){
                Main.LOGGER.info("[AiJTAG]: EQUAL.");
                ModMessages.PlayerSendToServer(new MSGServerRequestSyncTagJSON("="));
            }
            else{
                Main.LOGGER.info("[AiJTAG]: DIFFERENT.");
                ModMessages.PlayerSendToServer(new MSGServerRequestSyncTagJSON("!"));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}