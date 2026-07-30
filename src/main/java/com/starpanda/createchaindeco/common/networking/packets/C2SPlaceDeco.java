package com.starpanda.createchaindeco.common.networking.packets;

import com.starpanda.createchaindeco.CreateChainDeco;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record C2SPlaceDeco(BlockPos liftPos, int linkIndex, ItemStack stack) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<C2SPlaceDeco> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(CreateChainDeco.MODID, "place_deco"));

    public static final StreamCodec<FriendlyByteBuf, C2SPlaceDeco> CODEC = StreamCodec.ofMember(
            C2SPlaceDeco::write, C2SPlaceDeco::read);

    public static C2SPlaceDeco read(FriendlyByteBuf buf) {
        ItemStack stack = ItemStack.STREAM_CODEC.decode((RegistryFriendlyByteBuf) buf);
        return new C2SPlaceDeco(buf.readBlockPos(), buf.readInt(), stack);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.liftPos);
        buf.writeInt(this.linkIndex);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}