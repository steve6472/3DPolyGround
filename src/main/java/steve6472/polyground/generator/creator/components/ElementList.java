package steve6472.polyground.generator.creator.components;

import steve6472.polyground.block.model.IElement;
import steve6472.polyground.block.model.elements.CubeElement;
import steve6472.polyground.block.model.elements.PlaneElement;
import steve6472.polyground.block.model.elements.TriangleElement;
import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gfx.font.CustomChar;
import steve6472.sge.gfx.font.Font;
import steve6472.sge.gui.Component;
import steve6472.sge.main.MainApp;
import steve6472.sge.main.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 28.08.2020
 * Project: CaveGame
 *
 ***********************/
public class ElementList extends Component
{
	List<IElement> elements;
	IElement selected, hovered;
	private final List<Consumer<IElement>> selectElementEvent;

	public ElementList()
	{
		selectElementEvent = new ArrayList<>();
	}

	@Override
	public void init(MainApp mainApp)
	{
		elements = new ArrayList<>();
	}

	@Override
	public void tick()
	{
		hovered = null;
		int x = 7;
		int y = 4;

		for (IElement e : elements)
		{
			var v = processElement(e, true, x, y);
			x += v.getA();
			y += v.getB();
		}
	}

	@Override
	public void render()
	{
		SpriteRender.renderSingleBorderComponent(this, 0.0F, 0.0F, 0.0F, 1.0F, 0.5019608F, 0.5019608F, 0.5019608F, 1.0F);

		int x = 7;
		int y = 4;

		for (IElement e : elements)
		{
			var v = processElement(e, false, x, y);
			x += v.getA();
			y += v.getB();
		}
	}

	public void addSelectElementEvent(Consumer<IElement> event)
	{
		this.selectElementEvent.add(event);
	}

	public void addElement(IElement element)
	{
		this.elements.add(element);
	}

	public List<IElement> getElements()
	{
		return elements;
	}

	public IElement getSelected()
	{
		return selected;
	}

	private boolean mouseFlag;

	private Pair<Integer, Integer> processElement(IElement e, boolean tick, int x, int y)
	{
		CustomChar sign = CustomChar.ERROR;
		if (e instanceof TriangleElement) sign = CustomChar.FULL_TRIANGLE;
		if (e instanceof PlaneElement) sign = CustomChar.FULL_BOX;
		if (e instanceof CubeElement) sign = CustomChar.SELECTED_BOX;

		if (tick)
		{
			if (isCursorInComponent(getX() + 2, getY() + y - 2, getWidth() - 4, 10))
			{
				hovered = e;
				if (isLMBHolded())
				{
					if (!mouseFlag)
					{
						mouseFlag = true;
						if (e.getCreatorData().canOpen)
							e.getCreatorData().open = !e.getCreatorData().open;
					}
				} else if (isRMBHolded())
				{
					if (!mouseFlag)
					{
						mouseFlag = true;
						if (selected == e)
							selected = null;
						else
							selected = e;
						if (selected != null)
							selectElementEvent.forEach(c -> c.accept(selected));
					}
				} else
				{
					mouseFlag = false;
				}
			}
		} else
		{
			if (selected == e)
			{
				SpriteRender.fillRect(getX() + 2, getY() + y - 2, getWidth() - 4, 11, 0.0f, 0.8f, 0.8f, 0.5f);
			}
			if (isCursorInComponent(getX() + 2, getY() + y - 2, getWidth() - 4, 10))
			{
				SpriteRender.fillRect(getX() + 2, getY() + y - 2, getWidth() - 4, 11, 0.2f, 0.2f, 0.2f, 0.5f);
			}
			Font.renderCustom(getX() + x, getY() + y - 1, 1, "[#3080ff]", sign, "[#ffffff]", "[x4]", e.getName());
		}

		int dx = 0;
		int dy = 11;

		if (e.getCreatorData().open)
		{
			if (e.getChildren() != null)
			{
				for (IElement c : e.getChildren())
				{
					if (c == null)
						continue;

					var v = processElement(c, tick, x + dx + 12, y + dy);
					dx += v.getA();
					dy += v.getB();
				}
			}
		}

		return new Pair<>(dx, dy);
	}
}
