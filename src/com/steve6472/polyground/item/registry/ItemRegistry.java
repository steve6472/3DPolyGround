package com.steve6472.polyground.item.registry;

import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.item.Item;
import com.steve6472.sge.main.MainApp;
import com.steve6472.sss2.SSS;

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

	public ItemRegistry(CaveGame pg)
	{
		File[] blocks = new File(MainApp.class.getResource("/items").getFile()).listFiles();

		reference = new HashMap<>();
		ItemRegistry.items = new HashMap<>();

		ItemRegistry.items.put("air", Item.createAir());
		reference.put(0, "air");

		for (int i = 0; i < Objects.requireNonNull(blocks).length; i++)
		{
			if (blocks[i].isDirectory()) continue;

			SSS t = new SSS(blocks[i]);
			Item item;

			if (t.containsName("special") && SpecialItemRegistry.getKeys().contains(t.getString("special")))
			{
				item = SpecialItemRegistry.createSpecialItem(t.getString("special"), blocks[i], i + 1);
			} else
			{
				item = new Item(blocks[i], i + 1);
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
		if (id == -1) return Item.air;
		return items.get(reference.get(id));
	}

	public static Collection<Item> getAllItems()
	{
		return items.values();
	}
}
