package steve6472.polyground.world.biomes.registry;

import steve6472.polyground.world.biomes.Biome;

public class BiomeEntry<T extends Biome>
{
	private final int id;
	private final IBiomeFactory<T> factory;
	private final T instance;

	BiomeEntry(IBiomeFactory<T> factory, int id)
	{
		this.factory = factory;
		instance = createNew();
		this.id = id;
	}

	public T createNew()
	{
		return factory.create();
	}

	public T getInstance()
	{
		return instance;
	}

	public int getId()
	{
		return id;
	}
}