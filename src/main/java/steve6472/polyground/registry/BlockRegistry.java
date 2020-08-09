package steve6472.polyground.registry;

import steve6472.SSS;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.BlockTextureHolder;
import steve6472.polyground.block.model.Cube;
import steve6472.polyground.block.model.CubeFace;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.block.SpecialBlockRegistry;
import steve6472.polyground.registry.face.FaceRegistry;

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
	private static Block[] blocks;
	private static HashMap<String, Integer> reference;

	public static void register(CaveGame game)
	{
//		File[] blocksFile = new File(MainApp.class.getResource("/blocks").getFile()).listFiles();
		File[] blocksFile = new File("game/objects/blocks").listFiles();

		reference = new HashMap<>();
		List<Block> tempBlocks = new ArrayList<>();

		tempBlocks.add(Block.createAir());
		reference.put("air", 0);
		WaterRegistry.tempVolumes.add(1000.0);

		tempBlocks.add(Block.createError());
		reference.put("error", 1);
		WaterRegistry.tempVolumes.add(0.0);

		int systemBlocks = 2;

		for (int i = 0; i < Objects.requireNonNull(blocksFile).length; i++)
		{
			if (blocksFile[i].isDirectory())
				continue;

			SSS t = new SSS(blocksFile[i]);
			Block block;

			if (t.containsName("special") && SpecialBlockRegistry.getKeys().contains(t.getString("special")))
			{
				block = SpecialBlockRegistry.createSpecialBlock(t.getString("special"), blocksFile[i]);
			} else
			{
				block = new Block(blocksFile[i]);
			}

			game.getEventHandler().register(block);

			if (!reference.containsKey(block.getName()))
			{
				tempBlocks.add(block);
				reference.put(block.getName(), i + systemBlocks);
			} else
			{
				throw new IllegalArgumentException("Duplicate block name " + block.getName());
			}
		}

		blocks = new Block[tempBlocks.size()];

		for (int i = 0; i < tempBlocks.size(); i++)
		{
			blocks[i] = tempBlocks.get(i);
		}

		tempBlocks.forEach(Block::postLoad);

		for (int i = 0; i < tempBlocks.size(); i++)
		{
			for (BlockState bs : blocks[i].getDefaultState().getPossibleStates())
			{
				if (bs.getBlockModel() != null && bs.getBlockModel().getCubes() != null)
				{
					for (Cube c : bs.getBlockModel().getCubes())
					{
						for (CubeFace f : c.getFaces())
						{
							if (f != null)
								if (f.hasProperty(FaceRegistry.conditionedTexture))
									f.getProperty(FaceRegistry.conditionedTexture).fixBlockId();
						}
					}
				}
			}
		}

		BlockTextureHolder.compileTextures();

		game.mainRender.buildHelper.atlasSize = BlockTextureHolder.getAtlas().getTileCount();
		game.mainRender.buildHelper.texel = 1f / (float) BlockTextureHolder.getAtlas().getTileCount();
	}

	public static int getBlockIdByName(String name)
	{
		return reference.get(name);
	}

	public static Block getBlockByName(String name)
	{
		int ref = reference.get(name);
		return blocks[ref];
	}

	public static BlockState getDefaultState(String blockName)
	{
		return getBlockByName(blockName).getDefaultState();
	}

	public static BlockState getStateByName(String name, String states)
	{
		int ref = reference.get(name);
		Block block = blocks[ref];
		return block.getDefaultState().fromStateString(states);
	}

	public static Block getBlockById(int id)
	{
		return blocks[id];
	}

	public static Block[] getAllBlocks()
	{
		return blocks;
	}
}
