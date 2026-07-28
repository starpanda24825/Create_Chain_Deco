package com.starpanda.createchaindeco.network;

import com.starpanda.createchaindeco.CreateChainDeco;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SelectConveyorPositionPacket(BlockPos conveyorPos, int linkIndex) implements CustomPacketPayload {

	public static final CustomPacketPayload.Type<SelectConveyorPositionPacket> TYPE = new CustomPacketPayload.Type<>(
			ResourceLocation.fromNamespaceAndPath(CreateChainDeco.MODID, "select_conveyor_position"));

	public static final StreamCodec<FriendlyByteBuf, SelectConveyorPositionPacket> CODEC = StreamCodec.ofMember(
			SelectConveyorPositionPacket::write, SelectConveyorPositionPacket::read);

	public static SelectConveyorPositionPacket read(FriendlyByteBuf buf) {
		BlockPos conveyorPos = buf.readBlockPos();
		int linkIndex = buf.readInt();
		return new SelectConveyorPositionPacket(conveyorPos, linkIndex);
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeBlockPos(this.conveyorPos);
		buf.writeInt(this.linkIndex);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
