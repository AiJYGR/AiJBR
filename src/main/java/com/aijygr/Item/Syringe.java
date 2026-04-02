package com.aijygr.Item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Syringe extends Item {
    public Syringe(Properties properties) {
        // super(properties.food(new FoodProperties.Builder().alwaysEat().effect(() ->
        // new MobEffectInstance(MobEffects.HEAL,1),1).build()));
        super(properties.food(new FoodProperties.Builder().alwaysEat().build()));
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 50;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 5;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) // 使用东西的那一瞬间（按下右键的一瞬间）的代码
    {
        if (level.isClientSide())// 判断是否为服务端调用事件，处理逻辑
        {
            
        }
        return super.use(level, player, interactionHand);
    }

    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity)// 使用完物品的一瞬间的tick
    {
        if (!level.isClientSide()) {
            Player player;
            if (livingEntity instanceof Player) {
                player = (Player) livingEntity;
                player.setHealth(player.getHealth() + 4);
                player.getOnPos();
            }
        }
        return super.finishUsingItem(itemStack, level, livingEntity);
    }

    // public void onUsingTick(ItemStack stack, LivingEntity livingEntity, int
    // count) {
    // //使用物品的每一tick调用

    // super.onUsingTick(stack, livingEntity, count);
    // }
}
