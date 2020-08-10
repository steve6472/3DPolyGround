package steve6472.polyground.block.special;

import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.IntProperty;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.Player;
import steve6472.polyground.world.World;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

import java.io.File;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.08.2020
 * Project: CaveGame
 *
 ***********************/
public class SnowLayerBlock extends CustomBlock
{
	public static final IntProperty SNOW_LAYER = States.SNOW_LAYERS;

	public SnowLayerBlock(File f)
	{
		super(f);
		setDefaultState(getDefaultState().with(SNOW_LAYER, 1).get());
	}

	@Override
	public void onPlace(World world, BlockState state, Player player, EnumFace placedOn, int x, int y, int z)
	{
		super.onPlace(world, state, player, placedOn, x, y, z);

		if (world.getBlock(x, y - 1, z) == Block.air)
			world.setBlock(Block.air, x, y, z);
	}

	@Override
	public boolean isReplaceable(BlockState state)
	{
		return state.get(SNOW_LAYER) == 1;
	}

	@Override
	public void onClick(World world, BlockState state, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (clickedOn != EnumFace.UP)
		{
			super.onClick(world, state, player, clickedOn, click, x, y, z);
			return;
		}

		if (click.getAction() == KeyList.PRESS && click.getButton() == KeyList.RMB)
		{
			if (CaveGame.itemInHand.getBlockToPlace() == this)
			{
				int width = state.get(SNOW_LAYER);

				if (width < 8)
				{
					world.setState(state.with(SNOW_LAYER, width + 1).get(), x, y, z);
					player.processNextBlockPlace = false;
				}
			}
		} else if (click.getAction() == KeyList.PRESS && click.getButton() == KeyList.LMB)
		{
			int width = state.get(SNOW_LAYER);
			if (width > 1)
			{
				SnapBlock.activate(state, world, x, y, z);
				world.setState(state.with(SNOW_LAYER, width - 1).get(), x, y, z);
				player.processNextBlockBreak = false;
			}
		}
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(SNOW_LAYER);
	}
}
