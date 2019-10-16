package com.steve6472.polyground.block.registry;

import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.BlockLoader;
import com.steve6472.sge.main.MainApp;
import com.steve6472.sss2.SSS;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.08.2019
 * Project: SJP
 *
 ***********************/
public class BlockRegistry
{
	private static HashMap<String, Block> blocks;
	private static HashMap<Integer, String> reference;

	public BlockRegistry(CaveGame pg)
	{
		File[] blocks = new File(MainApp.class.getResource("/blocks").getFile()).listFiles();

		reference = new HashMap<>();
		BlockRegistry.blocks = new HashMap<>();

		BlockRegistry.blocks.put("air", Block.createAir());
		reference.put(0, "air");

		for (int i = 0; i < Objects.requireNonNull(blocks).length; i++)
		{
			if (blocks[i].isDirectory()) continue;

			SSS t = new SSS(blocks[i]);
			Block block;

			if (t.containsName("special") && SpecialBlockRegistry.getKeys().contains(t.getString("special")))
			{
				block = SpecialBlockRegistry.createSpecialBlock(t.getString("special"), blocks[i], i + 1);
			} else
			{
				block = new Block(blocks[i], i + 1);
			}

			pg.getEventHandler().register(block);

			if (!BlockRegistry.blocks.containsKey(block.getName()) && !reference.containsKey(i + 1))
			{
				BlockRegistry.blocks.put(block.getName(), block);
				reference.put(i + 1, block.getName());
			} else
			{
				throw new IllegalArgumentException("Duplicate block name " + block.getName() + " or id " + i);
			}
		}

		getAllBlocks().forEach(Block::postLoad);

		BlockLoader.compileTextures();

		pg.buildHelper.atlasSize = BlockLoader.getAtlas().getTileCount();
		pg.buildHelper.texel = 1f / (float) BlockLoader.getAtlas().getTileCount();
	}

	public static int getBlockIdByName(String name)
	{
		return blocks.get(name).getId();
	}

	public static Block getBlockByName(String name)
	{
		return blocks.get(name);
	}

	public static Block getBlockById(int id)
	{
		if (id == -1) return Block.air;
		return blocks.get(reference.get(id));
	}

	public static Collection<Block> getAllBlocks()
	{
		return blocks.values();
	}
}
