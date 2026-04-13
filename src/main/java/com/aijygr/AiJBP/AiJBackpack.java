package com.aijygr.AiJBP;

import com.aijygr.AiJGame.Game;
import com.aijygr.ModConfig;
import com.aijygr.ModMessages;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)//####################################
public class AiJBackpack extends Event
{
    public static class SlotPermissionLevel{
        short index;
        short permissionlevel;

        public SlotPermissionLevel(){
            index = 0;
            permissionlevel = Short.MAX_VALUE;
        }
        public SlotPermissionLevel(short index,short permissionlevel){
            this.index = index;
            this.permissionlevel = permissionlevel;
        }
    }

    //public static Map<SlotTag,List<SlotPermissionLevel>> slots = new HashMap<>();
    public static Map<String, List<SlotPermissionLevel>> slots = new HashMap<>();
    public static short playerPermission = 0;

    private static boolean isAvailable = true;
    public static void setAvailable(){
        isAvailable = true;
    }
    public static void clientsync(){
        setAvailable();
        InventoryLock.unlockAll();
    }
    public static void serverSwap(Inventory inventory,short s1, short s2){
        isAvailable = false;
        ModMessages.PlayerSendToServer(new MSGServerSwapItem(s1,s2));
        ItemStack item1 = inventory.getItem(s1).copy();
        ItemStack item2 = inventory.getItem(s2).copy();
        inventory.setItem(s1,item2.copy());
        inventory.setItem(s2,item1.copy());
    }
    public static void serverRemove(Inventory inventory, short index,boolean remove){
        isAvailable = false;
        ModMessages.PlayerSendToServer(new MSGServerRemoveItem(index,remove));
        inventory.setItem(index,ItemStack.EMPTY);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent event)
    {
        Player player = event.player;
        if(player != null && event.side == LogicalSide.CLIENT && event.phase == Phase.END)
        {
            if (Minecraft.getInstance().player == null || !player.getUUID().equals(Minecraft.getInstance().player.getUUID()))
                return;

//            if(Game.gametime%50==0)//
//            {
//                clientsync();
//            }
            if(Game.isReloaded && isAvailable){
                //1.检查背包格位 计算PermissionLevel
                //2.锁格子
                //3.扫描所有未上锁格子 检查非法位置
                //4.寻找合法的位置 并且移动
                //5.如果没有就丢弃物品（延迟）

                //Step1
                List<SlotPermissionLevel> bp = slots.get("BACKPACK");
                Inventory inventory = player.getInventory();
                if(bp != null){
                    short i = ModConfig.Server.Config.BACKPACK.DEFAULT_PERMISSIONLEVEL.get().shortValue();
                    for(SlotPermissionLevel it : bp){
                        ItemStack itemstack = inventory.getItem(it.index);
                        if(!itemstack.isEmpty() && itemstack.getItem() instanceof com.aijygr.Item.Backpack backpack){
                            if(i+backpack.getPermissionLevel()>=Short.MAX_VALUE)
                                i = Short.MAX_VALUE;
                            else{
                                i+=backpack.getPermissionLevel();
                            }
                        }
                    }
                    playerPermission = i;
                }
                //Step2
                for(Map.Entry<String, List<SlotPermissionLevel>> entry: slots.entrySet()){
                    for(SlotPermissionLevel slot:entry.getValue()){
                        if(slot.permissionlevel>playerPermission)
                            InventoryLock.lock(slot.index);
                        else
                            InventoryLock.unlock(slot.index);
                    }
                }

                //Step3
                for (Map.Entry<String, List<SlotPermissionLevel>> entry : slots.entrySet()) {
                    String slottag = entry.getKey();
                    List<SlotPermissionLevel> slots1 = entry.getValue();
                    for (SlotPermissionLevel slot1 : slots1) {
                        ItemStack itemstack = inventory.getItem(slot1.index);
                        if (!itemstack.isEmpty()
                                && !InventoryLock.isLocked(slot1.index)
                                && !Tagger.GetItemTags(itemstack).isEmpty()
                                && !Tagger.GetItemTags(itemstack).contains(slottag)) {
                            //Step4
                            for (String itemtag : Tagger.GetItemTags(itemstack)) {
                                List<SlotPermissionLevel> slots2 = slots.get(itemtag);
                                if (slots2 == null)
                                    continue;
                                for (SlotPermissionLevel slot2 : slots2) {
                                    if(slot2.equals(slot1))
                                        continue;
                                    ItemStack i = inventory.getItem(slot2.index);
                                    if (i.isEmpty()) {
                                        System.out.printf("%d MSGSVSWAP:(%d,%d)\n", Game.gametime,  slot1.index,  slot2.index);
                                        serverSwap(inventory,slot1.index,slot2.index);
                                        return;
                                    }
                                }
                            }
                            //Step5
                            System.out.printf("%d MSGSVRemove:(%d)\n", Game.gametime,slot1.index);
                            serverRemove(inventory,slot1.index,false);
                            return;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerPickup(PlayerEvent.ItemPickupEvent event){
        if(event.getEntity().level().isClientSide()&&event.getEntity().isLocalPlayer()){
            System.out.println("Player picked up");
            if(!isAvailable){
                System.out.println("not available");
                event.setCanceled(true);
            }
        }
    }

}
