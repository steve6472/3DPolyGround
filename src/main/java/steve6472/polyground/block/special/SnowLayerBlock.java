package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.IntProperty;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.Palette;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.World;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

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

	public SnowLayerBlock(JSONObject json)
	{
		super(json);
		setDefaultState(getDefaultState().with(SNOW_LAYER, 1).get());
		isFull = false;
	}

	@Override
	public void neighbourChange(BlockState state, World world, EnumFace updateFrom, int x, int y, int z)
	{
		if (!isValidPosition(state, world, x, y, z))
		{
			spawnLoot(state, world, x, y, z);
			world.setBlock(Block.air, x, y, z);
		}
	}

	@Override
	public boolean isValidPosition(BlockState state, World world, int x, int y, int z)
	{
		BlockState s = world.getState(x, y - 1, z);
		return (s.getBlock() == this && s.get(SNOW_LAYER) == 8) || s.getBlock().isFull && super.isValidPosition(state, world, x, y, z);
	}

	@Override
	public boolean isReplaceable(BlockState state)
	{
		return super.isReplaceable(state) && state.get(SNOW_LAYER) == 1;
	}

	@Override
	public void spawnLoot(BlockState state, World world, int x, int y, int z)
	{
		for (int i = 0; i < state.get(SNOW_LAYER); i++)
		{
			super.spawnLoot(state, world, x, y, z);
		}
	}

	@Override
	public void onClick(BlockState state, World world, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (clickedOn != EnumFace.UP || click.getAction() != KeyList.PRESS)
		{
			super.onClick(state, world, player, clickedOn, click, x, y, z);
			return;
		}

		Palette palette = player.getHoldedPalette();

		if (click.getButton() == KeyList.RMB)
		{
			if (player.getHoldedPaletteItem().getBlockToPlace() == this)
			{
				int width = state.get(SNOW_LAYER);

				if (width < 8)
				{
					world.setState(state.with(SNOW_LAYER, width + 1).get(), x, y, z);
					player.processNextBlockPlace = false;
					if (palette != null) palette.removeItem();
				}
			}
		} else if (click.getButton() == KeyList.LMB)
		{
			int width = state.get(SNOW_LAYER);
			if (width > 1)
			{
				if (player.gamemode.spawBlockLoot)
					super.spawnLoot(state, world, x, y, z);
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
