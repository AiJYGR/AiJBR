package com.aijygr.AiJBP.SYNC;

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


/*
* SyncBP the Json data
* From Server to Client Side
* Firstly Server sends HASH
* Client checks if local cache is differ from it, and sends sync request
* Server receives request, and send full file.
* */
public class SyncBP {
    public static final String DEFAULTFILE = """
{
  "Backpack":[
    [0,0,"MAINWPN"],
    [1,0,"MAINWPN"],
    [2,0,"SUBWPN"],
    [3,0,"MELEE"],
    [4,0,"SUPPLIES"],
    [5,0,"SUPPLIES"],
    [6,0,"SUPPLIES"],
    [7,0,"SUPPLIES"],
    [8,0,"SUPPLIES"]
  ]
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
            String hash = HASH(rawjson);
            ModMessages.ServerSendToAll(new MSGClientBPHash(hash));//////SYNC HASH!!!!!!!!
            Main.LOGGER.info("[AiJBR][SyncBP] Serverside hash = {}", hash);

        } catch (Exception e) {
            Main.LOGGER.error("[AiJBR][SyncBP]: {}", e.getMessage());
            throw new Exception("Failed: "+e.getMessage());
        }
    }
}
