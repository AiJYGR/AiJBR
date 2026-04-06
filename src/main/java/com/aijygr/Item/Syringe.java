package com.aijygr.Item;

import com.aijygr.Config;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class Syringe extends Item {
    public Syringe(Properties properties) {
        // super(properties.food(new FoodProperties.Builder().alwaysEat().effect(() ->
        // new MobEffectInstance(MobEffects.HEAL,1),1).build()));
        super(properties.food(new FoodProperties.Builder().alwaysEat().build()));
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return Config.ServerConfig.ITEM_MEDKIT_USEDURATION.get();
    }
    @Override
    public int getMaxStackSize(ItemStack stack) {
        return Config.ServerConfig.ITEM_MEDKIT_MAXSTACKSIZE.get();
    }
    @Override @NotNull
    public  InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) // 使用东西的那一瞬间（按下右键的一瞬间）的代码
    {
        if (level.isClientSide())// 判断是否为服务端调用事件，处理逻辑
        {
            
        }
        return super.use(level, player, interactionHand);
    }
    @NotNull
    public  ItemStack finishUsingItem(@NotNull ItemStack itemStack, Level level, @NotNull LivingEntity livingEntity)// 使用完物品的一瞬间的tick
    {
        if (!level.isClientSide()) {
            if (livingEntity instanceof Player player) {
                float h = Config.ServerConfig.ITEM_SYRINGE_HEALAMOUNT.get().floatValue();
                player.heal(h);
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
