package com.aijygr.aijbr.Network;

import com.aijygr.aijbr.AiJBP.*;
import com.aijygr.aijbr.AiJBP.SyncConfigJSON.BP.MSGClientBPHash;
import com.aijygr.aijbr.AiJBP.SyncConfigJSON.BP.MSGClientBPJSON;
import com.aijygr.aijbr.AiJBP.SyncConfigJSON.BP.MSGServerRequestSyncBPJSON;
import com.aijygr.aijbr.AiJBP.SyncConfigJSON.Tag.MSGClientTagJSON;
import com.aijygr.aijbr.AiJBP.SyncConfigJSON.Tag.MSGClientTagHash;
import com.aijygr.aijbr.AiJBP.SyncConfigJSON.Tag.MSGServerRequestSyncTagJSON;
import com.aijygr.aijbr.AiJGame.Client.MSGClientGameTime;
import com.aijygr.aijbr.AiJGame.Client.MSGClientPlayerInfo;
import com.aijygr.aijbr.AiJGame.Client.MSGClientRingInfo;
import com.aijygr.aijbr.Main;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {//GEMINI简直是我亲爹
    private static SimpleChannel INSTANCE;
    private static final String VERSION = "1.0";
    private static int packetId = 0;
    private static int id() { return packetId++; }

    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(ResourceLocation.fromNamespaceAndPath(Main.MODID, "main"))
                .networkProtocolVersion(() -> VERSION)
                .clientAcceptedVersions(s -> s.equals(VERSION))
                .serverAcceptedVersions(s -> s.equals(VERSION))
                .simpleChannel();
        // 注册包
        INSTANCE.messageBuilder(MSGClientTagJSON.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MSGClientTagJSON::decoder)
                .encoder(MSGClientTagJSON::encoder)
                .consumerMainThread(MSGClientTagJSON::handle)
                .add();
        INSTANCE.messageBuilder(MSGClientTagHash.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MSGClientTagHash::decoder)
                .encoder(MSGClientTagHash::encoder)
                .consumerMainThread(MSGClientTagHash::handle)
                .add();
        INSTANCE.messageBuilder(MSGServerRequestSyncTagJSON.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MSGServerRequestSyncTagJSON::decoder)
                .encoder(MSGServerRequestSyncTagJSON::encoder)
                .consumerMainThread(MSGServerRequestSyncTagJSON::handle)
                .add();
        INSTANCE.messageBuilder(MSGClientBPJSON.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MSGClientBPJSON::decoder)
                .encoder(MSGClientBPJSON::encoder)
                .consumerMainThread(MSGClientBPJSON::handle)
                .add();
        INSTANCE.messageBuilder(MSGClientBPHash.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MSGClientBPHash::decoder)
                .encoder(MSGClientBPHash::encoder)
                .consumerMainThread(MSGClientBPHash::handle)
                .add();
        INSTANCE.messageBuilder(MSGServerRequestSyncBPJSON.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MSGServerRequestSyncBPJSON::decoder)
                .encoder(MSGServerRequestSyncBPJSON::encoder)
                .consumerMainThread(MSGServerRequestSyncBPJSON::handle)
                .add();
        INSTANCE.messageBuilder(MSGServerLockInv.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MSGServerLockInv::decoder)
                .encoder(MSGServerLockInv::encoder)
                .consumerMainThread(MSGServerLockInv::handle)
                .add();
        INSTANCE.messageBuilder(MSGServerUnlockInv.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MSGServerUnlockInv::decoder)
                .encoder(MSGServerUnlockInv::encoder)
                .consumerMainThread(MSGServerUnlockInv::handle)
                .add();
        INSTANCE.messageBuilder(MSGServerSwapItem.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MSGServerSwapItem::decoder)
                .encoder(MSGServerSwapItem::encoder)
                .consumerMainThread(MSGServerSwapItem::handle)
                .add();
        INSTANCE.messageBuilder(MSGClientFinished.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MSGClientFinished::decoder)
                .encoder(MSGClientFinished::encoder)
                .consumerMainThread(MSGClientFinished::handle)
                .add();
        INSTANCE.messageBuilder(MSGServerRemoveItem.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MSGServerRemoveItem::decoder)
                .encoder(MSGServerRemoveItem::encoder)
                .consumerMainThread(MSGServerRemoveItem::handle)
                .add();
        INSTANCE.messageBuilder(MSGServerMoveEmpty.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MSGServerMoveEmpty::decoder)
                .encoder(MSGServerMoveEmpty::encoder)
                .consumerMainThread(MSGServerMoveEmpty::handle)
                .add();
        INSTANCE.messageBuilder(MSGClientRingInfo.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MSGClientRingInfo::decoder)
                .encoder(MSGClientRingInfo::encoder)
                .consumerMainThread(MSGClientRingInfo::handle)
                .add();
        INSTANCE.messageBuilder(MSGClientPlayerInfo.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MSGClientPlayerInfo::decoder)
                .encoder(MSGClientPlayerInfo::encoder)
                .consumerMainThread(MSGClientPlayerInfo::handle)
                .add();
        INSTANCE.messageBuilder(MSGClientGameTime.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MSGClientGameTime::decoder)
                .encoder(MSGClientGameTime::encoder)
                .consumerMainThread(MSGClientGameTime::handle)
                .add();
        INSTANCE.messageBuilder(MSGClientExecSync.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MSGClientExecSync::decoder)
                .encoder(MSGClientExecSync::encoder)
                .consumerMainThread(MSGClientExecSync::handle)
                .add();
    }
    public static <MSG> void ServerSendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
    public static <MSG> void ServerSendToAll(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
    public static <MSG> void PlayerSendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

}