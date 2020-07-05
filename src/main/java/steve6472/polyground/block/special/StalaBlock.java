package steve6472.polyground.block.special;

import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.IntProperty;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.Player;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

import java.io.File;
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

	public StalaBlock(File f, int id)
	{
		super(f, id);
		isFull = false;
		setDefaultState(getDefaultState().with(WIDTH, 1).get());
	}

	@Override
	public void onClick(SubChunk subChunk, BlockState state, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (!clickedOn.isSide())
			return;

		if (click.getAction() == KeyList.PRESS && click.getButton() == KeyList.RMB)
		{
			if (CaveGame.itemInHand.getBlockToPlace() == this)
			{
				int width = state.get(WIDTH);

				if (width < 7)
				{
					subChunk.setState(state.with(WIDTH, width + 1).get(), x, y, z);
					player.processNextBlockPlace = false;
					subChunk.rebuildAllLayers();
				}
			}
		} else if (click.getAction() == KeyList.PRESS && click.getButton() == KeyList.LMB)
		{
			int width = state.get(WIDTH);
			if (width > 1)
			{
				subChunk.setState(state.with(WIDTH, width - 1).get(), x, y, z);
				player.processNextBlockBreak = false;
				subChunk.rebuildAllLayers();
			}
		}
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(WIDTH);
	}
}
