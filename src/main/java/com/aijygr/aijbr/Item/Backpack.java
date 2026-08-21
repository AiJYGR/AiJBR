package com.aijygr.aijbr.Item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;


import java.util.function.Supplier;

public class Backpack extends Item {
    private final Supplier<Short> permissionSupplier;

    public Backpack(Properties pProperties, Supplier<Short> permissionSupplier) {
        super(pProperties);
        this.permissionSupplier = permissionSupplier;
    }

    public short getPermissionLevel() {
        return permissionSupplier.get();
    }

    @Override
    public int getMaxStackSize(ItemStack itemStack) {
        return 1;
    }
}
