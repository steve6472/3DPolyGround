package steve6472.polyground.item.special;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.Player;
import steve6472.polyground.item.Item;
import steve6472.polyground.registry.WaterRegistry;
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
public class WaterBucketItem extends Item
{
	public WaterBucketItem(File f, int id)
	{
		super(f, id);
	}

	@Override
	public void onClick(World world, BlockState state, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getAction() == KeyList.PRESS)
		{
			if (click.getButton() == KeyList.RMB)
			{
				int blockId = world.getState(x + clickedOn.getXOffset(), y + clickedOn.getYOffset(), z + clickedOn.getZOffset()).getId();
				if (WaterRegistry.volumes[blockId] > 0)
				{
					double liquid = world.getLiquidVolume(x + clickedOn.getXOffset(), y + clickedOn.getYOffset(), z + clickedOn.getZOffset());
					world.setLiquidVolume(x + clickedOn.getXOffset(), y + clickedOn.getYOffset(), z + clickedOn.getZOffset(), liquid + 1000.0);
				}
			}
		}
	}
}
