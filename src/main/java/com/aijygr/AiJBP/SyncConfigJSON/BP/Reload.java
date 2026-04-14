package com.aijygr.AiJBP.SyncConfigJSON.BP;

import com.aijygr.AiJBP.AiJBackpack;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class Reload {
    public static int ReloadBP() throws Exception {
        JsonObject json = SyncBP.json;
        int i = 0;
        AiJBackpack.slots.clear();

        try {
            JsonArray array = json.getAsJsonArray("Inventory");
            if(array==null){
                throw new Exception("Found no key called \"Inventory\".");
            }
            for (JsonElement element : array) {
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
            return i;
        } catch (Exception e) {
            String str = ("at value "+i+": "+e.getMessage());
            throw new Exception(str+e.getMessage());
        }
    }

}
