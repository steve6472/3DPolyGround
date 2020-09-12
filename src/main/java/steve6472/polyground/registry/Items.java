package steve6472.polyground.registry;

import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.model.ModelLoader;
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
public class Items
{
	private static HashMap<String, Item> items;
	private static HashMap<Integer, String> reference;

	public static void register(CaveGame game)
	{
		File[] items = new File("game/objects/items").listFiles();

		reference = new HashMap<>();
		Items.items = new HashMap<>();

		Items.items.put("air", Item.createAir());
		reference.put(0, "air");

		for (int i = 0; i < Objects.requireNonNull(items).length; i++)
		{
			if (items[i].isDirectory())
				continue;

			JSONObject json = new JSONObject(ModelLoader.read(items[i]));
			Item item = null;

			if (json.optBoolean("debug") && !CaveGame.DEBUG)
				continue;

			if (json.has("special") && SpecialItemRegistry.getKeys().contains(json.getJSONObject("special").getString("name")))
			{
				item = SpecialItemRegistry.createSpecialItem(json.getJSONObject("special").getString("name"), json, i + 1);
			} else
			{
				try
				{
					item = new Item(json, i + 1);
				} catch (Exception ex)
				{
					System.err.println("Error while creating item " + items[i].getName());
					ex.printStackTrace();
					game.exit();
				}
			}

			game.getEventHandler().register(item);

			if (!Items.items.containsKey(item.getName()) && !reference.containsKey(i + 1))
			{
				Items.items.put(item.getName(), item);
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
