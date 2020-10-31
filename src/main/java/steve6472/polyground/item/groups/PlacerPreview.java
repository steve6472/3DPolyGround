package steve6472.polyground.item.groups;

import steve6472.polyground.gfx.ItemRenderer;
import steve6472.polyground.registry.Blocks;
import steve6472.sge.main.MainApp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.10.2020
 * Project: CaveGame
 *
 ***********************/
public class PlacerPreview extends Preview
{
	public PlacerPreview(String name, String id, String preview, PreviewType previewType)
	{
		super(
			name,
			id,
			preview,
			previewType == PreviewType.GROUP ? 0.4f : 0.6f,
			previewType == PreviewType.GROUP ? 0.4f : 0.4f,
			previewType == PreviewType.GROUP ? 0.4f : 0.6f,
			previewType);
	}

	@Override
	public void renderPreview(MainApp mainApp, float x, float y)
	{
		ItemRenderer.renderBlock(Blocks.getBlockByName(preview()), mainApp, x, y, 30, 225, 0, 2f);
	}
}
