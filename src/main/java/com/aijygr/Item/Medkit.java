package com.aijygr.Item;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Medkit extends Item
{
    public Medkit(Properties properties)
    {
        //super(properties.food(new FoodProperties.Builder().alwaysEat().effect(() -> new MobEffectInstance(MobEffects.HEAL,1),1).build()));
        super(properties.food(new FoodProperties.Builder().alwaysEat().build()));
    }
    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 120;
    }
    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity)
    {
        if(!level.isClientSide())
        {
            if(livingEntity instanceof Player player)
            {
                player = (Player)livingEntity;
                player.setHealth(player.getHealth()+20);
            }
        }
        return super.finishUsingItem(itemStack, level, livingEntity);
    }
}
