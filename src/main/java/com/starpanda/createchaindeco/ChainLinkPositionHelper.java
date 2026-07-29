package com.starpanda.createchaindeco;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorBlockEntity;
import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorBlockEntity.ConnectionStats;
import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorShape;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ChainLinkPositionHelper {

	private ChainLinkPositionHelper() {
	}

	public static Vec3 getLinkPosition(Level level, BlockPos liftPos, int linkIndex) {
		if (level == null || liftPos == null) {
			return null;
		}
		var rawBe = level.getBlockEntity(liftPos);
		if (!(rawBe instanceof ChainConveyorBlockEntity be)) {
			return null;
		}

		be.prepareStats();

		if (linkIndex < 8) {
			if (linkIndex < 0) {
				return null;
			}
			return be.getPackagePosition(linkIndex * 45.0F, null);
		}

		int remaining = linkIndex - 8;
		Set<BlockPos> connections = be.connections;
		Map<BlockPos, ConnectionStats> statsMap = be.connectionStats;

		if (connections == null || statsMap == null) {
			return null;
		}

		List<BlockPos> orderedConnections = new ArrayList<>(connections);
		for (BlockPos connOffset : orderedConnections) {
			ConnectionStats stats = statsMap.get(connOffset);
			if (stats == null) {
				continue;
			}

			double dist = Vec3.atLowerCornerOf(connOffset).length();
			int dots = (int) Math.round(dist - 3.0);
			double length = stats.chainLength();
			double margin = length - dots;
			int count = (int) Math.round(length - 2.0 * margin) + 1;

			if (count <= 0) {
				continue;
			}

			if (remaining < count) {
				float chainPos = (float) (remaining + margin + 0.025);
				return be.getPackagePosition(chainPos, connOffset);
			}

			remaining -= count;
		}

		return null;
	}
}
