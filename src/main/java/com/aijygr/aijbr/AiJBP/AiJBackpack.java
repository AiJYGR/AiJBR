package com.aijygr.aijbr.AiJBP;

import com.aijygr.aijbr.Network.ModMessages;

import com.aijygr.aijbr.Reg;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


public class AiJBackpack
{
    public static class SlotwithPermissionLevel {
        short index;
        short permissionlevel;

        public SlotwithPermissionLevel(){
            index = 0;
            permissionlevel = Short.MAX_VALUE;
        }
        public SlotwithPermissionLevel(short index, short permissionlevel){
            this.index = index;
            this.permissionlevel = permissionlevel;
        }
    }

    public static Map<String, List<SlotwithPermissionLevel>> slots = new HashMap<>();
    public static short playerPermission = 0;

    private static boolean isAvailable = true;
    public static void setAvailable(){
        isAvailable = true;
    }
    public static void clientsync(){
        setAvailable();
        InventoryLock.unlockAll();
    }
    public static void serverRemove(short index, boolean remove){
        isAvailable = false;
        ModMessages.PlayerSendToServer(new MSGServerRemoveItem(index,remove));
    }
    public static void serverMoveEmpty(Player player, short index, short target) {
        isAvailable = false;
        Inventory inventory = player.getInventory();
        switch (target) {
            case 36:
                if(!inventory.getItem(index).getItem().canEquip(inventory.getItem(index), EquipmentSlot.FEET, player)){
                    serverRemove(index,false);
                    return;
                }
                break;
            case 37:
                if(!inventory.getItem(index).getItem().canEquip(inventory.getItem(index), EquipmentSlot.LEGS, player)){
                    serverRemove(index,false);
                    return;
                }
                break;
            case 38:
                if(!inventory.getItem(index).getItem().canEquip(inventory.getItem(index), EquipmentSlot.CHEST, player)){
                    serverRemove(index,false);
                    return;
                }
                break;
            case 39:
                if(!inventory.getItem(index).getItem().canEquip(inventory.getItem(index), EquipmentSlot.HEAD, player)){
                    serverRemove(index,false);
                    return;
                }
                break;
        }
        ModMessages.PlayerSendToServer(new MSGServerMoveEmpty(index,target));
    }
    /// try not to use this one.
    /// Use{@link AiJBackpack#serverMoveEmpty(Player, short, short)} instead
    public static void serverSwapItem(short index, short target) {
        isAvailable = false;
        ModMessages.PlayerSendToServer(new MSGServerSwapItem(index,target));
    }

    public static final Map<Supplier<? extends Item>, Integer> ITEM_LEVEL = new HashMap<>();

    static {
        // 注册等级：数字越高，物品越强
        ITEM_LEVEL.put(Reg.AiJBP_LVL1, 1);
        ITEM_LEVEL.put(Reg.AiJBP_LVL2, 2);
        ITEM_LEVEL.put(Reg.AiJBP_LVL3, 3);
        ITEM_LEVEL.put(Reg.IRON_ARMOR, 1);
        ITEM_LEVEL.put(Reg.DIAMOND_ARMOR, 2);
        ITEM_LEVEL.put(Reg.NETHERITE_ARMOR, 3);
    }
    public static int getLevel(ItemStack stack) {
        for (Map.Entry<Supplier<? extends Item>, Integer> entry : ITEM_LEVEL.entrySet()) {
            if (stack.is(entry.getKey().get())) {
                return entry.getValue();
            }
        }
        return 0;
    }
    public static boolean isBetterEquipment(ItemStack newStack, ItemStack oldStack) {
        int oldlevel = getLevel(oldStack);
        int newlevel = getLevel(newStack);
        if(oldlevel==0 || newlevel==0)
            return false;
        else
            return newlevel > oldlevel;
    }



}
