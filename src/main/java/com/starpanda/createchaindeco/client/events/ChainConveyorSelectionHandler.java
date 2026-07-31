package com.starpanda.createchaindeco.client.events;

import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorInteractionHandler;
import com.starpanda.createchaindeco.CreateChainDeco;
import com.starpanda.createchaindeco.common.networking.packets.C2SPlaceDeco;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = CreateChainDeco.MODID, value = Dist.CLIENT)
public class ChainConveyorSelectionHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onClickInput(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || !mc.player.isHolding(Items.LANTERN)) {
            return;
        }

        if (ChainConveyorInteractionHandler.selectedLift == null) {
            return;
        }

        KeyMapping key = event.getKeyMapping();

        if (key == mc.options.keyUse) {
            BlockPos liftPos = ChainConveyorInteractionHandler.selectedLift;
            int linkIndex = Math.round(ChainConveyorInteractionHandler.selectedChainPosition);

            CreateChainDeco.LOGGER.info("Sending PlaceDeco - liftPos: {}, linkIndex: {}, chainPos: {}",
                liftPos, linkIndex, ChainConveyorInteractionHandler.selectedChainPosition);

            PacketDistributor.sendToServer(new C2SPlaceDeco(liftPos, linkIndex));
            event.setCanceled(true);
        }
    }
}