package com.aijygr.Item;

import com.aijygr.LIB;
import com.aijygr.Main;
import com.aijygr.ModConfig;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
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
            int defense = 0;
//                case "iron" -> ModConfig.Server.Config.ITEM.ITEM_ARMOR_IRON_DEFENSE.get();
//                case "diamond" -> ModConfig.Server.Config.ITEM.ITEM_ARMOR_DIAMOND_DEFENSE.get();
//                case "netherite" -> ModConfig.Server.Config.ITEM.ITEM_ARMOR_NETHERITE_DEFENSE.get();
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
        if(stack.getItem() instanceof Armor armor){
            return switch (armor.getMaterial().getName()){
                case "iron" -> ModConfig.Server.Config.ITEM.ITEM_ARMOR_IRON_MAXDAMAGE.get();
                case "diamond" -> ModConfig.Server.Config.ITEM.ITEM_ARMOR_DIAMOND_MAXDAMAGE.get();
                case "netherite" -> ModConfig.Server.Config.ITEM.ITEM_ARMOR_NETHERITE_MAXDAMAGE.get();
                default -> 10;
            };
        }
        return 10;
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return 0;
    }

    public static void damageArmor(Armor armor,ItemStack itemstack,int armorDamage,LivingEntity target){
        if(armorDamage <= 0 )
            return;
        if(armor.getDamage(itemstack) + armorDamage < armor.getMaxDamage(itemstack)-1 )
            armor.setDamage(itemstack,armor.getDamage(itemstack) + armorDamage );
        else
        {
            target.broadcastBreakEvent(EquipmentSlot.CHEST);
            if (target instanceof Player player) {
                player.awardStat(Stats.ITEM_BROKEN.get(itemstack.getItem()));
            }
            armor.setDamage(itemstack,armor.getMaxDamage(itemstack) - 1);
        }
    }
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        DamageSource damagesource = event.getSource();
        if (!damagesource.is(DamageTypeTags.IS_PROJECTILE))
            return;

        LivingEntity target = event.getEntity();
        ItemStack itemstack = target.getItemBySlot(EquipmentSlot.CHEST);
        if (itemstack.getItem() instanceof Armor armor) {
            if(armor.getDamage(itemstack) == armor.getMaxDamage(itemstack)-1)//如果碎甲就不减伤
                return;
            //护甲损耗机制暂定: 75%穿透伤害+25%护甲伤害
            if(damagesource.is(DamageTypeTags.BYPASSES_ARMOR)){
                damageArmor(armor,itemstack,(int)Math.floor(event.getAmount()*0.75f),target);//75%穿透部分 计算护甲耐久消耗
            }
            else
            {
                Double percentage = switch (armor.getMaterial().getName()){
                    case "iron" -> ModConfig.Server.Config.ITEM.ITEM_ARMOR_IRON_DEFENSE.get();
                    case "diamond" -> ModConfig.Server.Config.ITEM.ITEM_ARMOR_DIAMOND_DEFENSE.get();
                    case "netherite" -> ModConfig.Server.Config.ITEM.ITEM_ARMOR_NETHERITE_DEFENSE.get();
                    default -> 0.0;
                };

                int armorDamage = (int)Math.floor(event.getAmount()*0.25f);
                if(armorDamage < 1)
                    armorDamage = 1;
                damageArmor(armor,itemstack,armorDamage,target);//25%非穿透部分 计算护甲耐久消耗

                double defense = event.getAmount()* percentage*0.01f;
                event.setAmount( event.getAmount() - (float)defense );//减伤
            }
        }
    }

//    @SubscribeEvent
//    public static void onLivingHurt(LivingHurtEvent event) {
//        DamageSource source = event.getSource();
//
//        if (source.is(DamageTypeTags.BYPASSES_ARMOR) || !source.is(DamageTypeTags.IS_PROJECTILE)) {
//            return;
//        }
//
//        LivingEntity target = event.getEntity();
//        ItemStack itemstack = target.getItemBySlot(EquipmentSlot.CHEST);
//        if (itemstack.getItem() instanceof Armor armor) {
//            if(armor.getDamage(itemstack) == armor.getMaxDamage(itemstack)-1)//如果碎甲就不减伤
//                return;
//            Double percentage = switch (armor.getMaterial().getName()){
//                case "iron" -> ModConfig.Server.Config.ITEM.ITEM_ARMOR_IRON_DEFENSE.get();
//                case "diamond" -> ModConfig.Server.Config.ITEM.ITEM_ARMOR_DIAMOND_DEFENSE.get();
//                case "netherite" -> ModConfig.Server.Config.ITEM.ITEM_ARMOR_NETHERITE_DEFENSE.get();
//                default -> 0.0;
//            };
//            double defense = event.getAmount()* percentage*0.01f;
//            event.setAmount( event.getAmount() - (float)defense );
//            int armorDamage = (int)Math.ceil(defense);
//            if(armor.getDamage(itemstack) + armorDamage < armor.getMaxDamage(itemstack))
//                armor.setDamage(itemstack,armor.getDamage(itemstack) + armorDamage );
//            else
//            {
//                //碎甲的逻辑
//                target.broadcastBreakEvent(EquipmentSlot.CHEST);
//                if (target instanceof Player player) {
//                    player.awardStat(Stats.ITEM_BROKEN.get(itemstack.getItem()));
//                }
//                armor.setDamage(itemstack,armor.getMaxDamage(itemstack) - 1);
//            }
//        }
//    }
}
