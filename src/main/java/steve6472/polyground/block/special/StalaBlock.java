package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.IntProperty;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.World;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 03.07.2020
 * Project: CaveGame
 *
 ***********************/
public class StalaBlock extends CustomBlock
{
	public static final IntProperty WIDTH = States.STALA_WIDTH;

	public StalaBlock(JSONObject json)
	{
		super(json);
		isFull = false;
		setDefaultState(getDefaultState().with(WIDTH, 1).get());
	}

	@Override
	public void onPlayerBreak(BlockState state, World world, Player player, EnumFace breakedFrom, int x, int y, int z)
	{
		if (player.getGamemode().spawBlockLoot)
		{
			for (int i = 0; i < state.get(WIDTH); i++)
			{
				spawnLoot(world, state, x, y, z);
			}
		}
	}

	@Override
	public void onClick(BlockState state, World world, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (!clickedOn.isSide())
			return;


		if (click.getAction() == KeyList.PRESS && click.getButton() == KeyList.RMB)
		{
			{
				int width = state.get(WIDTH);

				if (width < 7)
				{
					world.setState(state.with(WIDTH, width + 1).get(), x, y, z);
					player.processNextBlockPlace = false;
				}
			}
		} else if (click.getAction() == KeyList.PRESS && click.getButton() == KeyList.LMB)
		{
			int width = state.get(WIDTH);
			if (width > 1)
			{
				world.setState(state.with(WIDTH, width - 1).get(), x, y, z);
				player.processNextBlockBreak = false;
				if(player.gamemode.spawBlockLoot)
				{
					spawnLoot(world, state, x, y, z);
				}
			}
		}
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(WIDTH);
	}
}
