package com.aijygr.AiJBP;

import com.aijygr.AiJGame.Game;
import com.aijygr.Main;
import com.aijygr.ModMessages;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGServerSwapItem {
    private short value1,value2;
    private AiJBackpack.SlotType type1,type2;
    public MSGServerSwapItem(AiJBackpack.SlotType type1, short vaule1, AiJBackpack.SlotType type2, short value2) {
        this.type1 = type1;
        this.value1 = vaule1;
        this.type2 = type2;
        this.value2 = value2;
    }
    public MSGServerSwapItem(FriendlyByteBuf buf) {
        this.type1 = buf.readEnum(AiJBackpack.SlotType.class);
        this.value1 = buf.readShort();
        this.type2 = buf.readEnum(AiJBackpack.SlotType.class);
        this.value2 = buf.readShort();
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(this.type1);
        buf.writeShort(this.value1);
        buf.writeEnum(this.type2);
        buf.writeShort(this.value2);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            Inventory inventory = player.getInventory();
            ItemStack stack1,stack2;
            System.out.printf("%dReceived:(%s,%d,%s,%d)%n", Game.gametime,this.type1.name(),this.value1,this.type2.name(),this.value2);
            try{
                stack1 = switch (this.type1) {
                    case BACKPACK -> inventory.items.get(value1);
                    case ARMOR -> inventory.armor.get(value1);
                    case OFFHAND -> inventory.player.getOffhandItem();
                };
                stack2 = switch (this.type2){
                    case BACKPACK -> inventory.items.get(value2);
                    case ARMOR -> inventory.armor.get(value2);
                    case OFFHAND -> inventory.player.getOffhandItem();
                };
                ItemStack temp = stack1.copy();
                switch (this.type1) {
                    case BACKPACK -> inventory.items.set(value1, stack2);
                    case ARMOR -> inventory.armor.set(value1, stack2);
                    case OFFHAND -> inventory.offhand.set(0, stack2);
                }
                switch (this.type2) {
                    case BACKPACK -> inventory.items.set(value2, temp);
                    case ARMOR -> inventory.armor.set(value2, temp);
                    case OFFHAND -> inventory.offhand.set(0, temp);
                }
                ModMessages.ServerSendToPlayer(new MSGClientFinished(),ctx.get().getSender());

            }catch(Exception e){
                Main.LOGGER.warn(e.getMessage());
            }
            System.out.println("SUCCESS");
            player.containerMenu.broadcastChanges();
        });
        ctx.get().setPacketHandled(true);
    }
}