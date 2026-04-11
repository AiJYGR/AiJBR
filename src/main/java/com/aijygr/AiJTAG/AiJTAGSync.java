package com.aijygr.AiJTAG;

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

public class AiJTAGSync {
    public static final String DEFAULTFILE = """
            {
               "tacz:modern_kinetic_gun": {
                 "GunId": {
                   "tacz:ak47": ["MAINWPN"],
                   "tacz:scar_l": ["MAINWPN"],
                   "tacz:ai_awp": ["MAINWPN"],
                   "tacz:p320": ["MAINWPN","SUBWPN"]
                 }
               },
               "tacz:attachment":["SUPPLIES"],
               "tacz:ammo":["SUPPLIES"],
               "aijbr:medkit":["SUPPLIES"],
               "aijbr:syringe":["SUPPLIES"]
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
            Main.LOGGER.warn("[AiJTAG]: ", e);
        }
    }

    public static String HASH(String input) {
        return Hashing.sha256().hashString(CharMatcher.whitespace().removeFrom(input), StandardCharsets.UTF_8).toString();
    }

    private static Path getCachePath() {
        return FMLPaths.CONFIGDIR.get().resolve("AiJTAG.json.server.cache");
    }
    public static void loadLocalCache() {
        Path path = getCachePath();
        if (Files.exists(path)) {
            try {
                String strfilecontent = Files.readString(path, StandardCharsets.UTF_8);
                json = JsonParser.parseString(strfilecontent).getAsJsonObject();
                clienthash = HASH(strfilecontent);
                Main.LOGGER.info("[AiJTAG][Client] Successfully read Hash: {}", clienthash);
            } catch (Exception e) {
                Main.LOGGER.warn("[AiJTAG][Client] Read cache error: {}", e.getMessage());
            }
        }
        else
            Main.LOGGER.info("[AiJTAG][Client] No cache found.");
    }
    public static void saveLocalCache(String rawJson, String hash) {
        try {
            json = JsonParser.parseString(rawJson).getAsJsonObject();
            AiJTAGSync.clienthash = hash;
            Files.writeString(getCachePath(), rawJson, StandardCharsets.UTF_8);
            Main.LOGGER.info("[AiJTAG][Client] Cache saved.");
        } catch (Exception e) {
            Main.LOGGER.error("[AiJTAG][Client] Failed to write cache file.", e);
        }
    }
    public static void reload(MinecraftServer server) throws Exception { //  /AiJBR reload
        // serverconfig/AiJTAG.json
        Path jsonfilepath = server.getWorldPath(new LevelResource("serverconfig")).resolve("AiJTAG.json");
        File file = jsonfilepath.toFile();
        if (!file.exists()) {
            Main.LOGGER.info("[AiJTAG][Server]: JSON not found, try generating default file...");
            generateDefault(file);
        }
        try (FileReader reader = new FileReader(file)) {
            rawjson = Files.readString(jsonfilepath, StandardCharsets.UTF_8);
            json = JsonParser.parseString(rawjson).getAsJsonObject();
            String hash = HASH(rawjson);
            ModMessages.ServerSendToAll(new MSGClientTagHash(hash));//////SYNC HASH!!!!!!!!
            Main.LOGGER.info("[AiJTAG][Server] Serverside hash = {}", hash);

        } catch (Exception e) {
            Main.LOGGER.error("[AiJTAG][Server]: {}", e.getMessage());
            throw new Exception("Failed: "+e.getMessage());
        }
    }
}
