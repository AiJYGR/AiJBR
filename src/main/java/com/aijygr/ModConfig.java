package com.aijygr;

import com.aijygr.AiJGame.Ring.RingGeneration;
import net.minecraft.world.scores.Team;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;
public abstract class ModConfig {
    //public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec SERVER_CONFIG;

    public enum BOOL {
        TRUE(true),
        FALSE(false);

        private final boolean value;
        BOOL(boolean value) { this.value = value; }
        public boolean get() { return value; }
    }

    public static class Server {
        public static class Config {
            public static class BACKPACK {
                public static ForgeConfigSpec.IntValue DEFAULT_PERMISSIONLEVEL;
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
                public static ForgeConfigSpec.IntValue BACKPACK_LVL2_PERMISSIONLEVEL;
                public static ForgeConfigSpec.IntValue BACKPACK_LVL3_PERMISSIONLEVEL;
                public static ForgeConfigSpec.IntValue BACKPACK_LVL4_PERMISSIONLEVEL;
                public static ForgeConfigSpec.ConfigValue<Integer> SYRINGE_USEDURATION;
                public static ForgeConfigSpec.ConfigValue<Integer> SYRINGE_MAXSTACKSIZE;
                public static ForgeConfigSpec.DoubleValue SYRINGE_HEALAMOUNT;
                public static ForgeConfigSpec.ConfigValue<Integer> MEDKIT_USEDURATION;
                public static ForgeConfigSpec.ConfigValue<Integer> MEDKIT_MAXSTACKSIZE;
                public static ForgeConfigSpec.DoubleValue MEDKIT_HEALAMOUNT;
            }
            public static class DROPSHIP{
                public static ForgeConfigSpec.DoubleValue SPEED;
                public static ForgeConfigSpec.IntValue HEIGHT;
            }
            public static class TEAM {
                public static ForgeConfigSpec.IntValue TEAMNUM;
                public static ForgeConfigSpec.IntValue TEAMSIZE;
                public static ForgeConfigSpec.EnumValue<Team.CollisionRule> COLLISION_RULE;
                public static ForgeConfigSpec.EnumValue<Team.Visibility> DEATHMSG_VISIBILITY;
                public static ForgeConfigSpec.EnumValue<Team.Visibility> NAMETAG_VISIBILITY;
                public static ForgeConfigSpec.EnumValue<BOOL> ALLOWFRIENDLYFIRE;
                public static ForgeConfigSpec.EnumValue<BOOL> SEEFRIENTLYINVISIBLES;
            }
        }

        public static final class Default {
            public static class BACKPACK{
                public static final int DEFAULT_PERMISSIONLEVEL = 0;
            }
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
                public static final int DAMAGE_TICKING_TIME = 30;
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
                public static final short BACKPACK_LVL1_PERMISSIONLEVEL = 6;
                public static final short BACKPACK_LVL2_PERMISSIONLEVEL = 12;
                public static final short BACKPACK_LVL3_PERMISSIONLEVEL = 18;
                public static final short BACKPACK_LVL4_PERMISSIONLEVEL = 35;
            }
            public static final class DROPSHIP{
                public static final double SPEED = 0.1d;
                public static final short HEIGHT = 200;
            }
            public static class TEAM {
                public static int TEAMNUM = 20;
                public static int TEAMSIZE = 1;
                public static Team.CollisionRule COLLISION_RULE = Team.CollisionRule.ALWAYS;
                public static Team.Visibility DEATHMSG_VISIBILITY = Team.Visibility.ALWAYS;
                public static Team.Visibility NAMETAG_VISIBILITY = Team.Visibility.HIDE_FOR_OTHER_TEAMS;
                public static BOOL ALLOWFRIENDLYFIRE =  BOOL.TRUE;
                public static BOOL SEEFRIENTLYINVISIBLES = BOOL.TRUE;
            }
        }
    }

    static {
        //ForgeConfigSpec.Builder common_builder = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder server_builder = new ForgeConfigSpec.Builder();//栈结构 builder
        StringBuilder strbuilder = new StringBuilder();


        server_builder.comment("AiJYGR Backpack Management System");
        server_builder.push("AiJBackpack");    //AiJBackpack
        server_builder.comment(
                "# Default Permission Level",
                "- Define the permission level that the player has without a backpack.");
        Server.Config.BACKPACK.DEFAULT_PERMISSIONLEVEL = server_builder.defineInRange("DefaultPermissionLevel",Server.Default.BACKPACK.DEFAULT_PERMISSIONLEVEL,Short.MIN_VALUE,Short.MAX_VALUE);
        server_builder.pop();

        server_builder.comment("Ring Settings used in the game");
        server_builder.push("Ring");    //Ring
        server_builder.comment(
                "# Ring Initial Attributes",
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
        server_builder.comment(
                "# Weighted Algorithm",
                "- Decides the method used to weight the points",
                "- Available when the Generation Mode is weighted (EDGE_WEIGHTED/MID_WEIGHTED) "
        );
        Server.Config.RING.WEIGHTEDMODE = server_builder.defineEnum("WeightedMode", Server.Default.RING.WEIGHTEDMODE, RingGeneration.WeightedMode.values());
        server_builder.pop();

        server_builder.comment("Dropship Settings used in the game");
        server_builder.push("Dropship");
        server_builder.comment("# SPEED",
                "- Unit: Blocks per tick (Meters per tick)");
        Server.Config.DROPSHIP.SPEED = server_builder.defineInRange("Speed", Server.Default.DROPSHIP.SPEED,0.01,10.0);
        server_builder.comment("# Height",
                "Altitude that the Dropship travels on.");
        Server.Config.DROPSHIP.HEIGHT = server_builder.defineInRange("Height", Server.Default.DROPSHIP.HEIGHT,-60,500);
        server_builder.pop();

        server_builder.comment("AiJBR Mod Items Config");
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
        server_builder.comment("PermissionLevel: Used with BackpackSlotAttributes together.");
        server_builder.push("BACKPACK");
        Server.Config.ITEM.BACKPACK_LVL1_PERMISSIONLEVEL = server_builder.defineInRange("LVL1Permission",Server.Default.ITEM.BACKPACK_LVL1_PERMISSIONLEVEL,0,Short.MAX_VALUE);
        Server.Config.ITEM.BACKPACK_LVL2_PERMISSIONLEVEL = server_builder.defineInRange("LVL2Permission",Server.Default.ITEM.BACKPACK_LVL2_PERMISSIONLEVEL,0,Short.MAX_VALUE);
        Server.Config.ITEM.BACKPACK_LVL3_PERMISSIONLEVEL = server_builder.defineInRange("LVL3Permission",Server.Default.ITEM.BACKPACK_LVL3_PERMISSIONLEVEL,0,Short.MAX_VALUE);
        Server.Config.ITEM.BACKPACK_LVL4_PERMISSIONLEVEL = server_builder.defineInRange("LVL4Permission",Server.Default.ITEM.BACKPACK_LVL4_PERMISSIONLEVEL,0,Short.MAX_VALUE);

        server_builder.pop();
        server_builder.push("ARMOR");
        //server_builder.comment("");
        //CFGDOUBLE = server_builder.comment("comment.cfg.time").translation("translate.cfg.time").defineInRange("t", 200.0d, 0.0d, 20000.0d);
        server_builder.pop();


        server_builder.pop();

        server_builder.comment("Teams & Players Attributes");
        server_builder.push("Team");
        Server.Config.TEAM.TEAMNUM = server_builder.defineInRange("TeamNumber", Server.Default.TEAM.TEAMNUM,2,30);
        Server.Config.TEAM.TEAMSIZE = server_builder.defineInRange( "TeamSize", Server.Default.TEAM.TEAMSIZE,1,30);
        Server.Config.TEAM.DEATHMSG_VISIBILITY = server_builder.defineEnum("DeathMessageVisibility",Server.Default.TEAM.DEATHMSG_VISIBILITY);
        Server.Config.TEAM.NAMETAG_VISIBILITY = server_builder.defineEnum("NameTagVisibility",Server.Default.TEAM.NAMETAG_VISIBILITY);
        Server.Config.TEAM.COLLISION_RULE = server_builder.defineEnum("Collision_Rule",Server.Default.TEAM.COLLISION_RULE);
        Server.Config.TEAM.ALLOWFRIENDLYFIRE = server_builder.defineEnum("FriendlyFire",Server.Default.TEAM.ALLOWFRIENDLYFIRE);
        Server.Config.TEAM.SEEFRIENTLYINVISIBLES = server_builder.defineEnum("SeeFriendlyInvisibles",Server.Default.TEAM.SEEFRIENTLYINVISIBLES);
        server_builder.pop();

        SERVER_CONFIG = server_builder.build();
    } 
}
