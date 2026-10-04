package com.aijygr.aijbr;

import com.aijygr.aijbr.Block.ContainerBlock;
import com.aijygr.aijbr.Effects.Flying;
import com.aijygr.aijbr.Entity.BlockEntity.LootContainer;
import com.aijygr.aijbr.Entity.DropShip;
import com.aijygr.aijbr.Item.*;

import com.aijygr.aijbr.Item.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
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

import java.util.ArrayList;
import java.util.List;

public class Reg
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Main.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Main.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Main.MODID);
    public static final DeferredRegister<MobEffect> MOB_EFFECT = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Main.MODID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Main.MODID);

    public static final RegistryObject<Item> SYRINGE = ITEMS.register("syringe",()-> new Syringe(new Item.Properties()));
    public static final RegistryObject<Item> MEDKIT = ITEMS.register("medkit", () -> new Medkit(new Item.Properties()));
    public static final RegistryObject<Item> IRON_ARMOR = ITEMS.register("iron_armor", () -> new Armor(ArmorMaterials.IRON,new ArmorItem.Properties()));
    public static final RegistryObject<Item> DIAMOND_ARMOR = ITEMS.register("diamond_armor", () -> new Armor(ArmorMaterials.DIAMOND,new ArmorItem.Properties()));
    public static final RegistryObject<Item> NETHERITE_ARMOR = ITEMS.register("netherite_armor", () -> new Armor(ArmorMaterials.NETHERITE,new ArmorItem.Properties()));
    public static final RegistryObject<Item> AiJBP_LVL1 = ITEMS.register("backpack_lvl1",()->new Backpack(new Item.Properties(),() -> ModConfig.Server.Config.ITEM.BACKPACK_LVL1_PERMISSIONLEVEL.get().shortValue()));
    public static final RegistryObject<Item> AiJBP_LVL2 = ITEMS.register("backpack_lvl2",()->new Backpack(new Item.Properties(),() -> ModConfig.Server.Config.ITEM.BACKPACK_LVL2_PERMISSIONLEVEL.get().shortValue()));
    public static final RegistryObject<Item> AiJBP_LVL3 = ITEMS.register("backpack_lvl3",()->new Backpack(new Item.Properties(),() -> ModConfig.Server.Config.ITEM.BACKPACK_LVL3_PERMISSIONLEVEL.get().shortValue()));
    public static final RegistryObject<Item> AiJBP_LVL4 = ITEMS.register("backpack_lvl4",()->new Backpack(new Item.Properties(),() -> ModConfig.Server.Config.ITEM.BACKPACK_LVL4_PERMISSIONLEVEL.get().shortValue()));
    public static final RegistryObject<Item> LOCK = ITEMS.register("lock",()->new Lock(new Item.Properties()));

    public static final String LOOT_CONTAINER = "loot_container";
    public static final RegistryObject<Item> LOOTCONTAINER_ITEM = ITEMS.register(LOOT_CONTAINER, () -> new BlockItem(Reg.LOOTCONTAINER_BLOCK.get(),new Item.Properties()));
    public static final RegistryObject<Block> LOOTCONTAINER_BLOCK = BLOCKS.register(LOOT_CONTAINER,() -> new ContainerBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)));
    public static final RegistryObject<BlockEntityType<LootContainer>> LOOTCONTAINER_BLOCCKENTITY = BLOCK_ENTITIES.register(LOOT_CONTAINER, () -> BlockEntityType.Builder.of(LootContainer::new, LOOTCONTAINER_BLOCK.get()).build(null));
    public static final RegistryObject<EntityType<DropShip>> DROPSHIP = ENTITY_TYPES.register("dropship", () ->
            EntityType.Builder.<DropShip>of(DropShip::new, MobCategory.MISC)
                    .sized(2.0f,2.0f)
                    .clientTrackingRange(127)
                    .setUpdateInterval(19)
                    .setShouldReceiveVelocityUpdates(true)
                    .noSave()
                    .fireImmune()
                    .build("dropship"));

    public static final RegistryObject<MobEffect> FLYING_EFFECT = MOB_EFFECT.register("flying", Flying::new);
    public static final RegistryObject<SoundEvent> RING_DAMAGE_SOUND = SOUND_EVENTS.register("ring_damage", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Main.MODID, "ring_damage")));
    public static final RegistryObject<SoundEvent> RING_PHASE_SOUND = SOUND_EVENTS.register("ring_phase", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Main.MODID, "ring_phase")));
    public static final RegistryObject<SoundEvent> RING_CLOSE_SOUND = SOUND_EVENTS.register("ring_close", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Main.MODID, "ring_close")));
    public static final RegistryObject<SoundEvent> CONTAINER_OPEN_SOUND = SOUND_EVENTS.register("container_open", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Main.MODID,"container_open")));
    public static final RegistryObject<SoundEvent> CONTAINER_CLOSE_SOUND = SOUND_EVENTS.register("container_close", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Main.MODID,"container_close")));
    public static final ResourceKey<DamageType> AIJBR_RING_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE,ResourceLocation.fromNamespaceAndPath(Main.MODID, "ring_damage"));

    public static final ResourceKey<CreativeModeTab> AIJBR_CREATIVE_TAB = CREATIVE_MODE_TABS.register("aijbr_creative_tab", () ->CreativeModeTab.builder()
            .icon(() -> new ItemStack(Reg.SYRINGE.get()))
            .title(Component.translatable("aijbr_creative_tab"))
            .displayItems((parameters, output) -> {
                //普通物品
//                for(var it = ITEMS.getEntries().iterator(); it.hasNext(); ) {
//                    output.accept(it.next().get());
//                }
                output.accept(SYRINGE.get());
                output.accept(MEDKIT.get());
                output.accept(IRON_ARMOR.get());
                output.accept(DIAMOND_ARMOR.get());
                output.accept(NETHERITE_ARMOR.get());
                output.accept(AiJBP_LVL1.get());
                output.accept(AiJBP_LVL2.get());
                output.accept(AiJBP_LVL3.get());
                output.accept(AiJBP_LVL4.get());
                output.accept(LOOTCONTAINER_ITEM.get());

                //带有战利品表的LootContainer
                List<String> lootTables = new ArrayList<>(List.of("aijbr:chests/normal","aijbr:chests/loot1","aijbr:chests/loot_20epic","aijbr:chests/epic")) ;
                for (String lootTable : lootTables) {
                    ItemStack itemStack = new ItemStack(Reg.LOOTCONTAINER_ITEM.get());
                    itemStack.setTag(GenerateContainersTagWithLootTable(lootTable));
                    output.accept(itemStack);
                }

            })
            .build()).getKey();

    public static CompoundTag GenerateContainersTagWithLootTable(String lootTable){
        CompoundTag blockEntityTag = new CompoundTag();
        CompoundTag display =  new CompoundTag();
        ListTag loreList = new ListTag();
        loreList.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal(lootTable).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFFFF55))))));

        blockEntityTag.putString("LootTable",lootTable);
        display.put("Lore",loreList);
        display.putString("Name",Component.Serializer.toJson(Component.translatable("item."+Main.MODID+"."+LOOT_CONTAINER).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xAA00AA)))));

        CompoundTag tag = new CompoundTag();
        tag.put("display",display);
        tag.put("BlockEntityTag",blockEntityTag);

        return tag;
    }
}
