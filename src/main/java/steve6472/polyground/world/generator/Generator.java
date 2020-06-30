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
		Thread generatorThread = new Thread()
		{
			@Override
			public void run()
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
			}
		};

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

		int range = 5;

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
					world.addChunk(newChunk, true);
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
		boolean gen = false;

		if (sc.lastFeatureStage == FeatureStage.NONE)
			sc.lastFeatureStage = FeatureStage.LAND_ALTER;

		if (sc.areFeaturesGeneratedForStage(sc.lastFeatureStage))
		{
			moveStage(sc);
			return false;
		}

		HashMap<Biome, LinkedHashSet<IFeature>> generated = new HashMap<>();

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				if (sc.getParent().heightMap[i][j] == -1)
					continue;

				if (sc.getParent().heightMap[i][j] / 16 != sc.getLayer())
					continue;

				int y;
				Biome biome = BiomeRegistry.getBiome(sc.getBiomeId(i, y = (sc.getParent().heightMap[i][j] % 16), j));

				if (sc.isBiomeGenerated(biome))
					continue;

				if (sc.areFeaturesGeneratedForStage(biome, sc.lastFeatureStage))
					continue;

				for (FeatureEntry e : biome.getFeatures().get(sc.lastFeatureStage))
				{
					if (e.feature.getPlacement() != EnumFeaturePlacement.IN_HEIGHT_MAP && e.feature.getPlacement() != EnumFeaturePlacement.ON_HEIGHT_MAP)
						continue;

					if (sc.isFeatureGenerated(biome, sc.lastFeatureStage, e.feature))
						continue;

					if (maxRange >= e.feature.size())
					{
						if (random.nextDouble() < e.chance)
						{
							if (e.feature.canGenerate(sc, i, y + (e.feature.getPlacement() == EnumFeaturePlacement.ON_HEIGHT_MAP ? 1 : 0), j))
							{
								e.feature.generate(sc, i, y + (e.feature.getPlacement() == EnumFeaturePlacement.ON_HEIGHT_MAP ? 1 : 0), j);
							}
						}

						if (generated.containsKey(biome))
						{
							generated.get(biome).add(e.feature);
						} else
						{
							LinkedHashSet<IFeature> list = new LinkedHashSet<>();
							list.add(e.feature);
							generated.put(biome, list);
						}

						gen = true;
					}
				}
			}
		}

		if (!generated.isEmpty())
		{
//			System.out.println("sc: " + sc.getX() + " " + sc.getLayer() + " " + sc.getZ() + " " + sc.lastFeatureStage);

			for (Biome b : generated.keySet())
			{/*
				System.out.println("\t" + b.getName());
				for (IFeature f : generated.get(b))
				{
					System.out.println("\t\t" + f.getClass().getSimpleName());
				}*/
				sc.markAsGenerated(b, sc.lastFeatureStage, generated.get(b));
			}
		}

/*
		if (!sc.featuresToGenerate.isEmpty())
		{
			List<FeatureEntry> toRemove = new ArrayList<>();

			for (int i = 0; i < 16; i++)
			{
				for (int j = 0; j < 16; j++)
				{
					if (sc.getParent().heightMap[i][j] == -1)
						continue;

					if (sc.getParent().heightMap[i][j] / 16 != sc.getLayer())
						continue;

					int y;
					Biome biome = BiomeRegistry.getBiome(sc.getBiomeId(i, y = (sc.getParent().heightMap[i][j] % 16), j));
					for (FeatureEntry ce : sc.featuresToGenerate)
					{
						for (FeatureEntry e : biome.getFeatures())
						{
							if (e.feature.getPlacement() != EnumFeaturePlacement.IN_HEIGHT_MAP && e.feature.getPlacement() != EnumFeaturePlacement.ON_HEIGHT_MAP)
								continue;

							if (ce != e)
								continue;

							if (maxRange >= e.feature.size())
							{
								if (random.nextDouble() < e.chance)
								{
									if (e.feature.canGenerate(sc, i, y + (e.feature.getPlacement() == EnumFeaturePlacement.ON_HEIGHT_MAP ? 1 : 0), j))
									{
										e.feature.generate(sc, i, y + (e.feature.getPlacement() == EnumFeaturePlacement.ON_HEIGHT_MAP ? 1 : 0), j);
									}
								}
								if (!toRemove.contains(e))
									toRemove.add(e);
								gen = true;
							}
						}
					}
				}
			}
			for (FeatureEntry fe : toRemove)
			{
				sc.featuresToGenerate.remove(fe);
			}
		} else
		{
			for (int i = 0; i < 16; i++)
			{
				for (int j = 0; j < 16; j++)
				{
					if (sc.getParent().heightMap[i][j] == -1)
						continue;

					if (sc.getParent().heightMap[i][j] / 16 != sc.getLayer())
						continue;

					int y;
					Biome biome = BiomeRegistry.getBiome(sc.getBiomeId(i, y = (sc.getParent().heightMap[i][j] % 16), j));
					for (FeatureEntry e : biome.getFeatures())
					{
						if (e.feature.getPlacement() == EnumFeaturePlacement.IN_HEIGHT_MAP || e.feature.getPlacement() == EnumFeaturePlacement.ON_HEIGHT_MAP)
						{
							if (maxRange >= e.feature.size())
							{
								if (random.nextDouble() < e.chance)
								{
									if (e.feature.canGenerate(sc, i, y + (e.feature.getPlacement() == EnumFeaturePlacement.ON_HEIGHT_MAP ? 1 : 0), j))
									{
										e.feature.generate(sc, i, y + (e.feature.getPlacement() == EnumFeaturePlacement.ON_HEIGHT_MAP ? 1 : 0), j);
									}
								}
								gen = true;
							} else
							{
								if (!sc.featuresToGenerate.contains(e))
								{
									sc.featuresToGenerate.add(e);
								}
							}
						}
					}
				}
			}
		}*/

		moveStage(sc);

		return gen;
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
