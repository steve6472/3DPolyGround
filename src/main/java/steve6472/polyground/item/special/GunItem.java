package steve6472.polyground.item.special;

import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.MouseClick;
import steve6472.polyground.entity.Bullet;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.item.Item;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class GunItem extends Item
{
	public GunItem(JSONObject json, int id)
	{
		super(json, id);
	}

	@Override
	public void onClick(Player player, MouseClick click)
	{
		if (click.clickLMB())
		{
			CaveGame.getInstance().getWorld().getEntityManager().addEntity(new Bullet());
		}
	}

	@Override
	public void onClick(World world, Player player, MouseClick click)
	{
		if (click.clickLMB())
		{
			player.processNextBlockBreak = false;
		}
	}
}
