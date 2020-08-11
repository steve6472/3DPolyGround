package steve6472.polyground.block.special;

import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.HitResult;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.Player;
import steve6472.polyground.world.World;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.Util;
import steve6472.sge.main.events.MouseEvent;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.08.2019
 * Project: SJP
 *
 ***********************/
public class TimeSliderBlock extends Block
{
	public TimeSliderBlock(File f)
	{
		super(f);
	}

	@Override
	public void onClick(BlockState state, World world, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (clickedOn == EnumFace.SOUTH && click.getButton() == KeyList.RMB && click.getAction() == KeyList.PRESS)
		{
			HitResult hr = CaveGame.getInstance().hitPicker.getHitResult();

			world.shade = ((float) (hr.getPz() - Math.ceil(hr.getPz()) + 1));
			world.shade = (world.shade - 0.5f) * (1.2f) + 0.5f;

			world.shade = Util.clamp(0, 1, world.shade);

			player.processNextBlockPlace = false;
		}
	}

	@Override
	public boolean isTickable()
	{
		return false;
	}
}
