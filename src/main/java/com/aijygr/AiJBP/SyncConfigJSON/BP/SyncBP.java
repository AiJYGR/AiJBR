package com.aijygr.AiJBP.SyncConfigJSON.BP;

import com.aijygr.Main;
import com.aijygr.ModMessages;
import com.google.common.base.CharMatcher;
import com.google.common.hash.Hashing;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SyncBP {
    public static final String DEFAULTFILE = """
{
  "Comment":"0~8快捷栏  9~35 背包  36鞋子 37护腿 38护甲 39头盔 40副手",
  "Inventory":[
    [0,0,"MAINWPN"],
    [1,0,"MAINWPN"],
    [2,0,"SUBWPN"],
    [3,0,"MELEE"],
    [4,0,"SUPPLIES"],
    [5,0,"SUPPLIES"],
    [6,0,"SUPPLIES"],
    [7,0,"SUPPLIES"],
    [8,0,"SUPPLIES"],
    [9,0,"BACKPACK"],
    [10,1,"BACKPACK"],
    [11,2,"SUPPLIES"],
    [12,3,"SUPPLIES"],
    [13,4,"SUPPLIES"],
    [14,5,"SUPPLIES"],
    [15,6,"SUPPLIES"],
    [16,7,"SUPPLIES"],
    [17,8,"SUPPLIES"],
    [18,0,"ARMOR"],
    [19,9,"SUPPLIES"],
    [20,10,"SUPPLIES"],
    [21,11,"SUPPLIES"],
    [22,12,"SUPPLIES"],
    [23,13,"SUPPLIES"],
    [24,14,"SUPPLIES"],
    [25,15,"SUPPLIES"],
    [26,16,"SUPPLIES"],
    [27,0,"ARMOR"],
    [28,17,"SUPPLIES"],
    [29,18,"SUPPLIES"],
    [30,19,"SUPPLIES"],
    [31,20,"SUPPLIES"],
    [32,21,"SUPPLIES"],
    [33,22,"SUPPLIES"],
    [34,23,"SUPPLIES"],
    [35,24,"SUPPLIES"],
    [36,0,"DISABLE"],
    [37,0,"DISABLE"],
    [38,0,"ARMOR"],
    [39,0,"DISABLE"],
    [40,0,"DISABLE"]
  ],
  "Default":[32767,"DISABLE"]
}
        """;
    public static final int PMAXLENGTH = 262144; //!!!!!文件最大长度 256KB
    public static JsonObject json = new JsonObject();
    public static String rawjson = "";
    public static String clienthash = "";
    private static void generateDefault(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(DEFAULTFILE);
        } catch (IOException e) {
            Main.LOGGER.warn("[AiJBR][AiJBP]: ", e);
        }
    }

    public static String HASH(String input) {
        return Hashing.sha256().hashString(CharMatcher.whitespace().removeFrom(input), StandardCharsets.UTF_8).toString();
    }

    private static Path getCachePath() {
        return FMLPaths.CONFIGDIR.get().resolve("AiJBP.json.server.cache");
    }
    public static void loadLocalCache() {
        Path path = getCachePath();
        if (Files.exists(path)) {
            try {
                String strfilecontent = Files.readString(path, StandardCharsets.UTF_8);
                json = JsonParser.parseString(strfilecontent).getAsJsonObject();
                clienthash = HASH(strfilecontent);
                Main.LOGGER.info("[AiJBR][SyncBP] Successfully read Hash: {}", clienthash);
            } catch (Exception e) {
                Main.LOGGER.warn("[AiJBR][SyncBP] Read cache error: {}", e.getMessage());
            }
        }
        else
            Main.LOGGER.info("[AiJBR][SyncBP] No cache found.");
    }
    public static void saveLocalCache(String rawJson, String hash) {
        try {
            json = JsonParser.parseString(rawJson).getAsJsonObject();
            SyncBP.clienthash = hash;
            Files.writeString(getCachePath(), rawJson, StandardCharsets.UTF_8);
            Main.LOGGER.info("[AiJBR][SyncBP] Cache saved.");
        } catch (Exception e) {
            Main.LOGGER.error("[AiJBR][SyncBP] Failed to write cache file.", e);
        }
    }
    public static void reload(MinecraftServer server) throws Exception {
        // serverconfig/AiJBP.json
        Path jsonfilepath = server.getWorldPath(new LevelResource("serverconfig")).resolve("AiJBP.json");
        File file = jsonfilepath.toFile();
        if (!file.exists()) {
            Main.LOGGER.info("[AiJBR][SyncBP]: JSON not found, try generating default file...");
            generateDefault(file);
        }
        try (FileReader reader = new FileReader(file)) {
            rawjson = Files.readString(jsonfilepath, StandardCharsets.UTF_8);
            json = JsonParser.parseString(rawjson).getAsJsonObject();
            int i = Reload.ReloadBP();//服务器预检查
            String hash = HASH(rawjson);
            ModMessages.ServerSendToAll(new MSGClientBPHash(hash));//////SYNC HASH!!!!!!!!
            Main.LOGGER.info("[AiJBR][SyncBP]Read {} values. Serverside hash = {}",i , hash);

        } catch (Exception e) {
            throw new Exception("AiJBP.reload: "+e.getMessage());
        }
    }
}
