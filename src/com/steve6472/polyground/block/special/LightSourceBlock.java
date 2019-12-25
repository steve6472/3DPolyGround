package com.steve6472.polyground.block.special;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.blockdata.BlockData;
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
	int color;

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
		}
		f = null;
	}

	@Override
	public void createLight(int x, int y, int z, SubChunk sc, BlockData blockData)
	{
		for (EnumFace face : EnumFace.values())
		{
			int lx = x + face.getXOffset();
			int ly = y + face.getYOffset();
			int lz = z + face.getZOffset();

			if (sc.getNeighbouringSubChunk(lx, ly, lz) == null) continue;

			if (lx >= 0 && lx < 16 && lz >= 0 && lz < 16 && ly >= 0 && ly < 16)
				sc.setLight(lx, ly, lz, color);

			/* Random Color
			int seed = ((x << 4) | (y << 16) | z) | ((sc.getX() << 8) | (sc.getZ() << 6) | (sc.getLayer()));

			if (lx >= 0 && lx < 16 && lz >= 0 && lz < 16 && ly >= 0 && ly < 16)
				sc.setLight(lx, ly, lz, ColorUtil.getColor(RandomUtil.randomInt(0, 255, seed), RandomUtil.randomInt(0, 255, seed * 0x7f92), RandomUtil.randomInt(0, 255, seed << 6)));
			 */
		}
	}
}
