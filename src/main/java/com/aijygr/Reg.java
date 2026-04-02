package com.aijygr;

import com.aijygr.Block.ContainerBlock;
import com.aijygr.Entity.BlockEntity.LootContainer;
import com.aijygr.Item.Armor;
import com.aijygr.Item.ArmorMaterials_override;
import com.aijygr.Item.Medkit;
import com.aijygr.Item.Syringe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Reg
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Main.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Main.MODID);

    public static final RegistryObject<Item> SYRINGE = ITEMS.register("syringe",()-> new Syringe(new Item.Properties()));
    public static final RegistryObject<Item> MEDKIT = ITEMS.register("medkit", () -> new Medkit(new Item.Properties()));
    public static final RegistryObject<Item> IRON_ARMOR = ITEMS.register("iron_armor", () -> new Armor(ArmorMaterials_override.IRON,new ArmorItem.Properties()));
    public static final RegistryObject<Item> DIAMOND_ARMOR = ITEMS.register("diamond_armor", () -> new Armor(ArmorMaterials_override.DIAMOND,new ArmorItem.Properties()));
    public static final RegistryObject<Block> MYBLOCK = BLOCKS.register("loot_container",() -> new ContainerBlock(BlockBehaviour.Properties.copy(Blocks.CHEST)));


    public static final RegistryObject<BlockEntityType<LootContainer>> LOOTCONTAINER_REG = BLOCK_ENTITIES.register("loot_container", () -> BlockEntityType.Builder.of(LootContainer::new, MYBLOCK.get()).build(null));
    public static final RegistryObject<Item> MYBLOCKITEM = ITEMS.register("loot_container", () -> new BlockItem(Reg.MYBLOCK.get(),new Item.Properties()));
}
