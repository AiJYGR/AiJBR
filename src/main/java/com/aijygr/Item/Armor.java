package com.aijygr.Item;

import com.aijygr.LIB;
import com.aijygr.Main;
import com.aijygr.ModConfig;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class Armor extends ArmorItem
{
    public Armor(ArmorMaterials material, Item.Properties properties)
    {
        super(material,ArmorItem.Type.CHESTPLATE, properties);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.CHEST) {
            UUID uuid = LIB.makeUUID(Main.MODUUIDP1,Main.MODUUIDP2,0xA0,0x9);
            int defense = switch (this.material.getName()){
                case "iron" -> ModConfig.Server.Config.ITEM.ITEM_ARMOR_IRON_DEFENSE.get();
                case "diamond" -> ModConfig.Server.Config.ITEM.ITEM_ARMOR_DIAMOND_DEFENSE.get();
                case "netherite" -> ModConfig.Server.Config.ITEM.ITEM_ARMOR_NETHERITE_DEFENSE.get();
                default -> 0;
            };
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ARMOR, new AttributeModifier(
                    uuid,
                    "DEFENSE",
                    defense,
                    AttributeModifier.Operation.ADDITION
            ));
            return builder.build();
        }
        return super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public int getMaxStackSize(ItemStack stack)
    {
        return 1;
    }
    @Override
    public int getMaxDamage(ItemStack stack)
    {
        return 40;
    }

}
