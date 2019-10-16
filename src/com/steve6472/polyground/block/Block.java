package com.steve6472.polyground.block;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.block.model.BlockModel;
import com.steve6472.polyground.block.model.registry.Cube;
import com.steve6472.polyground.block.special.SnapBlock;
import com.steve6472.polyground.entity.Player;
import com.steve6472.polyground.world.BuildHelper;
import com.steve6472.polyground.world.Cull;
import com.steve6472.polyground.world.SubChunk;
import com.steve6472.sge.main.events.MouseEvent;

import java.io.File;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.08.2019
 * Project: SJP
 *
 ***********************/
public class Block
{
	public static Block air;

	public boolean isFull;

	private final int id;

	public String name;
	public BlockModel blockModel;

	public static Block createAir()
	{
		return air = new Block();
	}

	private Block()
	{
		name = "air";
		id = 0;
		isFull = false;
	}

	public Block(File f, int id)
	{
		isFull = true;
		name = f.getName().substring(0, f.getName().length() - 4);
		blockModel = new BlockModel(f);

		this.id = id;
	}

	public Cube getCube(int i)
	{
		return blockModel.getCube(i);
	}

	public BlockModel getBlockModel()
	{
		return blockModel;
	}

	public String getName()
	{
		return name;
	}

	public int getId()
	{
		return id;
	}

	public List<Cube> getCubes(int x, int y, int z)
	{
		return blockModel.getCubes();
	}

	public List<Cube> getCubes()
	{
		return blockModel.getCubes();
	}

	/* Other Something */

	public boolean rebuildChunkOnPlace() { return true; }
	public boolean isTickable() { return false; }
	public boolean isReplaceable() { return this == air; }

	/* Something */

	public int createModel(int x, int y, int z, SubChunk sc, BlockData blockData, BuildHelper buildHelper)
	{
		int tris = 0;

		for (Cube c : blockModel.getCubes())
		{
			buildHelper.setCube(c);
			for (EnumFace face : EnumFace.getFaces())
			{
				if (Cull.renderFace(x, y, z, face, this, sc))
					tris += buildHelper.face(face);
			}
		}

		return tris;
	}

	public void tick(SubChunk subChunk, BlockData blockData, int x, int y, int z) {}
	public void postLoad() {};

	/* Events */

	public void onPlace(SubChunk subChunk, BlockData blockData, Player player, EnumFace placedOn, int x, int y, int z) {}
	public void onBreak(SubChunk subChunk, BlockData blockData, Player player, EnumFace breakedFrom, int x, int y, int z) { SnapBlock.activate(this, subChunk, x, y, z); }

	/**
	 *
	 * Runs on block in the world
	 *
	 * @param subChunk Sub Chunk
	 * @param blockData Data of block
	 * @param player Player
	 * @param clickedOn Side the player has clicked on
	 * @param click event
	 * @param x x position of block
	 * @param y y position of block
	 * @param z z position of block
	 */
	public void onClick(SubChunk subChunk, BlockData blockData, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z) {}

	public void onUpdate(SubChunk subChunk, BlockData blockData, EnumFace updateFrom, int x, int y, int z) { }
}
