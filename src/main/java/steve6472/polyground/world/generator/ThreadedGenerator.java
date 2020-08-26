package steve6472.polyground.world.generator;

import steve6472.polyground.world.World;
import steve6472.polyground.world.biomes.Biome;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.polyground.world.generator.generators.IBiomeGenerator;
import steve6472.polyground.world.generator.generators.IHeightMapGenerator;
import steve6472.polyground.world.generator.generators.ISurfaceGenerator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.07.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public class ThreadedGenerator extends Thread
{
	private final BlockingQueue<SubChunk> chunkQueue;
	private boolean terminateRequested;

	private final IBiomeGenerator biomeGenerator;
	private final IHeightMapGenerator heightMapGenerator;
	private final ISurfaceGenerator surfaceGenerator;
	private final ChunkGenDataStorage chunkDataStorage;

	public ThreadedGenerator(World world, IBiomeGenerator biomeGenerator, IHeightMapGenerator heightMapGenerator, Function<ChunkGenDataStorage, ISurfaceGenerator> surfaceGenerator)
	{
		super("Generator");
		chunkDataStorage = new ChunkGenDataStorage(world.getHeight());

		this.biomeGenerator = biomeGenerator;
		this.heightMapGenerator = heightMapGenerator;
		this.surfaceGenerator = surfaceGenerator.apply(chunkDataStorage);

		chunkQueue = new LinkedBlockingQueue<>();
	}

	@Override
	public void run()
	{
		while (!terminateRequested)
		{
			try
			{
				SubChunk chunk = chunkQueue.take();
				generate(chunk);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
				terminate();
			}
		}
	}

	public void addToQueue(SubChunk chunk)
	{
		try
		{
			if (!chunkQueue.contains(chunk))
				chunkQueue.put(chunk);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			terminate();
		}
	}

	public void terminate()
	{
		terminateRequested = true;
	}

	/* Generation */

	private void generate(SubChunk chunk)
	{
		if (chunk.stage == EnumChunkStage.SHAPE || chunk.stage == EnumChunkStage.FEATURES)
			generateFeature(chunk);
		if (chunk.stage == EnumChunkStage.NONE)
			generateShape(chunk);
	}

	private void generateShape(SubChunk subChunk)
	{
		surfaceGenerator.load(subChunk.getX(), subChunk.getZ());
		surfaceGenerator.generate(subChunk);

		subChunk.stage = EnumChunkStage.SHAPE;

		// For chunks with biomes with no features
		ChunkGenData data = chunkDataStorage.getData(subChunk.getX(), subChunk.getLayer(), subChunk.getZ());

		if (data != ChunkGenDataStorage.NOT_GENERATED && data != ChunkGenDataStorage.GENERATED)
		{
			data.nextStage();
			if (data.stage == EnumFeatureStage.FINISHED)
			{
				subChunk.stage = EnumChunkStage.FINISHED;
			}
		}

		subChunk.rebuild();
		subChunk.updateNeighbours();
	}

	private void generateFeature(SubChunk subChunk)
	{
		subChunk.stage = EnumChunkStage.FEATURES;

		int maxRange = findMaxRange(3, subChunk.getX(), subChunk.getZ());

		boolean generatedFeature = false;

		ChunkGenData data = chunkDataStorage.getData(subChunk.getX(), subChunk.getLayer(), subChunk.getZ());

		if (data == ChunkGenDataStorage.NOT_GENERATED || data == ChunkGenDataStorage.GENERATED)
			return;

		data.nextStage();
		if (data.stage == EnumFeatureStage.FINISHED)
		{
			subChunk.stage = EnumChunkStage.FINISHED;
			return;
		}

		EnumFeatureStage stage = data.stage;

		if (!data.isRangeInStage(maxRange))
			return;

		if (stage == EnumFeatureStage.AIR || stage == EnumFeatureStage.CAVE_ALTER)
		{
			for (int x = 0; x < 16; x++)
			{
				for (int y = 0; y < 16; y++)
				{
					for (int z = 0; z < 16; z++)
					{
						Biome biome = subChunk.getWorld().getBiome(x + subChunk.getX() * 16, y + subChunk.getLayer() * 16, z + subChunk.getZ() * 16);

						// Biome does not have ANY features
						if (data.data.get(biome) == null)
							continue;

						// This biome does not contain any features for this stage, skip this biome
						if (data.getFeatures(biome, stage) == null)
							continue;

						for (ChunkGenData.FeatureGenEntry featureGenEntry : data.getFeatures(biome, stage))
						{
							FeatureEntry featureEntry = featureGenEntry.getFeature();
							Feature feature = featureEntry.getFeature();

							if (feature.size() <= maxRange)
							{
								if (subChunk.getWorld().getRandom().nextDouble() <= featureEntry.getChance())
								{
									if (feature.getPlacement() == EnumPlacement.IN_AIR || feature.getPlacement() == EnumPlacement.IN_GROUND)
									{
										if (feature.canGenerate(subChunk.getWorld(), x + subChunk.getX() * 16, y + subChunk.getLayer() * 16, z + subChunk.getZ() * 16))
										{
											feature.generate(subChunk.getWorld(), x + subChunk.getX() * 16, y + subChunk.getLayer() * 16, z + subChunk.getZ() * 16);
										}
										generatedFeature = true;
									}
								}

								featureGenEntry.markAsGenerated();
							}
						}
					}
				}
			}
		} else
		{
			for (int x = 0; x < 16; x++)
			{
				for (int z = 0; z < 16; z++)
				{
					Biome biome = subChunk.getWorld().getBiome(x + subChunk.getX() * 16, subChunk.getParent().heightMap[x][z], z + subChunk.getZ() * 16);

					// Biome does not have ANY features
					if (data.data.get(biome) == null)
						continue;

					// This biome does not contain any features for this stage, skip this biome
					if (data.getFeatures(biome, stage) == null)
						continue;

					for (ChunkGenData.FeatureGenEntry featureGenEntry : data.getFeatures(biome, stage))
					{
						FeatureEntry featureEntry = featureGenEntry.getFeature();
						Feature feature = featureEntry.getFeature();

						if (feature.size() <= maxRange)
						{
							if (subChunk.getWorld().getRandom().nextDouble() <= featureEntry.getChance())
							{
								int height = -1;
								if (subChunk.getParent().heightMap[x][z] / 16 == subChunk.getLayer() && feature.getPlacement() == EnumPlacement.IN_HEIGHT_MAP)
								{
									height = subChunk.getParent().heightMap[x][z];
									//       generate only if y is in correct layer
								} else if ((subChunk.getParent().heightMap[x][z] + 1) / 16 == subChunk.getLayer() && feature.getPlacement() == EnumPlacement.ON_HEIGHT_MAP)
								{
									height = subChunk.getParent().heightMap[x][z] + 1;
								} else if (feature.getPlacement() == EnumPlacement.IN_AIR)
								{
									height = subChunk.getParent().heightMap[x][z];
								}

								if (height != -1)
								{
									if (feature.canGenerate(subChunk.getWorld(), x + subChunk.getX() * 16, height, z + subChunk.getZ() * 16))
									{
										feature.generate(subChunk.getWorld(), x + subChunk.getX() * 16, height, z + subChunk.getZ() * 16);
									}
									generatedFeature = true;
								}
							}

							featureGenEntry.markAsGenerated();
						}
					}
				}
			}
		}

		data.removeGeneratedFeatures();
		data.nextStage();
		if (data.stage == EnumFeatureStage.FINISHED)
			subChunk.stage = EnumChunkStage.FINISHED;

		if (generatedFeature)
		{
			subChunk.rebuild();
			subChunk.updateNeighbours();
		}

	}

	private int findMaxRange(int maxRadius, int cx, int cz)
	{
		for (int i = maxRadius; i > 0; i--)
		{
			if (check(i, cx, cz))
				return i;
		}
		return 0;
	}

	private boolean check(int radius, int cx, int cz)
	{
		for (int i = -radius; i <= radius; i++)
		{
			for (int j = -radius; j <= radius; j++)
			{
				if (!chunkDataStorage.isChunkPresent(i + cx, j + cz))
				{
					return false;
				}
			}
		}
		return true;
	}
}
