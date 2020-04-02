package steve6472.polyground.generator.creator.components;

import steve6472.polyground.generator.creator.Creator;
import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gui.components.ToggleButton;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 07.12.2019
 * Project: SJP
 *
 ***********************/
public class ImageToggleButton extends ToggleButton
{
	private float X, Y, DX, DY;
	private EnumSizeType size;

	public ImageToggleButton(EnumSizeType size, int x, int y, int dx, int dy)
	{
		this.size = size;
		this.X = x;
		this.Y = y;
		this.DX = dx;
		this.DY = dy;
		super.setSize(size.w, size.h);
	}

	@Override
	public void render()
	{
		SpriteRender.manualStart();
		SpriteRender.manualSprite(Creator.UI);

		if (enabled && !hovered)
			SpriteRender.renderSpriteFromAtlas(x, y, size.bw, size.bh, 0, 2, 0, 4, 4);

		if (!enabled)
			SpriteRender.renderSpriteFromAtlas(x, y, size.bw, size.bh, 0, 2, 1, 4, 4);

		if (enabled && isToggled())
			SpriteRender.renderSpriteFromAtlas(x, y, size.bw, size.bh, 0, 3, 1, 4, 4);

		if (enabled && !isToggled())
			SpriteRender.renderSpriteFromAtlas(x, y, size.bw, size.bh, 0, 2, 2, 4, 4);

		if (!isEnabled())
		{
			SpriteRender.renderSpriteFromAtlas(x + 4, y + 4, size.iw, size.ih, 0, DX, DY, 8, 8);
		} else
		{
			SpriteRender.renderSpriteFromAtlas(x + 4, y + 4, size.iw, size.ih, 0, X, Y, 8, 8);
		}

		SpriteRender.manualEnd();
	}

	@Override
	public void setSize(int w, int h)
	{
		try
		{
			throw new IllegalAccessException("ImageToggleButton has fixed size!");
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
}
