package com.steve6472.polyground.block.special;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.HitResult;
import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.entity.Player;
import com.steve6472.polyground.world.chunk.SubChunk;
import com.steve6472.polyground.world.World;
import com.steve6472.sge.main.KeyList;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.events.MouseEvent;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.08.2019
 * Project: SJP
 *
 ***********************/
public class TimeSliderBlock extends Block
{
	public TimeSliderBlock(File f, int id)
	{
		super(f, id);
	}

	@Override
	public void onClick(SubChunk subChunk, BlockData blockData, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (clickedOn == EnumFace.SOUTH && click.getButton() == KeyList.RMB && click.getAction() == KeyList.PRESS)
		{
			HitResult hr = CaveGame.getInstance().hitPicker.getHitResult();
			World world = CaveGame.getInstance().world;

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
