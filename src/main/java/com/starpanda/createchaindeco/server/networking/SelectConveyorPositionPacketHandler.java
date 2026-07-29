package com.starpanda.createchaindeco.server.networking;

import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorBlockEntity;
import com.starpanda.createchaindeco.common.ChainDecoAttachmentHelper;
import com.starpanda.createchaindeco.common.ChainDecoLightHelper;
import com.starpanda.createchaindeco.common.ChainLinkPositionHelper;
import com.starpanda.createchaindeco.network.SelectConveyorPositionPacket;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SelectConveyorPositionPacketHandler {

	private SelectConveyorPositionPacketHandler() {
	}

	public static void handle(SelectConveyorPositionPacket payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			Player player = context.player();
			if (!(player instanceof ServerPlayer serverPlayer)) {
				return;
			}

			BlockPos liftPos = payload.conveyorPos();
			if (liftPos == null) {
				return;
			}

			double distSqr = serverPlayer.distanceToSqr(Vec3.atCenterOf(liftPos));
			if (distSqr > 64.0D) {
				return;
			}

			Level level = serverPlayer.level();
			BlockEntity rawBe = level.getBlockEntity(liftPos);
			if (!(rawBe instanceof ChainConveyorBlockEntity be)) {
				return;
			}

			ItemStack stack = serverPlayer.getMainHandItem();

			ChainDecoAttachmentHelper.setDecoration(be, payload.linkIndex(), stack);

			Vec3 worldPos = ChainLinkPositionHelper.getLinkPosition(level, liftPos, payload.linkIndex());
			if (worldPos != null) {
				ChainDecoLightHelper.placeLight(level, be, payload.linkIndex(), worldPos);
			}

			be.setChanged();
			level.sendBlockUpdated(liftPos, be.getBlockState(), be.getBlockState(), 3);
		});
	}
}
