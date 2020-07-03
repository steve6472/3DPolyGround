package steve6472.polyground.registry;

import steve6472.SSS;
import steve6472.polyground.CaveGame;
import steve6472.polyground.item.Item;
import steve6472.polyground.registry.specialitem.SpecialItemRegistry;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.08.2019
 * Project: SJP
 *
 ***********************/
public class ItemRegistry
{
	private static HashMap<String, Item> items;
	private static HashMap<Integer, String> reference;

	public static void register(CaveGame pg)
	{
//		File[] blocks = new File(MainApp.class.getResource("/items").getFile()).listFiles();
		File[] items = new File("game/objects/items").listFiles();
		//		File[] blocks = new File("items").listFiles();

		reference = new HashMap<>();
		ItemRegistry.items = new HashMap<>();

		ItemRegistry.items.put("air", Item.createAir());
		reference.put(0, "air");

		for (int i = 0; i < Objects.requireNonNull(items).length; i++)
		{
			if (items[i].isDirectory())
				continue;

			SSS t = new SSS(items[i]);
			Item item;

			if (t.containsName("special") && SpecialItemRegistry.getKeys().contains(t.getString("special")))
			{
				item = SpecialItemRegistry.createSpecialItem(t.getString("special"), items[i], i + 1);
			} else
			{
				item = new Item(items[i], i + 1);
			}

			pg.getEventHandler().register(item);

			if (!ItemRegistry.items.containsKey(item.getName()) && !reference.containsKey(i + 1))
			{
				ItemRegistry.items.put(item.getName(), item);
				reference.put(i + 1, item.getName());
			} else
			{
				throw new IllegalArgumentException("Duplicate item name " + item.getName() + " or id " + i);
			}
		}
	}

	public static int getItemIdByName(String name)
	{
		return items.get(name).getId();
	}

	public static Item getItemByName(String name)
	{
		return items.get(name);
	}

	public static Item getItemById(int id)
	{
		if (id == -1)
			return Item.air;
		return items.get(reference.get(id));
	}

	public static Collection<Item> getAllItems()
	{
		return items.values();
	}
}
