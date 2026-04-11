package com.aijygr.AiJBP;

import net.minecraft.world.entity.player.Player;

public class SlotTests {
    public static boolean HasPermission(Player player) {
        System.out.println("inv: "+player.getInventory().items.toString());
        return false;
    }
}
