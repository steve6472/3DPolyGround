package steve6472.polyground.item.special;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.Player;
import steve6472.polyground.item.Item;
import steve6472.polyground.registry.WaterRegistry;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class VoidBucketItem extends Item
{
	public VoidBucketItem(File f, int id)
	{
		super(f, id);
	}

	@Override
	public void onClick(SubChunk subChunk, BlockState state, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getAction() == KeyList.PRESS)
		{
			if (click.getButton() == KeyList.RMB)
			{
				int blockId = subChunk.getBlock(x + clickedOn.getXOffset() - subChunk.getX() * 16, y + clickedOn.getYOffset() - subChunk.getLayer() * 16, z + clickedOn.getZOffset() - subChunk.getZ() * 16).getId();
				if (WaterRegistry.volumes[blockId] > 0)
					subChunk.setLiquidVolumeEfficiently(x + clickedOn.getXOffset() - subChunk.getX() * 16, y + clickedOn.getYOffset() - subChunk.getLayer() * 16, z + clickedOn.getZOffset() - subChunk.getZ() * 16, 0.0);
			}
		}
	}
}
