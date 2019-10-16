package com.steve6472.polyground.block.model.registry;

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
public class CubeRegistry
{
	private static final HashMap<String, CubeEntry<? extends Cube>> cubeRegistry = new HashMap<>();

	public static final CubeEntry<Cube> cubes = register("cubes", Cube::new);
	public static final CubeEntry<TintedCube> tintedCubes = register("tintedCubes", TintedCube::new);

	public static <T extends Cube> CubeEntry<T> register(String id, ICubeFactory<T> factory)
	{
		CubeEntry<T> entry = new CubeEntry<>(factory);
		cubeRegistry.put(id, entry);
		return entry;
	}

	public static Cube createCube(String id, AABBf aabb)
	{
		return cubeRegistry.get(id).createNew(aabb);
	}

	public static Collection<CubeEntry<? extends Cube>> getEntries()
	{
		return cubeRegistry.values();
	}

	public static Set<String> getKeys()
	{
		return cubeRegistry.keySet();
	}
}
