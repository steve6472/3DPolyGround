package com.steve6472.polyground.world.biomes.registry;

import com.steve6472.polyground.block.model.registry.Cube;
import org.joml.AABBf;

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
	private static final HashMap<String, BiomeEntry<? extends Cube>> cubeRegistry = new HashMap<>();

	public static <T extends Cube> BiomeEntry<T> register(String id, IBiomeFactory<T> factory)
	{
		BiomeEntry<T> entry = new BiomeEntry<>(factory);
		cubeRegistry.put(id, entry);
		return entry;
	}

	public static Cube createCube(String id, AABBf aabb)
	{
		return cubeRegistry.get(id).createNew(aabb);
	}

	public static Collection<BiomeEntry<? extends Cube>> getEntries()
	{
		return cubeRegistry.values();
	}

	public static Set<String> getKeys()
	{
		return cubeRegistry.keySet();
	}
}
