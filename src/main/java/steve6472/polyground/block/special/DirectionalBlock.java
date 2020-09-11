package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.properties.EnumProperty;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.World;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 11.09.2020
 * Project: CaveGame
 *
 ***********************/
public class DirectionalBlock extends Block
{
	public static final EnumProperty<EnumFace> FACING = States.FACING;

	public DirectionalBlock(JSONObject json)
	{
		super(json);
		setDefaultState(getDefaultState().with(FACING, EnumFace.UP).get());
	}

	@Override
	public BlockState getStateForPlacement(World world, Player player, EnumFace placedOn, int x, int y, int z)
	{
		if (placedOn == null)
			return getDefaultState();

		return getDefaultState().with(FACING, placedOn).get();
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(FACING);
	}
}
