package com.aijygr.AiJBP;

import com.aijygr.Main;
import com.aijygr.ModMessages;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.aijygr.AiJBP.AiJBackpack.inventoryToSlotId;

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
            player.containerMenu.incrementStateId();
            System.out.printf("Server:{%s,%s}->",inventory.getItem(index).getDisplayName().getString(),inventory.getItem(target).getDisplayName().getString());
            if(inventory.getItem(target).isEmpty())
            {
                ItemStack item1 = player.getInventory().getItem(index).copy();
                ItemStack item2 = player.getInventory().getItem(target).copy();
                inventory.setItem(index,item2.copy());
                inventory.setItem(target,item1.copy());
                player.containerMenu.broadcastChanges();
            }
            else{
                System.out.print("REJECTED");
                //player.drop(inventory.getItem(index).copy(),false);
                //inventory.setItem(index,ItemStack.EMPTY.copy());
                player.containerMenu.sendAllDataToRemote();
            }
            System.out.printf("{%s,%s}\n",inventory.getItem(index).getDisplayName().getString(),inventory.getItem(target).getDisplayName().getString());
            ModMessages.ServerSendToPlayer(new MSGClientFinished(),ctx.get().getSender());
        });
        ctx.get().setPacketHandled(true);
    }
}