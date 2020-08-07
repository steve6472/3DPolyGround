package steve6472.polyground.world.generator;

import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 28.07.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public interface IFeature
{
	void generate(World world, int x, int y, int z);

	/**
	 * Specifies how many chunks in square radius have to be present for this feature to generate
	 * max is 3
	 * 0 is self
	 *
	 * @return size
	 */
	int size();

	boolean canGenerate(World world, int x, int y, int z);

	EnumPlacement getPlacement();
}
