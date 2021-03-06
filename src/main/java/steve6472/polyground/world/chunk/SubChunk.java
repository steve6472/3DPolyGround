package steve6472.polyground.world.chunk;

import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.TimedBlockPos;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.IWorld;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.gfx.ModelData;
import steve6472.polyground.gfx.ThreadedModelBuilder;
import steve6472.polyground.gui.InGameGui;
import steve6472.polyground.world.World;
import steve6472.polyground.world.biomes.Biome;
import steve6472.polyground.world.biomes.IBiomeProvider;
import steve6472.polyground.world.generator.EnumChunkStage;

import java.io.IOException;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class SubChunk implements IBiomeProvider
{
	private static final int MODEL_COUNT = ModelLayer.values().length;

	private final SubChunkModel[] model;
	private final Chunk parent;
	public EnumChunkStage stage;

	private final int layer;

	private boolean rebuild;

	private final int[][][] biomes;
	private final SubChunkBlocks blocks;
	private final SubChunkBlockData blockData;
	private final SubChunkWater water;

	private final ChunkPosStorage tickableBlocks, specialRender;

	public SubChunk(Chunk parent, int layer)
	{
		stage = EnumChunkStage.NONE;
		this.parent = parent;
		this.layer = layer;

		model = new SubChunkModel[MODEL_COUNT];
		for (int i = 0; i < MODEL_COUNT; i++)
		{
			model[i] = new SubChunkModel(ModelLayer.values()[i], this);
		}

		biomes = new int[16][16][16];

		tickableBlocks = new ChunkPosStorage();
		specialRender = new ChunkPosStorage();

		blockData = new SubChunkBlockData();
		blocks = new SubChunkBlocks();
		water = new SubChunkWater(this);
	}

	public void tick()
	{
		tickableBlocks.tick();
		if (!tickableBlocks.isEmpty())
		{
			tickableBlocks.iterate((x, y, z) ->
			{
				BlockState blockToTick = blocks.getStates()[x][y][z];
				blockToTick.getBlock().tick(blockToTick, getWorld(), x + getX() * 16, y + getLayer() * 16, z + getZ() * 16);
			});
		}

		specialRender.tick();

//		randomTicks.tick();
		for (int i = 0; i < getWorld().getGame().options.randomTicks; i++)
		{
			int x = getWorld().getRandom().nextInt(16);
			int y = getWorld().getRandom().nextInt(16);
			int z = getWorld().getRandom().nextInt(16);

			BlockState stateToTick = blocks.getStates()[x][y][z];
			Block blockToTick = stateToTick.getBlock();

			if (blockToTick == Block.AIR)
				continue;

			if (CaveGame.getInstance().options.renderRandomTicks)
				CaveGame.getInstance().options.renderRandomTicksList.add(new TimedBlockPos(x + getX() * 16, y + getLayer() * 16, z + getZ() * 16, 30));


			if (blockToTick.randomTickable())
				blockToTick.randomTick(stateToTick, getWorld(), x + getX() * 16, y + getLayer() * 16, z + getZ() * 16);
		}

		water.tick();

		if (water.isActive())
			InGameGui.waterActive++;
	}

	public void saveSubChunk() throws IOException
	{
		if (stage == EnumChunkStage.FINISHED)
			ChunkSerializer.serialize(this);
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

	public ChunkPosStorage getSpecialRender()
	{
		return specialRender;
	}

	public void rebuild()
	{
		rebuild = true;
	}

	public void tryRebuild(ThreadedModelBuilder builder)
	{
		if (!rebuild)
			return;

		for (SubChunkModel m : model)
			builder.addToQueue(m);

		rebuild = false;
	}

	public boolean isRebuilding()
	{
		for (SubChunkModel m : model)
			if (m.rebuildInProgress)
				return true;
		return false;
	}

	public void updateModel(ModelData data)
	{
		model[data.modelLayer.index].update(data);
	}

	public void updateNeighbours()
	{
		SubChunk sc;
		for (EnumFace f : EnumFace.getFaces())
			if ((sc = getWorld().getSubChunk(getX() + f.getXOffset(), layer + f.getYOffset(), getZ() + f.getZOffset())) != null) sc.rebuild();
	}

	/*
	 * Blocks
	 */

	public Block getBlock(int x, int y, int z)
	{
		return getState(x, y, z).getBlock();
	}

	public BlockState getState(int x, int y, int z)
	{
		return blocks.getState(x, y, z);
	}

	public void setBlock(Block block, int x, int y, int z)
	{
		setState(block.getDefaultState(), x, y, z);
	}

	public void setState(BlockState state, int x, int y, int z)
	{
		blocks.setState(state, x, y, z);
	}

	/*
	 * Liquid
	 */

	public double getLiquidVolume(int x, int y, int z)
	{
		return water.getLiquidVolume(x, y, z);
	}

	public void setLiquidVolume(int x, int y, int z, double volume)
	{
		water.setLiquidVolume(x, y, z, volume);
	}

	/*
	 * Biome
	 */

	public int getBiomeId(int x, int y, int z)
	{
		return biomes[x][y][z];
	}

	public Biome getBiome(int x, int y, int z)
	{
		return getWorld().biomes.getBiome(getBiomeId(x, y, z));
	}

	public void setBiome(int biomeId, int x, int y, int z)
	{
		biomes[x][y][z] = biomeId;
	}

	public void setBiome(Biome biome, int x, int y, int z)
	{
		setBiome(biome.getId(), x, y, z);
	}

	/*
	 * Data
	 */

	public BlockData[][][] getBlockData()
	{
		return blockData.getBlockData();
	}

	public BlockData getBlockData(int x, int y, int z)
	{
		return blockData.getBlockData(x, y, z);
	}

	public void setBlockData(BlockData blockData, int x, int y, int z)
	{
		this.blockData.setBlockData(blockData, x, y, z);

		if (blockData instanceof IWorld w)
			w.setWorld(getWorld(), x + getX() * 16, y + getLayer() * 16, z + getZ() * 16);
	}

	public SubChunkBlockData getBlockDataContainer()
	{
		return blockData;
	}

	@Override
	public String toString()
	{
		return "SubChunk{" + "stage=" + stage + ", layer=" + layer + '}';
	}
}
