package steve6472.polyground.world.generator.feature;


import org.json.JSONObject;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.World;
import steve6472.polyground.world.generator.EnumPlacement;
import steve6472.polyground.world.generator.Feature;
import steve6472.polyground.world.generator.feature.components.match.IBlockMatch;
import steve6472.polyground.world.generator.feature.components.match.Match;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.08.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public class StackablePillarFeature extends Feature
{
	private IBlockMatch blockUnder;
	private BlockState blockToPlace;
	private double chanceForNextBlock;
	private int maxHeight;

	public StackablePillarFeature()
	{

	}

	/*public StackablePillarFeature(BlockState blockUnder, BlockState blockToPlace, double chanceForNextBlock, int maxHeight)
	{
		this.blockUnder = blockUnder;
		this.blockToPlace = blockToPlace;
		this.chanceForNextBlock = chanceForNextBlock;
		this.maxHeight = maxHeight;
	}*/

	@Override
	public void load(JSONObject json)
	{
//		blockUnder = Blocks.loadStateFromJSON(json.getJSONObject("block_under"));
		blockUnder = Match.match(json.getJSONObject("block_under"));
		blockToPlace = Blocks.loadStateFromJSON(json.getJSONObject("block"));
		chanceForNextBlock = json.getDouble("next_chance");
		maxHeight = json.getInt("max_height");
	}

	@Override
	public void generate(World world, int x, int y, int z)
	{
		world.setState(blockToPlace, x, y, z);

		for (int i = y; i < y + maxHeight; i++)
		{
			if (world.getRandom().nextDouble() <= chanceForNextBlock)
			{
				world.setState(blockToPlace, x, i, z, 5);
			} else
			{
				return;
			}
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
//		return world.getState(x, y - 1, z) == blockUnder;
	}

	@Override
	public EnumPlacement getPlacement()
	{
		return EnumPlacement.ON_HEIGHT_MAP;
	}
}
