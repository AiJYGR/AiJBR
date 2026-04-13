package com.aijygr.AiJBP.SyncConfigJSON.BP;

import com.aijygr.AiJBP.AiJBackpack;
import com.aijygr.AiJGame.Game;
import com.aijygr.Main;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;

public class Reload {
    public static void ReloadBP() {
        JsonObject json = SyncBP.json;
        Player player = Minecraft.getInstance().player;
        int i = 0;
        AiJBackpack.slots.clear();
        try {
            for (JsonElement element : json.getAsJsonArray("Inventory")) {
                int j = 0;
                String tag = "";
                short slot = -1;
                short plvl = -1;
                for (JsonElement e : element.getAsJsonArray()) {
                    switch (j) {
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
                AiJBackpack.slots.computeIfAbsent(tag, k -> new ArrayList<>()).add(new AiJBackpack.SlotPermissionLevel(slot,plvl));
                i++;
            }
            Game.tryPlayerMessage(player, "msg.aijbr.green", "msg.client", " Read " + i + " values");
        } catch (Exception e) {
            Game.tryPlayerMessage(player, "msg.aijbr.red", "msg.client", " Read " + i + " values");
            Game.tryPlayerMessage(player,"msg.aijbr.red","msg.client"," ERR:",e.getMessage());
            Main.LOGGER.warn("[AiJBR] AiJBackpack JSON ERR:{}", e.getMessage());
        }
    }

}
