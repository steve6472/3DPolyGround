package steve6472.polyground.item.special;

import steve6472.polyground.CaveGame;
import steve6472.polyground.events.InGameGuiEvent;
import steve6472.polyground.item.Item;
import steve6472.sge.gfx.font.Font;
import steve6472.sge.main.events.Event;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class BlockInspectorItem extends Item
{
	public BlockInspectorItem(File f, int id)
	{
		super(f, id);
	}

	@Event
	public void renderBlock(InGameGuiEvent.PostRender e)
	{
		if (CaveGame.itemInHand == this && CaveGame.getInstance().hitPicker.hit)
			Font.render("Block: " + CaveGame.getInstance().hitPicker.getHitResult().getBlock().getName(), 5, CaveGame.getInstance().getHeight() - 15);
	}
}
