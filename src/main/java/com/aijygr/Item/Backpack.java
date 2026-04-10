package com.aijygr.Item;

import com.aijygr.ModConfig;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class Backpack extends Item {

    private static short permissionLevel = (short)0;
    public static short getPermissionLevel() {
        return ModConfig.Server.Config.ITEM.BACKPACK_LVL1_PERMISSIONLEVEL.get().shortValue();
        //return permissionLevel;
    }

    public Backpack(Properties pProperties,short plvl) {

        super(pProperties);
        this.permissionLevel = plvl;
    }

    @Override
    public int getMaxStackSize(ItemStack itemStack){
        return 1;
    }


}
