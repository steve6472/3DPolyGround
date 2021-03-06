package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.properties.BooleanProperty;
import steve6472.polyground.block.properties.IProperty;
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
public class StateTest extends CustomBlock
{
	private static final BooleanProperty LIT = States.LIT;

	public StateTest(JSONObject json)
	{
		super(json);
		isFull = false;
		setDefaultState(getDefaultState().with(LIT, false).get());
	}

	@Override
	public void onClick(BlockState state, World world, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getButton() == KeyList.RMB && click.getAction() == KeyList.PRESS)
		{
			world.setState(state.with(LIT, !state.get(LIT)).get(), x, y, z);
			player.processNextBlockPlace = false;
		}
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(LIT);
	}
}
