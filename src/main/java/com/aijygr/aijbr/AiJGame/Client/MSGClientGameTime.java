package com.aijygr.aijbr.AiJGame.Client;

import com.aijygr.aijbr.Network.ClientPackageHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGClientGameTime {
    private final int round;
    private final int roundtick;
    private final boolean isShrinking;

    public MSGClientGameTime(int round, int roundtick, boolean isShrinking) {
        this.round = round;
        this.roundtick = roundtick;
        this.isShrinking = isShrinking;
    }
    public static MSGClientGameTime decoder(FriendlyByteBuf buf) {
        return new MSGClientGameTime(buf.readInt(),buf.readInt(),buf.readBoolean());
    }
    public void encoder(FriendlyByteBuf buf) {
        buf.writeInt(round);
        buf.writeInt(roundtick);
        buf.writeBoolean(isShrinking);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ClientPackageHandler.MSGClientGameTime(this.round,this.roundtick,this.isShrinking);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}