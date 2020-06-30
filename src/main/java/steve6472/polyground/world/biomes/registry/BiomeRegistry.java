package steve6472.polyground.world.biomes.registry;

import steve6472.polyground.world.biomes.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class BiomeRegistry
{
	private static int ID = 0;
	private static final HashMap<String, BiomeEntry<? extends Biome>> biomeRegistry = new HashMap<>();

	public static final BiomeEntry<VoidBiome> voidBiome = register("void", VoidBiome::new);
	public static final BiomeEntry<ForestBiome> forest = register("forest", ForestBiome::new);
	public static final BiomeEntry<DesertBiome> desert = register("desert", DesertBiome::new);
	public static final BiomeEntry<OceanBiome> ocean = register("ocean", OceanBiome::new);
//	public static final BiomeEntry<OceanBiome> plains = register("plains", OceanBiome::new);

	public static <T extends Biome> BiomeEntry<T> register(String id, IBiomeFactory<T> factory)
	{
		BiomeEntry<T> entry = new BiomeEntry<>(factory, ID++);
		biomeRegistry.put(id, entry);
		return entry;
	}

	public static Biome getBiome(int id)
	{
		for (BiomeEntry<? extends Biome> entry : getEntries())
		{
			if (entry.getId() == id)
				return entry.getInstance();
		}
		return voidBiome.getInstance();
	}

	public static BiomeEntry<? extends Biome> getBiomeEntry(String name)
	{
		return biomeRegistry.get(name);
	}

	public static Collection<BiomeEntry<? extends Biome>> getEntries()
	{
		return biomeRegistry.values();
	}

	public static Set<String> getKeys()
	{
		return biomeRegistry.keySet();
	}
}
