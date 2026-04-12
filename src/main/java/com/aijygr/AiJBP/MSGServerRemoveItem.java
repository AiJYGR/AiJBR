package com.aijygr.AiJBP;

import com.aijygr.ModMessages;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGServerRemoveItem {
    private AiJBackpack.SlotType type;
    private short index;
    private boolean remove;

    public MSGServerRemoveItem(AiJBackpack.SlotType type, short index, boolean remove) {
        this.type = type;
        this.index = index;
        this.remove = remove;
    }
    public MSGServerRemoveItem(FriendlyByteBuf buf) {
        this.type = buf.readEnum(AiJBackpack.SlotType.class);
        this.index = buf.readShort();
        this.remove = buf.readBoolean();
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(type);
        buf.writeShort(index);
        buf.writeBoolean(remove);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            Inventory inventory = player.getInventory();
            switch (type) {
                case BACKPACK:
                    if(!remove)
                        player.drop(inventory.items.get(index), false);
                    inventory.items.set(index, ItemStack.EMPTY);
                    break;
                case ARMOR:
                    if(!remove)
                        player.drop(inventory.armor.get(index), false);
                    inventory.armor.set(index, ItemStack.EMPTY);
                    break;
                case OFFHAND:
                    if(!remove)
                        player.drop(inventory.offhand.get(index), false);
                    inventory.offhand.set(index, ItemStack.EMPTY);
                    break;
            }
            player.containerMenu.broadcastChanges();
            ModMessages.ServerSendToPlayer(new MSGClientFinished(),ctx.get().getSender());
        });
        ctx.get().setPacketHandled(true);
    }
}