package steve6472.polyground.registry.data;

import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.KnappingData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class DataRegistry
{
	private static final HashMap<String, DataEntry<? extends BlockData>> dataRegistry = new HashMap<>();

	public static final DataEntry<KnappingData> stoneKnapping = register("knapping", KnappingData::new);

	public static <T extends BlockData> DataEntry<T> register(String id, IDataFactory<T> factory)
	{
		DataEntry<T> entry = new DataEntry<>(factory, id);
		dataRegistry.put(id, entry);
		return entry;
	}

	public static BlockData createData(String id)
	{
		return dataRegistry.get(id).createNew();
	}

	public static Collection<DataEntry<? extends BlockData>> getEntries()
	{
		return dataRegistry.values();
	}

	public static Set<String> getKeys()
	{
		return dataRegistry.keySet();
	}
}
