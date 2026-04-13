package com.aijygr.AiJBP;

import com.aijygr.ModMessages;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGServerRemoveItem {
    private short index;
    private boolean remove;

    public MSGServerRemoveItem(short index, boolean remove) {
        this.index = index;
        this.remove = remove;
    }
    public MSGServerRemoveItem(FriendlyByteBuf buf) {
        this.index = buf.readShort();
        this.remove = buf.readBoolean();
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeShort(index);
        buf.writeBoolean(remove);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            Inventory inventory = player.getInventory();
            if(!remove){
                player.drop(inventory.getItem(index),false);
            }
            inventory.setItem(index,ItemStack.EMPTY);
            player.containerMenu.broadcastChanges();
            ModMessages.ServerSendToPlayer(new MSGClientFinished(),ctx.get().getSender());
        });
        ctx.get().setPacketHandled(true);
    }
}