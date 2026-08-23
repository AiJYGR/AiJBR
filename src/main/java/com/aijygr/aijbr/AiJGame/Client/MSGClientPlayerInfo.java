package com.aijygr.aijbr.AiJGame.Client;

import com.aijygr.aijbr.Network.ClientPackageHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MSGClientPlayerInfo {
    private final int players;
    private final int teams;

    public MSGClientPlayerInfo(int players, int teams) {
        this.players = players;
        this.teams = teams;
    }
    public static MSGClientPlayerInfo decoder(FriendlyByteBuf buf) {
        return new MSGClientPlayerInfo(buf.readInt(),buf.readInt());
    }
    public void encoder(FriendlyByteBuf buf) {
        buf.writeInt(players);
        buf.writeInt(teams);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ClientPackageHandler.MSGClientPlayerInfo(players, teams);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}