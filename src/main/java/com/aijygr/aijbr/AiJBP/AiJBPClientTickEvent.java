package com.aijygr.aijbr.AiJBP;

import com.aijygr.aijbr.AiJGame.Client.ClientGame;
import com.aijygr.aijbr.Item.Backpack;
import com.aijygr.aijbr.Item.Lock;
import com.aijygr.aijbr.Main;
import com.aijygr.aijbr.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;

import static com.aijygr.aijbr.AiJBP.AiJBackpack.*;


@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class AiJBPClientTickEvent {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event){////////////////////

        if(event.side.isServer())
            return;
        if(Minecraft.getInstance().isPaused())
            return;
        LocalPlayer player = Minecraft.getInstance().player;
        if(player != null && event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.END)
        {
            if((!player.isCreative()) && ClientGame.isBPSynced && ClientGame.isTagSynced /*&& isAvailable*/){
                //1.检查背包格位 计算PermissionLevel
                //2.锁格子
                //3.扫描所有未上锁格子 检查非法位置
                //4.寻找合法的位置 并且移动
                //5.如果没有就丢弃物品（延迟）
                //Step1
                List<SlotwithPermissionLevel> bp = slots.get("BACKPACK");
                Inventory inventory = player.getInventory();
                if(bp != null){
                    short i = ModConfig.Server.Config.ITEM.BACKPACK_DEFAULT_PERMISSIONLEVEL.get().shortValue();
                    for(SlotwithPermissionLevel it : bp){
                        ItemStack itemstack = inventory.getItem(it.index);
                        if(!itemstack.isEmpty() && itemstack.getItem() instanceof Backpack backpack){
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
                    playerPermission = ModConfig.Server.Config.ITEM.BACKPACK_DEFAULT_PERMISSIONLEVEL.get().shortValue();
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
                                                serverSwapItem(slot1.index, slot2.index);
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
            else
            {
                InventoryLock.unlockAll();
                playerPermission = Short.MAX_VALUE;

//                Inventory inventory = player.getInventory();
//                for(int i=0;i<inventory.getContainerSize();i++){
//                    if(inventory.getItem(i).getItem() instanceof Lock){
//                        serverRemove((short) i,true);
//                    }
//                }
                return;
            }
        }
    }
}
