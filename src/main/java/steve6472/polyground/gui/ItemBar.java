package steve6472.polyground.gui;

import steve6472.polyground.CaveGame;
import steve6472.polyground.gfx.ItemRenderer;
import steve6472.polyground.item.Item;
import steve6472.polyground.registry.Items;
import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gfx.font.Font;
import steve6472.sge.gui.Component;
import steve6472.sge.main.MainApp;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.ScrollEvent;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.09.2019
 * Project: SJP
 *
 ***********************/
public class ItemBar extends Component
{
	public int scroll = 0;

	@Override
	public void init(MainApp main)
	{
	}

	@Override
	public void tick()
	{

	}

	@Override
	public void render()
	{
		SpriteRender.renderDoubleBorder(getX(), getY() + 38 * 3, 38, 38, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0);

		for (int i = 0; ; i++)
		{
			if (i < 0 || i > Items.getAllItems().size())
				break;

			Item item = Items.getItemById(i + 1); // Ignore air

			if (item == null)
				continue;
			if (item == Item.air)
				continue;

			if (i - scroll > 3)
				break;
			if (i - scroll < -3)
				continue;

			ItemRenderer.renderModel(item.model, getMain(), getX() + 3 + 16, getY() + i * 38 + 38 * (3 + -scroll) + 19, 30, 225, 0, 1.25f);
		}

		CaveGame.itemInHand = Items.getItemById(scroll + 1);

		if (CaveGame.itemInHand == null)
		{
			scroll = 0;
			CaveGame.itemInHand = Items.getItemById(scroll + 1);
			return;
		}

		Font.renderCustom(getMain().getWidth() - Font.getTextWidth(CaveGame.itemInHand.getName(), 1) - 5, 5, 1f, CaveGame.itemInHand.getName());
	}

	@Event
	public void scroll(ScrollEvent e)
	{
		if (!isVisible())
			return;

		if (e.getyOffset() > 0)
			scroll--;
		if (e.getyOffset() < 0)
			scroll++;

		if (scroll < 0)
			scroll = 0;
		if (scroll >= Items.getAllItems().size() - 1)
			scroll = Items.getAllItems().size() - 2;
	}
}
