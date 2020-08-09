package steve6472.polyground.world.generator.generators.impl.flat;

import steve6472.polyground.world.biomes.Biome;
import steve6472.polyground.world.generator.generators.IBiomeGenerator;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 07.08.2020
 * Project: CaveGame
 *
 ***********************/
public class SingleBiomeGen implements IBiomeGenerator
{
	private final long seed;
	private final Biome biome;
	private final int biomeId;

	public SingleBiomeGen(long seed, Biome biome)
	{
		this.seed = seed;
		this.biome = biome;
		this.biomeId = biome.getId();
	}

	@Override
	public int getBiomeId(int x, int y, int z)
	{
		return biomeId;
	}

	@Override
	public Biome getBiome(int x, int y, int z)
	{
		return biome;
	}

	@Override
	public long getSeed()
	{
		return seed;
	}
}
