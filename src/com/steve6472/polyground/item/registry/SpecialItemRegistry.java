package com.steve6472.polyground.item.registry;

import com.steve6472.polyground.item.Item;
import com.steve6472.polyground.item.special.*;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 28.09.2019
 * Project: SJP
 *
 ***********************/
public class SpecialItemRegistry
{
	private static final HashMap<String, SpecialItemEntry<? extends Item>> specialItemRegistry = new HashMap<>();

	public static final SpecialItemEntry<SlabItem> slabItem = register("slab", SlabItem::new);
	public static final SpecialItemEntry<SpeedometerItem> speedometerItem = register("speedometer", SpeedometerItem::new);
	public static final SpecialItemEntry<BlockInspectorItem> blockInspector = register("block_inspector", BlockInspectorItem::new);
	public static final SpecialItemEntry<WorldEditItem> worldedit = register("worldedit", WorldEditItem::new);
	public static final SpecialItemEntry<RegeneratorItem> regenerator = register("regenerator", RegeneratorItem::new);

	public static <T extends Item> SpecialItemEntry<T> register(String id, ISpecialItemFactory<T> factory)
	{
		SpecialItemEntry<T> entry = new SpecialItemEntry<>(factory);
		specialItemRegistry.put(id, entry);
		return entry;
	}

	public static Item createSpecialItem(String id, File f, int itemId)
	{
		return specialItemRegistry.get(id).createNew(f, itemId);
	}

	public static Collection<SpecialItemEntry<? extends Item>> getEntries()
	{
		return specialItemRegistry.values();
	}

	public static Set<String> getKeys()
	{
		return specialItemRegistry.keySet();
	}
}
