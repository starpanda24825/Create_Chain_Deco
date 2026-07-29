package com.starpanda.createchaindeco;

import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorBlockEntity;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import net.minecraft.world.item.ItemStack;

public class ChainDecoAttachmentHelper {

	private ChainDecoAttachmentHelper() {
	}

	public static Int2ObjectMap<ItemStack> getDecorations(ChainConveyorBlockEntity be) {
		return be.getData(ModAttachments.CHAIN_DECORATIONS);
	}

	public static void setDecoration(ChainConveyorBlockEntity be, int linkIndex, ItemStack stack) {
		if (stack.isEmpty()) {
			removeDecoration(be, linkIndex);
			return;
		}
		Int2ObjectMap<ItemStack> decorations = getDecorations(be);
		decorations.put(linkIndex, stack);
		be.setChanged();
	}

	public static void removeDecoration(ChainConveyorBlockEntity be, int linkIndex) {
		Int2ObjectMap<ItemStack> decorations = getDecorations(be);
		if (decorations.containsKey(linkIndex)) {
			decorations.remove(linkIndex);
			be.setChanged();
		}
	}

	public static ItemStack getDecoration(ChainConveyorBlockEntity be, int linkIndex) {
		Int2ObjectMap<ItemStack> decorations = getDecorations(be);
		ItemStack stack = decorations.get(linkIndex);
		if (stack == null || stack.isEmpty()) {
			return ItemStack.EMPTY;
		}
		return stack.copy();
	}
}
