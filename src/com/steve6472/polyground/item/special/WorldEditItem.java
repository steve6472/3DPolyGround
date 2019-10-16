package com.steve6472.polyground.item.special;

import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.entity.Player;
import com.steve6472.polyground.events.InGameGuiEvent;
import com.steve6472.polyground.item.Item;
import com.steve6472.polyground.world.SubChunk;
import com.steve6472.sge.main.KeyList;
import com.steve6472.sge.main.events.Event;
import com.steve6472.sge.main.events.MouseEvent;
import org.joml.Vector3i;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class WorldEditItem extends Item
{
	public WorldEditItem(File f, int id)
	{
		super(f, id);
		firstPos = new Vector3i();
		secondPos = new Vector3i();
	}

	private Vector3i firstPos, secondPos;

	@Event
	public void renderPos(InGameGuiEvent.PostRender e)
	{
		if (CaveGame.itemInHand == this)
		{
//			Font.render("First  Pos: " + firstPos.x + "/" + firstPos.y + "/" + firstPos.z, 5, CaveGame.getInstance().getHeight() - 25);
//			Font.render("Second Pos: " + secondPos.x + "/" + secondPos.y + "/" + secondPos.z, 5, CaveGame.getInstance().getHeight() - 15);
		}
	}

	@Override
	public void onClick(SubChunk subChunk, BlockData blockData, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getAction() == KeyList.PRESS)
		{
			if (click.getButton() == KeyList.LMB)
			{
				firstPos.set(player.getHitResult().getX(), player.getHitResult().getY(), player.getHitResult().getZ());
				player.processNextBlockBreak = false;
			}
			if (click.getButton() == KeyList.RMB)
			{
				secondPos.set(player.getHitResult().getX(), player.getHitResult().getY(), player.getHitResult().getZ());
				player.processNextBlockPlace = false;
			}
		}
	}

	public Vector3i getFirstPos()
	{
		return firstPos;
	}

	public Vector3i getSecondPos()
	{
		return secondPos;
	}
}
