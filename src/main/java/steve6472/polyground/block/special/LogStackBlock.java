package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.IntProperty;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.World;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 04.09.2020
 * Project: CaveGame
 *
 ***********************/
public class LogStackBlock extends CustomBlock
{
	public static IntProperty LOGS = States.LOGS;

	public LogStackBlock(JSONObject json)
	{
		super(json);
		setDefaultState(getDefaultState().with(LOGS, 6).get());
	}

	@Override
	public BlockState getStateForPlacement(World world, Player player, EnumFace placedOn, int x, int y, int z)
	{
		return super.getStateForPlacement(world, player, placedOn, x, y, z).with(LOGS, world.getRandom().nextInt(6) + 1).get();
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(LOGS);
	}
}
