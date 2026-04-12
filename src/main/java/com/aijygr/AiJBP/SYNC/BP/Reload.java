package com.aijygr.AiJBP.SYNC.BP;

import com.aijygr.AiJBP.Backpack;
import com.aijygr.AiJGame.Game;
import com.aijygr.Main;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class Reload {
    public static void ReloadBP()
    {
        JsonArray json = SyncBP.json;
        try{
            int i = 0;
            for (JsonElement element : json) {
                int j = 0;
                String tag = "";
                short slot = -1;
                short plvl = -1;
                for(JsonElement e : element.getAsJsonArray()){
                    switch(j){
                        case 0:
                            slot = e.getAsShort();
                            break;
                        case 1:
                            plvl = e.getAsShort();
                            break;
                        case 2:
                            tag = e.getAsString();
                    }
                    j++;
                }
                Backpack.backpack.add(new Backpack.BackpackSlotAttribute(slot,plvl,tag));
                i++;
            }
            Player player = Minecraft.getInstance().player;
            Game.tryPlayerMessage(player,"msg.aijbr.green","msg.client"," Read "+i+" values");
        }catch(Exception e){
            Main.LOGGER.warn("[AiJBR] Backpack JSON ERR:",e.getMessage());
        }
    }

}
