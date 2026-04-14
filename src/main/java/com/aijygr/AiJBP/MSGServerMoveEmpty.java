package com.aijygr.AiJBP;

import com.aijygr.Main;
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
            if(inventory.getItem(target).isEmpty())
            {
                inventory.setItem(target,inventory.getItem(index).copy());
                inventory.setItem(index,ItemStack.EMPTY.copy());
                player.containerMenu.broadcastChanges();
            }
            else{
                System.out.println("REJECTED");
                //player.containerMenu.sendAllDataToRemote();
            }
            //player.containerMenu.sendAllDataToRemote();

            ModMessages.ServerSendToPlayer(new MSGClientFinished(),ctx.get().getSender());
        });
        ctx.get().setPacketHandled(true);
    }
}