package com.aijygr;

import java.util.ArrayList;
import java.util.List;


import com.aijygr.Events.Game.Ring.RingGeneration;
import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    //public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec.DoubleValue CFGDOUBLE;    //获取值
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> SERVERCFG_BACKPACK_SLOTS;
    public static ForgeConfigSpec.ConfigValue<List<? extends Integer>> SERVERCFG_RING_INITIAL_ATTRUBUTES;
    public static ForgeConfigSpec.ConfigValue<Integer> SERVERCFG_DAMAGE_TICKING_TIME;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> SERVERCFG_RING_PHASE_ATTRIBUTES;

    public static final ArrayList<Integer> DEFALUTCFG_RING_INITIAL_ATTRUBUTES =  new ArrayList<>(List.of(512,300));
    public static final ArrayList<String> DEFAUTLCFG_BACKPACK_SLOTS = new ArrayList<String>(List.of(
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
    public static final ArrayList<String> DEFAULTCFG_RING_ATTRIBUTES = new ArrayList<>(List.of(
            "256,200,0.2,0.005,200,0.2,0.005",
            "128,200,0.5,0.005,200,0.5,0.005",
            "64,200,0.5,0.01 ,100,1  ,0.01",
            "32 ,100,1  ,0.01 ,100,1.5,0.01",
            "16 ,100,2  ,0.05 ,100,2.5  ,0.05",
            "0.1,100,3  ,0.1  ,500,3    ,0.1"
    ));
    public static final int DEFAULTCFG_DAMAGE_TICKING_TIME = 30;
    public static final ArrayList<RingGeneration.GenerationMode> DEFAULTCFG_GENERATIONMODES = new ArrayList<>(List.of(
            RingGeneration.GenerationMode.RANDOM,
            RingGeneration.GenerationMode.EDGE_WEIGHTED,
            RingGeneration.GenerationMode.UNIFORM,
            RingGeneration.GenerationMode.MID_WEIGHTED,
            RingGeneration.GenerationMode.RANDOM,
            RingGeneration.GenerationMode.UNIFORM
            ));


    static {
        //ForgeConfigSpec.Builder common_builder = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder server_builder = new ForgeConfigSpec.Builder();//栈结构 builder

        server_builder.comment("AiJBR Server Settings").push("AiJBR_SV");

        server_builder.push("Backpack");
        server_builder.comment(
                "# BackPack Slots Attributes",
                "- For Each String, the format is: \"Slot,PermissionLevel,Type\"",
                "- Example:  \"0,0,+mainwpn\"",
                "- It represents that Slot 0 has PermissionLevel of 0, its type is \"+mainwpn\"."
        );
        SERVERCFG_BACKPACK_SLOTS = server_builder.defineList("BackPackSlotsAttributes", DEFAUTLCFG_BACKPACK_SLOTS, (obj)->{return obj instanceof String;});
        server_builder.pop();

        server_builder.push("Ring");
        server_builder.comment(
                "# Ring Damage Ticking Time",
                "- Interval ticks between between 2 ring damages."
        );
        SERVERCFG_DAMAGE_TICKING_TIME = server_builder.defineInRange("DamageTickingTime", DEFAULTCFG_DAMAGE_TICKING_TIME,1,Integer.MAX_VALUE);
        server_builder.comment(
                "# Ring Initial Setting",
                "- Format: InitialRingSize, WaitingTick",
                "- Default: 1024, 300   Must be Integer."
        );
        SERVERCFG_RING_INITIAL_ATTRUBUTES = server_builder.defineList("RingInitialAttributes",DEFALUTCFG_RING_INITIAL_ATTRUBUTES,(obj)->{return obj instanceof Integer;});

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
        SERVERCFG_RING_PHASE_ATTRIBUTES = server_builder.defineList("RingPhaseAttributes", DEFAULTCFG_RING_ATTRIBUTES,(obj)->{return obj instanceof String;});

        CFGDOUBLE = server_builder.comment("comment.cfg.time").translation("translate.cfg.time").defineInRange("t", 200.0d, 0.0d, 20000.0d);



        StringBuilder strmodes = new StringBuilder();
        RingGeneration.GenerationMode[] modes = RingGeneration.GenerationMode.values();
        for(int i = 0; i < modes.length; i++) {
            strmodes.append(modes[i].name());
            strmodes.append(" ");
        }
        server_builder.comment(
                "# Ring Generation Mode",
                "- For each String, it decides the method used in generation of the next ring.",
                "- UNIFORM: Possibilities of every points in the map are equal",
                "- EDGE_WEIGHTED: The next ring has more possibility to be at the edge" ,
                "- MID_WEIGHTED: The next ring has more possibility to be at the middle" ,
                "- TANGENT: The next ring must touch the edge" ,
                "- RANDOM: Randomly choose one of above method to generate");
        server_builder.comment("Allowed Values: "+strmodes.toString());
        server_builder.defineList("GenerationMode",DEFAULTCFG_GENERATIONMODES ,(obj)->{return obj instanceof RingGeneration.WeightedMode;});

        server_builder.comment(
                "# Weighted Algorithm",
                "- Decides the method used to weight the points",
                "- Available when the Generation Mode is weighted (EDGE_WEIGHTED\\MID_WEIGHTED\\RANDOM when selected WEIGHTED) "
        );
        server_builder.defineEnum("WeightedMode", RingGeneration.WeightedMode.MUL2, RingGeneration.WeightedMode.values());
        server_builder.pop();


        server_builder.pop();
        SERVER_CONFIG = server_builder.build();
    }
}
