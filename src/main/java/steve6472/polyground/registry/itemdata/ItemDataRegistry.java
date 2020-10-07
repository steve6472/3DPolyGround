package steve6472.polyground.registry.itemdata;

import steve6472.polyground.item.itemdata.BrushData;
import steve6472.polyground.item.itemdata.ItemData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class ItemDataRegistry
{
	private static final HashMap<String, ItemDataEntry<? extends ItemData>> dataRegistry = new HashMap<>();

	public static final ItemDataEntry<BrushData> brushData = register("brush", BrushData::new);

	public static <T extends ItemData> ItemDataEntry<T> register(String id, IItemDataFactory<T> factory)
	{
		ItemDataEntry<T> entry = new ItemDataEntry<>(factory, id);
		dataRegistry.put(id, entry);
		return entry;
	}

	public static ItemData createData(String id)
	{
		return dataRegistry.get(id).createNew();
	}

	public static Collection<ItemDataEntry<? extends ItemData>> getEntries()
	{
		return dataRegistry.values();
	}

	public static Set<String> getKeys()
	{
		return dataRegistry.keySet();
	}
}
