package com.steve6472.polyground.block.special;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.HitResult;
import com.steve6472.polyground.entity.Player;
import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.world.SubChunk;
import com.steve6472.sge.main.KeyList;
import com.steve6472.sge.main.events.MouseEvent;
import com.steve6472.sss2.SSS;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.09.2019
 * Project: SJP
 *
 ***********************/
public class DoubleSlabBlock extends Block
{
	private Block top, bottom;

	private File f;

	public DoubleSlabBlock(File f, int id)
	{
		super(f, id);
		this.f = f;
	}

	@Override
	public void postLoad()
	{
		if (f.isFile())
		{
			SSS sss = new SSS(f);

			bottom = BlockRegistry.getBlockByName(sss.getString("bottom"));
			top = BlockRegistry.getBlockByName(sss.getString("top"));
		}
	}

	@Override
	public void onClick(SubChunk subChunk, BlockData blockData, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (!(click.getAction() == KeyList.PRESS && click.getButton() == KeyList.LMB)) return;

		HitResult hitResult = CaveGame.getInstance().hitPicker.getHitResult();

		Block placed = subChunk.getWorld().getBlock(hitResult.getX(), hitResult.getY(), hitResult.getZ());
		Block tobeplaced = null;

		if (hitResult.getFace() == EnumFace.UP)
		{
			subChunk.getWorld().setBlock(hitResult, tobeplaced = bottom);
		} else if (hitResult.getFace() == EnumFace.DOWN)
		{
			subChunk.getWorld().setBlock(hitResult, tobeplaced = top);
		} else if (hitResult.getFace().isSide())
		{
			if (Double.parseDouble("0." + ("" + hitResult.getPy()).split("\\.")[1]) >= 0.5f)
			{
				subChunk.getWorld().setBlock(hitResult, tobeplaced = bottom);
			} else
			{
				subChunk.getWorld().setBlock(hitResult, tobeplaced = top);
			}
		}

		assert tobeplaced != null;

		if (tobeplaced == bottom)
			tobeplaced = top;
		else
			tobeplaced = bottom;

		tobeplaced
			.onBreak(
				subChunk,
				subChunk.getBlockData(
					hitResult.getX(),
					hitResult.getY(),
					hitResult.getZ()),
				player,
				hitResult.getFace(),
				hitResult.getX(),
				hitResult.getY(),
				hitResult.getZ());

		player.processNextBlockBreak = false;
	}

	private enum EnumSlabType
	{
		TOP, BOTTOM, DOUBLE, OPPOSITE
	}
}
