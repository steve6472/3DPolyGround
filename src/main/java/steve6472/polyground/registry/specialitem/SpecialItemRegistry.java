package steve6472.polyground.registry.specialitem;

import org.json.JSONObject;
import steve6472.polyground.item.Item;
import steve6472.polyground.item.special.*;

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

	public static final SpecialItemEntry<BlockItem> blockItem = register("block", BlockItem::new);
	public static final SpecialItemEntry<SlabItem> slabItem = register("slab", SlabItem::new);
	public static final SpecialItemEntry<SpeedometerItem> speedometerItem = register("speedometer", SpeedometerItem::new);
	public static final SpecialItemEntry<BlockInspectorItem> blockInspector = register("block_inspector", BlockInspectorItem::new);
	public static final SpecialItemEntry<WorldEditItem> worldedit = register("worldedit", WorldEditItem::new);
	public static final SpecialItemEntry<RiftPlacerItem> riftplacer = register("riftplacer", RiftPlacerItem::new);
	public static final SpecialItemEntry<GunItem> gun = register("gun", GunItem::new);
	public static final SpecialItemEntry<WaterBucketItem> waterBucket = register("waterbucket", WaterBucketItem::new);
	public static final SpecialItemEntry<VoidBucketItem> voidBucket = register("voidbucket", VoidBucketItem::new);
	public static final SpecialItemEntry<TeleportPlacerItem> teleplacer = register("tele_placer", TeleportPlacerItem::new);
	public static final SpecialItemEntry<BrushItem> brush = register("brush", BrushItem::new);
	public static final SpecialItemEntry<ChipItem> chip = register("chip", ChipItem::new);

	public static <T extends Item> SpecialItemEntry<T> register(String id, ISpecialItemFactory<T> factory)
	{
		SpecialItemEntry<T> entry = new SpecialItemEntry<>(factory);
		specialItemRegistry.put(id, entry);
		return entry;
	}

	public static Item createSpecialItem(String id, JSONObject json, int itemId)
	{
		return specialItemRegistry.get(id).createNew(json, itemId);
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
