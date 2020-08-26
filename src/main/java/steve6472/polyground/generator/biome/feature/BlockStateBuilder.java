package steve6472.polyground.generator.biome.feature;

import steve6472.polyground.block.properties.IProperty;

import java.util.HashMap;

public class BlockStateBuilder
{
	String block;
	final HashMap<IProperty<?>, Comparable<?>> states;

	private BlockStateBuilder()
	{
		this.states = new HashMap<>();
		this.block = "";
	}

	public static BlockStateBuilder create()
	{
		return new BlockStateBuilder();
	}

	public BlockStateBuilder block(String block)
	{
		this.block = block;
		return this;
	}

	public <T extends Comparable<T>> BlockStateBuilder state(IProperty<T> property, Comparable<T> value)
	{
		states.put(property, value);
		return this;
	}
}