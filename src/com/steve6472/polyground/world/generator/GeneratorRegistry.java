package com.steve6472.polyground.world.generator;

import java.util.HashMap;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.09.2019
 * Project: SJP
 *
 ***********************/
public class GeneratorRegistry
{
	HashMap<String, IGenerator> generators;

	public GeneratorRegistry()
	{
		generators = new HashMap<>();

		generators.put("simplex", new SimplexGenerator());
		generators.put("flat", new FlatGenerator());
		generators.put("world", new WorldGenerator());
		generators.put("cave", new CaveGenerator());
	}

	public IGenerator getGenerator(String name)
	{
		return generators.get(name);
	}
}
