package com.aijygr.AiJBP;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class InventoryLock {
    public static List<Integer> locks = new ArrayList<>();
    public static boolean hasLocked(int i){
        return locks.contains(i);
    }
    public static void lock(int i){
        locks.add(i);
    }
    public static void unlock(int i){
        if(hasLocked(i)){
            locks.remove(i);
        }
    }



    @SubscribeEvent
    public static void onSlotClick(ScreenEvent.MouseButtonPressed.Pre event) {
        if(event.getScreen() instanceof AbstractContainerScreen<?> screen){
            Slot slot = screen.getSlotUnderMouse();
            if (slot != null && slot.container instanceof Inventory) {
                System.out.println("Press Inv"+slot.getSlotIndex());

                if(slot.getSlotIndex()==9)
                    event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public static void onSlotReleased(ScreenEvent.MouseButtonReleased event) {
        if(event.getScreen() instanceof AbstractContainerScreen<?> screen){
            Slot slot = screen.getSlotUnderMouse();
            if (slot != null && slot.container instanceof Inventory) {
                System.out.println("Release"+slot.getSlotIndex());
                if(slot.getSlotIndex()==9)
                    event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public static void onSlotDrag(ScreenEvent.MouseDragged.Pre event){
        if(event.getScreen() instanceof AbstractContainerScreen<?> screen){
            Slot slot = screen.getSlotUnderMouse();
            if (slot != null && slot.container instanceof Inventory) {
                System.out.println("Drag:"+slot.getSlotIndex());
                if(slot.getSlotIndex()==9)
                    event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public static void onSlotKey(ScreenEvent.KeyPressed.Pre event){
        if(event.getScreen() instanceof AbstractContainerScreen<?> screen){
            Slot slot = screen.getSlotUnderMouse();
            if (slot != null && slot.container instanceof Inventory) {
                System.out.println("Key"+slot.getSlotIndex());
                if(slot.getSlotIndex()==9)
                    event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public static void onSlotClick(ContainerScreenEvent event){

    }
}
