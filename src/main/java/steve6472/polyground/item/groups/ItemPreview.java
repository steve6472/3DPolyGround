package steve6472.polyground.item.groups;

import steve6472.polyground.block.Block;
import steve6472.polyground.gfx.ItemRenderer;
import steve6472.polyground.item.Item;
import steve6472.polyground.registry.Items;
import steve6472.sge.main.MainApp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.10.2020
 * Project: CaveGame
 *
 ***********************/
public class ItemPreview extends Preview
{
	public ItemPreview(String name, String id, String preview, PreviewType previewType)
	{
		super(
			name,
			id,
			preview,
			previewType == PreviewType.GROUP ? 0.4f : 0.4f,
			previewType == PreviewType.GROUP ? 0.4f : 0.4f,
			previewType == PreviewType.GROUP ? 0.4f : 1.0f,
			previewType);
	}

	@Override
	public void renderPreview(MainApp mainApp, float x, float y)
	{
		Item item = Items.getItemByName(preview());
		if (item == null)
			ItemRenderer.renderBlock(Block.ERROR, mainApp, x, y, 30, 225, 0, 2f);
		else
			ItemRenderer.renderModel(item.model, mainApp, x, y, 30, 225, 0, 2f);
	}
}
