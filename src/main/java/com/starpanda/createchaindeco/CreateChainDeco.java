package com.starpanda.createchaindeco;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.starpanda.createchaindeco.config.Config;
import com.starpanda.createchaindeco.network.SelectConveyorPositionPacket;
import com.starpanda.createchaindeco.server.networking.SelectConveyorPositionPacketHandler;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.connection.ConnectionProtocol;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PacketFlow;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(CreateChainDeco.MODID)
public class CreateChainDeco {
    public static final String MODID = "createchaindeco";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public CreateChainDeco(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerPayloads);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    private void registerPayloads(final RegisterPayloadHandlersEvent event) {
        event.registrar(MODID)
            .register(SelectConveyorPositionPacket.TYPE,
                      SelectConveyorPositionPacket.CODEC,
                      SelectConveyorPositionPacketHandler::handle);
    }
}
