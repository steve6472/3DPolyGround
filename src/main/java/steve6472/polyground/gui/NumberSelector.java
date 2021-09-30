package steve6472.polyground.gui;

import org.joml.Vector3f;
import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gfx.font.Font;
import steve6472.sge.gui.Component;
import steve6472.sge.gui.components.schemes.SchemeButton;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.MainApp;
import steve6472.sge.main.util.MathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12.08.2020
 * Project: CaveGame
 *
 ***********************/
public class NumberSelector extends Component
{
	private int max, min, value;
	private List<Consumer<NumberSelector>> changeEvents;
	private boolean isEnabled;
	private boolean leftHover, rightHover;
	private int buttonWidth;
	private SchemeButton buttonScheme;
	private int fontSize;

	public NumberSelector()
	{
		this.changeEvents = new ArrayList<>();
		buttonWidth = 20;
		isEnabled = true;
		fontSize = 1;
		this.setButtonScheme((SchemeButton)MainApp.getSchemeRegistry().copyDefaultScheme(SchemeButton.class));
	}

	@Override
	public void init(MainApp mainApp)
	{

	}

	@Override
	public void tick()
	{
		if (isEnabled)
		{
			leftHover = this.isCursorInComponent(this.x, this.y, buttonWidth, this.height);

			if (leftHover)
			{
				this.onMouseClicked(KeyList.LMB, (c) ->
				{
					setValue(value - 1);
					changeEvents.forEach(d -> d.accept(this));
				});
			} else
			{
				rightHover = this.isCursorInComponent(this.x + width - buttonWidth, this.y, buttonWidth, this.height);
				if (rightHover)
				{
					this.onMouseClicked(KeyList.LMB, (c) ->
					{
						setValue(value + 1);
						changeEvents.forEach(d -> d.accept(this));
					});
				}
			}
		}
	}

	@Override
	public void render()
	{
		renderButton(leftHover, x);
		renderButton(rightHover, x + width - buttonWidth);
		renderText();
	}

	protected void renderText()
	{
		int fontWidth;
		int fontHeight;
		String text = Integer.toString(value);
		fontWidth = Font.getTextWidth(text, this.fontSize) / 2;
		fontHeight = 8 * this.fontSize / 2;

		Font.renderCustom(this.x + this.width / 2 - fontWidth, this.y + this.height / 2 - fontHeight, (float)this.fontSize, this.getColor(isEnabled ? getButtonScheme().enabled : getButtonScheme().disabled), text);
	}

	protected String getColor(Vector3f c)
	{
		return "[" + c.x + "," + c.y + "," + c.z + ",1.0]";
	}

	private void renderButton(boolean hovered, int x)
	{
		if (this.isEnabled && !hovered) {
			SpriteRender.renderDoubleBorder(x, y, buttonWidth, height, buttonScheme.enabledOutsideBorder, buttonScheme.enabledInsideBorder, buttonScheme.enabledFill);
		}

		if (!isEnabled) {
			SpriteRender.renderDoubleBorder(x, y, buttonWidth, height, buttonScheme.disabledOutsideBorder, buttonScheme.disabledInsideBorder, buttonScheme.disabledFill);
		}

		if (isEnabled && hovered) {
			SpriteRender.renderDoubleBorder(x, y, buttonWidth, height, buttonScheme.hoveredOutsideBorder, buttonScheme.hoveredInsideBorder, buttonScheme.hoveredFill);
		}
	}

	public void addChangeEvent(Consumer<NumberSelector> event)
	{
		changeEvents.add(event);
	}

	public boolean isEnabled()
	{
		return isEnabled;
	}

	public void setEnabled(boolean enabled)
	{
		isEnabled = enabled;
	}


	public int getButtonWidth()
	{
		return buttonWidth;
	}

	public void setButtonWidth(int buttonWidth)
	{
		this.buttonWidth = buttonWidth;
	}

	public int getMax()
	{
		return max;
	}

	public void setMax(int max)
	{
		this.max = max;
	}

	public int getMin()
	{
		return min;
	}

	public void setMin(int min)
	{
		this.min = min;
	}

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = MathUtil.clamp(min, max, value);
	}

	public void setButtonScheme(SchemeButton buttonScheme)
	{
		this.buttonScheme = buttonScheme;
	}

	public SchemeButton getButtonScheme()
	{
		return buttonScheme;
	}

	public int getFontSize()
	{
		return fontSize;
	}

	public void setFontSize(int fontSize)
	{
		this.fontSize = fontSize;
	}
}
