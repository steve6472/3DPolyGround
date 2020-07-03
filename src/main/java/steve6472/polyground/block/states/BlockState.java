package steve6472.polyground.block.states;

import steve6472.polyground.block.Block;
import steve6472.polyground.block.model.BlockModel;
import steve6472.polyground.block.properties.IProperty;

import java.util.ArrayList;
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
	private final List<BlockState> possibleStates;
	private final BlockModel blockModel;

	public BlockState(Block block, BlockModel model, HashMap<IProperty<?>, Comparable<?>> properties, List<BlockState> possibleStates)
	{
		this.block = block;
		this.blockModel = model;
		this.properties = properties;
		if (possibleStates == null)
		{
			this.possibleStates = new ArrayList<>(1);
			this.possibleStates.add(this);
		} else
		{
			this.possibleStates = possibleStates;
		}
	}

	public <T extends Comparable<T>> T get(IProperty<T> property)
	{
		return (T) properties.get(property);
	}

	public <T extends Comparable<T>, V extends T> StateFinder with(IProperty<T> property, V value)
	{
		return new StateFinder(possibleStates).with(property, value);
	}

	public BlockModel getBlockModel()
	{
		return blockModel;
	}

	public Block getBlock()
	{
		return block;
	}

	public List<BlockState> getPossibleStates()
	{
		return possibleStates;
	}

	@Override
	public String toString()
	{
		return "BlockState{" + "block=" + block + ", properties=" + properties + '}';
	}
}
