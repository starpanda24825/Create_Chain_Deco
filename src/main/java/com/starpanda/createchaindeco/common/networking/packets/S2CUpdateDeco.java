package com.starpanda.createchaindeco.common.networking.packets;

import com.starpanda.createchaindeco.CreateChainDeco;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record S2CUpdateDeco(BlockPos liftPos, int linkIndex, ItemStack stack, double px, double py, double pz) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CUpdateDeco> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(CreateChainDeco.MODID, "update_deco"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CUpdateDeco> CODEC = StreamCodec.ofMember(
            S2CUpdateDeco::write, S2CUpdateDeco::read);

    public static S2CUpdateDeco read(RegistryFriendlyByteBuf buf) {
        BlockPos liftPos = buf.readBlockPos();
        int linkIndex = buf.readInt();
        ItemStack stack = ItemStack.STREAM_CODEC.decode(buf);
        double px = buf.readDouble();
        double py = buf.readDouble();
        double pz = buf.readDouble();
        return new S2CUpdateDeco(liftPos, linkIndex, stack, px, py, pz);
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(this.liftPos);
        buf.writeInt(this.linkIndex);
        ItemStack.STREAM_CODEC.encode(buf, this.stack);
        buf.writeDouble(this.px);
        buf.writeDouble(this.py);
        buf.writeDouble(this.pz);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}