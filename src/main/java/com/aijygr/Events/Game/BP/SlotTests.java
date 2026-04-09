package com.aijygr.Events.Game.BP;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SlotTests {
    public static boolean IsValidSlot(Player player, ItemStack itemStack, Backpack.BackpackSlotAttribute slotAttribute) {
        CompoundTag itemStackTagTag = itemStack.getTag();
        CompoundTag playerTag = player.getPersistentData();
        String permissionLvlKey = "AiJBR_BackpackPermissionLvl";
        playerTag.putShort(permissionLvlKey,(short)0);
//        if (player.getInventory().hasAnyMatching((i) -> {
//            if (i.getDescriptionId().equals(itemStack.getDescriptionId())) {
//                return true;
//            }
//            return false;
//        })){
//            return false;
//        }

        System.out.println("inv: "+player.getInventory().toString());

        return false;
    }
}
