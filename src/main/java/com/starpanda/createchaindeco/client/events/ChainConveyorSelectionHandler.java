package com.starpanda.createchaindeco.client.events;

import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorInteractionHandler;
import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorShape;
import com.starpanda.createchaindeco.CreateChainDeco;
import com.starpanda.createchaindeco.common.networking.packets.C2SPlaceDeco;

import net.createmod.catnip.outliner.Outliner;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = CreateChainDeco.MODID, value = Dist.CLIENT)
public class ChainConveyorSelectionHandler {

	private static final String SELECTION_A_KEY = "ChainDecoSelectionA";
	private static final String SELECTION_B_KEY = "ChainDecoSelectionB";

	private static BlockPos selectionALift;
	private static float selectionAChainPosition;
	private static BlockPos selectionAConnection;
	private static Vec3 selectionABakedPosition;

	private static BlockPos selectionBLift;
	private static float selectionBChainPosition;
	private static BlockPos selectionBConnection;
	private static Vec3 selectionBBakedPosition;

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		renderStoredSelections();
	}

	private static boolean isActive() {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null)
			return false;
		return mc.player.isHolding(Items.LANTERN);
	}

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
			CreateChainDeco.LOGGER.info("Sending PlaceDeco packet - liftPos: {}, linkIndex: {}, selectedChainPosition: {}", 
    		liftPos, linkIndex, ChainConveyorInteractionHandler.selectedChainPosition);
			PacketDistributor.sendToServer(new C2SPlaceDeco(liftPos, linkIndex, mc.player.getMainHandItem().copy()));
			advanceSelection();
			event.setCanceled(true);
		} else if (key == mc.options.keyAttack) {
			advanceSelection();
			event.setCanceled(true);
		}
	}

	private static void advanceSelection() {
		BlockPos lift = ChainConveyorInteractionHandler.selectedLift;
		float chainPos = ChainConveyorInteractionHandler.selectedChainPosition;
		BlockPos conn = ChainConveyorInteractionHandler.selectedConnection;
		Vec3 baked = ChainConveyorInteractionHandler.selectedBakedPosition;

		if (selectionALift == null) {
			selectionALift = lift != null ? lift.immutable() : null;
			selectionAChainPosition = chainPos;
			selectionAConnection = conn != null ? conn.immutable() : null;
			selectionABakedPosition = baked != null ? baked : null;
		} else if (selectionBLift == null) {
			selectionBLift = lift != null ? lift.immutable() : null;
			selectionBChainPosition = chainPos;
			selectionBConnection = conn != null ? conn.immutable() : null;
			selectionBBakedPosition = baked != null ? baked : null;
		} else {
			selectionALift = null;
			selectionAChainPosition = 0f;
			selectionAConnection = null;
			selectionABakedPosition = null;
			selectionBLift = null;
			selectionBChainPosition = 0f;
			selectionBConnection = null;
			selectionBBakedPosition = null;
		}
	}

	private static void renderStoredSelections() {
		if (!isActive()) {
			Outliner.getInstance().keep(SELECTION_A_KEY);
			Outliner.getInstance().keep(SELECTION_B_KEY);
			return;
		}

		if (selectionABakedPosition != null) {
			Outliner.getInstance()
				.chaseAABB(SELECTION_A_KEY, new AABB(selectionABakedPosition, selectionABakedPosition))
				.colored(new Color(0xFF_6CB2FF))
				.lineWidth(1 / 6f)
				.disableLineNormals();
		} else {
			Outliner.getInstance().keep(SELECTION_A_KEY);
		}

		if (selectionBBakedPosition != null) {
			Outliner.getInstance()
				.chaseAABB(SELECTION_B_KEY, new AABB(selectionBBakedPosition, selectionBBakedPosition))
				.colored(new Color(0xFF_FF6CAB))
				.lineWidth(1 / 6f)
				.disableLineNormals();
		} else {
			Outliner.getInstance().keep(SELECTION_B_KEY);
		}
	}

	public static Vec3 getSnapIndicatorPosition() {
		return ChainConveyorInteractionHandler.selectedBakedPosition;
	}

	public static BlockPos getHoveredLift() {
		return ChainConveyorInteractionHandler.selectedLift;
	}

	public static BlockPos getHoveredConnection() {
		return ChainConveyorInteractionHandler.selectedConnection;
	}

	public static float getHoveredChainPosition() {
		return ChainConveyorInteractionHandler.selectedChainPosition;
	}

	public static ChainConveyorShape getHoveredShape() {
		return ChainConveyorInteractionHandler.selectedShape;
	}

	public static BlockPos getSelectionALift() {
		return selectionALift;
	}

	public static BlockPos getSelectionAConnection() {
		return selectionAConnection;
	}

	public static float getSelectionAChainPosition() {
		return selectionAChainPosition;
	}

	public static BlockPos getSelectionBLift() {
		return selectionBLift;
	}

	public static BlockPos getSelectionBConnection() {
		return selectionBConnection;
	}

	public static float getSelectionBChainPosition() {
		return selectionBChainPosition;
	}

	public static void clearSelections() {
		selectionALift = null;
		selectionAChainPosition = 0f;
		selectionAConnection = null;
		selectionABakedPosition = null;
		selectionBLift = null;
		selectionBChainPosition = 0f;
		selectionBConnection = null;
		selectionBBakedPosition = null;
	}
}
