package com.aijygr.AiJBP;

import com.aijygr.AiJBP.SYNC.Tag.SyncTag;
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
    private static List<AiJBackpack.SlotTag> tags = new ArrayList<>();
    private static AiJBackpack.SlotTag getPermativeEnumString(JsonElement element){
        if (element.isJsonPrimitive()) {
            if(element.getAsJsonPrimitive().isString()){
                String str = element.getAsJsonPrimitive().getAsString();
                return AiJBackpack.SlotTag.getEnum(str);
            }
        }
        return null;
    }
    private static List<AiJBackpack.SlotTag> getArrayEnumString(JsonElement element){
        List<AiJBackpack.SlotTag> a = new ArrayList<>();
        AiJBackpack.SlotTag t;
        if(element.isJsonArray()){
            for(JsonElement e : element.getAsJsonArray()){
                t = getPermativeEnumString(e);
                if(t!=null)
                    a.add(t);
                else return null;
            }
            return a;
        }
        t = getPermativeEnumString(element);
        if (t!=null){
            a.add(t);
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
                    tags = getArrayEnumString(e);
                    if(tags != null)
                        return;
                }
                else if (e.isJsonObject()) {
                    if (tag.getTagType(key) == Tag.TAG_STRING) {
                        String subId = tag.getString(key);
                        JsonObject subObj = e.getAsJsonObject();
                        if (subObj.has(subId)) {
                            tags = getArrayEnumString(subObj.get(subId));
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


    public static List<AiJBackpack.SlotTag> GetItemTags(ItemStack itemStack){
        JsonObject json = SyncTag.json;
        tags.clear();
        if(itemStack == null || itemStack.isEmpty())
            return null;
        if(json ==null)
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
                    tags = getArrayEnumString(element);
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
