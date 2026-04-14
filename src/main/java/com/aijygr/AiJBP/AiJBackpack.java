package com.aijygr.AiJBP;

import com.aijygr.AiJGame.Game;
import com.aijygr.ModConfig;
import com.aijygr.ModMessages;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
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
    /*@Deprecated
    public static void serverSwap(Inventory inventory,short s1, short s2){
        isAvailable = false;
        ModMessages.PlayerSendToServer(new MSGServerSwapItem(s1,s2));
    }*/
    public static void serverMoveEmpty(short index, short target) {
        //System.out.printf("[%d]MSGSVMoveEmpty:(%d,%d) ", Game.gametime, index, target);
        //System.out.printf("Client:{%s,%s}\n",player.getInventory().getItem(index).getDisplayName().getString(),player.getInventory().getItem(target).getDisplayName().getString());
        isAvailable = false;
        ModMessages.PlayerSendToServer(new MSGServerMoveEmpty(index,target));
    }
    public static void serverRemove(short index, boolean remove){
        //System.out.printf("[%d]MSGSVMoveEmpty:(%d,%s)", Game.gametime, index, remove);
        //System.out.printf("Client:{%s}\n",player.getInventory().getItem(index).getDisplayName().getString());
        isAvailable = false;
        ModMessages.PlayerSendToServer(new MSGServerRemoveItem(index,remove));
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event){
        LocalPlayer player = Minecraft.getInstance().player;
        if(player != null && event.side == LogicalSide.CLIENT && event.phase == Phase.END)
        {
            if(player.isCreative()){
                InventoryLock.unlockAll();
                playerPermission = Short.MAX_VALUE;
                return;
            }
            if(Game.isReloaded /*&& isAvailable*/){
                //1.检查背包格位 计算PermissionLevel
                //2.锁格子
                //3.扫描所有未上锁格子 检查非法位置
                //4.寻找合法的位置 并且移动
                //5.如果没有就丢弃物品（延迟）
                player.containerMenu.incrementStateId();
                player.containerMenu.broadcastChanges();
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
                                i += backpack.getPermissionLevel();
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
                        if ((!itemstack.isEmpty())
                                && (!InventoryLock.isLocked(slot1.index))
                                && (Tagger.GetItemTags(itemstack) != null)
                                && (!Tagger.GetItemTags(itemstack).contains(slottag)))
                        {
                            //Step4
                            boolean f = false;
                            for (String itemtag : Tagger.GetItemTags(itemstack)) {
                                if(f) break;
                                List<SlotPermissionLevel> slots2 = slots.get(itemtag);
                                if (slots2 == null)
                                    continue;
                                for (SlotPermissionLevel slot2 : slots2) {
                                    if(slot2.equals(slot1))
                                        continue;
                                    ItemStack i = inventory.getItem(slot2.index);
                                    if (i.isEmpty()) {
                                        serverMoveEmpty(slot1.index,slot2.index);
                                        f = true;
                                        break;
                                    }
                                }
                            }
                            //Step5
                            serverRemove(slot1.index,false);
                            return;//在高速更改物品栏时，有极小概率在背包内仍有空格的时候扔出物品 所以return
                        }
                    }
                }
            }
        }
    }
}
