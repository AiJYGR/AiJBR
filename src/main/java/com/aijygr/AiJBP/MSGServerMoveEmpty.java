package com.aijygr.AiJBP;

import com.aijygr.ModMessages;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGServerMoveEmpty {
    private short index, target;

    public MSGServerMoveEmpty(short vaule1, short target) {
        this.index = vaule1;
        this.target = target;
    }
    public MSGServerMoveEmpty(FriendlyByteBuf buf) {
        this.index = buf.readShort();
        this.target = buf.readShort();
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeShort(this.index);
        buf.writeShort(this.target);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            Inventory inventory = player.getInventory();
            ItemStack item1 = player.getInventory().getItem(index).copy();
            ItemStack item2 = player.getInventory().getItem(target).copy();
            if(inventory.getItem(target).isEmpty())
            {
                inventory.setItem(index,item2.copy());
                inventory.setItem(target,item1.copy());
            }
            else if (ItemStack.isSameItemSameTags(item1, item2)) {
                int transfer = Math.min(item1.getCount(), item2.getMaxStackSize() - item2.getCount());
                if (transfer > 0) {
                    item2.grow(transfer);
                    item1.shrink(transfer);
                    inventory.setItem(target, item2);
                    inventory.setItem(index, item1.isEmpty() ? ItemStack.EMPTY : item1);
                } else {
                    System.out.println("REJECTED2");
                }
            }
            else{
                System.out.print("REJECTED");
            }
            ModMessages.ServerSendToPlayer(new MSGClientFinished(),ctx.get().getSender());
        });
        ctx.get().setPacketHandled(true);
    }
}