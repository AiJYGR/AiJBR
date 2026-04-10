package com.aijygr;

import com.aijygr.AiJTAG.MSGClientTagJSON;
import com.aijygr.AiJTAG.MSGClientTagHash;
import com.aijygr.AiJTAG.MSGServerRequestSyncTagJSON;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {//呜呜好难 GEMINI救救我QwQ
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

        // 注册 SyncTag 包
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