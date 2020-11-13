package steve6472.polyground.item.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.HitResult;
import steve6472.polyground.MouseClick;
import steve6472.polyground.block.properties.enums.EnumSlabType;
import steve6472.polyground.block.special.SlabBlock;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.World;

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
	public void onClick(World world, Player player, MouseClick click)
	{
		if (!click.clickRMB())
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
				if (stateToPlace.getBlock().isValidPosition(click.getState(), world, X, Y, Z))
				{
					placedBlock = true;
					world.setState(stateToPlace, X, Y, Z, 1);
				}
			}
		}

		player.processNextBlockPlace = false;

		if (!placedBlock)
		{
			BlockState placed = world.getState(click.getOffsetX(), click.getOffsetY(), click.getOffsetZ());

			if (placed.getBlock() != getBlock())
			{
				return;
			}

			if (placed.get(SlabBlock.TYPE) != EnumSlabType.DOUBLE)
			{
				world.setState(placed.with(SlabBlock.TYPE, EnumSlabType.DOUBLE).with(SlabBlock.AXIS, placed.get(SlabBlock.AXIS)).get(),
					click.getOffsetX(), click.getOffsetY(), click.getOffsetZ());
			}
		}
	}
}
