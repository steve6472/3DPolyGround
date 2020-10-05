package steve6472.polyground.registry;

import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.BlockAtlas;
import steve6472.polyground.block.model.BlockModel;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.block.model.ModelLoader;
import steve6472.polyground.block.model.elements.Bakery;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.block.SpecialBlockRegistry;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
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
		LinkedHashMap<Block, JSONObject> temp = new LinkedHashMap<>();

		temp.put(Block.createAir(), new JSONObject());
		reference.put("air", 0);
		WaterRegistry.tempVolumes.add(1000.0);

		temp.put(Block.createError(), new JSONObject());
		reference.put("error", 1);
		WaterRegistry.tempVolumes.add(0.0);

		int systemBlocks = 2;

		if (blocksFile != null)
		{
			for (int i = 0; i < Objects.requireNonNull(blocksFile).length; i++)
			{
				if (blocksFile[i].isDirectory())
					continue;

				if (!blocksFile[i].getName().endsWith(".json"))
				{
					continue;
				}

				JSONObject json = new JSONObject(ModelLoader.read(blocksFile[i]));
				Block block;

				if (json.optBoolean("debug") && !CaveGame.DEBUG)
				{
					systemBlocks--;
					continue;
				}

				if (json.has("special") && SpecialBlockRegistry.getKeys().contains(json.getJSONObject("special").getString("name")))
				{
					block = SpecialBlockRegistry.createSpecialBlock(json.getJSONObject("special").getString("name"), json);
				} else
				{
					block = new Block(json);
				}

				if (!reference.containsKey(block.getName()))
				{
					temp.put(block, json);

					reference.put(block.getName(), i + systemBlocks);
				} else
				{
					throw new IllegalArgumentException("Duplicate block name " + block.getName());
				}
			}
		}

		blocks = new Block[temp.size()];

		int index = 0;
		for (Block block : temp.keySet())
		{
			game.getEventHandler().register(block);
			blocks[index] = block;
			index++;
		}

		for (Map.Entry<Block, JSONObject> entry : temp.entrySet())
		{
			Block key = entry.getKey();
			JSONObject value = entry.getValue();
			try
			{
				key.load(value.optJSONObject("special"));
			} catch (Exception ex)
			{
				System.err.println("Error while loading block '" + key.getName() + "'");
				ex.printStackTrace();
				System.exit(2);
			}
		}

		BlockAtlas.putTexture("white");

		BlockAtlas.compileTextures(0);

		game.mainRender.buildHelper.atlasSize = BlockAtlas.getAtlas().getTileCount();
		game.mainRender.buildHelper.texel = 1f / (float) BlockAtlas.getAtlas().getTileCount();

		Bakery.load(BlockAtlas.getTexture(BlockAtlas.getTextureId("white")), game.mainRender.buildHelper);

		for (Block b : blocks)
		{
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
					model.createModel(game.mainRender.buildHelper);
				}
			});
		}
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

	public static void finish(CaveGame game)
	{
		for (Block block : blocks)
		{
			block.item = Items.getItemByName(block.item.getName());
//			System.out.println(block.item.getClass().getSimpleName());
		}
	}
}
