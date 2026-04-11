package com.aijygr.Item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class Lock extends Item {

    public Lock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getMaxStackSize(ItemStack itemStack){
        return 1;
    }
}
