package com.starpanda.createchaindeco.server.networking;

import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorBlockEntity;
import com.starpanda.createchaindeco.common.ChainDecoAttachmentHelper;
import com.starpanda.createchaindeco.common.ChainDecoLightHelper;
import com.starpanda.createchaindeco.common.ChainLinkPositionHelper;
import com.starpanda.createchaindeco.common.networking.packets.C2SPlaceDeco;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {

    public static void handlePlaceDeco(C2SPlaceDeco packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            BlockPos liftPos = packet.liftPos();

            if (!player.blockPosition().closerThan(liftPos, 10)) return;

            BlockEntity be = player.level().getBlockEntity(liftPos);
            if (!(be instanceof ChainConveyorBlockEntity conveyor)) return;

            ItemStack stack = player.getMainHandItem().copy();
            if (stack.isEmpty()) return;

            int linkIndex = packet.linkIndex();

            ChainDecoAttachmentHelper.setDecoration(conveyor, linkIndex, stack);

            Vec3 worldPos = ChainLinkPositionHelper.getLinkPosition(player.level(), liftPos, linkIndex);
            if (worldPos != null) {
                ChainDecoLightHelper.placeLight(player.level(), conveyor, linkIndex, worldPos);
            }
        });
    }
}