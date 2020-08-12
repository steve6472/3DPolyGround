package steve6472.polyground.gui;

import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gfx.font.Font;
import steve6472.sge.main.events.Event;

import java.util.function.Supplier;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12.08.2020
 * Project: CaveGame
 *
 ***********************/
public class OptNumberSelector extends NumberSelector
{
	Supplier<Integer> sup;
	String text;

	@Event
	public void show(ShowEvent e)
	{
		setValue(sup.get());
	}

	@Override
	public void render()
	{
		if (this.text != null)
		{
			int tx = Font.getTextWidth(this.text, 1);
			SpriteRender.renderSingleBorder(x, y, width, 25, 0.6F, 0.6F, 0.6F, 1.0F, 0.0F, 0.2F, 0.3F, 0.2F);
			Font.render(this.text, this.x + (this.width - 22) / 2 - tx / 2, this.y + 25 / 2 - this.getFontSize() * 4 - 8);
		}
		super.render();
	}
}
