package com.starpanda.createchaindeco;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.world.item.ItemStack;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentCopyHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {

	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
		DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, CreateChainDeco.MODID);

	public static final Supplier<AttachmentType<Int2ObjectMap<ItemStack>>> CHAIN_DECORATIONS =
		ATTACHMENT_TYPES.register("chain_decorations", () -> AttachmentType
			.builder((Supplier<Int2ObjectMap<ItemStack>>) () -> new Int2ObjectOpenHashMap<>())
			.serialize(new IAttachmentSerializer<ListTag, Int2ObjectMap<ItemStack>>() {
				// Encode: map -> ListTag of CompoundTag { index: int, item: CompoundTag }
				@Override
				public ListTag write(Int2ObjectMap<ItemStack> map, HolderLookup.Provider provider) {
					ListTag list = new ListTag();
					map.forEach((index, stack) -> {
						if (stack != null && !stack.isEmpty()) {
							CompoundTag entry = new CompoundTag();
							entry.put("index", IntTag.valueOf(index));
							entry.put("item", stack.save(provider));
							list.add(entry);
						}
					});
					return list;
				}

				// Decode: ListTag -> map with ItemStack.parseOptional
				@Override
				public Int2ObjectMap<ItemStack> read(IAttachmentHolder holder, ListTag tag, HolderLookup.Provider provider) {
					Int2ObjectMap<ItemStack> map = new Int2ObjectOpenHashMap<>();
					for (int i = 0; i < tag.size(); i++) {
						CompoundTag entry = tag.getCompound(i);
						int index = entry.getInt("index");
						ItemStack stack = ItemStack.parseOptional(provider, entry.getCompound("item"));
						if (!stack.isEmpty()) {
							map.put(index, stack);
						}
					}
					return map;
				}
			})
			.copyHandler(new IAttachmentCopyHandler<Int2ObjectMap<ItemStack>>() {
				@Override
				public Int2ObjectMap<ItemStack> copy(Int2ObjectMap<ItemStack> original, IAttachmentHolder holder, HolderLookup.Provider provider) {
					Int2ObjectMap<ItemStack> copy = new Int2ObjectOpenHashMap<>();
					original.forEach((index, stack) -> {
						if (stack != null) {
							copy.put(index, stack.copy());
						}
					});
					return copy;
				}
			})
			.build());

	public static final Supplier<AttachmentType<Int2ObjectMap<Long>>> CHAIN_LIGHT_POSITIONS =
		ATTACHMENT_TYPES.register("chain_light_positions", () -> AttachmentType
			.builder((Supplier<Int2ObjectMap<Long>>) () -> new Int2ObjectOpenHashMap<>())
			.serialize(new IAttachmentSerializer<ListTag, Int2ObjectMap<Long>>() {
				// Encode: map -> ListTag of CompoundTag { index: int, pos: long }
				@Override
				public ListTag write(Int2ObjectMap<Long> map, HolderLookup.Provider provider) {
					ListTag list = new ListTag();
					map.forEach((index, packedPos) -> {
						if (packedPos != null) {
							CompoundTag entry = new CompoundTag();
							entry.put("index", IntTag.valueOf(index));
							entry.put("pos", LongTag.valueOf(packedPos));
							list.add(entry);
						}
					});
					return list;
				}

				// Decode: ListTag -> map
				@Override
				public Int2ObjectMap<Long> read(IAttachmentHolder holder, ListTag tag, HolderLookup.Provider provider) {
					Int2ObjectMap<Long> map = new Int2ObjectOpenHashMap<>();
					for (int i = 0; i < tag.size(); i++) {
						CompoundTag entry = tag.getCompound(i);
						int index = entry.getInt("index");
						long packedPos = entry.getLong("pos");
						map.put(index, Long.valueOf(packedPos));
					}
					return map;
				}
			})
			.copyHandler(new IAttachmentCopyHandler<Int2ObjectMap<Long>>() {
				@Override
				public Int2ObjectMap<Long> copy(Int2ObjectMap<Long> original, IAttachmentHolder holder, HolderLookup.Provider provider) {
					return new Int2ObjectOpenHashMap<>(original);
				}
			})
			.build());

			public static final Supplier<AttachmentType<Int2ObjectMap<double[]>>> CHAIN_RENDER_POSITIONS =
		ATTACHMENT_TYPES.register("chain_render_positions", () -> AttachmentType
			.builder((Supplier<Int2ObjectMap<double[]>>) () -> new Int2ObjectOpenHashMap<>())
			.serialize(new IAttachmentSerializer<ListTag, Int2ObjectMap<double[]>>() {
				@Override
				public ListTag write(Int2ObjectMap<double[]> map, HolderLookup.Provider provider) {
					ListTag list = new ListTag();
					map.forEach((index, pos) -> {
						if (pos != null && pos.length == 3) {
							CompoundTag entry = new CompoundTag();
							entry.put("index", IntTag.valueOf(index));
							entry.putDouble("x", pos[0]);
							entry.putDouble("y", pos[1]);
							entry.putDouble("z", pos[2]);
							list.add(entry);
						}
					});
					return list;
				}

				@Override
				public Int2ObjectMap<double[]> read(IAttachmentHolder holder, ListTag tag, HolderLookup.Provider provider) {
					Int2ObjectMap<double[]> map = new Int2ObjectOpenHashMap<>();
					for (int i = 0; i < tag.size(); i++) {
						CompoundTag entry = tag.getCompound(i);
						int index = entry.getInt("index");
						map.put(index, new double[]{
							entry.getDouble("x"),
							entry.getDouble("y"),
							entry.getDouble("z")
						});
					}
					return map;
				}
			})
			.copyHandler(new IAttachmentCopyHandler<Int2ObjectMap<double[]>>() {
				@Override
				public Int2ObjectMap<double[]> copy(Int2ObjectMap<double[]> original, IAttachmentHolder holder, HolderLookup.Provider provider) {
					return new Int2ObjectOpenHashMap<>(original);
				}
			})
			.build());
}
