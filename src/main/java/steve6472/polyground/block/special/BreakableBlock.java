package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.item.ItemEntity;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.item.Item;
import steve6472.polyground.registry.Items;
import steve6472.polyground.world.World;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;
import steve6472.sge.main.util.RandomUtil;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 20.10.2020
 * Project: CaveGame
 *
 ***********************/
public class BreakableBlock extends CustomBlock
{
	private Item item;
	int minCount, maxCount;
	float distance;
	boolean randomRotation;

	public BreakableBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public void load(JSONObject json)
	{
		item = Items.getItemByName(json.getString("item"));
		if (json.optBoolean("random_count"))
		{
			minCount = json.getInt("min");
			maxCount = json.getInt("max");
			if (minCount > maxCount)
			{
				maxCount = 1;
				minCount = 1;
			}
		} else
		{
			maxCount = json.optInt("count", 1);
			minCount = maxCount;
		}
		distance = json.optFloat("distance", 0);
		randomRotation = json.optBoolean("random_rotation", false);
	}

	@Override
	public void onClick(BlockState state, World world, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getButton() == KeyList.LMB)
		{
			spawnLoot(world, state, x, y, z);
			world.setBlock(Block.AIR, x, y, z);
		}
	}

	@Override
	public void spawnLoot(World world, BlockState state, int x, int y, int z)
	{
		for (int i = 0; i < RandomUtil.randomInt(minCount, maxCount); i++)
		{
			ItemEntity item = new ItemEntity(this.item, null, x + 0.5f, y, z + 0.5f);

			if (distance > 0)
				item.addPosition(RandomUtil.randomFloat(-distance, distance), 0, RandomUtil.randomFloat(-distance, distance));

			if (randomRotation)
				item.setYaw((float) RandomUtil.randomRadian());

			world.getEntityManager().addEntity(item);
		}
	}
}
