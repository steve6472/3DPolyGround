package steve6472.polyground.world.generator;

import steve6472.polyground.world.biomes.Biome;

import java.util.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.07.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public class ChunkGenData
{
	HashMap<Biome, HashMap<EnumFeatureStage, List<FeatureGenEntry>>> data;
	HashMap<EnumFeatureStage, Integer> maxRange;
	EnumFeatureStage stage;
	ChunkGenDataStorage storage;
	int x, y, z;

	private ChunkGenData(EnumFeatureStage stage)
	{
		this.stage = stage;
	}

	private ChunkGenData init(ChunkGenDataStorage storage, int x, int y, int z)
	{
		data = new HashMap<>();
		maxRange = new HashMap<>();
		this.storage = storage;
		this.x = x;
		this.y = y;
		this.z = z;

		return this;
	}

	public void addFeature(Biome biome, EnumFeatureStage stage, FeatureEntry feature)
	{
		if (!data.containsKey(biome))
			data.put(biome, new HashMap<>());

		HashMap<EnumFeatureStage, List<FeatureGenEntry>> biomeData = data.get(biome);

		if (biomeData.containsKey(stage))
		{
			biomeData.get(stage).add(new FeatureGenEntry(feature));
			maxRange.put(stage, Math.max(maxRange.get(stage), feature.getFeature().size()));
		} else
		{
			List<FeatureGenEntry> list = new ArrayList<>();
			list.add(new FeatureGenEntry(feature));
			biomeData.put(stage, list);
			maxRange.put(stage, feature.getFeature().size());
		}
	}

	public boolean isRangeInStage(int range)
	{
		Integer i = maxRange.get(stage);
		if (i == null)
			return false;
		return i <= range;
	}

	public Collection<Biome> getBiomes()
	{
		return data.keySet();
	}

	public List<FeatureGenEntry> getFeatures(Biome biome, EnumFeatureStage stage)
	{
		return data.get(biome).get(stage);
	}

	public boolean isStageGenerated(Biome biome, EnumFeatureStage stage)
	{
		if (data.containsKey(biome))
			return !data.get(biome).containsKey(stage);

		return true;
	}

	public void removeGeneratedFeatures()
	{
		for (Iterator<Biome> biomeIterator = data.keySet().iterator(); biomeIterator.hasNext();)
		{
			Biome biome = biomeIterator.next();

			for (Iterator<EnumFeatureStage> stageIterator = data.get(biome).keySet().iterator(); stageIterator.hasNext();)
			{
				EnumFeatureStage stage = stageIterator.next();

				if (data.get(biome).containsKey(stage))
				{
					data.get(biome).get(stage).removeIf(FeatureGenEntry::isGenerated);
				}

				if (data.get(biome).get(stage).isEmpty())
					stageIterator.remove();
			}

			if (data.get(biome).isEmpty())
				biomeIterator.remove();
		}
	}

	public boolean isFullyGenerated()
	{
		return data == null || data.isEmpty();
	}

	public void nextStage()
	{
//		System.out.println(this);
		boolean flag = false;
		for (EnumFeatureStage s : EnumFeatureStage.getValues())
		{
			if (s == stage)
				flag = true;

			if (!flag)
				continue;

			boolean isBiomeGenerated = true;
			for (Biome b : data.keySet())
			{
//				System.out.println("\t" + b + " " + !isStageGenerated(b, s));
				if (!isStageGenerated(b, s))
				{
					isBiomeGenerated = false;
				}
			}

			if (isBiomeGenerated)
			{
				increseStage();
			} else
			{
//				System.out.println(stage);
				return;
			}
		}
//		System.out.println(stage);
//		System.out.println("\n");
	}

	public void increseStage()
	{
		if (stage == EnumFeatureStage.FINISHED)
		{
			storage.markSubChunkAsGenerated(x, y, z);
		} else
		{
			stage = EnumFeatureStage.getValues()[stage.ordinal() + 1];
		}
	}

	public static ChunkGenData createNewData(ChunkGenDataStorage storage, int x, int y, int z)
	{
		return new ChunkGenData(EnumFeatureStage.NONE).init(storage, x, y, z);
	}

	public static ChunkGenData createGenerated()
	{
		return new ChunkGenData(EnumFeatureStage.FINISHED);
	}

	public static ChunkGenData createNotGenerated()
	{
		return new ChunkGenData(EnumFeatureStage.NONE);
	}

	@Override
	public String toString()
	{
		return "ChunkGenData{" + "x=" + x + ", y=" + y + ", z=" + z + ", data=" + data + ", stage=" + stage + '}';
	}

	public static class FeatureGenEntry
	{
		private final FeatureEntry feature;
		private boolean generated;

		private FeatureGenEntry(FeatureEntry feature)
		{
			this.feature = feature;
			this.generated = false;
		}

		public FeatureEntry getFeature()
		{
			return feature;
		}

		public void markAsGenerated()
		{
			this.generated = true;
		}

		public boolean isGenerated()
		{
			return generated;
		}

		@Override
		public String toString()
		{
			return "FeatureGenEntry{" + "feature=" + feature + ", generated=" + generated + '}';
		}
	}
}
