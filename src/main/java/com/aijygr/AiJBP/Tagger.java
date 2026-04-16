package com.aijygr.AiJBP;

import com.aijygr.AiJBP.SyncConfigJSON.Tag.SyncTag;
import com.aijygr.Main;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Tagger {
    @Nullable
    private static String getPermativeEnumString(JsonElement element){
        if (element.isJsonPrimitive()) {
            if(element.getAsJsonPrimitive().isString()){
                return element.getAsJsonPrimitive().getAsString();
            }
        }
        return null;
    }
    @NotNull
    private static List<String> getArrayEnumString(JsonElement element){
        List<String> a = new ArrayList<>();
        String t;
        if(element.isJsonArray()){
            for(JsonElement e : element.getAsJsonArray()){
                t = getPermativeEnumString(e);
                if(t!=null)
                    a.add(t);
                else return new ArrayList<>();
            }
            return a;
        }
        t = getPermativeEnumString(element);
        if (t!=null){
            a.add(t);
            return a;
        }
        else return new ArrayList<>();
    }
    @Nullable
    private static List<String> handleObject(JsonObject object, CompoundTag tag,int d) {
        //终止条件：getArrayString 或层数>10
        if (d > 10 || object == null || tag == null)
            return null;
        Set<String> keys = tag.getAllKeys();
        for (String key : keys) {
            if (object.has(key)) {
                JsonElement e = object.get(key);
                if (e.isJsonPrimitive() || e.isJsonArray()) {
                    return getArrayEnumString(e);
                }
                else if (e.isJsonObject())
                {
                    if (tag.getTagType(key) == Tag.TAG_STRING)
                    {
                        String subId = tag.getString(key);
                        JsonObject subObj = e.getAsJsonObject();
                        if (subObj.has(subId)) {
                            return getArrayEnumString(subObj.get(subId));
                        }
                    } else if (tag.getTagType(key) == Tag.TAG_COMPOUND) {
                        return handleObject(e.getAsJsonObject(), tag.getCompound(key), d + 1);
                    }
                }
            }

        }
        return null;
    }
    @Nullable
    public static List<String> GetItemTags(ItemStack itemstack)
    {
        JsonObject json = SyncTag.json;
        if(itemstack == null || itemstack.isEmpty())
            return null;
        if(json ==null)
        {
            Main.LOGGER.info("json is null!");
            return null;
        }
        if(json.isJsonObject()){
            JsonObject mainObj = json.getAsJsonObject();
            String itemid = ForgeRegistries.ITEMS.getKey(itemstack.getItem()).toString();
            List<String> tags = null;
            if(mainObj.has(itemid)){
                JsonElement element = mainObj.get(itemid);
                if(element.isJsonPrimitive()||element.isJsonArray()){
                    tags = getArrayEnumString(element);
                }else if(element.isJsonObject()){
                    tags = handleObject(element.getAsJsonObject(),itemstack.getTag(), 0);
                }
            }
            if(tags!=null)//找到了定义
                return tags;
            else if(mainObj.has("DEFAULT")){
                JsonElement element = mainObj.get("DEFAULT");
                if(element.isJsonPrimitive()||element.isJsonArray()){
                    return getArrayEnumString(element);
                }
            }
            return null;
        }
        return null;
    }
}
