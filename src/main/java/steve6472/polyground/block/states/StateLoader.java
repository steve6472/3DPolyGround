package steve6472.polyground.block.states;

import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.model.BlockModel;
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
	public static void generateState(Block block, BlockModel model)
	{
		block.setDefaultState(new BlockState(block, model, null, null, null));
	}

	public static void generateStates(Block block, List<IProperty<?>> properties, JSONObject blockstates)
	{
		if (properties.isEmpty())
		{
			block.setDefaultState(new BlockState(block, new BlockModel(blockstates.getString("model"), 0), null, null, blockstates.optJSONArray("tags")));
			return;
		}


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
			possibleValues.add(new ArrayList<>(properties.size()));

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

			String modelPath = "";
			int rotation = 0;
			JSONArray tags = null;

			if (models == null)
			{
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
						modelPath = models.get(j).getString("model");
						rotation = models.get(j).optInt("rotation");
						tags = models.get(j).optJSONArray("tags");
						break;
					}
				}
			}


			try
			{
				BlockState state = new BlockState(block, new BlockModel(modelPath, rotation), map, tileStates, tags);
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
