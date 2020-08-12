package steve6472.polyground.block.states;

import org.json.JSONArray;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.model.BlockModel;
import steve6472.polyground.block.properties.IProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 02.07.2020
 * Project: StateTest
 *
 ***********************/
public class BlockState
{
	private static int ID = 0;

	private final Block block;
	private final HashMap<IProperty<?>, Comparable<?>> properties;
	private final List<BlockState> possibleStates;
	private final BlockModel blockModel;
	private final Set<String> tags;
	private final int id;

	public BlockState(Block block, BlockModel model, HashMap<IProperty<?>, Comparable<?>> properties, List<BlockState> possibleStates, JSONArray tags)
	{
		this.id = ID++;
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
		if (tags != null)
		{
			String[] t = new String[tags.length()];
			for (int i = 0; i < tags.length(); i++)
			{
				t[i] = tags.getString(i);
			}
			this.tags = Set.of(t);
		} else
		{
			this.tags = Set.of();
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

	public HashMap<IProperty<?>, Comparable<?>> getProperties()
	{
		return properties;
	}

	public int getId()
	{
		return id;
	}

	public BlockState fromStateString(String stateString)
	{
		if (stateString.isBlank())
			return this;

		if (stateString.charAt(0) != '[' || stateString.charAt(stateString.length() - 1) != ']' || !stateString.substring(1, stateString.length() - 1).contains(","))
			return this;

		String[] states = stateString.substring(1, stateString.length() - 1).split(",");
		HashMap<String, String> map = new HashMap<>();
		for (String s : states)
		{
			String[] a = s.split("=");
			map.put(a[0], a[1]);
		}

		for (BlockState state : possibleStates)
		{
			boolean match = true;
			for (IProperty<?> property : state.getProperties().keySet())
			{
				String val = map.get(property.getName());
				Comparable<?> c = state.getProperties().get(property);
				if (!val.equals(c.toString()))
				{
					match = false;
					break;
				}
			}
			if (match)
				return state;
		}

		return getBlock().getDefaultState();
	}

	public boolean hasTag(String tag)
	{
		return tags.contains(tag);
	}

	public String getStateString()
	{
		if (properties == null)
			return "";
		StringBuilder sb = new StringBuilder("[");
		for (IProperty<?> property : properties.keySet())
		{
			sb.append(property.getName()).append("=").append(properties.get(property).toString()).append(",");
		}
		sb.setLength(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}

	@Override
	public String toString()
	{
		return "BlockState{" + "block=" + block.getName() + ", tags=" + tags + (properties == null ? "}" : ", properties=" + properties + '}');
	}
}
