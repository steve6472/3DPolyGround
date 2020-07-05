package steve6472.polyground.item.special;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.properties.enums.EnumSlabType;
import steve6472.polyground.block.special.SlabBlock;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.Player;
import steve6472.polyground.item.Item;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 24.09.2019
 * Project: SJP
 *
 ***********************/
public class SlabItem extends Item
{
	public SlabItem(File f, int id)
	{
		super(f, id);
	}

	@Override
	public void onClick(SubChunk subChunk, BlockState state, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getAction() == KeyList.PRESS && click.getButton() == KeyList.RMB)
		{
			if (!player.processNextBlockPlace)
				return;

			BlockState placed = subChunk.getState(x + clickedOn.getXOffset(), y + clickedOn.getYOffset(), z + clickedOn.getZOffset());

			if (!(placed.getBlock() instanceof SlabBlock))
				return;

			if (placed.get(SlabBlock.TYPE) != EnumSlabType.DOUBLE)
			{
				subChunk.setState(placed.with(SlabBlock.TYPE, EnumSlabType.DOUBLE).with(SlabBlock.AXIS, placed.get(SlabBlock.AXIS)).get(),
					x + clickedOn.getXOffset(), y + clickedOn.getYOffset(), z + clickedOn.getZOffset());
				subChunk.rebuildAllLayers();
			}
		}
	}
}
