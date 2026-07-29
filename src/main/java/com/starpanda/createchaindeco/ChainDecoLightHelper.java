package com.starpanda.createchaindeco;

import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorBlockEntity;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

public class ChainDecoLightHelper {

	private ChainDecoLightHelper() {
	}

	public static BlockPos placeLight(Level level, ChainConveyorBlockEntity be, int linkIndex, Vec3 worldPos) {
		assert level != null && !level.isClientSide : "placeLight must be called server-side";
		assert be != null : "block entity must not be null";

		BlockPos target = BlockPos.containing(worldPos);
		if (!level.getBlockState(target).isAir()) {
			return null;
		}

		level.setBlock(target, ModBlocks.CHAIN_LIGHT.get().defaultBlockState(), Block.UPDATE_ALL);

		Int2ObjectMap<Long> lightPositions = be.getData(ModAttachments.CHAIN_LIGHT_POSITIONS);
		lightPositions.put(linkIndex, Long.valueOf(target.asLong()));
		be.setChanged();

		return target;
	}

	public static void removeLight(Level level, ChainConveyorBlockEntity be, int linkIndex) {
		assert level != null && !level.isClientSide : "removeLight must be called server-side";
		assert be != null : "block entity must not be null";

		Int2ObjectMap<Long> lightPositions = be.getData(ModAttachments.CHAIN_LIGHT_POSITIONS);
		Long packed = lightPositions.get(linkIndex);
		if (packed == null) {
			return;
		}

		BlockPos pos = BlockPos.of(packed);
		if (level.getBlockState(pos).getBlock() == ModBlocks.CHAIN_LIGHT.get()) {
			level.removeBlock(pos, false);
		}

		lightPositions.remove(linkIndex);
		be.setChanged();
	}
}
