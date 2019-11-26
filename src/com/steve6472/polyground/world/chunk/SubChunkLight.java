package com.steve6472.polyground.world.chunk;

import com.steve6472.polyground.world.World;
import com.steve6472.sge.main.util.ColorUtil;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.11.2019
 * Project: SJP
 *
 ***********************/
public class SubChunkLight
{
	private SubChunk subChunk;

	private int[][][] light;

	public SubChunkLight(SubChunk subChunk)
	{
		this.subChunk = subChunk;
		light = new int[16][16][16];
	}

	public void clearLight()
	{
		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					light[i][j][k] = 0;
				}
			}
		}
	}

	/**
	 *
	 * Can check neighbour chunks.
	 * Should be more efficient for chunk border light checking
	 * as it does not have to create new Chunk Key everytime
	 *
	 * @param x x coordinate of light
	 * @param y y coordinate of light
	 * @param z z coordinate of light
	 * @return int Light
	 */
	public int getLightEfficiently(int x, int y, int z)
	{
		int maxLayer = subChunk.getParent().getSubChunks().length;

		World world = subChunk.getWorld();

		if (x >= 0 && x < 16 && z >= 0 && z < 16 && y >= 0 && y < 16)
		{
			return getLight(x, y, z);
		} else
		{
			if (x == 16)
			{
				SubChunk sc = world.getSubChunk(subChunk.getX() + 1, subChunk.getLayer(), subChunk.getZ());
				if (sc == null)
					return 0;
				return sc.getLightEfficiently(0, y, z);
			} else if (x == -1)
			{
				SubChunk sc = world.getSubChunk(subChunk.getX() - 1, subChunk.getLayer(), subChunk.getZ());
				if (sc == null)
					return 0;
				return sc.getLightEfficiently(15, y, z);
			}

			if (z == 16)
			{
				SubChunk sc = world.getSubChunk(subChunk.getX(), subChunk.getLayer(), subChunk.getZ() + 1);
				if (sc == null)
					return 0;
				return sc.getLightEfficiently(x, y, 0);
			} else if (z == -1)
			{
				SubChunk sc = world.getSubChunk(subChunk.getX(), subChunk.getLayer(), subChunk.getZ() - 1);
				if (sc == null)
					return 0;
				return sc.getLightEfficiently(x, y, 15);
			}

			if (y == -1 && subChunk.getLayer() > 0)
			{
				return subChunk.getParent().getSubChunks()[subChunk.getLayer() - 1].getLightEfficiently(x, 15, z);
			} else if (y == 16 && subChunk.getLayer() + 1 < maxLayer)
			{
				return subChunk.getParent().getSubChunks()[subChunk.getLayer() + 1].getLightEfficiently(x, 0, z);
			} else
			{
				return 0;
			}
		}
	}

	/**
	 * If the current light is not 0 (no color) it blends the two together
	 *
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 * @param color light color
	 */
	public void setLight(int x, int y, int z, int color)
	{
		int l = light[x][y][z];
		if (l == 0)
			light[x][y][z] = color;
		else
			light[x][y][z] = ColorUtil.blend(l, color, 0.5);
	}

	public int getLight(int x, int y, int z)
	{
		return light[x][y][z];
	}
}
