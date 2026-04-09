package com.aijygr.Events.Game.BP;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SlotTests {
    public static boolean HasPermission(Player player) {
        System.out.println("inv: "+player.getInventory().items.toString());
        return false;
    }
}
