package com.starpanda.createchaindeco.common.networking;

import com.starpanda.createchaindeco.client.networking.ClientPayloadHandler;
import com.starpanda.createchaindeco.server.networking.ServerPayloadHandler;
import com.starpanda.createchaindeco.common.networking.packets.C2SPlaceDeco;
import com.starpanda.createchaindeco.common.networking.packets.S2CUpdateDeco;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ChainDecoNetworking {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
            C2SPlaceDeco.TYPE,
            C2SPlaceDeco.CODEC,
            ServerPayloadHandler::handlePlaceDeco
        );

        registrar.playToClient(
            S2CUpdateDeco.TYPE,
            S2CUpdateDeco.CODEC,
            ClientPayloadHandler::handleUpdateDeco
        );
    }
}