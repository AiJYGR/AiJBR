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
    public enum SlotTag{
        MAINWPN,
        SUBWPN,
        MELEE,
        //NADES,
        SUPPLIES,
        BACKPACK,
        ARMOR,
        DISABLE,
        //DEFAULT
    }
    public enum SlotType{
        BACKPACK,
        ARMOR,
        OFFHAND
    }
    public static class SlotPermissionLevel{
        short index = 0;
        short permissionlevel = 0;
        SlotType type = SlotType.BACKPACK;
        public SlotPermissionLevel(){
            index = 0;
            permissionlevel = Short.MAX_VALUE;
            type = SlotType.BACKPACK;
        }
        public SlotPermissionLevel(short index,short permissionlevel,SlotType type){
            this.index = index;
            this.permissionlevel = permissionlevel;
            this.type = type;
        }
    }

    //public static List<SlotAttribute> backpack = new ArrayList<>();
    //public static Map<SlotTag,List<SlotPermissionLevel>> backpack = new HashMap<>();
    //public static List<SlotAttribute> armor = new ArrayList<>();
    //public static Map<SlotTag,List<SlotPermissionLevel>> armor = new HashMap<>();
    public static Map<SlotTag,List<SlotPermissionLevel>> slots = new HashMap<>();
    public static String s = "";
    public static Inventory inventory;
    public static short playerPermission = 0;



    private static boolean isAvaliable = true;
    public static void setAvaliable(){
        isAvaliable = true;
    }
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent event)
    {
        Player player = event.player;
        if(player != null && event.side == LogicalSide.CLIENT && event.phase == Phase.START)
        {
            if (Minecraft.getInstance().player == null || !player.getUUID().equals(Minecraft.getInstance().player.getUUID()))
                return;
            Inventory inventory = player.getInventory();
            if(Game.gametime%50==0)// 防止如果不小心/clear @s
            {
                InventoryLock.unlockAll();
                setAvaliable();
            }
            if(Game.isReloaded && isAvaliable){
                //1.检查背包格位 计算permissionlevel
                //2.锁格子
                //3.扫描所有未上锁格子 检查非法位置
                //4.寻找合法的位置 并且移动 如果没有，丢弃物品（40tick延迟）
                List<SlotPermissionLevel> bp = slots.get(SlotTag.BACKPACK);
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
                slots.forEach((tag,slotPermissionLevel)->{
                    for(SlotPermissionLevel slot:  slotPermissionLevel){
                        if(slot.permissionlevel>playerPermission)
                            InventoryLock.lock(slot.index);
                        else
                            InventoryLock.unlock(slot.index);
                    }
                });
                //Step3
                // Step3: 扫描所有未上锁格子 检查非法位置
                for (Map.Entry<SlotTag, List<SlotPermissionLevel>> entry : slots.entrySet()) {
                    SlotTag slottag = entry.getKey();
                    List<SlotPermissionLevel> slotPermissionLevels = entry.getValue();

                    for (SlotPermissionLevel slot : slotPermissionLevels) {
                        ItemStack itemstack = switch (slot.type) {
                            case BACKPACK -> inventory.items.get(slot.index);
                            case ARMOR -> inventory.armor.get(slot.index);
                            case OFFHAND -> inventory.offhand.get(0);
                        };

                        // 检测非法物品
                        if (!itemstack.isEmpty()
                                && !InventoryLock.isLocked(slot.index)
                                && !Tagger.GetItemTags(itemstack).isEmpty()
                                && !Tagger.GetItemTags(itemstack).contains(slottag.name())) {

                            System.out.println(slot.index + " illegal");
                            for (String itemtag : Tagger.GetItemTags(itemstack)) {
                                SlotTag targetTagType= SlotTag.valueOf(itemtag);
                                List<SlotPermissionLevel> slots2 = slots.get(targetTagType);
                                if (slots2 == null) continue;

                                for (SlotPermissionLevel s : slots2) {
                                    ItemStack i = switch (s.type) {
                                        case BACKPACK -> inventory.items.get(s.index);
                                        case ARMOR -> inventory.armor.get(s.index);
                                        case OFFHAND -> inventory.offhand.get(0);
                                    };

                                    if (i.isEmpty()) {
                                        System.out.println(String.format("%dMSG:(%s,%d,%s,%d)", Game.gametime, s.type.name(), s.index, slot.type.name(), slot.index));
                                        ModMessages.PlayerSendToServer(new MSGServerSwapItem(s.type, s.index, slot.type, slot.index));
                                        isAvaliable = false;
                                        inventory.setItem(slot.index, ItemStack.EMPTY);///???
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }



}
