package com.aijygr;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    //public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec.DoubleValue CFGDOUBLE;    //获取值
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> SERVER_AIJBR_CFG_ARRAY;

    static {
    
        //ForgeConfigSpec.Builder common_builder = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder server_builder = new ForgeConfigSpec.Builder();//栈结构 builder
        ArrayList<String> DEFAULT_AIJBR_CFG_ARRAY = new ArrayList<String>(List.of(
            "0,0,+mainwpn",
            "1,0,+mainwpn",
            "2,0,+subwpn",
            "3,0,+melee",
            "4,0,+nades",
            "5,0,+supplies",
            "6,0,+supplies",
            "7,1,+supplies",
            "8,1,+supplies"
        ));
        server_builder.comment("AiJBP BackPack Settings").push("AiJBP");
        server_builder.comment("# Slots Attributes",
            "- For Each String, the format is: \"Slot,PermissionLevel,Type\"",
            "- Example:  \"0,0,+mainwpn\"",
            "- It represents that Slot 0 has PermissionLevel of 0, its type is \"+mainwpn\"."
        );
        SERVER_AIJBR_CFG_ARRAY = server_builder.defineList("Slots", DEFAULT_AIJBR_CFG_ARRAY, (obj)->{return obj instanceof String;});
        CFGDOUBLE = server_builder.comment("comment.cfg.time").translation("translate.cfg.time").defineInRange("t", 200.0d, 0.0d, 20000.0d);
        server_builder.pop();
        SERVER_CONFIG = server_builder.build();

    }
}
