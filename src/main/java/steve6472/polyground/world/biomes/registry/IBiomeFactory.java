package steve6472.polyground.world.biomes.registry;

import steve6472.polyground.world.biomes.Biome;

@FunctionalInterface
public interface IBiomeFactory<T extends Biome>
{
	T create();
}