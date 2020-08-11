package steve6472.polyground.block.states;

import steve6472.polyground.block.properties.IProperty;

import java.util.List;

/**
 * Sort/Order dependant State Finder
 */
public class StateFinder
{
	private int lastIndex = 0;
	private final List<BlockState> tileStates;

	StateFinder(List<BlockState> tileStates)
	{
		this.tileStates = tileStates;
	}

	public <T extends Comparable<T>, V extends T> StateFinder with(IProperty<T> property, V value)
	{
		for (int i = lastIndex; i < tileStates.size(); i++)
		{
			BlockState ts = tileStates.get(i);
			if (ts.get(property) == value)
			{
				lastIndex = i;
				break;
			}
		}

		return this;
	}

	public BlockState get()
	{
		return tileStates.get(lastIndex);
	}
}