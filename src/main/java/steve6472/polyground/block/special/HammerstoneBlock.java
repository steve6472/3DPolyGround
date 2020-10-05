package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.properties.EnumProperty;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.enums.EnumStoneType;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.item.ItemEntity;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.World;
import steve6472.sge.main.events.MouseEvent;
import steve6472.sge.main.util.RandomUtil;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 05.10.2020
 * Project: CaveGame
 *
 ***********************/
public class HammerstoneBlock extends Block
{
	public static final EnumProperty<EnumStoneType> STONE_TYPE = States.STONE_TYPE;

	public HammerstoneBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public void onClick(BlockState state, World world, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		world.setBlock(Block.air, x, y, z);
		ItemEntity e = new ItemEntity(null, item, x + 0.5f, y + 0.25f, z + 0.5f);
		world.getEntityManager().addEntity(e);
	}

	@Override
	public BlockState getStateForPlacement(World world, Player player, EnumFace placedOn, int x, int y, int z)
	{
		return getDefaultState().with(STONE_TYPE, EnumStoneType.getValues()[RandomUtil.randomInt(0, 2)]).get();
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(STONE_TYPE);
	}
}
