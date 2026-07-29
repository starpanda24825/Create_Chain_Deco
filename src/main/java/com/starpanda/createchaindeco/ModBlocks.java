package com.starpanda.createchaindeco;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

import net.neoforged.neoforge.registries.DeferredBlock;

public class ModBlocks {

	public static final DeferredBlock<Block> CHAIN_LIGHT = CreateChainDeco.BLOCKS.register("chain_light",
		() -> new ChainLightBlock(BlockBehaviour.Properties.of()
			.noCollission()
			.noOcclusion()
			.lightLevel(state -> 15)
			.strength(-1.0F, 3600000.0F)
			.noLootTable()
			.pushReaction(PushReaction.BLOCK)));

	private ModBlocks() {
	}
}
