package com.aijygr.AiJBP;

import com.aijygr.AiJBP.SYNC.SyncTag;
import com.aijygr.Main;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Tagger {
    private static JsonObject json = new JsonObject();
    private static List<String> tags = new ArrayList<>();
    private static boolean isPermativeString(JsonElement element){
        if (element.isJsonPrimitive()) {
            return element.getAsJsonPrimitive().isString();
        }
        return false;
    }
    private static List<String> getArrayString(JsonElement element){ //StringArray or String
        ArrayList<String> a = new ArrayList<>();
        if(element.isJsonArray()){
            for(JsonElement e : element.getAsJsonArray()){
                if(isPermativeString(e))
                    a.add(e.getAsString());
                else return null;
            }
            return a;
        }else if(isPermativeString(element)){
            a.add(element.getAsString());
            return a;
        }
        else return null;
    }

    private static void handleObject(JsonObject object, CompoundTag tag,int d)
    {
        //终止条件：getArrayString 或层数>10
        if (d > 10 || object == null || tag == null)
            return;
        Set<String> keys = tag.getAllKeys();
        for(String key : keys){
            if(object.has(key)){
                JsonElement e = object.get(key);
                if (e.isJsonPrimitive() || e.isJsonArray()) {
                    tags = getArrayString(e);
                    if(tags != null)
                        return;
                }
                else if (e.isJsonObject()) {
                    if (tag.getTagType(key) == Tag.TAG_STRING) {
                        String subId = tag.getString(key);
                        JsonObject subObj = e.getAsJsonObject();
                        if (subObj.has(subId)) {
                            tags = getArrayString(subObj.get(subId));
                            if(tags != null)
                                return;
                        }
                    }
                    else if (tag.getTagType(key) == Tag.TAG_COMPOUND) {
                        handleObject(e.getAsJsonObject(), tag.getCompound(key), d+1);
                    }
                }
            }
        }
    }


    public static List<String> GetItemTag(ItemStack itemStack){
        json = SyncTag.json;
        tags.clear();
        if(itemStack == null || itemStack.isEmpty())
            return null;
        if(json==null)
        {
            Main.LOGGER.info("json is null!");
            return null;
        }
        if(json.isJsonObject()){
            JsonObject mainObj = json.getAsJsonObject();
            String itemid;
            itemid = ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString();
            if(mainObj.has(itemid)){
                JsonElement element = mainObj.get(itemid);
                if(element.isJsonPrimitive()||element.isJsonArray()){
                    tags = getArrayString(element);
                    return tags;
                }else if(element.isJsonObject()){
                    handleObject(element.getAsJsonObject(),itemStack.getTag(), 0);
                }
            }
            return tags;
        }
        else return null;
    }
}
