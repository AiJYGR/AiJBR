package com.aijygr.Item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;

public class Armor extends ArmorItem
{
    public Armor(ArmorMaterials_override material,Item.Properties properties)
    {
        super(material,ArmorItem.Type.CHESTPLATE, properties);
    }
    @Override
    public int getMaxStackSize(ItemStack stack)
    {
        return 1;
    }
    @Override
    public int getMaxDamage(ItemStack stack)
    {
        return 30;
    }
//    @Override
//    public void onArmorTick(ItemStack stack, Level level, Player player) {
//        if (!level.isClientSide())
//        {
//        }
//    }
    @Override
    public ItemStack getDefaultInstance()
    {
        ItemStack stack = super.getDefaultInstance();
        CompoundTag enchantmentTag = new CompoundTag();
        ListTag enchantmentsList = new ListTag();
        CompoundTag enchantment = new CompoundTag();
        enchantment.putString("id", Enchantments.ALL_DAMAGE_PROTECTION.toString());
        enchantment.putInt("lvl", 8);
        enchantmentsList.add(enchantment);
        enchantmentTag.put("Enchantments", enchantmentsList);
        stack.setTag(enchantmentTag);
        System.out.println(Enchantments.ALL_DAMAGE_PROTECTION.toString());
        return stack;
    }
}
