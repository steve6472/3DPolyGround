package steve6472.polyground.world.generator;

import steve6472.polyground.CaveGame;
import steve6472.polyground.events.InGameGuiEvent;
import steve6472.polyground.world.World;
import steve6472.polyground.world.biomes.Biome;
import steve6472.polyground.world.biomes.registry.BiomeRegistry;
import steve6472.polyground.world.chunk.Chunk;
import steve6472.polyground.world.chunk.EnumChunkState;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.polyground.world.generator.feature.EnumFeaturePlacement;
import steve6472.polyground.world.generator.feature.FeatureEntry;
import steve6472.polyground.world.generator.feature.IFeature;
import steve6472.sge.main.events.Event;

import java.util.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.05.2020
 * Project: CaveGame
 *
 ***********************/
public class Generator
{
	private final World world;
	private final Random random;

	private final List<SubChunk> toGenerate;
	private SubChunk current;
	private boolean canStart;

	public Generator(World world)
	{
		canStart = true;
		this.world = world;
		random = new Random();
		toGenerate = new ArrayList<>();
		Thread generatorThread = new Thread(() ->
		{
			while (true)
			{
				if (current == null)
				{
					if (!toGenerate.isEmpty())
					{
						current = toGenerate.get(0);

						current.generate();
						current.setShouldRebuild(true);

						toGenerate.remove(current);
						current = null;
					}
				}

				try
				{
					Thread.sleep(5);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		});

		generatorThread.start();
	}

	@Event
	public void draw(InGameGuiEvent.PreRender e)
	{
		//		Font.render(5, 100, "" + toGenerate.size());
		//		Font.render(5, 110, "" + CaveGame.getInstance().world.getChunk((int) Math.floor(CaveGame.getInstance().getPlayer().getX()) >> 4, (int) Math.floor(CaveGame.getInstance().getPlayer().getZ()) >> 4).getSubChunk((int) Math.floor(CaveGame.getInstance().getPlayer().getY()) >> 4));
	}

	byte timer;

	public void tick()
	{
		//		timer++;
		//		if (timer < 10)
		//			return;
		//		timer = 0;
		int px = (int) Math.floor(CaveGame.getInstance().getPlayer().getX()) >> 4;
		int pz = (int) Math.floor(CaveGame.getInstance().getPlayer().getZ()) >> 4;

		int range = 3;

		for (int i = -range; i <= range; i++)
		{
			for (int j = -range; j <= range; j++)
			{
				Chunk c;
				if ((c = world.getChunk(i + px, j + pz)) == null)
				{
					Chunk newChunk = new Chunk(i + px, j + pz, world);
					for (SubChunk sc : newChunk.getSubChunks())
						sc.setShouldRebuild(false);
					world.addChunk(newChunk);
					return;
				} else
				{
					for (SubChunk sc : c.getSubChunks())
					{
						if (sc.state == EnumChunkState.NOT_GENERATED)
						{
							if (!toGenerate.contains(sc))
								toGenerate.add(sc);
							//							sc.generate();
							//							sc.rebuild();
						}
					}

					int maxRange = findMaxRange(3, c.getX(), c.getZ());
					for (SubChunk sc : c.getSubChunks())
					{
						if (sc.state == EnumChunkState.SHAPE || sc.state == EnumChunkState.FEATURES)
						{
							if (generateFeatures(sc, maxRange))
								return;
						}
					}
				}
			}
		}
	}

	private boolean generateFeatures(SubChunk sc, int maxRange)
	{
		sc.state = EnumChunkState.FEATURES;
		boolean gen;

		if (sc.lastFeatureStage == FeatureStage.NONE)
			sc.lastFeatureStage = FeatureStage.getValues()[1];

		if (sc.areFeaturesGeneratedForStage(sc.lastFeatureStage))
		{
			moveStage(sc);
			return false;
		}

		if (sc.maxRange.containsKey(sc.lastFeatureStage))
		{
			if (sc.maxRange.get(sc.lastFeatureStage) > maxRange)
			{
				return false;
			}
		}

		HashMap<Biome, LinkedHashSet<IFeature>> generated = new HashMap<>();

		if (sc.lastFeatureStage.isOnGround())
		{
			gen = onGroundStage(sc, generated, maxRange);
		} else
		{
			gen = inGroundStage(sc, generated, maxRange);
		}

		if (!generated.isEmpty())
		{
			for (Biome b : generated.keySet())
			{
				sc.markAsGenerated(b, sc.lastFeatureStage, generated.get(b));
			}
		}

		moveStage(sc);

		return gen;
	}

	private boolean inGroundStage(SubChunk sc, HashMap<Biome, LinkedHashSet<IFeature>> generated, int maxRange)
	{
		boolean gen = false;

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					Biome biome;
					biome = BiomeRegistry.getBiome(sc.getBiomeId(i, j, k));

					if (sc.isBiomeGenerated(biome))
						continue;

					if (sc.areFeaturesGeneratedForStage(biome, sc.lastFeatureStage))
						continue;

					for (FeatureEntry e : biome.getFeatures().get(sc.lastFeatureStage))
					{
						if (e.feature.getPlacement() == EnumFeaturePlacement.IN_GROUND)
							if (genInGround(sc, e, maxRange, i, j, k, generated))
								gen = true;
					}
				}
			}
		}

		return gen;
	}

	private boolean genInGround(SubChunk sc, FeatureEntry e, int maxRange, int i, int j, int k, HashMap<Biome, LinkedHashSet<IFeature>> generated)
	{
		if (maxRange < e.feature.size())
			return false;

		Biome biome = BiomeRegistry.getBiome(sc.getBiomeId(i, j, k));

		if (sc.isFeatureGenerated(biome, sc.lastFeatureStage, e.feature))
			return false;

//		if (i == 0 && j == 0 && k == 0)
//			System.out.println("Trying to generate " + e.feature);

		if (random.nextDouble() < e.chance)
		{
			if (e.feature.canGenerate(world, i + sc.getX() * 16, j + sc.getLayer() * 16, k + sc.getZ() * 16))
			{
				e.feature.generate(world, i + sc.getX() * 16, j + sc.getLayer() * 16, k + sc.getZ() * 16);
			}
		}

		setAsGenerated(biome, e.feature, generated);

		return true;
	}

	private boolean onGroundStage(SubChunk sc, HashMap<Biome, LinkedHashSet<IFeature>> generated, int maxRange)
	{
		boolean gen = false;

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				if (sc.getParent().heightMap[i][j] == -1)
					continue;

				if (sc.getParent().heightMap[i][j] / 16 != sc.getLayer())
					continue;

				int y;
				Biome biome;
				biome = BiomeRegistry.getBiome(sc.getBiomeId(i, y = (sc.getParent().heightMap[i][j] % 16), j));

				if (sc.isBiomeGenerated(biome))
					continue;

				if (sc.areFeaturesGeneratedForStage(biome, sc.lastFeatureStage))
					continue;

				for (FeatureEntry e : biome.getFeatures().get(sc.lastFeatureStage))
				{
					if (e.feature.getPlacement() == EnumFeaturePlacement.IN_HEIGHT_MAP || e.feature.getPlacement() == EnumFeaturePlacement.ON_HEIGHT_MAP)
					{
						if (genHeightMap(sc, biome, e, maxRange, i, y, j, generated))
							gen = true;
					}
					else if (e.feature.getPlacement() == EnumFeaturePlacement.IN_GROUND)
						if (genInGround(sc, e, maxRange, i, y, j, generated))
							gen = true;
				}
			}
		}

		return gen;
	}

	private boolean genHeightMap(SubChunk sc, Biome biome, FeatureEntry e, int maxRange, int i, int y, int j, HashMap<Biome, LinkedHashSet<IFeature>> generated)
	{
		if (sc.isFeatureGenerated(biome, sc.lastFeatureStage, e.feature))
			return false;

		if (maxRange < e.feature.size())
			return false;

		if (random.nextDouble() < e.chance)
		{
			if (e.feature.canGenerate(world, i + sc.getX() * 16, y + (e.feature.getPlacement() == EnumFeaturePlacement.ON_HEIGHT_MAP ? 1 : 0) + sc.getLayer() * 16, j + sc.getZ() * 16))
			{
				e.feature.generate(world, i + sc.getX() * 16, y + (e.feature.getPlacement() == EnumFeaturePlacement.ON_HEIGHT_MAP ? 1 : 0) + sc.getLayer() * 16, j + sc.getZ() * 16);
			}
		}

		setAsGenerated(biome, e.feature, generated);

		return true;
	}

	private void setAsGenerated(Biome biome, IFeature feature, HashMap<Biome, LinkedHashSet<IFeature>> generated)
	{
		if (generated.containsKey(biome))
		{
			generated.get(biome).add(feature);
		} else
		{
			LinkedHashSet<IFeature> list = new LinkedHashSet<>();
			list.add(feature);
			generated.put(biome, list);
		}
	}

	private void moveStage(SubChunk sc)
	{
		if (sc.lastFeatureStage == FeatureStage.FINISHED)
		{
			sc.state = EnumChunkState.FULL;
			sc.presentBiomes = null;
		}
		else if (sc.areFeaturesGeneratedForStage(sc.lastFeatureStage))
		{
			sc.lastFeatureStage = FeatureStage.getValues()[sc.lastFeatureStage.ordinal() + 1];
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
				Chunk c;
				if ((c = world.getChunk(cx + i, cz + j)) != null)
				{
					for (SubChunk sc : c.getSubChunks())
					{
						if (sc.state == EnumChunkState.NOT_GENERATED)
							return false;
					}
				} else
				{
					return false;
				}
			}
		}
		return true;
	}
}
