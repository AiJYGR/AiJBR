package com.aijygr.AiJBP.SyncConfigJSON.BP;

import com.aijygr.AiJGame.Game;
import com.aijygr.Main;
import com.aijygr.ModMessages;
import net.minecraft.client.Minecraft;
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
            try{
                if(SyncBP.clienthash.isEmpty()){
                    SyncBP.loadLocalCache();
                }
                if(SyncBP.clienthash.equals(this.str)){
                    ModMessages.PlayerSendToServer(new MSGServerRequestSyncBPJSON("="));
                    Reload.ReloadBP();
                    Game.tryPlayerMessage(Minecraft.getInstance().player,"msg.aijbr.green","[MSGClient BPHASH] Success.");
                }
                else{
                    ModMessages.PlayerSendToServer(new MSGServerRequestSyncBPJSON("!"));
                }
            }catch(Exception e){
                Main.LOGGER.error("[MSGClientBPHASH]:{}", e.getMessage());
            }
        });
        ctx.get().setPacketHandled(true);
    }
}