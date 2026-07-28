package com.starpanda.createchaindeco;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = CreateChainDeco.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = CreateChainDeco.MODID, value = Dist.CLIENT)
public class CreateChainDecoClient {
	public CreateChainDecoClient(ModContainer container) {
		container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		NeoForge.EVENT_BUS.register(ChainConveyorSelectionHandler.class);
	}

	@SubscribeEvent
	static void onClientSetup(FMLClientSetupEvent event) {
	}
}
