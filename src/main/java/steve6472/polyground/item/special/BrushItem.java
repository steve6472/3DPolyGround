package steve6472.polyground.item.special;

import org.json.JSONObject;
import steve6472.polyground.item.Item;
import steve6472.polyground.item.itemdata.BrushData;
import steve6472.polyground.item.itemdata.IItemData;
import steve6472.polyground.item.itemdata.ItemData;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 07.10.2020
 * Project: CaveGame
 *
 ***********************/
public class BrushItem extends Item implements IItemData
{
	public BrushItem(JSONObject json, int id)
	{
		super(json, id);
	}

	@Override
	public ItemData createNewItemData()
	{
		return new BrushData();
	}
}
