package com.aijygr.Item;
import com.aijygr.ModConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class Medkit extends Item
{
    public Medkit(Properties properties)
    {
        //super(properties.food(new FoodProperties.Builder().alwaysEat().effect(() -> new MobEffectInstance(MobEffects.HEAL,1),1).build()));
        super(properties.food(new FoodProperties.Builder().alwaysEat().build()));
    }
    @Override
    public int getUseDuration(ItemStack itemStack) {
        return ModConfig.Server.Config.ITEM.MEDKIT_USEDURATION.get();
    }
    @Override
    public int getMaxStackSize(ItemStack stack) {
        return ModConfig.Server.Config.ITEM.MEDKIT_MAXSTACKSIZE.get();
    }
    public ItemStack finishUsingItem(@NotNull ItemStack itemStack, Level level, @NotNull LivingEntity livingEntity)
    {
        if(!level.isClientSide())
        {
            if(livingEntity instanceof Player player)
            {
                player.heal(ModConfig.Server.Config.ITEM.MEDKIT_HEALAMOUNT.get().floatValue());
            }
        }
        return super.finishUsingItem(itemStack, level, livingEntity);
    }
}
