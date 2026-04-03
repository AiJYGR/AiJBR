package com.aijygr;

import java.util.ArrayList;
import java.util.List;


import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    //public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec.DoubleValue CFGDOUBLE;    //获取值
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> SERVERCFG_BACKPACK_SLOTS;
    public static ForgeConfigSpec.ConfigValue<String> SERVERCFG_RING_INITIAL_ATTRUBUTES;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> SERVERCFG_RING_PHASE_ATTRIBUTES;
    static {
    
        //ForgeConfigSpec.Builder common_builder = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder server_builder = new ForgeConfigSpec.Builder();//栈结构 builder
        ArrayList<String> DEFAULT_CONFIG_BACKPACK_SLOTS = new ArrayList<String>(List.of(
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
        ArrayList<String> DEFAULT_CONFIG_RING_ATTRIBUTES = new ArrayList<>(List.of(
                "512,200,0.2,0.005,200,0.2,0.005",
                "256,200,0.5,0.005,200,0.5,0.005",
                "128,200,0.5,0.01 ,100,1  ,0.01",
                "64 ,100,1  ,0.01 ,100,1.5,0.01",
                "32 ,100,2  ,0.05 ,100,2.5  ,0.05",
                "0.1,100,3  ,0.1  ,inf,3    ,0.1"
        ));


        server_builder.comment("AiJBR Server Settings").push("AiJBR_SV");

        server_builder.push("Backpack");
        server_builder.comment(
                "# BackPack Slots Attributes",
                "- For Each String, the format is: \"Slot,PermissionLevel,Type\"",
                "- Example:  \"0,0,+mainwpn\"",
                "- It represents that Slot 0 has PermissionLevel of 0, its type is \"+mainwpn\"."
        );
        SERVERCFG_BACKPACK_SLOTS = server_builder.defineList("BackPackSlotsAttributes", DEFAULT_CONFIG_BACKPACK_SLOTS, (obj)->{return obj instanceof String;});
        server_builder.pop();

        server_builder.push("RingPhase");
        server_builder.comment(
                "# Ring Initial Setting",
                "-Format: \"InitialRingSize,WaitingTick\"",
                "- Example: \"1024, 300\""
        );

        SERVERCFG_RING_INITIAL_ATTRUBUTES = server_builder.define("RingInitialAttrubutes","1024,300");

        server_builder.comment(
                "# Rings Phase Attributes",
                "- For Each String, the format is: \"RingSize,WaitingTick,BasicDamage,DamagePerBlock,ClosingTick,BasicDamage,DamagePerBlock \"",
                "- Example:  \"512,200,0.2,0.005,200,0.2,0.005\"",
                "- RingSize: The radius of a ring at the Phase End.",
                "- WaitingTick: Ticks waited for each phase before the ring starts to close.",
                "- BasicDamage: The basic damage player would take for being outside the ring.",
                "- DamagePerBlock: Penalties for being very far from the ring.",
                "- ClosingTick: Ticks for the ring to close."
        );
        SERVERCFG_RING_PHASE_ATTRIBUTES = server_builder.defineList("RingPhaseAttributes",DEFAULT_CONFIG_RING_ATTRIBUTES,(obj)->{return obj instanceof String;});

        CFGDOUBLE = server_builder.comment("comment.cfg.time").translation("translate.cfg.time").defineInRange("t", 200.0d, 0.0d, 20000.0d);
        server_builder.pop();


        server_builder.pop();
        SERVER_CONFIG = server_builder.build();
    }
}
