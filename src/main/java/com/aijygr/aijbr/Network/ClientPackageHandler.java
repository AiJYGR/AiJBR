package com.aijygr.aijbr.Network;

import com.aijygr.aijbr.AiJBP.AiJBackpack;
import com.aijygr.aijbr.AiJBP.MSGClientFinished;
import com.aijygr.aijbr.AiJBP.SyncConfigJSON.BP.MSGServerRequestSyncBPJSON;
import com.aijygr.aijbr.AiJBP.SyncConfigJSON.BP.Reload;
import com.aijygr.aijbr.AiJBP.SyncConfigJSON.BP.SyncBP;
import com.aijygr.aijbr.AiJBP.SyncConfigJSON.Tag.MSGClientTagHash;
import com.aijygr.aijbr.AiJBP.SyncConfigJSON.Tag.MSGServerRequestSyncTagJSON;
import com.aijygr.aijbr.AiJBP.SyncConfigJSON.Tag.SyncTag;
import com.aijygr.aijbr.AiJGame.Client.ClientGame;
import com.aijygr.aijbr.LIB;
import com.aijygr.aijbr.Main;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;

import static com.aijygr.aijbr.AiJBP.SyncConfigJSON.Tag.SyncTag.HASH;

public class ClientPackageHandler {//用于抽离逻辑
    public static void MSGClientTagJson(String str){
        SyncTag.json = JsonParser.parseString(str).getAsJsonObject();
        String hash = HASH(str);
        SyncTag.saveLocalCache(str,hash);
        ClientGame.isTagSynced = true;
        LIB.tryPlayerMessage(Minecraft.getInstance().player,"msg.aijbr.green","[MSGClient TagJSON] Success.");
    }
    public static void MSGClientTagHash(String str){
        try{
            if(SyncTag.clienthash.isEmpty()){
                SyncTag.loadLocalCache();
            }
            if(SyncTag.clienthash.equals(str)){
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
    }
    public static void MSGClientBPJson(String str){
        try{
            SyncBP.json = JsonParser.parseString(str).getAsJsonObject();
            String hash = HASH(str);
            SyncBP.saveLocalCache(str,hash);
            Reload.ReloadBP();
            LIB.tryPlayerMessage(Minecraft.getInstance().player,"msg.aijbr.green","[MSGClient BPJSON] Success.");
        }catch(Exception e){
            Main.LOGGER.error("[MSGClientBPJSON]:{}", e.getMessage());
        }
    }
    public static void MSGClientBPHash(String str){
        try{
            if(SyncBP.clienthash.isEmpty()){
                SyncBP.loadLocalCache();
            }
            if(SyncBP.clienthash.equals(str)){
                ModMessages.PlayerSendToServer(new MSGServerRequestSyncBPJSON("="));
                Reload.ReloadBP();
                LIB.tryPlayerMessage(Minecraft.getInstance().player,"msg.aijbr.green","[MSGClient BPHASH] Success.");
            }
            else{
                ModMessages.PlayerSendToServer(new MSGServerRequestSyncBPJSON("!"));
            }
        }catch(Exception e){
            Main.LOGGER.error("[MSGClientBPHASH]:{}", e.getMessage());
        }
    }
    public static void MSGClientFinished(){
        AiJBackpack.setAvailable();
    }
    public static void MSGClientRingInfo(int x, int z, double size, String generationmode){
        ClientGame.setClientRing(x, z, size,generationmode);
    }
    public static void MSGClientPlayerInfo(int players, int teams){
        ClientGame.setClientPlayer(players, teams);
    }
    public static void MSGClientGameTime(int round, int roundtick, boolean isShrinking){
        ClientGame.setClientTime(round,roundtick,isShrinking);
    }
    public static void MSGClientExecSync(){
        AiJBackpack.clientsync();
    }
}
