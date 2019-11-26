package com.steve6472.polyground.gui;

import com.steve6472.sge.gfx.font.Font;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.11.2019
 * Project: SJP
 *
 ***********************/
public class TextLog
{
	private int displayTime = 2000;
	private int fadeStart = 1000;
	private int maxDisplayed = 16;

	private List<Long> displayed;
	private List<Object[]> texts;

	public TextLog()
	{
		displayed = new ArrayList<>(8);
		texts = new ArrayList<>(8);
	}

	public void tick()
	{
		long now = System.currentTimeMillis();

		for (int j = 0; j < displayed.size(); j++)
		{
			displayed.set(j, displayed.get(j) - 1);
		}

		int i = displayed.size() - 1;
		for (Iterator<Long> iter = displayed.iterator(); iter.hasNext(); )
		{
			Long dis = iter.next();
			if (dis + displayTime <= now)
			{
				iter.remove();
				texts.remove(displayed.size() - i);
			}
			i--;
		}
	}

	public void addLine(Object... text)
	{
		if (displayed.size() >= maxDisplayed)
		{
			displayed.remove(0);
			texts.remove(0);
		}
		displayed.add(System.currentTimeMillis());
		texts.add(text);
	}

	public void render(int x, int y)
	{
		long now = System.currentTimeMillis();

		for (int i = 0; i < displayed.size(); i++)
		{
			long dis = displayed.get(i);
			long difference = now - dis - fadeStart;

			if (dis + fadeStart <= now)
			{
				Object[] arr = new Object[texts.get(i).length + 1];
				arr[0] = "[1.0,1.0,1.0," + (1 - (float) (difference) / (float) displayTime * 2f) + "]";

				System.arraycopy(texts.get(i), 0, arr, 1, texts.get(i).length);

				Font.renderCustom(x, y + i * 10, 1, arr);
			} else
				Font.renderCustom(x, y + i * 10, 1, texts.get(i));
		}
	}

	public int getDisplayTime()
	{
		return displayTime;
	}

	public void setDisplayTime(int displayTime)
	{
		this.displayTime = displayTime;
	}

	public int getFadeStart()
	{
		return fadeStart;
	}

	public void setFadeStart(int fadeStart)
	{
		this.fadeStart = fadeStart;
	}

	public int getMaxDisplayed()
	{
		return maxDisplayed;
	}

	public void setMaxDisplayed(int maxDisplayed)
	{
		this.maxDisplayed = maxDisplayed;
	}
}




