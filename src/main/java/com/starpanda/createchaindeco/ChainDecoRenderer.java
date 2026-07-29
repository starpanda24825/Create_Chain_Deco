package com.starpanda.createchaindeco;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorBlockEntity;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.SectionPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.Vec3;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid = CreateChainDeco.MODID, value = Dist.CLIENT)
public class ChainDecoRenderer {

	private ChainDecoRenderer() {
	}

	@SubscribeEvent
	public static void onRenderLevel(RenderLevelStageEvent event) {
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		ClientLevel level = mc.level;
		if (level == null) {
			return;
		}

		Vec3 cameraPos = event.getCamera().getPosition();
		PoseStack poseStack = event.getPoseStack();
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		BlockRenderDispatcher blockRenderer = mc.getBlockRenderer();
		BlockState lanternState = Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true);

		int cxCam = SectionPos.blockToSectionCoord((int) cameraPos.x);
		int czCam = SectionPos.blockToSectionCoord((int) cameraPos.z);
		int radius = Math.min(mc.options.renderDistance().get(), 8);

		for (int cx = cxCam - radius; cx <= cxCam + radius; cx++) {
			for (int cz = czCam - radius; cz <= czCam + radius; cz++) {
				LevelChunk chunk = (LevelChunk) level.getChunkSource().getChunk(cx, cz, ChunkStatus.FULL, false);
				if (chunk == null) {
					continue;
				}

				for (net.minecraft.world.level.block.entity.BlockEntity be : chunk.getBlockEntities().values()) {
					if (!(be instanceof ChainConveyorBlockEntity conveyor)) {
						continue;
					}

					Int2ObjectMap<ItemStack> decorations = conveyor.getData(ModAttachments.CHAIN_DECORATIONS);
					if (decorations.isEmpty()) {
						continue;
					}

					for (Int2ObjectMap.Entry<ItemStack> entry : decorations.int2ObjectEntrySet()) {
						int linkIndex = entry.getIntKey();
						ItemStack stack = entry.getValue();

						Vec3 worldPos = ChainLinkPositionHelper.getLinkPosition(level, conveyor.getBlockPos(), linkIndex);
						if (worldPos == null) {
							continue;
						}

						poseStack.pushPose();
						poseStack.translate(worldPos.x - cameraPos.x, worldPos.y - cameraPos.y - 1.0, worldPos.z - cameraPos.z);
						blockRenderer.renderSingleBlock(lanternState, poseStack, bufferSource, LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
						poseStack.popPose();
					}
				}
			}
		}

		bufferSource.endBatch();
	}
}
