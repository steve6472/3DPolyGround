package steve6472.polyground.item.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.HitResult;
import steve6472.polyground.block.properties.enums.EnumSlabType;
import steve6472.polyground.block.special.SlabBlock;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.item.Palette;
import steve6472.polyground.entity.player.EnumGameMode;
import steve6472.polyground.entity.player.EnumSlot;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.World;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 24.09.2019
 * Project: SJP
 *
 ***********************/
public class SlabItem extends BlockItem
{
	public SlabItem(JSONObject json, int id)
	{
		super(json, id);
	}

	@Override
	public void onClick(World world, BlockState state, Player player, EnumSlot slot, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getAction() != KeyList.PRESS || click.getButton() != KeyList.RMB)
		{
			return;
		}

		boolean placedBlock = false;

		if (player.getHitResult().isHit())
		{
			if (player.processNextBlockPlace)
			{
				HitResult hr = player.getHitResult();

				EnumFace face = hr.getFace();
				int X = hr.getX() + face.getXOffset();
				int Y = hr.getY() + face.getYOffset();
				int Z = hr.getZ() + face.getZOffset();

				BlockState stateToPlace = getBlock().getStateForPlacement(world, player, face, X, Y, Z);
				if (stateToPlace.getBlock().isValidPosition(state, world, X, Y, Z))
				{
					if (player.getGamemode() == EnumGameMode.SURVIVAL)
					{
						Palette palette = player.getHoldedPalette();
						if (palette != null)
						{
							palette.removeItem();
						}
					}
					placedBlock = true;
					world.setState(stateToPlace, X, Y, Z, 1);
				}
			}
		}

		player.processNextBlockPlace = false;

		if (!placedBlock)
		{
			BlockState placed = world.getState(x + clickedOn.getXOffset(), y + clickedOn.getYOffset(), z + clickedOn.getZOffset());

			if (placed.getBlock() != getBlock())
			{
				return;
			}

			if (placed.get(SlabBlock.TYPE) != EnumSlabType.DOUBLE)
			{
				if (player.getGamemode() == EnumGameMode.SURVIVAL)
				{
					Palette palette = player.getHoldedPalette();
					if (palette != null)
					{
						palette.removeItem();
					}
				}
				world.setState(placed.with(SlabBlock.TYPE, EnumSlabType.DOUBLE).with(SlabBlock.AXIS, placed.get(SlabBlock.AXIS)).get(),
					x + clickedOn.getXOffset(), y + clickedOn.getYOffset(), z + clickedOn.getZOffset());
			}
		}
	}
}
