package steve6472.polyground.world.generator.generators;

import steve6472.polyground.world.biomes.Biome;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.polyground.world.generator.ChunkGenData;
import steve6472.polyground.world.generator.ChunkGenDataStorage;
import steve6472.polyground.world.generator.EnumFeatureStage;
import steve6472.polyground.world.generator.FeatureEntry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.07.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public interface ISurfaceGenerator
{
	void load(int cx, int cz);

	void generate(SubChunk subChunk);

	IHeightMapGenerator getHeightMapGenerator();

	ChunkGenDataStorage getDataStorage();

	default void setChunkData(Biome biome, int cx, int layer, int cz)
	{
		ChunkGenDataStorage storage = getDataStorage();

		if (!storage.isChunkPresent(cx, cz))
			storage.createChunk(cx, cz);

		ChunkGenData data = storage.getData(cx, layer, cz);

		if (data == ChunkGenDataStorage.GENERATED || data == ChunkGenDataStorage.NOT_GENERATED)
			return;

		if (data.getBiomes().contains(biome))
			return;

		for (EnumFeatureStage stage : EnumFeatureStage.getValues())
		{
			for (FeatureEntry feature : biome.getFeaturesForStage(stage))
			{
				data.addFeature(biome, stage, feature);
			}
		}
	}
}
