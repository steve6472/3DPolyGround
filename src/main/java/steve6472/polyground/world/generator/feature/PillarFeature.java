package steve6472.polyground.world.generator.feature;


import org.json.JSONObject;
import steve6472.polyground.world.World;
import steve6472.polyground.world.generator.EnumPlacement;
import steve6472.polyground.world.generator.Feature;
import steve6472.polyground.world.generator.feature.components.match.IBlockMatch;
import steve6472.polyground.world.generator.feature.components.match.Match;
import steve6472.polyground.world.generator.feature.components.provider.IBlockProvider;
import steve6472.polyground.world.generator.feature.components.provider.Provider;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.08.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public class PillarFeature extends Feature
{
	private IBlockMatch blockUnder;
	private IBlockProvider blockToPlace;
	private int minHeight;
	private int maxHeight;

	public PillarFeature() {}

	@Override
	public void load(JSONObject json)
	{
		blockUnder = Match.match(json.getJSONObject("block_under"));
		blockToPlace = Provider.provide(json.getJSONObject("block"));
		minHeight = json.getInt("min_height");
		maxHeight = json.getInt("max_height");
	}

	@Override
	public void generate(World world, int x, int y, int z)
	{
		int height = world.getRandom().nextInt(maxHeight - minHeight + 1) + minHeight;

		for (int i = y; i < y + height; i++)
		{
			world.setState(blockToPlace.getState(world, x, i, z), x, i, z, 5);
		}
	}

	/**
	 * Specifies how many chunks in square radius have to be present for this feature to generate
	 * max is 3
	 * 0 is self
	 *
	 * @return size
	 */
	@Override
	public int size()
	{
		return 0;
	}

	@Override
	public boolean canGenerate(World world, int x, int y, int z)
	{
		return blockUnder.matches(world.getState(x, y - 1, z));
	}

	@Override
	public EnumPlacement getPlacement()
	{
		return EnumPlacement.ON_HEIGHT_MAP;
	}
}
