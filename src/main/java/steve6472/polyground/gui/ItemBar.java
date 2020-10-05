package steve6472.polyground.gui;

import steve6472.polyground.CaveGame;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.gfx.shaders.ItemTextureShader;
import steve6472.polyground.item.Item;
import steve6472.polyground.item.ItemAtlas;
import steve6472.polyground.registry.Items;
import steve6472.polyground.tessellators.ItemTextureTessellator;
import steve6472.sge.gfx.Sprite;
import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gfx.Tessellator;
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
	private int scroll = 0;
	private ItemTextureTessellator itemTextureTessellator;

	@Override
	public void init(MainApp main)
	{
		itemTextureTessellator = new ItemTextureTessellator(6 * 7 * 2);
	}

	@Override
	public void tick()
	{

	}

	@Override
	public void render()
	{
		SpriteRender.renderDoubleBorder(getX(), getY() + 38 * 3, 38, 38, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0);

		ItemAtlas itemAtlas = CaveGame.getInstance().itemAtlas;
		Sprite.bind(0, itemAtlas.itemAtlas.texture);

		MainRender.shaders.itemTextureShader.bind();
		MainRender.shaders.itemTextureShader.setUniform(ItemTextureShader.ATLAS, 0);

		itemTextureTessellator.begin(6 * 7);

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

			renderItem(getX() + 3, getY() + i * 38 + 3 + 38 * (3 + -scroll), 32, 32, i);
		}

		itemTextureTessellator.loadPos(0);
		itemTextureTessellator.loadTexture(1);
		itemTextureTessellator.loadAlpha(2);
		itemTextureTessellator.draw(Tessellator.TRIANGLES);
		itemTextureTessellator.disable(0, 1, 2);

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

	private void renderItem(int x, int y, int w, int h, int index)
	{
		float u = index % CaveGame.getInstance().itemAtlas.getTileCount();
		float v = index / CaveGame.getInstance().itemAtlas.getTileCount();

		float t = 1f / (float) CaveGame.getInstance().itemAtlas.getTileCount();

		float minU = u * t;
		float minV = v * t;
		float maxU = u * t + t;
		float maxV = v * t + t;

		float topAlpha = 1;
		float bottomAlpha = 1;

		if (index - scroll > 2)
		{
			topAlpha = 1.25f;
			bottomAlpha = 0;
		}

		if (index - scroll < -2)
		{
			topAlpha = 0;
			bottomAlpha = 1.25f;
		}

		itemTextureTessellator.pos(x, y).texture(minU, minV).alpha(topAlpha).endVertex();
		itemTextureTessellator.pos(x, y + h).texture(minU, maxV).alpha(bottomAlpha).endVertex();
		itemTextureTessellator.pos(x + w, y + h).texture(maxU, maxV).alpha(bottomAlpha).endVertex();

		itemTextureTessellator.pos(x + w, y + h).texture(maxU, maxV).alpha(bottomAlpha).endVertex();
		itemTextureTessellator.pos(x + w, y).texture(maxU, minV).alpha(topAlpha).endVertex();
		itemTextureTessellator.pos(x, y).texture(minU, minV).alpha(topAlpha).endVertex();
	}
}
