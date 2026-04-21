package com.aijygr;

import com.aijygr.Block.ContainerBlock;
import com.aijygr.Entity.BlockEntity.LootContainer;
import com.aijygr.Entity.DropShip;
import com.aijygr.Item.*;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.*;
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
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Main.MODID);

    public static final RegistryObject<Item> SYRINGE = ITEMS.register("syringe",()-> new Syringe(new Item.Properties()));
    public static final RegistryObject<Item> MEDKIT = ITEMS.register("medkit", () -> new Medkit(new Item.Properties()));
    public static final RegistryObject<Item> IRON_ARMOR = ITEMS.register("iron_armor", () -> new Armor(ArmorMaterials_override.IRON,new ArmorItem.Properties()));
    public static final RegistryObject<Item> DIAMOND_ARMOR = ITEMS.register("diamond_armor", () -> new Armor(ArmorMaterials_override.DIAMOND,new ArmorItem.Properties()));
    public static final RegistryObject<Item> AiJBP_LVL1 = ITEMS.register("backpack_lvl1",()->new Backpack(new Item.Properties().fireResistant(),() -> ModConfig.Server.Config.ITEM.BACKPACK_LVL1_PERMISSIONLEVEL.get().shortValue()));
    public static final RegistryObject<Item> AiJBP_LVL2 = ITEMS.register("backpack_lvl2",()->new Backpack(new Item.Properties().fireResistant(),() -> ModConfig.Server.Config.ITEM.BACKPACK_LVL2_PERMISSIONLEVEL.get().shortValue()));
    public static final RegistryObject<Item> AiJBP_LVL3 = ITEMS.register("backpack_lvl3",()->new Backpack(new Item.Properties().fireResistant(),() -> ModConfig.Server.Config.ITEM.BACKPACK_LVL3_PERMISSIONLEVEL.get().shortValue()));
    public static final RegistryObject<Item> AiJBP_LVL4 = ITEMS.register("backpack_lvl4",()->new Backpack(new Item.Properties().fireResistant(),() -> ModConfig.Server.Config.ITEM.BACKPACK_LVL4_PERMISSIONLEVEL.get().shortValue()));
    public static final RegistryObject<Item> LOCK = ITEMS.register("lock",()->new Lock(new Item.Properties()));

    public static final RegistryObject<Item> LOOTCONTAINER_ITEM = ITEMS.register("loot_container", () -> new BlockItem(Reg.LOOTCONTAINER_BLOCK.get(),new Item.Properties()));
    public static final RegistryObject<Block> LOOTCONTAINER_BLOCK = BLOCKS.register("loot_container",() -> new ContainerBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)));
    public static final RegistryObject<BlockEntityType<LootContainer>> LOOTCONTAINER_BLOCCKENTITY = BLOCK_ENTITIES.register("loot_container", () -> BlockEntityType.Builder.of(LootContainer::new, LOOTCONTAINER_BLOCK.get()).build(null));
    public static final RegistryObject<EntityType<DropShip>> DROPSHIP = ENTITY_TYPES.register("dropship", () ->
            EntityType.Builder.<DropShip>of(DropShip::new, MobCategory.MISC)
                    .sized(2.0f,2.0f)
                    .clientTrackingRange(255)
                    .updateInterval(40)
                    .setShouldReceiveVelocityUpdates(true)
                    .noSave()
                    .fireImmune()
                    .build("dropship"));

    public static final ResourceKey<DamageType> AIJBR_RING_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE,ResourceLocation.fromNamespaceAndPath(Main.MODID, "ring_damage"));
}
