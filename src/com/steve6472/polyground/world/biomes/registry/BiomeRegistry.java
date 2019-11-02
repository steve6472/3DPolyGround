package com.steve6472.polyground.world.biomes.registry;

import com.steve6472.polyground.world.biomes.Biome;
import com.steve6472.polyground.world.biomes.BlueLand;
import com.steve6472.polyground.world.biomes.GreenLand;
import com.steve6472.polyground.world.biomes.RedLand;

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
	private static final HashMap<String, BiomeEntry<? extends Biome>> biomeRegistry = new HashMap<>();

	public static final BiomeEntry<RedLand> redLand = register("red_land", RedLand::new);
	public static final BiomeEntry<GreenLand> greenLand = register("green_land", GreenLand::new);
	public static final BiomeEntry<BlueLand> blueLand = register("blue_land", BlueLand::new);

	public static <T extends Biome> BiomeEntry<T> register(String id, IBiomeFactory<T> factory)
	{
		BiomeEntry<T> entry = new BiomeEntry<>(factory);
		biomeRegistry.put(id, entry);
		return entry;
	}

	public static Biome createCube(String id)
	{
		return biomeRegistry.get(id).createNew();
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
