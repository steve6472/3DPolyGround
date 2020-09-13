package steve6472.polyground.item.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.HitResult;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.player.EnumGameMode;
import steve6472.polyground.entity.player.EnumSlot;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.item.Item;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.World;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.09.2020
 * Project: CaveGame
 *
 ***********************/
public class BlockItem extends Item
{
	private final Block block;

	public BlockItem(JSONObject json, int id)
	{
		super(json, id);

		block = Blocks.getBlockByName(json.optJSONObject("special").optString("block", "error"));
	}

	@Override
	public void onClick(World world, BlockState state, Player player, EnumSlot slot, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getAction() != KeyList.PRESS || click.getButton() != KeyList.RMB || player.getGamemode() != EnumGameMode.CREATIVE)
			return;

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
				world.setState(stateToPlace, X, Y, Z, 5);
			}
		}
	}

	public Block getBlock()
	{
		return block;
	}
}
