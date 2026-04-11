package com.aijygr;

import com.aijygr.AiJGame.Ring.RingGeneration;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;
public abstract class ModConfig {
    //public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec SERVER_CONFIG;

    public static class Server {
        public static class Config {
            public static class BACKPACK {
                //public static ForgeConfigSpec.ConfigValue<String> BACKPACK_SLOT_DEFAULT;
                //public static ForgeConfigSpec.ConfigValue<String> ENABLEAIJBR;
                public static ForgeConfigSpec.ConfigValue<List<? extends String>> BACKPACK_SLOTS;
            }

            public static class RING{
                public static ForgeConfigSpec.EnumValue<RingGeneration.WeightedMode> WEIGHTEDMODE;
                public static ForgeConfigSpec.ConfigValue<List<? extends Integer>> RING_INITIAL_ATTRUBUTES;
                public static ForgeConfigSpec.ConfigValue<Integer> DAMAGE_TICKING_TIME;
                public static ForgeConfigSpec.ConfigValue<List<? extends String>> RING_ATTRIBUTES;
                public static ForgeConfigSpec.ConfigValue<List<? extends String>> GENERATIONMODES;
            }

            public static class ITEM{
                public static ForgeConfigSpec.IntValue BACKPACK_LVL1_PERMISSIONLEVEL;
                public static ForgeConfigSpec.ConfigValue<Integer> SYRINGE_USEDURATION;
                public static ForgeConfigSpec.ConfigValue<Integer> SYRINGE_MAXSTACKSIZE;
                public static ForgeConfigSpec.DoubleValue SYRINGE_HEALAMOUNT;
                public static ForgeConfigSpec.ConfigValue<Integer> MEDKIT_USEDURATION;
                public static ForgeConfigSpec.ConfigValue<Integer> MEDKIT_MAXSTACKSIZE;
                public static ForgeConfigSpec.DoubleValue MEDKIT_HEALAMOUNT;
            }
        }

        public static final class Default {
            /*
            public static class BACKPACK{
                public static final String BACKPACK_SLOT_DEFAULT = "DEFAULT";
                public static final String ENABLE_AIJBR = "INGAME";
                public static final List<String> BACKPACK_SLOTS = new ArrayList<>(List.of(
                        "0,0,mainwpn",
                        "1,0,mainwpn",
                        "2,0,subwpn",
                        "3,0,melee",
                        "4,0,nades",
                        "5,0,supplies",
                        "6,0,supplies",
                        "7,1,supplies",
                        "8,1,supplies"
                ));
            }*/
            public static class RING{
                public static final List<Integer> RING_INITIAL_ATTRUBUTES =  new ArrayList<>(List.of(512,300));
                public static final List<String> RING_ATTRIBUTES = new ArrayList<>(List.of(
                        "256,200,0.2,0.005,200,0.2,0.005",
                        "128,200,0.5,0.005,200,0.5,0.005",
                        "64,200,0.5,0.01 ,100,1  ,0.01",
                        "32 ,100,1  ,0.01 ,100,1.5,0.01",
                        "16 ,100,2  ,0.05 ,100,2.5  ,0.05",
                        "0.1,100,3  ,0.1  ,500,3    ,0.1"
                ));
                public static final int DAMAGE_TICKING_TIME = 10;
                public static final List<String> GENERATIONMODES = new ArrayList<>(List.of(
                        RingGeneration.GenerationMode.EDGE_WEIGHTED.name(),
                        RingGeneration.GenerationMode.UNIFORM.name(),
                        RingGeneration.GenerationMode.TANGENT.name(),
                        RingGeneration.GenerationMode.RANDOM.name(),
                        RingGeneration.GenerationMode.RANDOM.name(),
                        RingGeneration.GenerationMode.UNIFORM.name()
                ));
                public static final RingGeneration.WeightedMode WEIGHTEDMODE = RingGeneration.WeightedMode.MUL2;
            }
            public static class ITEM{
                public static final int ITEM_SYRINGE_USEDURATION = 50;
                public static final int ITEM_SYRINGE_MAXSTACKSIZE = 5;
                public static final double ITEM_SYRINGE_HEALAMOUNT = 5.0f;
                public static final int ITEM_MEDKIT_USEDURATION = 120;
                public static final int ITEM_MEDKIT_MAXSTACKSIZE = 2;
                public static final double ITEM_MEDKIT_HEALAMOUNT = 10.0f;
                public static final short BACKPACK_LVL1_PERMISSIONLEVEL = 10;
            }

        }
    }
    public static ForgeConfigSpec.DoubleValue CFGDOUBLE;    //获取值

    static {
        //ForgeConfigSpec.Builder common_builder = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder server_builder = new ForgeConfigSpec.Builder();//栈结构 builder
        StringBuilder strbuilder = new StringBuilder();

        server_builder.comment("AiJBR Server Settings").push("AiJBR_SV");//File Start
        /*
        server_builder.push("Backpack");    //Backpack
        strbuilder.delete(0, strbuilder.length());
        for (Backpack.SlotTag it : Backpack.SlotTag.values()) {
            strbuilder.append(it.name());
            strbuilder.append(" ");
        }
        server_builder.comment(
                "# BackPack Slots Attributes",
                "- For Each String, the format is: \"Slot,PermissionLevel,Type\"",
                "- Example:  \"0,0,mainwpn\"",
                "- It represents that Slot 0 has PermissionLevel of 0, its type is \"mainwpn\".",
                "Allowed Values: "+strbuilder.toString()
        );
        Server.Config.BACKPACK.BACKPACK_SLOTS = server_builder.defineList("BackPackSlotsAttributes", Server.Default.BACKPACK.BACKPACK_SLOTS, (obj)->{
            if (!(obj instanceof String)) {
                System.out.println("[AiJBR] BackpackSlotAttributes Config ERR: "+obj.toString()+" is not a String");
                return false;
            }
            for(Backpack.SlotTag it : Backpack.SlotTag.values())
                if (obj.equals(it.name()))
                    return true;
            System.out.println("[AiJBR] BackpackSlotAttributes Config ERR: Incorrect value \""+obj+"\"");
            return false;
        });
        server_builder.comment("#Default Attributes",
                "- Define the Default Attributes of undefined slots above.");
        Server.Config.BACKPACK.BACKPACK_SLOT_DEFAULT = server_builder.define("Default", Server.Default.BACKPACK.BACKPACK_SLOT_DEFAULT,(obj)->{return obj instanceof String;});
        server_builder.comment("#Enable AiJBP",
                "When to enable AiJYGR's Backpack");
        Server.Config.BACKPACK.ENABLEAIJBR = server_builder.define("EnableAiJBP", Server.Default.BACKPACK.ENABLE_AIJBR,(obj)->{return obj instanceof String;});
        server_builder.pop();
        */
        server_builder.push("Ring");    //Ring
        server_builder.comment(
                "# Ring Initial Setting",
                "- Format: InitialRingSize, WaitingTick",
                "- Default: 512, 300   Must be Integer."
        );
        Server.Config.RING.RING_INITIAL_ATTRUBUTES = server_builder.defineList("RingInitialAttributes", Server.Default.RING.RING_INITIAL_ATTRUBUTES,(obj)->{return obj instanceof Integer;});

        server_builder.comment(
                "# Rings Phase Attributes",
                "- For Each String, the format is: \"RingSize,WaitingTick,BasicDamage,DamagePerBlock,ClosingTick,BasicDamage,DamagePerBlock \"",
                "- Example:  \"512,200,0.2,0.005,200,0.2,0.005\"",
                "- RingSize: The radius of a ring at the Phase End.",
                "- WaitingTick: Ticks waited for each phase before the ring starts to close.",
                "- BasicDamage: The basic damage player would take for being outside the ring.",
                "- DamagePerBlock: Penalties for being very far from the ring.",
                "- ClosingTick: Ticks for ring to close of this round.",
                "# The Total Rounds is defined here, by the number of inputs."
        );
        Server.Config.RING.RING_ATTRIBUTES =  server_builder.defineList("RingAttributes", Server.Default.RING.RING_ATTRIBUTES,(obj)->{return obj instanceof String;});

        server_builder.comment(
                "# Ring Damage Ticking Time",
                "- Interval ticks between two ring damages."
        );
        Server.Config.RING.DAMAGE_TICKING_TIME = server_builder.defineInRange("DamageTickingTime", Server.Default.RING.DAMAGE_TICKING_TIME,1,Short.MAX_VALUE);

        server_builder.comment(
                "# Ring Generation Mode",
                "- For each String, it decides the algorithm used in generation of the ring of this round.",
                "- UNIFORM: Possibilities of every points in the map are equal",
                "- EDGE_WEIGHTED: The next ring has more possibility to be at the edge" ,
                "- MID_WEIGHTED: The next ring has more possibility to be at the middle" ,
                "- TANGENT: The next ring must touch the edge. Possibilities of every points on the edge are equal" ,
                "- RANDOM: Randomly choose one of above methods to generate");
        strbuilder.delete(0, strbuilder.length());
        RingGeneration.GenerationMode[] generationModes = RingGeneration.GenerationMode.values();
        for (RingGeneration.GenerationMode it : generationModes) {
            strbuilder.append(it.name());
            strbuilder.append(" ");
        }
        server_builder.comment(("Allowed Values: "+strbuilder));
        Server.Config.RING.GENERATIONMODES =  server_builder.defineList("GenerationModes", Server.Default.RING.GENERATIONMODES,(obj)->{
            if (!(obj instanceof String)) {
                System.out.println("[AiJBR] GenerationModes Config ERR: "+obj.toString()+" is not a String");
                return false;
            }
            for (RingGeneration.GenerationMode it: generationModes)
                if (obj.equals(it.name()))
                    return true;
            System.out.println("[AiJBR] GenerationModes Config ERR: Incorrect value \""+obj+"\"");
            return false;
        });
        //ServerConfig.GENERATIONMODES =  server_builder.defineList("GenerationModes", ServerConfig.Default.GENERATIONMODES,(obj)->{return obj instanceof String;});

        server_builder.comment(
                "# Weighted Algorithm",
                "- Decides the method used to weight the points",
                "- Available when the Generation Mode is weighted (EDGE_WEIGHTED/MID_WEIGHTED) "
        );
        Server.Config.RING.WEIGHTEDMODE = server_builder.defineEnum("WeightedMode", Server.Default.RING.WEIGHTEDMODE, RingGeneration.WeightedMode.values());
        server_builder.pop();

        server_builder.push("Items");   //ITEMS
        server_builder.comment(
                "UseDuration: Define ticks cost when using an item." ,
                "MaxStackSize: The stack size of a slot of this item",
                "HealAmount: Define Health gained after finishing using it.");
        server_builder.push("MEDS");
        server_builder.push("SYRINGE");
        Server.Config.ITEM.SYRINGE_USEDURATION = server_builder.defineInRange("UseDuration", Server.Default.ITEM.ITEM_SYRINGE_USEDURATION,1,6000);
        Server.Config.ITEM.SYRINGE_MAXSTACKSIZE = server_builder.defineInRange("MaxStackSize", Server.Default.ITEM.ITEM_SYRINGE_MAXSTACKSIZE,1,6000);
        Server.Config.ITEM.SYRINGE_HEALAMOUNT = server_builder.defineInRange("HealAmount", Server.Default.ITEM.ITEM_SYRINGE_HEALAMOUNT, 0.0f, 10000.0f);
        server_builder.pop();
        server_builder.push("MEDKIT");
        Server.Config.ITEM.MEDKIT_USEDURATION = server_builder.defineInRange("UseDuration", Server.Default.ITEM.ITEM_MEDKIT_USEDURATION,1,6000);
        Server.Config.ITEM.MEDKIT_MAXSTACKSIZE = server_builder.defineInRange("MaxStackSize", Server.Default.ITEM.ITEM_MEDKIT_MAXSTACKSIZE,1,6000);
        Server.Config.ITEM.MEDKIT_HEALAMOUNT = server_builder.defineInRange("HealAmount", Server.Default.ITEM.ITEM_MEDKIT_HEALAMOUNT,0.0f,10000.0f);
        server_builder.pop();
        server_builder.pop();

        server_builder.comment(
                "PermissionLevel: Used with BackpackSlotAttributes together.");
        server_builder.push("BACKPACK");
        Server.Config.ITEM.BACKPACK_LVL1_PERMISSIONLEVEL = server_builder.defineInRange("LVL1Permission",Server.Default.ITEM.BACKPACK_LVL1_PERMISSIONLEVEL,0,Short.MAX_VALUE);
        server_builder.pop();
        server_builder.push("ARMOR");
        server_builder.comment("");

        CFGDOUBLE = server_builder.comment("comment.cfg.time").translation("translate.cfg.time").defineInRange("t", 200.0d, 0.0d, 20000.0d);

        server_builder.pop();
        SERVER_CONFIG = server_builder.build();
    }
}
