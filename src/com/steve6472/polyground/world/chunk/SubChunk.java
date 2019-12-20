package com.steve6472.polyground.world.chunk;

import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.world.World;
import com.steve6472.polyground.world.biomes.IBiomeProvider;
import com.steve6472.polyground.world.generator.IGenerator;

import java.io.IOException;
import java.util.Iterator;

import static com.steve6472.sge.gfx.VertexObjectCreator.unbindVAO;

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

	private int[][][] biomes;
	private SubChunkLight light;
	private SubChunkBlocks blocks;
	private SubChunkBlockData blockData;

	private ChunkPosStorage tickableBlocks, scheduledUpdates, newScheduledUpdates;

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

		biomes = new int[16][16][16];

		tickableBlocks = new ChunkPosStorage();
		scheduledUpdates = new ChunkPosStorage();
		newScheduledUpdates = new ChunkPosStorage();

		blockData = new SubChunkBlockData(this);
		light = new SubChunkLight(this);
		blocks = new SubChunkBlocks(this);

		light.clearLight();
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

		tickableBlocks.iterate((x, y, z) ->
		{
			Block blockToTick = BlockRegistry.getBlockById(blocks.getIds()[x][y][z]);
			blockToTick.tick(this, blockData.getBlockData(x, y, z), x, y, z);
		});

		scheduledUpdates.addAll(newScheduledUpdates);
		newScheduledUpdates.clear();

		for (Iterator<Short> iter = scheduledUpdates.iterator(); iter.hasNext();)
		{
			short i = iter.next();

			short x = (short) (i >> 8);
			short y = (short) ((i >> 4) & 0xf);
			short z = (short) (i & 0xf);

			Block blockToUpdate = BlockRegistry.getBlockById(blocks.getIds()[x][y][z]);
			blockToUpdate.onUpdate(this, blockData.getBlockData(x, y, z), EnumFace.NONE, x, y, z);
			iter.remove();
		}
	}

	public void saveSubChunk() throws IOException
	{
		ChunkSerializer.serialize(this);
	}

	public void addScheduledUpdate(int x, int y, int z)
	{
		short r = (short) (x << 8 | y << 4 | z);
		if (!scheduledUpdates.has(r) && !newScheduledUpdates.has(r))
			newScheduledUpdates.addNonSafe(r);
	}

	public void rebuild()
	{
		long start = System.nanoTime();

		if (CaveGame.getInstance().options.lightDebug)
		{
			CaveGame.getInstance().particles.getMap().values().forEach(list -> list.forEach(particle ->
			{
				if (particle.hasTag("DebugLight" + getX() + "_" + getLayer() + "_" + getZ()))
				{
					particle.forcedDeath = true;
				}
			}));
		}

		clearLight();

		for (int i = 0; i < MODEL_COUNT; i++)
		{
			model[i].rebuild(this);
		}

		/* Can be called only once after finishing updating all sub-chunk models */
		unbindVAO();

		long end = System.nanoTime();

		if (CaveGame.getInstance().options.subChunkBuildTime)
			CaveGame.getInstance().inGameGui.chat.addText("SubChunk " + getX() + "/" + getLayer() + "/" + getZ() + " Took " + (end - start) / 1000000.0 + "ms to build");

		if (CaveGame.getInstance().options.chunkModelDebug)
			System.out.println("");
	}

	public void forceRebuild()
	{
		updateAllLayers();
		rebuild();
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

	public BlockData[][][] getBlockData()
	{
		return blockData.getBlockData();
	}

	public BlockData getBlockData(int x, int y, int z)
	{
		if (x >= 0 && x < 16 && z >= 0 && z < 16 && y >= 0 && y < 16)
			return getBlockData()[x][y][z];
		else
			return null;
	}

	/**
	 *
	 * Can check neighbour chunks.
	 * Should be more efficient for chunk border block data checking
	 * as it does not have to create new Chunk Key everytime
	 *
	 * @param x x coordinate of light
	 * @param y y coordinate of light
	 * @param z z coordinate of light
	 * @return int Light
	 */
	public BlockData getBlockDataEfficiently(int x, int y, int z)
	{
		return blockData.getBlockDataEfficiently(x, y, z);
	}

	public void setBlockEntity(int x, int y, int z, BlockData blockData)
	{
		getBlockData()[x][y][z] = blockData;
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

	public void updateAllLayers()
	{
		for (SubChunkModel m : model)
		{
			m.setShouldUpdate(true);
		}
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

	public ChunkPosStorage getTickableBlocks()
	{
		return tickableBlocks;
	}

	/*
	 * Light
	 */

	public void clearLight()
	{
		light.clearLight();
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
		return light.getLightEfficiently(x, y, z);
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
		light.setLight(x, y, z, color);
	}

	public int getLight(int x, int y, int z)
	{
		return light.getLight(x, y, z);
	}

	/*
	 * Blocks
	 */

	public int[][][] getIds()
	{
		return blocks.getIds();
	}

	public int getBlockId(int x, int y, int z)
	{
		return blocks.getBlockId(x, y, z);
	}

	public Block getBlock(int x, int y, int z)
	{
		return blocks.getBlock(x, y, z);
	}

	public void setBlock(int x, int y, int z, int id)
	{
		blocks.setBlock(x, y, z, id);
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
		return blocks.getBlockEfficiently(x, y, z);
	}

	public void setBlock(int x, int y, int z, Block block)
	{
		blocks.setBlock(x, y, z, block);
	}
}
