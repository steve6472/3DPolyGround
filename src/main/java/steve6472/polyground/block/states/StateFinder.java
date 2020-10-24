package steve6472.polyground.block.states;

import steve6472.polyground.block.properties.IProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Sort/Order dependant State Finder
 */
public class StateFinder implements IBlockState
{
	private final List<BlockState> tileStates;

	StateFinder(List<BlockState> tileStates)
	{
		this.tileStates = new ArrayList<>(tileStates);
	}

	public <T extends Comparable<T>, V extends T> StateFinder with(IProperty<T> property, V value)
	{
		tileStates.removeIf(pb -> pb.get(property) != value);
		return this;
	}

	public BlockState get()
	{
		return tileStates.get(0);
	}
}