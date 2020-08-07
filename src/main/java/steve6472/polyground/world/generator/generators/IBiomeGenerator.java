package steve6472.polyground.world.generator.generators;

import steve6472.polyground.world.biomes.Biome;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.07.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public interface IBiomeGenerator
{
	int getBiomeId(int x, int y, int z);

	Biome getBiome(int x, int y, int z);

	long getSeed();
}
