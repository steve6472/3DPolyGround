package com.steve6472.polyground.world;

import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.world.biomes.IBiomeProvider;
import com.steve6472.polyground.world.generator.IGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class SubChunk implements IBiomeProvider
{
	private static final int MODEL_COUNT = 2;

	private SubChunkModel[] model;
	private Chunk parent;

	private int layer;
	public float renderTime = 0.1f;

	private BlockData[][][] blockData;
	private int[][][] ids;
	private int[][][] biomes;
	private List<Short> scheduledUpdates;
	private List<Short> tickableBlocks;

	public SubChunk(Chunk parent, int layer)
	{
		this.parent = parent;
		this.layer = layer;

		model = new SubChunkModel[MODEL_COUNT];
		for (int i = 0; i < MODEL_COUNT; i++)
		{
			model[i] = new SubChunkModel(i);
			SubChunkBuilder.init(model[i]);
		}

		blockData = new BlockData[16][16][16];
		ids = new int[16][16][16];
		biomes = new int[16][16][16];
		tickableBlocks = new ArrayList<>();
		scheduledUpdates = new ArrayList<>();

		renderTime = 1;
	}

	public static IGenerator generator;

	public void generate()
	{
		generator.generate(this);
	}

	public void tick()
	{
		if (renderTime <= 1.0f)
			renderTime += 0.0005f;

		for (short i : tickableBlocks)
		{
			short x = (short) (i >> 8);
			short y = (short) ((i >> 4) & 0xf);
			short z = (short) (i & 0xf);

			Block blockToTick = BlockRegistry.getBlockById(ids[x][y][z]);
			blockToTick.tick(this, blockData[x][y][z], x, y, z);
		}

//		for (short i : scheduledUpdates)
//		{
//			short x = (short) (i >> 8);
//			short y = (short) ((i >> 4) & 0xf);
//			short z = (short) (i & 0xf);
//
//			Block blockToUpdate = BlockRegistry.getBlockById(ids[x][y][z]);
//			blockToUpdate.onUpdate(this, blockData[x][y][z], EnumFace.NONE, x, y, z);
//		}
	}

	public void saveSubChunk(String worldName) throws IOException
	{
		Saver.saveSubChunk(worldName, this);
	}

	public void loadSubChunk(String worldName) throws IOException
	{
		Saver.loadSubChunk(worldName, this);
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
		if (!scheduledUpdates.contains(r))
			scheduledUpdates.add(r);
	}

	public void removeScheduledUpdate(int x, int y, int z)
	{
		short r = (short) (x << 8 | y << 4 | z);
		if (scheduledUpdates.contains(r))
			scheduledUpdates.remove((Short) r);
	}

	public boolean isScheduledUpdate(int x, int y, int z)
	{
		short r = (short) (x << 8 | y << 4 | z);
		return scheduledUpdates.contains(r);
	}

	public void rebuild()
	{
		for (int i = 0; i < MODEL_COUNT; i++)
		{
			model[i].rebuild(this);
		}
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
		return getBlockData()[Math.floorMod(x, 16)][Math.floorMod(y, 16)][Math.floorMod(z, 16)];
	}

	public void setBlockEntity(int x, int y, int z, BlockData blockData)
	{
		getBlockData()[Math.floorMod(x, 16)][Math.floorMod(y, 16)][Math.floorMod(z, 16)] = blockData;
	}

	public int getBlockId(int x, int y, int z)
	{
		return getIds()[Math.floorMod(x, 16)][Math.floorMod(y, 16)][Math.floorMod(z, 16)];
	}

	public Block getBlock(int x, int y, int z)
	{
		return BlockRegistry.getBlockById(getBlockId(x, y, z));
	}

	public void setBlock(int x, int y, int z, int id)
	{
		getIds()[Math.floorMod(x, 16)][Math.floorMod(y, 16)][Math.floorMod(z, 16)] = id;
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

		if (x >= 0 && x < 16 && z >= 0 && z < 16 && y >= 0 && y < 16)
		{
			return getBlock(x, y, z);
		} else
		{
			if (x == 16)
			{
				SubChunk sc = getWorld().getSubChunk(getX() + 1, getLayer(), getZ());
				if (sc == null)
					return Block.air;
				return sc.getBlockEfficiently(0, y, z);
			} else if (x == -1)
			{
				SubChunk sc = getWorld().getSubChunk(getX() - 1, getLayer(), getZ());
				if (sc == null)
					return Block.air;
				return sc.getBlockEfficiently(15, y, z);
			}

			if (z == 16)
			{
				SubChunk sc = getWorld().getSubChunk(getX(), getLayer(), getZ() + 1);
				if (sc == null)
					return Block.air;
				return sc.getBlockEfficiently(x, y, 0);
			} else if (z == -1)
			{
				SubChunk sc = getWorld().getSubChunk(getX(), getLayer(), getZ() - 1);
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
