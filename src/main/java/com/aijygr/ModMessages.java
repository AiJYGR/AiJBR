package com.aijygr;

import com.aijygr.AiJBP.*;
import com.aijygr.AiJBP.SyncConfigJSON.BP.MSGClientBPHash;
import com.aijygr.AiJBP.SyncConfigJSON.BP.MSGClientBPJSON;
import com.aijygr.AiJBP.SyncConfigJSON.BP.MSGServerRequestSyncBPJSON;
import com.aijygr.AiJBP.SyncConfigJSON.Tag.MSGClientTagJSON;
import com.aijygr.AiJBP.SyncConfigJSON.Tag.MSGClientTagHash;
import com.aijygr.AiJBP.SyncConfigJSON.Tag.MSGServerRequestSyncTagJSON;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {//GEMINI简直是我亲爹
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() { return packetId++; }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(ResourceLocation.fromNamespaceAndPath(Main.MODID, "main"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();
        INSTANCE = net;
        // 注册包
        net.messageBuilder(MSGClientTagJSON.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MSGClientTagJSON::new)
                .encoder(MSGClientTagJSON::encode)
                .consumerMainThread(MSGClientTagJSON::handle)
                .add();
        net.messageBuilder(MSGClientTagHash.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MSGClientTagHash::new)
                .encoder(MSGClientTagHash::encode)
                .consumerMainThread(MSGClientTagHash::handle)
                .add();
        net.messageBuilder(MSGServerRequestSyncTagJSON.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MSGServerRequestSyncTagJSON::new)
                .encoder(MSGServerRequestSyncTagJSON::encode)
                .consumerMainThread(MSGServerRequestSyncTagJSON::handle)
                .add();
        net.messageBuilder(MSGClientBPJSON.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MSGClientBPJSON::new)
                .encoder(MSGClientBPJSON::encode)
                .consumerMainThread(MSGClientBPJSON::handle)
                .add();
        net.messageBuilder(MSGClientBPHash.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MSGClientBPHash::new)
                .encoder(MSGClientBPHash::encode)
                .consumerMainThread(MSGClientBPHash::handle)
                .add();
        net.messageBuilder(MSGServerRequestSyncBPJSON.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MSGServerRequestSyncBPJSON::new)
                .encoder(MSGServerRequestSyncBPJSON::encode)
                .consumerMainThread(MSGServerRequestSyncBPJSON::handle)
                .add();
        net.messageBuilder(MSGServerLockInv.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MSGServerLockInv::new)
                .encoder(MSGServerLockInv::encode)
                .consumerMainThread(MSGServerLockInv::handle)
                .add();
        net.messageBuilder(MSGServerUnlockInv.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MSGServerUnlockInv::new)
                .encoder(MSGServerUnlockInv::encode)
                .consumerMainThread(MSGServerUnlockInv::handle)
                .add();
        net.messageBuilder(MSGServerSwapItem.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MSGServerSwapItem::new)
                .encoder(MSGServerSwapItem::encode)
                .consumerMainThread(MSGServerSwapItem::handle)
                .add();
        net.messageBuilder(MSGClientFinished.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MSGClientFinished::new)
                .encoder(MSGClientFinished::encode)
                .consumerMainThread(MSGClientFinished::handle)
                .add();
        net.messageBuilder(MSGServerRemoveItem.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MSGServerRemoveItem::new)
                .encoder(MSGServerRemoveItem::encode)
                .consumerMainThread(MSGServerRemoveItem::handle)
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