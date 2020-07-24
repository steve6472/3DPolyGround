package steve6472.polyground.world.chunk;

import steve6472.polyground.AABBUtil;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.WaterRegistry;
import steve6472.polyground.world.World;
import steve6472.sge.main.Util;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12.04.2020
 * Project: CaveGame
 *
 ***********************/
public class SubChunkWater
{
	private double totalVolume = 0;
	private final double[][][] liquid;
	private final SubChunk subChunk;
	private boolean isActive;

	public SubChunkWater(SubChunk subChunk)
	{
		liquid = new double[16][16][16];
		this.subChunk = subChunk;
		isActive = false;
	}

	public void tick()
	{
		if (!isActive)
			return;

		World world = subChunk.getWorld();
		
		int max = CaveGame.getInstance().options.maxWaterTick;

		if (max != -1 && (world.reachedMax || world.currentWaterTickIndex < world.lastWaterTickIndex))
		{
			world.currentWaterTickIndex++;
			return;
		}

		world.currentWaterTickIndex++;
		if (world.currentWaterTickIndex == max || world.currentWaterTickIndex == world.lastWaterTickIndex + max)
		{
//			world.lastWaterTickIndex = world.currentWaterTickIndex;
			world.reachedMax = true;
		}

		totalVolume = 0;

		boolean shouldRender = world.getGame().mainRender.frustum.insideFrsutum(subChunk.getX() * 16, subChunk.getLayer() * 16, subChunk.getZ() * 16, subChunk.getX() * 16 + 16, subChunk.getLayer() * 16 + 16, subChunk.getZ() * 16 + 16);

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					// Decay
					if (getLiquidVolume(i, j, k) < 0.000001)
					{
						setLiquidVolume(i, j, k, 0);
						continue;
					}

					if (getLiquidVolume(i, j, k) == 0)
						continue;

					double vol = getLiquidVolume(i, j, k);
					totalVolume += vol;

					if (shouldRender/* &&
						(getLiquidVolumeEfficiently(i, j + 1, k) <= 1000
						|| getLiquidVolumeEfficiently(i, j - 1, k) <= 1000
						|| getLiquidVolumeEfficiently(i, j, k + 1) <= 1000
						|| getLiquidVolumeEfficiently(i, j, k - 1) <= 1000
						|| getLiquidVolumeEfficiently(i + 1, j, k) <= 1000
						|| getLiquidVolumeEfficiently(i - 1, j, k) <= 1000)*/)
					{

						boolean inFrustum = world.getGame().mainRender.frustum.insideFrsutum(i + subChunk.getX() * 16, j + subChunk.getLayer() * 16, k + subChunk.getZ() * 16, 1.4f);

						if (inFrustum)
						{
							CaveGame.lastWaterCount++;
							if (world.getGame().mainRender.waterTess.hasSpace())
							{
								if (j != 15 || subChunk.getLayer() != subChunk.getParent().getSubChunks().length)
								{
									double upVolume = subChunk.getLiquidVolume(i, j + 1, k);
//									double blockVolume = WaterRegistry.volumes[subChunk.getBlockId(i, j, k)];
									if (upVolume > 0)
										AABBUtil.addWater(i + subChunk.getX() * 16, j + subChunk.getLayer() * 16, k + subChunk.getZ() * 16, 1f, CaveGame.getInstance().mainRender.waterTess);
									else
										AABBUtil.addWater(i + subChunk.getX() * 16, j + subChunk.getLayer() * 16, k + subChunk.getZ() * 16, (float) (vol / 1000.0), CaveGame.getInstance().mainRender.waterTess);
								} else
								{
									AABBUtil.addWater(i + subChunk.getX() * 16, j + subChunk.getLayer() * 16, k + subChunk.getZ() * 16, (float) (vol / 1000.0), CaveGame.getInstance().mainRender.waterTess);
								}
							}
						}
					}

					// Flow Down
					if (j != 0 || subChunk.getLayer() != 0)
					{
						double volume = getLiquidVolume(i, j, k);

						BlockState down = subChunk.getState(i, j - 1, k);
						double freeBlockVolume = WaterRegistry.volumes[down.getId()];
						if (freeBlockVolume > 0)
						{
							double downVolume = subChunk.getLiquidVolume(i, j - 1, k);
							double freeDownVolume = Util.clamp(0.0, 1000.0, freeBlockVolume - downVolume);
							if (freeDownVolume > 0)
							{
								double flow = Util.clamp(0.0, freeDownVolume, volume);
								setLiquidVolume(i, j, k, volume - flow);
								world.setLiquidVolume(
									i + subChunk.getX() * 16,
									j - 1 + subChunk.getLayer() * 16,
									k + subChunk.getZ() * 16,
									world.getLiquidVolume(
										i + subChunk.getX() * 16,
										j - 1 + subChunk.getLayer() * 16,
										k + subChunk.getZ() * 16) + flow);
							}
						}
					}

					// Flow North
					if (i != 15 || world.getSubChunk(subChunk.getX() + 1, subChunk.getLayer(), subChunk.getZ()) != null)
						flowSide(i, j, k, 1, 0);
					// Flow East
					if (k != 15 || world.getSubChunk(subChunk.getX(), subChunk.getLayer(), subChunk.getZ() + 1) != null)
						flowSide(i, j, k, 0, 1);
					// Flow South
					if (i != 0 || world.getSubChunk(subChunk.getX() - 1, subChunk.getLayer(), subChunk.getZ()) != null)
						flowSide(i, j, k, -1, 0);
					// Flow west
					if (k != 0 || world.getSubChunk(subChunk.getX(), subChunk.getLayer(), subChunk.getZ() - 1) != null)
						flowSide(i, j, k, 0, -1);

					// Pressure
					if (j != 15 || subChunk.getLayer() != subChunk.getParent().getSubChunks().length)
					{
						double volume = getLiquidVolume(i, j, k);
						double freeBlockVolumeThis = WaterRegistry.volumes[subChunk.getState(i, j, k).getId()];

						if (volume > freeBlockVolumeThis)
						{
							BlockState up = subChunk.getState(i, j + 1, k);
							double freeBlockVolume = WaterRegistry.volumes[up.getId()];

							if (freeBlockVolume > 0)
							{
								double flow = (volume - freeBlockVolumeThis) / 20.0;
								setLiquidVolume(i, j, k, volume - flow);
								world.setLiquidVolume(
									i + subChunk.getX() * 16,
									j + 1 + subChunk.getLayer() * 16,
									k + subChunk.getZ() * 16,
									world.getLiquidVolume(
										i + subChunk.getX() * 16,
										j + 1 + subChunk.getLayer() * 16,
										k + subChunk.getZ() * 16) + flow);
							}
						}
					}
				}
			}
		}

		if (totalVolume == 0)
			isActive = false;
	}

	private boolean flowSide(int i, int j, int k, int dx, int dz)
	{
		double volume = getLiquidVolume(i, j, k);
		BlockState north = subChunk.getState(i + dx, j, k + dz);
		double freeBlockVolume = WaterRegistry.volumes[north.getId()];

		if (freeBlockVolume > 0)
		{
			double sideVolume = subChunk.getLiquidVolume(i + dx, j, k + dz);
//			double freeSideVolume = Util.clamp(0.0, 1000.0, freeBlockVolume - sideVolume);

			if (sideVolume < volume)
			{
				double flow = volume - sideVolume;
				flow /= 5.0;
				setLiquidVolume(i, j, k, volume - flow);
				subChunk.getWorld().setLiquidVolume(
					i + subChunk.getX() * 16 + dx,
					j + subChunk.getLayer() * 16,
					k + subChunk.getZ() * 16 + dz,
					subChunk.getWorld().getLiquidVolume(
						i + subChunk.getX() * 16 + dx,
						j + subChunk.getLayer() * 16,
						k + subChunk.getZ() * 16 + dz) + flow);
				return true;
			}
		}
		return false;
	}

	public double getLiquidVolume(int x, int y, int z)
	{
		return liquid[x][y][z];
	}

	public void setLiquidVolume(int x, int y, int z, double volume)
	{
		liquid[x][y][z] = volume;
		if (volume > 0)
			isActive = true;
	}

	public boolean isActive()
	{
		return isActive;
	}
}
