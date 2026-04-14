package com.aijygr.AiJBP;

import com.aijygr.ModMessages;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@Deprecated
public class MSGServerSwapItem {
    private short value1,value2;

    public MSGServerSwapItem(short vaule1, short value2) {
        this.value1 = vaule1;
        this.value2 = value2;
    }
    public MSGServerSwapItem(FriendlyByteBuf buf) {
        this.value1 = buf.readShort();
        this.value2 = buf.readShort();
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeShort(this.value1);
        buf.writeShort(this.value2);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            Inventory inventory = player.getInventory();
            ItemStack item1 = inventory.getItem(value1).copy();
            ItemStack item2 = inventory.getItem(value2).copy();
            inventory.setItem(value1,item2.copy());
            inventory.setItem(value2,item1.copy());
            ModMessages.ServerSendToPlayer(new MSGClientFinished(),ctx.get().getSender());
        });
        ctx.get().setPacketHandled(true);
    }
}