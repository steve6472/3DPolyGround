package steve6472.polyground.world.chunk;

import steve6472.polyground.AABBUtil;
import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.registry.WaterRegistry;
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

		System.out.println("Last: " + subChunk.getWorld().lastWaterTickIndex);

		int max = CaveGame.getInstance().options.maxWaterTick;

		if (max != -1 && (subChunk.getWorld().reachedMax || subChunk.getWorld().currentWaterTickIndex < subChunk.getWorld().lastWaterTickIndex))
		{
			subChunk.getWorld().currentWaterTickIndex++;
			return;
		}

		subChunk.getWorld().currentWaterTickIndex++;
		if (subChunk.getWorld().currentWaterTickIndex == max || subChunk.getWorld().currentWaterTickIndex == subChunk.getWorld().lastWaterTickIndex + max)
		{
//			subChunk.getWorld().lastWaterTickIndex = subChunk.getWorld().currentWaterTickIndex;
			subChunk.getWorld().reachedMax = true;
		}

		totalVolume = 0;

		boolean shouldRender = subChunk.getWorld().getGame().mainRender.frustum.insideFrsutum(subChunk.getX() * 16, subChunk.getLayer() * 16, subChunk.getZ() * 16, subChunk.getX() * 16 + 16, subChunk.getLayer() * 16 + 16, subChunk.getZ() * 16 + 16);

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

					if (shouldRender &&
						(getLiquidVolumeEfficiently(i, j + 1, k) <= 1000
						|| getLiquidVolumeEfficiently(i, j - 1, k) <= 1000
						|| getLiquidVolumeEfficiently(i, j, k + 1) <= 1000
						|| getLiquidVolumeEfficiently(i, j, k - 1) <= 1000
						|| getLiquidVolumeEfficiently(i + 1, j, k) <= 1000
						|| getLiquidVolumeEfficiently(i - 1, j, k) <= 1000))
					{

						boolean inFrustum = subChunk.getWorld().getGame().mainRender.frustum.insideFrsutum(i + subChunk.getX() * 16, j + subChunk.getLayer() * 16, k + subChunk.getZ() * 16, 1.4f);

						if (inFrustum)
						{
							CaveGame.lastWaterCount++;
							if (subChunk.getWorld().getGame().mainRender.waterTess.hasSpace())
							{
								if (j != 15 || subChunk.getLayer() != subChunk.getParent().getSubChunks().length)
								{
									double upVolume = subChunk.getLiquidVolumeEfficiently(i, j + 1, k);
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

						Block down = subChunk.getBlockEfficiently(i, j - 1, k);
						double freeBlockVolume = WaterRegistry.volumes[down.getId()];
						if (freeBlockVolume > 0)
						{
							double downVolume = subChunk.getLiquidVolumeEfficiently(i, j - 1, k);
							double freeDownVolume = Util.clamp(0.0, 1000.0, freeBlockVolume - downVolume);
							if (freeDownVolume > 0)
							{
								double flow = Util.clamp(0.0, freeDownVolume, volume);
								setLiquidVolume(i, j, k, volume - flow);
								setLiquidVolumeEfficiently(i, j - 1, k, getLiquidVolumeEfficiently(i, j - 1, k) + flow);
							}
						}
					}

					// Flow North
					if (i != 15 || subChunk.getParent().getNeighbouringChunk(EnumFace.NORTH) != null)
						flowSide(i, j, k, 1, 0);
					// Flow East
					if (k != 15 || subChunk.getParent().getNeighbouringChunk(EnumFace.EAST) != null)
						flowSide(i, j, k, 0, 1);
					// Flow South
					if (i != 0 || subChunk.getParent().getNeighbouringChunk(EnumFace.SOUTH) != null)
						flowSide(i, j, k, -1, 0);
					// Flow west
					if (k != 0 || subChunk.getParent().getNeighbouringChunk(EnumFace.WEST) != null)
						flowSide(i, j, k, 0, -1);

					// Pressure
					if (j != 15 || subChunk.getLayer() != subChunk.getParent().getSubChunks().length)
					{
						double volume = getLiquidVolume(i, j, k);
						double freeBlockVolumeThis = WaterRegistry.volumes[subChunk.getBlockEfficiently(i, j, k).getId()];

						if (volume > freeBlockVolumeThis)
						{
							Block up = subChunk.getBlockEfficiently(i, j + 1, k);
							double freeBlockVolume = WaterRegistry.volumes[up.getId()];

							if (freeBlockVolume > 0)
							{
								double flow = (volume - freeBlockVolumeThis) / 20.0;
								setLiquidVolume(i, j, k, volume - flow);
								setLiquidVolumeEfficiently(i, j + 1, k, getLiquidVolumeEfficiently(i, j + 1, k) + flow);
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
		Block north = subChunk.getBlockEfficiently(i + dx, j, k + dz);
		double freeBlockVolume = WaterRegistry.volumes[north.getId()];

		if (freeBlockVolume > 0)
		{
			double sideVolume = subChunk.getLiquidVolumeEfficiently(i + dx, j, k + dz);
//			double freeSideVolume = Util.clamp(0.0, 1000.0, freeBlockVolume - sideVolume);

			if (sideVolume < volume)
			{
				double flow = volume - sideVolume;
				flow /= 5.0;
				setLiquidVolume(i, j, k, volume - flow);
				setLiquidVolumeEfficiently(i + dx, j, k + dz, getLiquidVolumeEfficiently(i + dx, j, k + dz) + flow);
				return true;
			}
		}
		return false;
	}

	public double getLiquidVolume(int x, int y, int z)
	{
		return liquid[x][y][z];
	}

	public double getLiquidVolumeEfficiently(int x, int y, int z)
	{
		if (x >= 0 && x < 16 && z >= 0 && z < 16 && y >= 0 && y < 16)
		{
			return getLiquidVolume(x, y, z);
		} else
		{
			SubChunk sc = subChunk.getNeighbouringSubChunk(x, y, z);
			if (sc == null)
				return 1000.0;
			return sc.getLiquidVolume(Math.floorMod(x, 16), Math.floorMod(y, 16), Math.floorMod(z, 16));
		}
	}

	public void setLiquidVolume(int x, int y, int z, double volume)
	{
		liquid[x][y][z] = volume;
		if (volume > 0)
			isActive = true;
	}

	public void setLiquidVolumeEfficiently(int x, int y, int z, double volume)
	{
		if (x >= 0 && x < 16 && z >= 0 && z < 16 && y >= 0 && y < 16)
		{
			setLiquidVolume(x, y, z, volume);
		} else
		{
			SubChunk sc = subChunk.getNeighbouringSubChunk(x, y, z);
			if (sc == null)
				return;
			sc.setLiquidVolume(Math.floorMod(x, 16), Math.floorMod(y, 16), Math.floorMod(z, 16), volume);
		}
	}

	public boolean isActive()
	{
		return isActive;
	}
}
