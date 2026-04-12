package com.aijygr.AiJBP;

import com.aijygr.AiJGame.Game;
import com.aijygr.ModConfig;
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
public class Backpack extends Event
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
        public SlotPermissionLevel(){
            index = 0;
            permissionlevel = Short.MAX_VALUE;
        }
        public SlotPermissionLevel(short index,short permissionlevel){
            this.index = index;
            this.permissionlevel = permissionlevel;
        }
    }
    public static class SlotAttribute {
        SlotTag tag;
        SlotPermissionLevel slot =  new SlotPermissionLevel();
        public SlotAttribute(short index, short permissionLevel, SlotTag tag) {
            this.tag = tag;
            this.slot.permissionlevel = permissionLevel;
            this.slot.index = index;
        }
        public SlotAttribute(short index, short permissionLevel, String tag) {
            this.tag = SlotTag.valueOf(tag);
            this.slot.permissionlevel = permissionLevel;
            this.slot.index = index;
        }
        public SlotAttribute(short index, String tag) {
            this.tag = SlotTag.valueOf(tag);
            this.slot.index = index;
            this.slot.permissionlevel = Short.MAX_VALUE;
        }
        public SlotAttribute() {
            this.tag = SlotTag.DISABLE;
            this.slot.index = 0;
            this.slot.permissionlevel = Short.MAX_VALUE;
        }
        public String getTagName() {
            return tag.name();
        }
    }
    //public static List<SlotAttribute> backpack = new ArrayList<>();
    public static Map<SlotTag,List<SlotPermissionLevel>> backpack = new HashMap<>();
    //public static List<SlotAttribute> armor = new ArrayList<>();
    public static Map<SlotTag,List<SlotPermissionLevel>> armor = new HashMap<>();
    public static SlotAttribute offhand = new SlotAttribute();
    public static String s = "";
    public static Inventory inventory;
    public static short playerPermission = 0;



    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent event)
    {
        Player player = event.player;
        if(player != null && event.side == LogicalSide.CLIENT && event.phase == Phase.START)
        {
            if (Minecraft.getInstance().player == null || !player.getUUID().equals(Minecraft.getInstance().player.getUUID()))
                return;
            Inventory inventory = player.getInventory();
//            if(Game.gametime%200 == 0)
//            {
//                System.out.println("\n"+Game.gametime);
//                int i = 0;
//                List<String> tags = Tagger.GetItemTag(inventory.getItem(0));
//                if(tags != null)
//                {
//                    StringBuilder s = new StringBuilder();
//                    for(String tag : tags)
//                        s.append(tag).append(" ");
//                    System.out.println("TAG:"+s);
//                }
//                InventoryLock.lock(12);
//            }
//            if(Game.gametime%200 == 100)
//            {
//                InventoryLock.unlock(12);
//                if(Game.isReloaded){
//                    backpack.forEach((tag,slotPermissionLevel)->{
//                        for(SlotPermissionLevel it:  slotPermissionLevel)
//                            System.out.println(tag+" "+it.index+" "+it.permissionlevel);
//                    });
//                }
//            }
            if(Game.isReloaded/*&&Game.gametime%100==30*/){
                if(Game.gametime%30==0)// 防止如果不小心/clear @s
                    InventoryLock.unlockAll();
                //1.检查背包格位 计算permissionlevel
                //2.锁格子
                //3.扫描所有未上锁格子 检查非法位置
                //4.寻找合法的位置 并且移动 如果没有，丢弃物品（40tick延迟）
                List<SlotPermissionLevel> bp = backpack.get(SlotTag.BACKPACK);
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
                backpack.forEach((tag,slotPermissionLevel)->{
                    for(SlotPermissionLevel slot:  slotPermissionLevel){
                        if(slot.permissionlevel>playerPermission)
                            InventoryLock.lock(slot.index);
                        else
                            InventoryLock.unlock(slot.index);
                    }
                });
                //Step3
                backpack.forEach((slottag,slotPermissionLevel)->{
                    for(SlotPermissionLevel slot : slotPermissionLevel){
                        ItemStack itemstack = inventory.getItem(slot.index);
                        //检测非法物品
                        if(!itemstack.isEmpty()
                                &&!InventoryLock.isLocked(slot.index)
                                &&!Tagger.GetItemTags(itemstack).isEmpty()
                                &&!Tagger.GetItemTags(itemstack).contains(slottag.name())){
                            //寻找合法位置
                            for(String itemtag : Tagger.GetItemTags(itemstack)){
                                backpack.get(SlotTag.valueOf(itemtag));

                            }
                        }
                    }
                });



            }
        }
    }



}
