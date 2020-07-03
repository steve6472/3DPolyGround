package steve6472.polyground.block.states;

import steve6472.polyground.block.Block;
import steve6472.polyground.block.properties.IProperty;

import java.util.HashMap;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 02.07.2020
 * Project: StateTest
 *
 ***********************/
public class BlockState
{
	private final Block block;
	private final HashMap<IProperty<?>, Comparable<?>> properties;
	private final List<BlockState> tileStates;

	public BlockState(Block block, HashMap<IProperty<?>, Comparable<?>> properties, List<BlockState> tileStates)
	{
		this.block = block;
		this.properties = properties;
		this.tileStates = tileStates;
	}

	public <T extends Comparable<T>> T get(IProperty<T> property)
	{
		return (T) properties.get(property);
	}

	public <T extends Comparable<T>, V extends T> StateFinder with(IProperty<T> property, V value)
	{
		return new StateFinder(tileStates).with(property, value);
	}

	public Block getBlock()
	{
		return block;
	}

	@Override
	public String toString()
	{
		return "TileState{" + "tile=" + block + ", properties=" + properties + '}';
	}
}
