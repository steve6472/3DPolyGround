package steve6472.polyground.registry;

import org.json.JSONObject;
import steve6472.SSS;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.BlockTextureHolder;
import steve6472.polyground.block.model.BlockModel;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.block.SpecialBlockRegistry;

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
public class Blocks
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

		if (blocksFile != null)
		{
			for (int i = 0; i < Objects.requireNonNull(blocksFile).length; i++)
			{
				if (blocksFile[i].isDirectory())
					continue;

				SSS t = new SSS(blocksFile[i]);
				Block block;

				if (t.containsName("debug") && t.getBoolean("debug") && !CaveGame.DEBUG)
					continue;

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
		}

		blocks = new Block[tempBlocks.size()];

		for (int i = 0; i < tempBlocks.size(); i++)
		{
			blocks[i] = tempBlocks.get(i);
		}

		tempBlocks.forEach(Block::postLoad);

		/*
		for (int i = 0; i < tempBlocks.size(); i++)
		{
			for (BlockState bs : blocks[i].getDefaultState().getPossibleStates())
			{
				if (bs.getBlockModel(world, x, y, z) != null && bs.getBlockModel(world, x, y, z).getCubes() != null)
				{
					for (Cube c : bs.getBlockModel(world, x, y, z).getCubes())
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
		}*/

		BlockTextureHolder.compileTextures(0);

		game.mainRender.buildHelper.atlasSize = BlockTextureHolder.getAtlas().getTileCount();
		game.mainRender.buildHelper.texel = 1f / (float) BlockTextureHolder.getAtlas().getTileCount();

		tempBlocks.forEach(b -> {
			b.getDefaultState().getPossibleStates().forEach(ps -> {
				for (BlockModel model : ps.getBlockModels())
				{
					if (model.getElements() != null)
					{
						for (IElement triangle : model.getElements())
						{
							triangle.fixUv(game.mainRender.buildHelper.texel);
						}
					}
				}
			});
		});
	}

	public static Block getBlockByName(String name)
	{
		Integer ref = reference.get(name);
		if (ref == null)
			return Block.error;
		return blocks[ref];
	}

	public static BlockState getDefaultState(String name)
	{
		return getBlockByName(name).getDefaultState();
	}

	public static BlockState getStateByName(String name, String states)
	{
		int ref = reference.get(name);
		Block block = blocks[ref];
		return block.getDefaultState().fromStateString(states);
	}

	public static BlockState loadStateFromJSON(JSONObject state)
	{
		if (state.has("properties"))
		{
			StringBuilder sb = new StringBuilder("[");
			JSONObject properties = state.getJSONObject("properties");
			if (properties.isEmpty())
				return getDefaultState(state.getString("block"));

			for (String key : properties.keySet())
			{
				sb.append(key).append("=").append(properties.getString(key)).append(",");
			}
			sb.setLength(sb.length() - 1);
			sb.append("]");

			return getStateByName(state.getString("block"), sb.toString());
		} else
		{
			return getDefaultState(state.getString("block"));
		}
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
