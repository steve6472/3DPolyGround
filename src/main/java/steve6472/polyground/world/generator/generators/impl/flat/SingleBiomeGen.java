package steve6472.polyground.world.generator.generators.impl.flat;

import steve6472.polyground.world.biomes.Biome;
import steve6472.polyground.world.generator.generators.ISetBiomeGenerator;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 07.08.2020
 * Project: CaveGame
 *
 ***********************/
public class SingleBiomeGen implements ISetBiomeGenerator
{
	private final long seed;
	private final List<Biome> biome;

	public SingleBiomeGen(long seed, Biome biome)
	{
		this.seed = seed;
		this.biome = new ArrayList<>();
		this.biome.add(biome);
	}

	@Override
	public Biome getBiome(int x, int y, int z)
	{
		return biome.get(0);
	}

	@Override
	public void setBiomes(List<Biome> biomes)
	{
		throw new UnsupportedOperationException("Single Biome Gen can not be assigned new biomes");
	}

	@Override
	public List<Biome> biomes()
	{
		return biome;
	}

	@Override
	public long getSeed()
	{
		return seed;
	}
}
