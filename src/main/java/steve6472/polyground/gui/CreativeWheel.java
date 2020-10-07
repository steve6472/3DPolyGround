package steve6472.polyground.gui;

import org.lwjgl.opengl.GL30;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.Block;
import steve6472.polyground.entity.player.EnumGameMode;
import steve6472.polyground.gfx.ItemRenderer;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.item.Item;
import steve6472.polyground.item.ItemGroups;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.registry.Items;
import steve6472.polyground.tessellators.Basic2DTessellator;
import steve6472.sge.gfx.font.Font;
import steve6472.sge.gui.Component;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.MainApp;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.KeyEvent;
import steve6472.sge.main.events.MouseEvent;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2020
 * Project: CaveGame
 *
 ***********************/
public class CreativeWheel extends Component
{
	private final Basic2DTessellator tess;
	private boolean open;
	private String overIndex;
	private boolean overItem;

	private ItemGroups.ItemGroup currentGroup;

	public CreativeWheel()
	{
		this.tess = new Basic2DTessellator(1024);
	}

	@Override
	public void init(MainApp mainApp)
	{
	}

	public void load()
	{
		currentGroup = CaveGame.getInstance().itemGroups.getRoot();
	}

	@Override
	public void tick()
	{

	}

	@Override
	public void render()
	{
		if (open)
		{
			if (CaveGame.getInstance().getPlayer().gamemode != EnumGameMode.CREATIVE)
			{
				open = false;
				return;
			}
			CaveGame.getInstance().options.isInMenu = true;

			int c = currentGroup.groups().size() + currentGroup.items().size();

			tess.begin(6 * c);

			tess.color(0.2f, 0.2f, 0.2f, 1f);

			float oneDeg = 0.017453292519943295f;
			float half = oneDeg * (180.0f / (float) c);
			float f = 180f;
			float g = 110f;

			overIndex = "";

			int i = 0;
			for (String s : currentGroup.groups().keySet())
			{
				render(s, c, i, half, f, g, false);
				i++;
			}
			for (Item item : currentGroup.items())
			{
				render(item.name, c, i, half, f, g, true);
				i++;
			}

			MainRender.shaders.color2DShader.bind();

			tess.loadPos(0);
			tess.loadColor(1);
			tess.draw(GL30.GL_TRIANGLES);
			tess.disable(0, 1);

			i = 0;
			for (String s : currentGroup.groups().keySet())
			{
				render(half, f, g, i, c, s, false);
				i++;
			}
			for (Item item : currentGroup.items())
			{
				render(half, f, g, i, c, item.getName(), true);
				i++;
			}

			Font.render(getX(), getY(), overIndex);
		}
	}

	private void render(float half, float f, float g, int i, int c, String s, boolean items)
	{
		double ang = 6.28318530718 / c * i;

		float v0x = cos(ang - half) * f + getX();
		float v0y = sin(ang - half) * f + getY();

		float v1x = cos(ang + half) * g + getX();
		float v1y = sin(ang + half) * g + getY();

		float v2x = cos(ang + half) * f + getX();
		float v2y = sin(ang + half) * f + getY();

		float v3x = cos(ang - half) * g + getX();
		float v3y = sin(ang - half) * g + getY();

		float x = (v0x + v1x + v2x + v3x) / 4f;
		float y = (v0y + v1y + v2y + v3y) / 4f;

		if (items)
		{
			Block b = Blocks.getBlockByName(s);
			if (b == Block.error)
			{
				Item item = Items.getItemByName(s);
				ItemRenderer.renderModel(item.model, getMain(), x, y, 30, 225, 0, 2f);
			} else
			{
				ItemRenderer.renderBlock(b, getMain(), x, y, 30, 225, 0, 2f);
			}
		} else
		{
			ItemRenderer.renderBlock(Blocks.getBlockByName(currentGroup.groups().get(s).preview()), getMain(), x, y, 30, 225, 0, 2f);
		}

		if (items)
		{
			Font.render((int) (x) - Font.getTextWidth(s, 1) / 2, (int) (y) + 30, s);
		} else
		{
			Font.render((int) (x) - Font.getTextWidth(currentGroup.groups().get(s).name(), 1) / 2, (int) (y) + 30, currentGroup.groups().get(s).name());
		}
	}

	private void render(String s, int c, int i, float half, float f, float g, boolean items)
	{
		double ang = 6.28318530718 / c * i;

		float v0x = cos(ang - half) * f + getX();
		float v0y = sin(ang - half) * f + getY();

		float v1x = cos(ang + half) * g + getX();
		float v1y = sin(ang + half) * g + getY();

		float v2x = cos(ang + half) * f + getX();
		float v2y = sin(ang + half) * f + getY();

		float v3x = cos(ang - half) * g + getX();
		float v3y = sin(ang - half) * g + getY();

		boolean isInside = pointInRect(getMain().getMouseX(), getMain().getMouseY(), v0x, v0y, v1x, v1y, v2x, v2y, v3x, v3y);

		if (isInside)
		{
			overIndex = s;
			overItem = items;
		}

		float col = isInside ? 0.8f : 0.4f;
		float itemCol = items ? 1f : col;

		tess.pos(v0x, v0y)
			.color(col, col, itemCol, 0.8f)
			.endVertex();

		tess.pos(v1x, v1y)
			.color(col - 0.2f, col - 0.2f, itemCol - 0.2f, 0.9f)
			.endVertex();

		tess.pos(v2x, v2y)
			.color(col, col, itemCol, 0.8f)
			.endVertex();


		tess.pos(v0x, v0y)
			.color(col, col, itemCol, 0.8f)
			.endVertex();

		tess.pos(v3x, v3y)
			.color(col - 0.2f, col - 0.2f, itemCol - 0.2f, 0.9f)
			.endVertex();

		tess.pos(v1x, v1y)
			.color(col - 0.2f, col - 0.2f, itemCol - 0.2f, 0.9f)
			.endVertex();
	}

	@Event
	public void select(MouseEvent e)
	{
		if (isOpen() && e.getAction() == KeyList.PRESS && e.getButton() == KeyList.LMB)
		{
			if (overItem && !overIndex.isBlank())
			{
				Item selectedItem = Items.getItemByName(overIndex);
				CaveGame.getInstance().inGameGui.itemBar.scroll = selectedItem.getId() - 1;
				open = false;
			} else
			{
				if (!overIndex.isBlank() && currentGroup.groups().get(overIndex).groups().size() + currentGroup.groups().get(overIndex).items().size() >= 3)
				{
					currentGroup = currentGroup.groups().get(overIndex);
				}
			}
		}
	}

	private boolean pointInRect(float px, float py, float v0x, float v0y, float v1x, float v1y, float v2x, float v2y, float v3x, float v3y)
	{
		return pointInTriangle(px, py, v0x, v0y, v1x, v1y, v2x, v2y) || pointInTriangle(px, py, v0x, v0y, v3x, v3y, v1x, v1y);
	}

	private float sign(float p0x, float p0y, float p1x, float p1y, float p2x, float p2y)
	{
		return (p0x - p2x) * (p1y - p2y) - (p1x - p2x) * (p0y - p2y);
	}

	private boolean pointInTriangle(float px, float py, float v0x, float v0y, float v1x, float v1y, float v2x, float v2y)
	{
		float d1, d2, d3;
		boolean has_neg, has_pos;

		d1 = sign(px, py, v0x, v0y, v1x, v1y);
		d2 = sign(px, py, v1x, v1y, v2x, v2y);
		d3 = sign(px, py, v2x, v2y, v0x, v0y);

		has_neg = (d1 < 0) || (d2 < 0) || (d3 < 0);
		has_pos = (d1 > 0) || (d2 > 0) || (d3 > 0);

		return !(has_neg && has_pos);
	}


	private float sin(double ang)
	{
		return (float) Math.sin(ang);
	}

	private float cos(double ang)
	{
		return (float) Math.cos(ang);
	}

	@Event
	public void toggle(KeyEvent e)
	{
		if (((!CaveGame.getInstance().options.isInMenu && !CaveGame.getInstance().inGameGui.chat.isFocused()) || isOpen()) && e.getAction() == KeyList.PRESS && e.getKey() == KeyList.E && CaveGame.getInstance().getPlayer().gamemode == EnumGameMode.CREATIVE)
		{
			open = !open;
			currentGroup = CaveGame.getInstance().itemGroups.getRoot();
		}
	}

	public boolean isOpen()
	{
		return open;
	}
}
