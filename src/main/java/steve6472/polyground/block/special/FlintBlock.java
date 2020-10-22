package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.World;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 20.10.2020
 * Project: CaveGame
 *
 ***********************/
public class FlintBlock extends FourDirectionalBlock
{
	public FlintBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public void onClick(BlockState state, World world, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getButton() != KeyList.LMB || click.getAction() != KeyList.PRESS)
			return;

		if (!player.holdsItem())
			return;

		if (!player.getItemInHand().getName().startsWith("hammerstone"))
			return;

		player.heldItem.remove(player);

		world.setBlock(Blocks.getBlockByName("flint_knapping"), x, y, z);
	}
}
