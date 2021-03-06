package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.properties.EnumProperty;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.enums.EnumAxis;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.World;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 05.07.2020
 * Project: CaveGame
 *
 ***********************/
public class PillarBlock extends Block
{
	public static final EnumProperty<EnumAxis> AXIS = States.AXIS;

	public PillarBlock(JSONObject json)
	{
		super(json);
		setDefaultState(getDefaultState().with(AXIS, EnumAxis.Y).get());
	}

	@Override
	public BlockState getStateForPlacement(World world, Player player, EnumFace placedOn, int x, int y, int z)
	{
		if (placedOn == null)
			return getDefaultState();

		return switch (placedOn)
			{
				case UP, DOWN -> getDefaultState().with(AXIS, EnumAxis.Y).get();
				case NORTH, SOUTH -> getDefaultState().with(AXIS, EnumAxis.X).get();
				case EAST, WEST -> getDefaultState().with(AXIS, EnumAxis.Z).get();
				case NONE -> getDefaultState();
			};
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(AXIS);
	}
}
