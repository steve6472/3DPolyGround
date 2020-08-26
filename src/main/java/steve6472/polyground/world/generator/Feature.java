package steve6472.polyground.world.generator;

import org.json.JSONObject;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 28.07.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public abstract class Feature
{
	public Feature()
	{
	}

	public abstract void load(JSONObject json);

	public abstract void generate(World world, int x, int y, int z);

	/**
	 * Specifies how many chunks in square radius have to be present for this feature to generate
	 * max is 3
	 * 0 is self
	 *
	 * @return size
	 */
	public abstract int size();

	public abstract boolean canGenerate(World world, int x, int y, int z);

	public abstract EnumPlacement getPlacement();
}
