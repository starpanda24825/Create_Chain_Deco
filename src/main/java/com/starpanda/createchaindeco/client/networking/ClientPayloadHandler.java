package com.starpanda.createchaindeco.client.networking;

import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorBlockEntity;
import com.starpanda.createchaindeco.common.ChainDecoAttachmentHelper;
import com.starpanda.createchaindeco.common.networking.packets.S2CUpdateDeco;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {

    public static void handleUpdateDeco(S2CUpdateDeco packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;

            BlockPos liftPos = packet.liftPos();
            BlockEntity be = mc.level.getBlockEntity(liftPos);
            if (!(be instanceof ChainConveyorBlockEntity conveyor)) return;

            ChainDecoAttachmentHelper.setDecoration(conveyor, packet.linkIndex(), packet.stack());
        });
    }
}