package com.aijygr.AiJBP;

import com.aijygr.AiJGame.Client.ClientGame;
import com.aijygr.Item.Lock;
import com.aijygr.ModConfig;
import com.aijygr.ModMessages;

import com.aijygr.Reg;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
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
    public static void serverMoveEmpty(LocalPlayer player,short index, short target) {
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
    /// Use{@link AiJBackpack#serverMoveEmpty(LocalPlayer, short, short)} instead
    public static void serverSwapItem(LocalPlayer player, short index, short target) {
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
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event){
        if(Minecraft.getInstance().isPaused())
            return;
        LocalPlayer player = Minecraft.getInstance().player;
        if(player != null && event.side == LogicalSide.CLIENT && event.phase == Phase.END)
        {
            if(player.isCreative()){
                InventoryLock.unlockAll();
                playerPermission = Short.MAX_VALUE;
                return;
            }
            if(ClientGame.isBPSynced && ClientGame.isTagSynced /*&& isAvailable*/){
                //1.检查背包格位 计算PermissionLevel
                //2.锁格子
                //3.扫描所有未上锁格子 检查非法位置
                //4.寻找合法的位置 并且移动
                //5.如果没有就丢弃物品（延迟）
                //Step1
                List<SlotwithPermissionLevel> bp = slots.get("BACKPACK");
                Inventory inventory = player.getInventory();
                if(bp != null){
                    short i = ModConfig.Server.Config.BACKPACK.DEFAULT_PERMISSIONLEVEL.get().shortValue();
                    for(SlotwithPermissionLevel it : bp){
                        ItemStack itemstack = inventory.getItem(it.index);
                        if(!itemstack.isEmpty() && itemstack.getItem() instanceof com.aijygr.Item.Backpack backpack){
                            if(i+backpack.getPermissionLevel()>=Short.MAX_VALUE)
                                i = Short.MAX_VALUE;
                            else{
                                i += backpack.getPermissionLevel();
                            }
                        }
                    }
                    playerPermission = i;
                }
                else
                    playerPermission = ModConfig.Server.Config.BACKPACK.DEFAULT_PERMISSIONLEVEL.get().shortValue();
                //Step2
                for(Map.Entry<String, List<SlotwithPermissionLevel>> entry: slots.entrySet()){
                    for(SlotwithPermissionLevel slot:entry.getValue()){
                        if(slot.permissionlevel>playerPermission)
                            InventoryLock.lock(slot.index);
                        else
                            InventoryLock.unlock(slot.index);
                    }
                }
                //Step3
                for (Map.Entry<String, List<SlotwithPermissionLevel>> entry : slots.entrySet()) {
                    String slottag = entry.getKey();
                    List<SlotwithPermissionLevel> slots1 = entry.getValue();
                    for (SlotwithPermissionLevel slot1 : slots1) {
                        ItemStack itemstack = inventory.getItem(slot1.index);
                        if ((!itemstack.isEmpty())&& (!InventoryLock.isLocked(slot1.index)))
                        {
                            if(itemstack.getItem() instanceof Lock){
                                serverRemove(slot1.index,true);
                            }
                            List<String> tags = Tagger.GetItemTags(itemstack);
                            if(tags == null)
                            {
                                serverRemove(slot1.index,false);
                                return;
                            }
                            if(tags.isEmpty()){
                                break;
                            }
                            if (!tags.contains(slottag))
                            {
                                //Step4
                                for (String itemtag : Tagger.GetItemTags(itemstack)) {
                                    List<SlotwithPermissionLevel> slots2 = slots.get(itemtag);
                                    if (slots2 == null)
                                        continue;
                                    for (SlotwithPermissionLevel slot2 : slots2) {
                                        if(slot2.equals(slot1))
                                            continue;
                                        ItemStack i = inventory.getItem(slot2.index);
                                        if (!i.isEmpty()) {//尝试堆叠并且叠加物品
                                            if (ItemStack.isSameItemSameTags(itemstack, i) && i.getCount() < i.getMaxStackSize()) {
                                                serverMoveEmpty(player, slot1.index, slot2.index);
                                                return;
                                            }
                                            else if (isBetterEquipment(itemstack, i)) {
                                                //serverRemove(slot2.index,true);
                                                //serverMoveEmpty(player, slot1.index, slot2.index);
                                                serverSwapItem(player, slot1.index, slot2.index);
                                                return;
                                            }
                                        }
                                        else if (i.isEmpty()) {
                                            serverMoveEmpty(player,slot1.index,slot2.index);
                                            return;
                                        }
                                    }
                                }
                                //Step5
                                serverRemove(slot1.index,false);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }


}
