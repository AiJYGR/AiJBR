package com.aijygr.AiJBP;

import com.aijygr.Main;
import com.aijygr.Reg;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGServerUnlockInv {
    private Short value = -1;
    public MSGServerUnlockInv(Short value) { this.value = value; }
    public MSGServerUnlockInv(FriendlyByteBuf buf) {
        this.value = buf.readShort();
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeShort(this.value);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            Inventory inventory = null;
            if (player != null) {
                inventory = player.getInventory();
                ItemStack stack = inventory.getItem(value);
                if (!stack.isEmpty() && stack.is(Reg.LOCK.get())) {
                    inventory.setItem(value, ItemStack.EMPTY);
                    player.containerMenu.broadcastChanges();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}