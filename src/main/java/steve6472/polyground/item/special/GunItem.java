package steve6472.polyground.item.special;

import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.Bullet;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.item.Item;
import steve6472.polyground.world.World;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class GunItem extends Item
{
	public GunItem(File f, int id)
	{
		super(f, id);
	}

	@Override
	public void onClick(Player player, MouseEvent click)
	{
		if (click.getAction() == KeyList.PRESS)
		{
			if (click.getButton() == KeyList.LMB)
			{
				CaveGame.getInstance().getWorld().getEntityManager().addEntity(new Bullet());
			}
		}
	}

	@Override
	public void onClick(World world, BlockState state, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getAction() == KeyList.PRESS)
		{
			if (click.getButton() == KeyList.LMB)
			{
				player.processNextBlockBreak = false;
			}
		}
	}
}
