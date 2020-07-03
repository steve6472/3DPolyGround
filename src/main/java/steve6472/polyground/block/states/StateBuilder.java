package steve6472.polyground.block.states;

import steve6472.polyground.block.Block;
import steve6472.polyground.block.model.BlockModel;
import steve6472.polyground.block.properties.IProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 03.07.2020
 * Project: StateTest
 *
 ***********************/
public class StateBuilder
{
	public static void generateStates(Block block, BlockModel model, List<IProperty<?>> properties)
	{
		if (properties.isEmpty())
		{
			block.setDefaultState(new BlockState(block, model, null, null));
			return;
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

			BlockState state = new BlockState(block, model, map, tileStates);
			if (block.getDefaultState() == null)
				block.setDefaultState(state);
			tileStates.add(state);
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
