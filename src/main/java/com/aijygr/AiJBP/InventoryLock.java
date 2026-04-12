package com.aijygr.AiJBP;

import com.aijygr.Item.Lock;
import com.aijygr.ModMessages;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class InventoryLock {
    private static List<Short> locks = new ArrayList<>();
    public static boolean isLocked(short i){
        return locks.contains(i);
    }
    public static void lock(short i){
        if(isLocked(i)){
            return;
        }
        locks.add(i);
        ModMessages.PlayerSendToServer(new MSGServerLockInv(i));
    }
    public static void lock(int i){
        lock((short)i);
    }
    public static void unlock(short i){
        if(isLocked(i)){
            locks.remove(Short.valueOf(i));
            ModMessages.PlayerSendToServer(new MSGServerUnlockInv(i));
        }
    }
    public static void unlock(int i){
        unlock((short)i);
    }
    public static void unlockAll(){
        for(short i : locks){
            ModMessages.PlayerSendToServer(new MSGServerUnlockInv(i));
        }
        locks.clear();
    }

    /*  Disable the interacts   */
    @SubscribeEvent
    public static void onSlotClick(ScreenEvent.MouseButtonPressed.Pre event) {
        if(event.getScreen() instanceof AbstractContainerScreen<?> screen){
            Slot slot = screen.getSlotUnderMouse();
            if (slot != null && slot.container instanceof Inventory) {
                //System.out.println("Presse:"+slot.getSlotIndex());
                if(isLocked((short) slot.getSlotIndex()))
                    event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public static void onSlotRelease(ScreenEvent.MouseButtonReleased event) {
        if(event.getScreen() instanceof AbstractContainerScreen<?> screen){
            Slot slot = screen.getSlotUnderMouse();
            if (slot != null && slot.container instanceof Inventory) {
                if(isLocked((short) slot.getSlotIndex()))
                    event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public static void onSlotDrag(ScreenEvent.MouseDragged.Pre event){
        if(event.getScreen() instanceof AbstractContainerScreen<?> screen){
            Slot slot = screen.getSlotUnderMouse();
            if (slot != null && slot.container instanceof Inventory) {
                if(isLocked((short) slot.getSlotIndex()))
                    event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public static void onSlotKey(ScreenEvent.KeyPressed.Pre event){
        if(event.getScreen() instanceof AbstractContainerScreen<?> screen){
            if(event.getKeyCode()==GLFW_KEY_ESCAPE)
                return;
            Slot slot = screen.getSlotUnderMouse();
            if (slot != null && slot.container instanceof Inventory) {
                if(isLocked((short) slot.getSlotIndex()))
                    event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack itemstack = event.getItemStack();
        if (!itemstack.isEmpty() && itemstack.getItem() instanceof Lock) {
            event.getToolTip().clear();
        }
    }
}
