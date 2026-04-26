package com.aijygr.AiJBP.SyncConfigJSON.Tag;

import com.aijygr.AiJGame.Client.ClientGame;
import com.aijygr.LIB;
import com.aijygr.Main;
import com.aijygr.ModMessages;
import net.minecraft.client.Minecraft;
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
            try{
                if(SyncTag.clienthash.isEmpty()){
                    SyncTag.loadLocalCache();
                }
                if(SyncTag.clienthash.equals(this.str)){
                    ModMessages.PlayerSendToServer(new MSGServerRequestSyncTagJSON("="));
                    ClientGame.isTagSynced = true;
                    LIB.tryPlayerMessage(Minecraft.getInstance().player,"msg.aijbr.green","[MSGClient TagHASH] Success.");
                }
                else{
                    ModMessages.PlayerSendToServer(new MSGServerRequestSyncTagJSON("!"));
                }
            } catch (Exception e){
                Main.LOGGER.error("[MSGClientTagHASH]:{}", e.getMessage());
            }
        });
        ctx.get().setPacketHandled(true);
    }
}