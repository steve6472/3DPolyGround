package com.steve6472.polyground.world.chunk;

import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.world.World;
import com.steve6472.polyground.world.biomes.IBiomeProvider;
import com.steve6472.polyground.world.generator.IGenerator;
import com.steve6472.sge.main.util.ColorUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class SubChunk implements IBiomeProvider
{
	private static final int MODEL_COUNT = ModelLayer.values().length;

	private SubChunkModel[] model;
	private Chunk parent;

	private int layer;
	public float renderTime = 0.1f;

	private BlockData[][][] blockData;
	private int[][][] ids;
	private int[][][] biomes;
	private int[][][] light;
	private List<Short> scheduledUpdates;
	private List<Short> tickableBlocks;
	private List<Short> newScheduledUpdates;

	public SubChunk(Chunk parent, int layer)
	{
		this.parent = parent;
		this.layer = layer;

		model = new SubChunkModel[MODEL_COUNT];
		for (int i = 0; i < MODEL_COUNT; i++)
		{
			model[i] = new SubChunkModel(ModelLayer.values()[i]);
			SubChunkBuilder.init(model[i]);
		}

		blockData = new BlockData[16][16][16];
		ids = new int[16][16][16];
		biomes = new int[16][16][16];
		light = new int[16][16][16];
		tickableBlocks = new ArrayList<>();
		scheduledUpdates = new ArrayList<>();
		newScheduledUpdates = new ArrayList<>();

		clearLight();
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

	public int getLight(int x, int y, int z)
	{
		return light[x][y][z];
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

	public static IGenerator generator;

	public void generate()
	{
		generator.generate(this);
	}

	public void tick()
	{
		if (renderTime <= 1.0f)
			renderTime += 0.025f;

		for (short i : tickableBlocks)
		{
			short x = (short) (i >> 8);
			short y = (short) ((i >> 4) & 0xf);
			short z = (short) (i & 0xf);

			Block blockToTick = BlockRegistry.getBlockById(ids[x][y][z]);
			blockToTick.tick(this, blockData[x][y][z], x, y, z);
		}

		scheduledUpdates.addAll(newScheduledUpdates);
		newScheduledUpdates.clear();

		for (Iterator<Short> iter = scheduledUpdates.iterator(); iter.hasNext();)
		{
			short i = iter.next();

			short x = (short) (i >> 8);
			short y = (short) ((i >> 4) & 0xf);
			short z = (short) (i & 0xf);

			Block blockToUpdate = BlockRegistry.getBlockById(ids[x][y][z]);
			blockToUpdate.onUpdate(this, blockData[x][y][z], EnumFace.NONE, x, y, z);
			iter.remove();
		}
	}

	public void saveSubChunk() throws IOException
	{
		ChunkSerializer.serialize(this);
	}

	public void addTickableBlock(int x, int y, int z)
	{
		short r = (short) (x << 8 | y << 4 | z);
		if (!tickableBlocks.contains(r))
			tickableBlocks.add(r);
	}

	public void removeTickableBlock(int x, int y, int z)
	{
		short r = (short) (x << 8 | y << 4 | z);
		if (tickableBlocks.contains(r))
			tickableBlocks.remove((Short) r);
	}

	public boolean isTickableBlock(int x, int y, int z)
	{
		short r = (short) (x << 8 | y << 4 | z);
		return tickableBlocks.contains(r);
	}



	public void addScheduledUpdate(int x, int y, int z)
	{
		short r = (short) (x << 8 | y << 4 | z);
		if (!scheduledUpdates.contains(r) && !newScheduledUpdates.contains(r))
			newScheduledUpdates.add(r);
	}

	public void rebuild()
	{
		clearLight();
		for (int i = 0; i < MODEL_COUNT; i++)
		{
			model[i].rebuild(this);
		}

		if (CaveGame.getInstance().options.chunkModelDebug)
			System.out.println("");
	}

	public void unload()
	{
		for (int i = 0; i < MODEL_COUNT; i++)
		{
			model[i].unload();
		}
	}

	public SubChunkModel getModel(int modelLayer)
	{
		return model[modelLayer];
	}

	public boolean isEmpty(int modelLayer)
	{
		return model[modelLayer].triangleCount == 0;
	}

	public boolean isEmpty()
	{
		for (int i = 0; i < MODEL_COUNT; i++)
		{
			if (!isEmpty(i))
				return false;
		}

		return true;
	}

	public World getWorld()
	{
		return getParent().getWorld();
	}

	public float getRenderTime()
	{
		return renderTime;
	}

	public int[][][] getIds()
	{
		return ids;
	}

	public BlockData[][][] getBlockData()
	{
		return blockData;
	}

	public BlockData getBlockData(int x, int y, int z)
	{
		return getBlockData()[x][y][z];
	}

	public void setBlockEntity(int x, int y, int z, BlockData blockData)
	{
		getBlockData()[x][y][z] = blockData;
	}

	public int getBlockId(int x, int y, int z)
	{
		return getIds()[x][y][z];
	}

	public Block getBlock(int x, int y, int z)
	{
		return BlockRegistry.getBlockById(getBlockId(x, y, z));
	}

	public void setBlock(int x, int y, int z, int id)
	{
		getIds()[x][y][z] = id;
	}

	/**
	 *
	 * Can check neighbour chunks.
	 * Should be more efficient for chunk border block checking
	 * as it does not have to create new Chunk Key everytime
	 *
	 * @param x x coordinate of block
	 * @param y y coordinate of block
	 * @param z z coordinate of block
	 * @return Block
	 */
	public Block getBlockEfficiently(int x, int y, int z)
	{
		int maxLayer = parent.getSubChunks().length;

		World world = getWorld();

		if (x >= 0 && x < 16 && z >= 0 && z < 16 && y >= 0 && y < 16)
		{
			return getBlock(x, y, z);
		} else
		{
			if (x == 16)
			{
				SubChunk sc = world.getSubChunk(getX() + 1, getLayer(), getZ());
				if (sc == null)
					return Block.air;
				return sc.getBlockEfficiently(0, y, z);
			} else if (x == -1)
			{
				SubChunk sc = world.getSubChunk(getX() - 1, getLayer(), getZ());
				if (sc == null)
					return Block.air;
				return sc.getBlockEfficiently(15, y, z);
			}

			if (z == 16)
			{
				SubChunk sc = world.getSubChunk(getX(), getLayer(), getZ() + 1);
				if (sc == null)
					return Block.air;
				return sc.getBlockEfficiently(x, y, 0);
			} else if (z == -1)
			{
				SubChunk sc = world.getSubChunk(getX(), getLayer(), getZ() - 1);
				if (sc == null)
					return Block.air;
				return sc.getBlockEfficiently(x, y, 15);
			}

			if (y == -1 && getLayer() > 0)
			{
				return parent.getSubChunks()[getLayer() - 1].getBlockEfficiently(x, 15, z);
			} else if (y == 16 && getLayer() + 1 < maxLayer)
			{
				return parent.getSubChunks()[getLayer() + 1].getBlockEfficiently(x, 0, z);
			} else
			{
				return Block.air;
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
		int maxLayer = parent.getSubChunks().length;

		World world = getWorld();

		if (x >= 0 && x < 16 && z >= 0 && z < 16 && y >= 0 && y < 16)
		{
			return getLight(x, y, z);
		} else
		{
			if (x == 16)
			{
				SubChunk sc = world.getSubChunk(getX() + 1, getLayer(), getZ());
				if (sc == null)
					return 0;
				return sc.getLightEfficiently(0, y, z);
			} else if (x == -1)
			{
				SubChunk sc = world.getSubChunk(getX() - 1, getLayer(), getZ());
				if (sc == null)
					return 0;
				return sc.getLightEfficiently(15, y, z);
			}

			if (z == 16)
			{
				SubChunk sc = world.getSubChunk(getX(), getLayer(), getZ() + 1);
				if (sc == null)
					return 0;
				return sc.getLightEfficiently(x, y, 0);
			} else if (z == -1)
			{
				SubChunk sc = world.getSubChunk(getX(), getLayer(), getZ() - 1);
				if (sc == null)
					return 0;
				return sc.getLightEfficiently(x, y, 15);
			}

			if (y == -1 && getLayer() > 0)
			{
				return parent.getSubChunks()[getLayer() - 1].getLightEfficiently(x, 15, z);
			} else if (y == 16 && getLayer() + 1 < maxLayer)
			{
				return parent.getSubChunks()[getLayer() + 1].getLightEfficiently(x, 0, z);
			} else
			{
				return 0;
			}
		}
	}

	public SubChunk getNeighbouringChunk(int x, int y, int z)
	{
		int maxLayer = parent.getSubChunks().length;

		World world = getWorld();

		if (x >= 0 && x < 16 && z >= 0 && z < 16 && y >= 0 && y < 16)
		{
			return this;
		} else
		{
			if (x == 16)
			{
				return world.getSubChunk(getX() + 1, getLayer(), getZ());
			} else if (x == -1)
			{
				return world.getSubChunk(getX() - 1, getLayer(), getZ());
			}

			if (z == 16)
			{
				return world.getSubChunk(getX(), getLayer(), getZ() + 1);
			} else if (z == -1)
			{
				return world.getSubChunk(getX(), getLayer(), getZ() - 1);
			}

			if (y == -1 && getLayer() > 0)
			{
				return parent.getSubChunks()[getLayer() - 1];
			} else if (y == 16 && getLayer() + 1 < maxLayer)
			{
				return parent.getSubChunks()[getLayer() + 1];
			} else
			{
				return null;
			}
		}
	}

	public void setBlock(int x, int y, int z, Block block)
	{
		setBlock(x, y, z, block.getId());
	}

	public Chunk getParent()
	{
		return parent;
	}

	public int getX()
	{
		return getParent().getX();
	}

	public int getLayer()
	{
		return layer;
	}

	public int getZ()
	{
		return getParent().getZ();
	}

	public int getTriangleCount(int modelLayer)
	{
		return model[modelLayer].triangleCount;
	}

	public static int getModelCount()
	{
		return MODEL_COUNT;
	}

	@Override
	public int[][][] getBiomeIds()
	{
		return biomes;
	}
}
