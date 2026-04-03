package com.aijygr;

import com.mojang.logging.LogUtils;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Main.MODID)
public class Main
{
    public static final String MODID = "aijbr";
    public static final Logger LOGGER = LogUtils.getLogger();
    public Main(FMLJavaModLoadingContext modloadingcontext)
    {
        var bus = modloadingcontext.getModEventBus();
        Reg.ITEMS.register(bus);
        Reg.BLOCKS.register(bus);
        Reg.CREATIVE_MODE_TABS.register(bus);
        Reg.BLOCK_ENTITIES.register(bus);

        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        modloadingcontext.registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
    }
}
