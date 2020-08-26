package steve6472.polyground.world.generator.generators;

import steve6472.polyground.world.biomes.Biome;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.07.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public interface IBiomeGenerator
{
	Biome getBiome(int x, int y, int z);

	void setBiomes(List<Biome> biomes);

	List<Biome> biomes();

	long getSeed();
}
