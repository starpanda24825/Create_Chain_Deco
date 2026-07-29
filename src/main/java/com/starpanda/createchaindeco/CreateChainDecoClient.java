package com.starpanda.createchaindeco;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.starpanda.createchaindeco.client.events.ChainConveyorSelectionHandler;
import com.starpanda.createchaindeco.client.renderer.ChainDecoRenderer;
import com.starpanda.createchaindeco.config.Config;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(value = CreateChainDeco.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = CreateChainDeco.MODID, value = Dist.CLIENT)
public class CreateChainDecoClient {
	public CreateChainDecoClient(ModContainer container) {
		container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		NeoForge.EVENT_BUS.register(ChainConveyorSelectionHandler.class);
		NeoForge.EVENT_BUS.register(ChainDecoRenderer.class);
	}

	@SubscribeEvent
	static void onClientSetup(FMLClientSetupEvent event) {
	}
}
