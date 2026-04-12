package com.aijygr.Item;

import com.aijygr.ModConfig;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.NotNull;

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
