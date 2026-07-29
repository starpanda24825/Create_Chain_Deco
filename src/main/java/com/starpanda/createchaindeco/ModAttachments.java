package com.starpanda.createchaindeco;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
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
}
