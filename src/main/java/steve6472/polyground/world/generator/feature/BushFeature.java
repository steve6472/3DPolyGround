package steve6472.polyground.world.generator.feature;

import org.json.JSONObject;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.World;
import steve6472.polyground.world.generator.EnumPlacement;
import steve6472.polyground.world.generator.Feature;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.05.2020
 * Project: CaveGame
 *
 ***********************/
public class BushFeature extends Feature
{
	private BlockState blockUnder, log, leaves;
	private boolean undergroundBush;

	public BushFeature(BlockState blockUnder, BlockState log, BlockState leaves, boolean undergroundBush)
	{
		this.blockUnder = blockUnder;
		this.log = log;
		this.leaves = leaves;
		this.undergroundBush = undergroundBush;
	}

	@Override
	public void load(JSONObject json)
	{
		blockUnder = Blocks.loadStateFromJSON(json.getJSONObject("block_under"));
		log = Blocks.loadStateFromJSON(json.getJSONObject("log"));
		leaves = Blocks.loadStateFromJSON(json.getJSONObject("leaves"));
		undergroundBush = json.optBoolean("underground", false);
	}

	@Override
	public void generate(World world, int x, int y, int z)
	{
		world.setState(log, x, y + 1, z);
		world.setState(log, x, y + 2, z);
		world.setState(leaves, x, y + 3, z);
		world.setState(leaves, x + 1, y + 2, z, (b) -> b.getBlock() == Block.AIR);
		world.setState(leaves, x - 1, y + 2, z, (b) -> b.getBlock() == Block.AIR);
		world.setState(leaves, x, y + 2, z + 1, (b) -> b.getBlock() == Block.AIR);
		world.setState(leaves, x, y + 2, z - 1, (b) -> b.getBlock() == Block.AIR);
	}

	@Override
	public int size()
	{
		return undergroundBush ? 0 : 1;
	}

	@Override
	public boolean canGenerate(World world, int x, int y, int z)
	{
		if (undergroundBush)
		{
			// Don't generate on border!
			if (Math.abs(x) % 16 == 0 || Math.abs(x) % 16 == 15 || Math.abs(z) % 16 == 0 || Math.abs(z) % 16 == 15)
				return false;
			return world.getState(x, y, z) == blockUnder && world.getBlock(x, y + 1, z) == Block.AIR && world.getBlock(x, y + 2, z) == Block.AIR && world.getBlock(x, y + 3, z) == Block.AIR;
		} else
		{
			return world.getState(x, y, z) == blockUnder;
		}
	}

	@Override
	public EnumPlacement getPlacement()
	{
		return undergroundBush ? EnumPlacement.IN_GROUND : EnumPlacement.IN_HEIGHT_MAP;
	}

	@Override
	public String toString()
	{
		return "BushFeature{" + "blockUnder=" + blockUnder + ", log=" + log + ", leaves=" + leaves + ", generateOnBorder=" + undergroundBush + '}';
	}
}
