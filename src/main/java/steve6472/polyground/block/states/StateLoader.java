package steve6472.polyground.block.states;

import org.joml.AABBf;
import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.PrettyJson;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.model.BlockModel;
import steve6472.polyground.block.model.CubeHitbox;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.block.properties.EnumProperty;
import steve6472.polyground.block.properties.IProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 03.07.2020
 * Project: StateTest
 *
 ***********************/
public class StateLoader
{
	// Generates air and error blocks
	public static void generateState(Block block, BlockModel model, String... tags)
	{
		JSONArray t = new JSONArray();
		for (String s : tags)
			t.put(s);
		block.setDefaultState(new BlockState(block, new BlockModel[] {model}, null, null, t, false));
	}

	private static BlockModel[] loadModels(JSONArray modelsArray, JSONArray rotation)
	{
		BlockModel[] models = new BlockModel[modelsArray.length()];

		for (int i = 0; i < modelsArray.length(); i++)
		{
			models[i] = new BlockModel(modelsArray.getString(i), rotation);
		}

		return models;
	}

	public static void generateStates(Block block, List<IProperty<?>> properties, JSONObject blockstates)
	{
		if (properties.isEmpty())
		{
			if (blockstates.optBoolean("empty"))
			{
				block.setDefaultState(new BlockState(block, new BlockModel[] {new BlockModel(new IElement[0], new CubeHitbox(new AABBf(0, 0, 0, 1, 1, 1)))}, null, null, blockstates.optJSONArray("tags"), false));
				return;
			}
			if (blockstates.has("models"))
			{
				block.setDefaultState(new BlockState(block, loadModels(blockstates.getJSONArray("models"), new JSONArray().put(0).put(0).put(0)), null, null, blockstates.optJSONArray("tags"), blockstates.optBoolean("custom")));
			} else
			{
				block.setDefaultState(new BlockState(block, new BlockModel[]{new BlockModel(blockstates.getString("model"), new JSONArray().put(0).put(0).put(0))}, null, null, blockstates.optJSONArray("tags"), blockstates.optBoolean("custom")));
			}
			return;
		}

		BlockModel blockModel = null;
		Map<JSONObject, JSONObject> models = null;
		if (blockstates.has("models"))
		{
			JSONArray array = blockstates.getJSONArray("models");

			models = new HashMap<>(array.length());
			for (int i = 0; i < array.length(); i++)
			{
				JSONObject m = array.getJSONObject(i);
				models.put(m.getJSONObject("state"), m);
			}
		} else
		{
			if (blockstates.optBoolean("empty"))
				blockModel = new BlockModel(new IElement[0], new CubeHitbox(new AABBf(0, 0, 0, 1, 1, 1)));
			else
				blockModel = new BlockModel(blockstates.getString("model"), new JSONArray().put(0).put(0).put(0));
		}

		// Generate all possible state values

		int possibleStatesCount = 1;
		List<Integer> possibleValuesCount = new ArrayList<>(properties.size());
		List<Integer> possibleValuesCount_ = new ArrayList<>(properties.size());

		List<List<Comparable<?>>> possibleValues = new ArrayList<>(possibleStatesCount);

		for (IProperty<?> p : properties)
		{
			possibleValuesCount.add(p.getPossibleValues().length);
			possibleValuesCount_.add(p.getPossibleValues().length);
			possibleStatesCount *= p.getPossibleValues().length;
		}

		for (int i = 0; i < possibleStatesCount; i++)
		{
			possibleValues.add(new ArrayList<>(properties.size()));
		}

		for (int i = 0; i < possibleStatesCount; i++)
		{
			List<Comparable<?>> states = possibleValues.get(i);

			for (int j = 0; j < possibleValuesCount.size(); j++)
			{
				states.add(properties.get(j).getPossibleValues()[possibleValuesCount_.get(j) - 1]);
			}
			sub(possibleValuesCount_, possibleValuesCount, possibleValuesCount.size() - 1);
		}

		// Create all posible states
		List<BlockState> tileStates = new ArrayList<>(possibleStatesCount);
		for (List<Comparable<?>> list : possibleValues)
		{
			HashMap<IProperty<?>, Comparable<?>> map = new HashMap<>();

			for (int i = 0; i < list.size(); i++)
			{
				map.put(properties.get(i), list.get(i));
			}

			JSONArray modelsArray = null;
			String modelPath = null;
			JSONArray rotation = new JSONArray().put(0).put(0).put(0);
			JSONArray tags = null;
			JSONObject matchObject = null;
			boolean custom = false;

			if (models == null)
			{
				if (blockstates.has("model"))
					modelPath = blockstates.getString("model");
			} else
			{
				for (JSONObject j : models.keySet())
				{
					boolean match = true;

					for (IProperty<?> p : map.keySet())
					{
						if (j.has(p.getName()))
						{
							if (p instanceof EnumProperty e)
							{
								if (map.get(p) != j.getEnum(e.getClazz(), p.getName()))
									match = false;
							} else
							{
								if (map.get(p) != j.get(p.getName()))
									match = false;
							}
						}
					}

					if (match)
					{
						matchObject = models.get(j);
						if (matchObject.has("model"))
						{
							modelPath = matchObject.getString("model");
						} else
						{
							modelsArray = matchObject.getJSONArray("models");
						}
						if (matchObject.has("rotation"))
						{
							rotation = matchObject.getJSONArray("rotation");
						}
						custom = matchObject.optBoolean("custom");
						tags = matchObject.optJSONArray("tags");
						break;
					}
				}
			}

			try
			{
				BlockState state;
				if (modelPath == null && blockModel != null)
				{
					state = new BlockState(block, new BlockModel[] {blockModel}, map, tileStates, null, false);
				} else if (modelPath != null)
				{
					state = new BlockState(block, new BlockModel[] {new BlockModel(modelPath, rotation)}, map, tileStates, tags, custom);
				} else if (modelsArray != null)
				{
					state = new BlockState(block, loadModels(modelsArray, rotation), map, tileStates, tags, custom);
				} else
				{
					System.err.println(PrettyJson.prettify(matchObject));
					throw new IllegalStateException("wut, no model found");
				}
				if (block.getDefaultState() == null)
					block.setDefaultState(state);
				tileStates.add(state);
			} catch (IllegalArgumentException ex)
			{
				System.err.println("State for " + block.getName() + " failed " + map);
				ex.printStackTrace();
			}
		}
	}

	/**
	 * I no longer know what this does
	 * help
	 * please
	 * somebody tell me
	 *
	 * @param list list
	 * @param max max
	 * @param index index
	 */
	private static void sub(List<Integer> list, List<Integer> max, int index)
	{
		if (index == -1)
			return;

		int v = list.get(index);
		if (v > 1)
		{
			v--;
			list.set(index, v);
		} else
		{
			list.set(index, max.get(index));
			sub(list, max, index - 1);
		}
	}
}
