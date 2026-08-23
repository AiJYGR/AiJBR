package com.aijygr.aijbr.AiJBP;

import com.aijygr.aijbr.Network.ClientPackageHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGClientExecSync {
    public MSGClientExecSync() {
    }
    public static MSGClientExecSync decoder(FriendlyByteBuf buf) {
        return new MSGClientExecSync();
    }
    public void encoder(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ClientPackageHandler.MSGClientExecSync();
            });
        });
        ctx.get().setPacketHandled(true);
    }
}