package com.steve6472.polyground.block.special;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.entity.Player;
import com.steve6472.polyground.world.chunk.SubChunk;
import com.steve6472.sss2.SSS;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.11.2019
 * Project: SJP
 *
 ***********************/
public class LightSourceBlock extends Block
{
	private File f;
	private int color;
	private int radius;

	public LightSourceBlock(File f, int id)
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
			if (sss.hasValue("color"))
				color = sss.getHexInt("color");
			else
				color = 0xffffff;

			if (sss.hasValue("radius"))
				radius = sss.getInt("radius");
			else
				radius = 3;
		}
		f = null;
	}

	@Override
	public void onPlace(SubChunk subChunk, BlockData blockData, Player player, EnumFace placedOn, int x, int y, int z)
	{
//		subChunk.rebuildLight();
	}

	@Override
	public void createLight(int x, int y, int z, SubChunk sc, BlockData blockData, boolean isInverted)
	{
		/*
		* Must be in interval <0;15>
		* Can update chunks only in 3x3x3 cube
		*/
/*
		System.out.println(String.format("Light from %d/%d/%d inv: %b", x, y, z, isInverted));

		if (radius > 15)
			throw new IllegalArgumentException("Radius can not be bigger than 15! (" + radius + ") Block: " + getName());


		boolean[][][] checked = new boolean[radius * 2 + 1][radius * 2 + 1][radius * 2 + 1];
		List<Node> queue = new ArrayList<>();
		List<Node> toAdd = new ArrayList<>();

		sc.setLight(x, y, z, color);
		checked[radius][radius][radius] = true;

		for (EnumFace f : EnumFace.getFaces())
		{
			int lx = radius + f.getXOffset();
			int ly = radius + f.getYOffset();
			int lz = radius + f.getZOffset();

			Block block = sc.getBlockEfficiently(lx - radius + x, ly - radius + y, lz - radius + z);
			if (block == null || !block.isFull)
				toAdd.add(new Node(lx, ly, lz, 0));
		}

		while (!toAdd.isEmpty())
		{
			queue.addAll(toAdd);
			toAdd.clear();

			for (Iterator<Node> iterator = queue.iterator(); iterator.hasNext(); )
			{
				Node i = iterator.next();
				iterator.remove();

				double distance = i.distance;

				if (!isInverted)
					sc.setLight(i.x - radius + x, i.y - radius + y, i.z - radius + z, ColorUtil.dim(color, Math.max(1 - (distance / ((double) radius)), 0)));
				else
					sc.removeLight(i.x - radius + x, i.y - radius + y, i.z - radius + z, ColorUtil.dim(color, Math.max(1 - (distance / ((double) radius)), 0)));

				Block block = sc.getBlockEfficiently(i.x - radius + x, i.y - radius + y, i.z - radius + z);
				if (block != null && block.isFull)
					continue;

				for (EnumFace f : EnumFace.getFaces())
				{
					int lx = i.x + f.getXOffset();
					int ly = i.y + f.getYOffset();
					int lz = i.z + f.getZOffset();

					if (lx < 0 || lx >= checked.length || ly < 0 || ly >= checked[lx].length || lz < 0 || lz >= checked[lx][ly].length)
						continue;

					if (checked[lx][ly][lz])
						continue;

					checked[lx][ly][lz] = true;

					toAdd.add(new Node(lx, ly, lz, i.distance + 1));
				}
			}
		}*/
	}
/*
	private class Node
	{
		public int x, y, z;
		int distance;

		Node(int x, int y, int z, int distance)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.distance = distance;
		}
	}*/
}
