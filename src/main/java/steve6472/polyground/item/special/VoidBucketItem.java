package steve6472.polyground.item.special;

import org.json.JSONObject;
import steve6472.polyground.MouseClick;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.item.Item;
import steve6472.polyground.registry.WaterRegistry;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class VoidBucketItem extends Item
{
	public VoidBucketItem(JSONObject json, int id)
	{
		super(json, id);
	}

	@Override
	public void onClick(World world, Player player, MouseClick click)
	{
		if (click.clickRMB())
		{
			int blockId = world.getState(click.getOffsetX(), click.getOffsetY(), click.getOffsetZ()).getId();
			if (WaterRegistry.volumes[blockId] > 0)
				world.setLiquidVolume(click.getOffsetX(), click.getOffsetY(), click.getOffsetZ(), 0.0);
		}
	}
}
