package com.aijygr.AiJBP;

import com.aijygr.Reg;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGServerLockInv {
    private Short value;
    public MSGServerLockInv(Short value) { this.value = value; }
    public MSGServerLockInv(FriendlyByteBuf buf) {
        this.value = buf.readShort();
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeShort(this.value);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            Inventory inventory;
            if (player != null) {
                inventory = player.getInventory();
                ItemStack stack = inventory.getItem(value);
                if(stack.isEmpty()){
                    inventory.setItem(value, new ItemStack(Reg.LOCK.get()));
                    player.containerMenu.broadcastChanges();
                }
                if (!stack.isEmpty() && !stack.is(Reg.LOCK.get())) {
                    player.drop(stack, false, true);
                    inventory.setItem(value, new ItemStack(Reg.LOCK.get()));
                    player.containerMenu.broadcastChanges();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}