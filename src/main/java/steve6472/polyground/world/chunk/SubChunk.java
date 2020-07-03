package steve6472.polyground.world.chunk;

import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.gui.InGameGui;
import steve6472.polyground.world.World;
import steve6472.polyground.world.biomes.Biome;
import steve6472.polyground.world.biomes.IBiomeProvider;
import steve6472.polyground.world.generator.FeatureStage;
import steve6472.polyground.world.generator.IGenerator;
import steve6472.polyground.world.generator.feature.FeatureEntry;
import steve6472.polyground.world.generator.feature.IFeature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import static steve6472.sge.gfx.VertexObjectCreator.unbindVAO;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class SubChunk implements IBiomeProvider
{
	private static final int MODEL_COUNT = ModelLayer.values().length;
	public LinkedHashMap<Biome, BiomeEntry> presentBiomes;
	public HashMap<FeatureStage, Integer> maxRange;

	private final SubChunkModel[] model;
	private final Chunk parent;
	public EnumChunkState state;
	public FeatureStage lastFeatureStage;

	private final int layer;

	private boolean shouldRebuild;

	private final int[][][] biomes;
	private final SubChunkBlocks blocks;
	private final SubChunkBlockData blockData;
	private final SubChunkWater water;

	private final ChunkPosStorage tickableBlocks, scheduledUpdates, newScheduledUpdates;

	public SubChunk(Chunk parent, int layer)
	{
		presentBiomes = new LinkedHashMap<>();
		maxRange = new HashMap<>();
		state = EnumChunkState.NOT_GENERATED;
		lastFeatureStage = FeatureStage.NONE;
		this.parent = parent;
		this.layer = layer;

		model = new SubChunkModel[MODEL_COUNT];
		for (int i = 0; i < MODEL_COUNT; i++)
		{
			model[i] = new SubChunkModel(ModelLayer.values()[i], this);
			SubChunkBuilder.init(model[i]);
		}

		biomes = new int[16][16][16];

		tickableBlocks = new ChunkPosStorage();
		scheduledUpdates = new ChunkPosStorage();
		newScheduledUpdates = new ChunkPosStorage();

		blockData = new SubChunkBlockData(this);
		blocks = new SubChunkBlocks(this);
		water = new SubChunkWater(this);
	}

	/* Biome and features */

	public void addBiome(Biome biome)
	{
		if (!this.presentBiomes.containsKey(biome))
		{
			this.presentBiomes.put(biome, new BiomeEntry(biome));

			for (FeatureStage stage : biome.getFeatures().keySet())
			{
				for (FeatureEntry entry : biome.getFeatures().get(stage))
				{
					if (maxRange.containsKey(stage))
					{
						int min = Math.max(maxRange.get(stage), entry.feature.size());
						maxRange.replace(stage, min);
					} else
					{
						maxRange.put(stage, entry.feature.size());
					}
				}
			}
		}
	}

	public boolean isBiomeGenerated(Biome biome)
	{
		if (presentBiomes.containsKey(biome))
		{
			return presentBiomes.get(biome).isFullyGenerated;
		}
		return true;
	}

	public boolean areFeaturesGeneratedForStage(FeatureStage stage)
	{
		for (BiomeEntry e : presentBiomes.values())
		{
			if (e.generated.get(stage) == null)
				return true;

			for (Boolean value : e.generated.get(stage).values())
			{
				if (!value)
					return false;
			}
			return true;
		}
		return true;
	}

	public boolean areFeaturesGeneratedForStage(Biome biome, FeatureStage stage)
	{
		if (presentBiomes.containsKey(biome))
		{
			BiomeEntry e = presentBiomes.get(biome);
			if (!e.generated.containsKey(stage))
				return true;
			for (Boolean value : e.generated.get(stage).values())
			{
				if (!value)
					return false;
			}
			return true;
			}
		return true;
	}

	public boolean isFeatureGenerated(Biome biome, FeatureStage stage, IFeature feature)
	{
		if (!presentBiomes.containsKey(biome))
			return false;

		BiomeEntry e = presentBiomes.get(biome);
		return e.generated.get(stage).get(feature);
	}

	public void markAsGenerated(Biome biome, FeatureStage stage, LinkedHashSet<IFeature> features)
	{
		BiomeEntry entry = presentBiomes.get(biome);

		if (entry == null)
			return;

		for (IFeature feature : features)
		{
			HashMap<IFeature, Boolean> map = entry.generated.get(stage);
			if (map == null)
				return;

			if (map.containsKey(feature))
				map.replace(feature, true);
		}
	}

	/* -------------------------- */

	public static IGenerator generator;

	public void generate()
	{
		generator.generate(this);
		state = EnumChunkState.SHAPE;
	}

	public void tick()
	{
		if (!tickableBlocks.isEmpty())
		{
			tickableBlocks.iterate((x, y, z) ->
			{
				Block blockToTick = blocks.getStates()[x][y][z].getBlock();
//				blockToTick.tick(this, blockData.getBlockData(x, y, z), x, y, z);
			});
		}

		scheduledUpdates.addAll(newScheduledUpdates);
		newScheduledUpdates.clear();

		if (!scheduledUpdates.isEmpty())
		{
			for (Iterator<Short> iter = scheduledUpdates.iterator(); iter.hasNext(); )
			{
				short i = iter.next();

				short x = (short) (i >> 8);
				short y = (short) ((i >> 4) & 0xf);
				short z = (short) (i & 0xf);

				Block blockToUpdate = blocks.getStates()[x][y][z].getBlock();
//				blockToUpdate.onUpdate(this, blockData.getBlockData(x, y, z), EnumFace.NONE, x, y, z);
				iter.remove();
			}
		}

		water.tick();

		if (water.isActive())
			InGameGui.waterActive++;
	}

	public void saveSubChunk() throws IOException
	{
		if (state == EnumChunkState.FULL)
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
		if (!shouldRebuild)
			return;

		if (MainRender.CHUNK_REBUILT >= CaveGame.getInstance().options.maxChunkRebuild && CaveGame.getInstance().options.maxChunkRebuild != -1)
			return;

		MainRender.CHUNK_REBUILT++;

		long start = System.nanoTime();

		if (CaveGame.getInstance().options.lightDebug)
		{
			getWorld().getGame().mainRender.particles.getMap().values().forEach(list -> list.forEach(particle ->
			{
				if (particle.hasTag("DebugLight" + getX() + "_" + getLayer() + "_" + getZ()))
				{
					particle.forcedDeath = true;
				}
			}));
		}

		for (int i = 0; i < MODEL_COUNT; i++)
		{
			model[i].rebuild(getParent().getWorld().getGame().options.fastChunkBuild);
		}

		/* Can be called only once after finishing updating all sub-chunk models */
		unbindVAO();

		long end = System.nanoTime();

		if (CaveGame.getInstance().options.subChunkBuildTime)
			CaveGame.getInstance().inGameGui.chat.addText("SubChunk " + getX() + "/" + getLayer() + "/" + getZ() + " Took " + (end - start) / 1000000.0 + "ms to build");

		if (CaveGame.getInstance().options.chunkModelDebug)
			System.out.println("");

		shouldRebuild = false;
	}

	public void forceRebuild()
	{
		rebuildAllLayers();
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

	@Deprecated
	public BlockData[][][] getBlockData()
	{
		return blockData.getBlockData();
	}

	@Deprecated
	public BlockData getBlockData(int x, int y, int z)
	{
		if (x >= 0 && x < 16 && z >= 0 && z < 16 && y >= 0 && y < 16)
			return getBlockData()[x][y][z];
		else
			return null;
	}

	/**
	 * Can check neighbour chunks.
	 * Should be more efficient for chunk border block data checking
	 * as it does not have to create new Chunk Key everytime
	 *
	 * @param x x coordinate of light
	 * @param y y coordinate of light
	 * @param z z coordinate of light
	 * @return int Light
	 */
	@Deprecated
	public BlockData getBlockDataEfficiently(int x, int y, int z)
	{
		return blockData.getBlockDataEfficiently(x, y, z);
	}

	@Deprecated
	public void setBlockEntity(int x, int y, int z, BlockData blockData)
	{
		getBlockData()[x][y][z] = blockData;
	}

	public SubChunk getNeighbouringSubChunk(int x, int y, int z)
	{
		EnumFace faceX = x >= 16 ? EnumFace.NORTH : x <= -1 ? EnumFace.SOUTH : EnumFace.NONE;
		EnumFace faceZ = z >= 16 ? EnumFace.EAST : z <= -1 ? EnumFace.WEST : EnumFace.NONE;
		EnumFace faceY = y >= 16 ? EnumFace.UP : y <= -1 ? EnumFace.DOWN : EnumFace.NONE;

		int layer = getLayer() + faceY.getYOffset();

		if (layer < 0 || layer >= parent.getSubChunks().length)
			return null;

		if (faceX == EnumFace.NONE && faceZ == EnumFace.NONE)
		{
			return parent.getSubChunk(layer);
		} else if (faceZ == EnumFace.NONE)
		{
			Chunk chunk = parent.getNeighbouringChunk(faceX);
			if (chunk == null)
				return null;
			else
				return parent.getNeighbouringChunk(faceX).getSubChunk(layer);
		} else if (faceX == EnumFace.NONE)
		{
			Chunk chunk = parent.getNeighbouringChunk(faceZ);
			if (chunk == null)
				return null;
			else
				return parent.getNeighbouringChunk(faceZ).getSubChunk(layer);
		} else
		{
			Chunk chunk = parent.getNeighbouringChunk(faceX);
			if (chunk == null)
			{
				chunk = parent.getNeighbouringChunk(faceZ);
			}
			if (chunk == null)
			{
				return null;
			} else
			{
				Chunk chunk_ = chunk.getNeighbouringChunk(faceZ);
				if (chunk_ == null)
					chunk_ = chunk.getNeighbouringChunk(faceX);
				if (chunk_ == null)
					return null;
				return chunk_.getSubChunk(layer);
			}
		}
	}

	public void rebuildAllLayers()
	{
		for (SubChunkModel m : model)
		{
			m.setShouldUpdate(true);
		}
		setShouldRebuild(true);
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
		setState(block.getStateForPlacement(this, x, y, z), x, y, z);
	}

	public void setState(BlockState state, int x, int y, int z)
	{
		blocks.setState(state, x, y, z);
	}

	/*
	 * Liquid
	 */

	public double getLiquidVolumeEfficiently(int x, int y, int z)
	{
		return water.getLiquidVolumeEfficiently(x, y, z);
	}

	public double getLiquidVolume(int x, int y, int z)
	{
		return water.getLiquidVolume(x, y, z);
	}

	public void setLiquidVolume(int x, int y, int z, double volume)
	{
		water.setLiquidVolume(x, y, z, volume);
	}

	public void setLiquidVolumeEfficiently(int x, int y, int z, double volume)
	{
		water.setLiquidVolumeEfficiently(x, y, z, volume);
	}

	public boolean shouldUpdate()
	{
		return shouldRebuild;
	}

	public void setShouldRebuild(boolean shouldRebuild)
	{
		this.shouldRebuild = shouldRebuild;
	}

	@Override
	public String toString()
	{
		return "SubChunk{" + "parent=" + parent + ", state=" + state + ", lastFeatureStage=" + lastFeatureStage + ", layer=" + layer + ", shouldRebuild=" + shouldRebuild + '}';
	}

	public class BiomeEntry
	{
		public HashMap<FeatureStage, HashMap<IFeature, Boolean>> generated;
		public boolean isFullyGenerated;

		public BiomeEntry(Biome biome)
		{
			this.generated = new HashMap<>(FeatureStage.getValues().length);
			for (FeatureStage key : biome.getFeatures().keySet())
			{
				for (FeatureEntry value : biome.getFeatures().get(key))
				{
					if (generated.containsKey(key))
					{
						generated.get(key).put(value.feature, false);
					} else
					{
						HashMap<IFeature, Boolean> map = new HashMap<>();
						map.put(value.feature, false);
						generated.put(key, map);
					}
				}
			}
			isFullyGenerated = generated.size() == 0;
		}
	}
}
