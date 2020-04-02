package steve6472.polyground.registry;

import steve6472.SSS;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.BlockTextureHolder;
import steve6472.polyground.registry.block.SpecialBlockRegistry;
import steve6472.sge.main.MainApp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.08.2019
 * Project: SJP
 *
 ***********************/
public class BlockRegistry
{
	//	private static HashMap<String, Block> blocks;
	//	private static HashMap<Integer, String> reference;

	private static List<Block> blocks;
	//	private static HashMap<Integer, Block> blocks;
	private static HashMap<String, Integer> reference;

	public BlockRegistry(CaveGame pg)
	{
		File[] blocksFile = new File(MainApp.class.getResource("/blocks").getFile()).listFiles();
		//		File[] blocksFile = new File("blocks").listFiles();

		reference = new HashMap<>();
		//		blocks = new HashMap<>();
		blocks = new ArrayList<>();

		blocks.add(Block.createAir());
		reference.put("air", 0);

		//		blocks.put("air", Block.createAir());
		//		reference.put(0, "air");

		blocks.add(Block.createError());
		reference.put("error", 1);

		//		blocks.put("error", Block.createError());
		//		reference.put(1, "error");

		int systemBlocks = 2;

		for (int i = 0; i < Objects.requireNonNull(blocksFile).length; i++)
		{
			if (blocksFile[i].isDirectory())
				continue;

			SSS t = new SSS(blocksFile[i]);
			Block block;

			if (t.containsName("special") && SpecialBlockRegistry.getKeys().contains(t.getString("special")))
			{
				block = SpecialBlockRegistry.createSpecialBlock(t.getString("special"), blocksFile[i], i + systemBlocks);
			} else
			{
				block = new Block(blocksFile[i], i + systemBlocks);
			}

			pg.getEventHandler().register(block);

			if (!reference.containsKey(block.getName()))
			{
				blocks.add(block);
				reference.put(block.getName(), i + systemBlocks);
				//				BlockRegistry.blocks.put(block.getName(), block);
				//				reference.put(i + systemBlocks, block.getName());
			} else
			{
				throw new IllegalArgumentException("Duplicate block name " + block.getName());
			}
		}

		getAllBlocks().forEach(Block::postLoad);

		BlockTextureHolder.compileTextures();

		pg.buildHelper.atlasSize = BlockTextureHolder.getAtlas().getTileCount();
		pg.buildHelper.texel = 1f / (float) BlockTextureHolder.getAtlas().getTileCount();
	}

	public static int getBlockIdByName(String name)
	{
		//		return blocks.get(name).getId();
		return reference.get(name);
	}

	public static Block getBlockByName(String name)
	{
		//		return blocks.get(name);
		int ref = reference.get(name);
		return blocks.get(ref);
	}

	public static Block getBlockById(int id)
	{
		if (id == -1)
			return Block.air;
		return blocks.get(id);
		//		return blocks.get(reference.get(id));
	}

	public static List<Block> getAllBlocks()
	{
		return blocks;
	}
}
