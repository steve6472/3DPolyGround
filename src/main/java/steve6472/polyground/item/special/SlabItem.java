package steve6472.polyground.item.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.properties.enums.EnumSlabType;
import steve6472.polyground.block.special.SlabBlock;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.player.EnumSlot;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.item.Item;
import steve6472.polyground.world.World;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 24.09.2019
 * Project: SJP
 *
 ***********************/
public class SlabItem extends Item
{
	public SlabItem(JSONObject json, int id)
	{
		super(json, id);
	}

	@Override
	public void onClick(World world, BlockState state, Player player, EnumSlot slot, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getAction() == KeyList.PRESS && click.getButton() == KeyList.RMB)
		{
			if (!player.processNextBlockPlace)
				return;

			BlockState placed = world.getState(x + clickedOn.getXOffset(), y + clickedOn.getYOffset(), z + clickedOn.getZOffset());

			if (!(placed.getBlock() instanceof SlabBlock))
				return;

			if (placed.get(SlabBlock.TYPE) != EnumSlabType.DOUBLE)
			{
				world.setState(placed.with(SlabBlock.TYPE, EnumSlabType.DOUBLE).with(SlabBlock.AXIS, placed.get(SlabBlock.AXIS)).get(),
					x + clickedOn.getXOffset(), y + clickedOn.getYOffset(), z + clickedOn.getZOffset());
			}
		}
	}
}
