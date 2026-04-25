package com.aijygr;

import com.mojang.logging.LogUtils;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.slf4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(Main.MODID)
public class Main
{
    public static final String MODID = "aijbr";
    public static final int MODUUIDP1 = 0x0041694A;
    public static final int MODUUIDP2 = 0x00004252;

    public static final Logger LOGGER = LogUtils.getLogger();
    public Main(FMLJavaModLoadingContext modloadingcontext)
    {
        var bus = modloadingcontext.getModEventBus();
        Reg.ITEMS.register(bus);
        Reg.BLOCKS.register(bus);
        Reg.CREATIVE_MODE_TABS.register(bus);
        Reg.BLOCK_ENTITIES.register(bus);
        Reg.ENTITY_TYPES.register(bus);

        ModMessages.register();//reg net

        modloadingcontext.registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, ModConfig.CLIENT_CONFIG);
        modloadingcontext.registerConfig(net.minecraftforge.fml.config.ModConfig.Type.SERVER, ModConfig.SERVER_CONFIG);
    }
}
