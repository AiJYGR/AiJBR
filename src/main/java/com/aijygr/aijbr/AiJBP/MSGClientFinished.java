package com.aijygr.aijbr.AiJBP;

import com.aijygr.aijbr.Network.ClientPackageHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGClientFinished {
    public MSGClientFinished() {
    }
    public static MSGClientFinished decoder(FriendlyByteBuf buf) {
        return new MSGClientFinished();
    }
    public void encoder(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,() -> ()-> {
                ClientPackageHandler.MSGClientFinished();
            });
        });
        ctx.get().setPacketHandled(true);
    }
}