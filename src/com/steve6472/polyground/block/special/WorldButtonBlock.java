package com.steve6472.polyground.block.special;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.entity.Player;
import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.world.chunk.SubChunk;
import com.steve6472.sge.main.KeyList;
import com.steve6472.sge.main.events.MouseEvent;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.08.2019
 * Project: SJP
 *
 ***********************/
public class WorldButtonBlock extends Block
{
	public WorldButtonBlock(File f, int id)
	{
		super(f, id);
		isFull = false;
	}

	@Override
	public void onClick(SubChunk subChunk, BlockData blockData, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getAction() != KeyList.PRESS) return;

		if (click.getButton() == KeyList.LMB)
		{
			player.processNextBlockBreak = false;
			return;
		}

		if (click.getButton() != KeyList.RMB) return;

		try
		{
			player.processNextBlockPlace = false;
			CaveGame.getInstance().commandRegistry.dispatcher.execute("loadworld house", CaveGame.getInstance().commandRegistry.commandSource);
		} catch (CommandSyntaxException e)
		{
			e.printStackTrace();
		}
	}
}
