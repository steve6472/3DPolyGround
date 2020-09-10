package steve6472.polyground.block.special;

import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 10.08.2020
 * Project: CaveGame
 *
 ***********************/
public class TagBelowBlock extends CustomBlock
{
	private String[] tags;
	private boolean and = false;

	public TagBelowBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public void load(JSONObject json)
	{
		JSONArray arr = json.getJSONArray("tags");
		tags = new String[arr.length()];
		for (int i = 0; i < arr.length(); i++)
		{
			tags[i] = arr.getString(i);
		}
		and = json.getString("operation").equals("and");
	}

	@Override
	public void neighbourChange(BlockState state, World world, EnumFace updateFrom, int x, int y, int z)
	{
		if (!isValidPosition(state, world, x, y, z))
		{
			spawnLoot(state, world, x, y, z);
			world.setBlock(Block.air, x, y, z);
		}
	}

	@Override
	public boolean isValidPosition(BlockState state, World world, int x, int y, int z)
	{
		BlockState st = world.getState(x, y - 1, z);
		if (and)
		{
			for (String s : tags)
			{
				if (!st.hasTag(s))
					return false;
			}
			return super.isValidPosition(state, world, x, y, z);
		} else
		{
			for (String s : tags)
			{
				if (st.hasTag(s))
					return super.isValidPosition(state, world, x, y, z);
			}
			return false;
		}
	}
}
