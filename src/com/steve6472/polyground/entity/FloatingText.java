package com.steve6472.polyground.entity;

import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.PolyUtil;
import com.steve6472.polyground.events.InGameGuiEvent;
import com.steve6472.sge.gfx.SpriteRender;
import com.steve6472.sge.gfx.font.Font;
import com.steve6472.sge.main.events.Event;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12.11.2019
 * Project: SJP
 *
 ***********************/
public class FloatingText extends EntityBase
{
	private boolean isDead;

	private String text;

	private Vector2f screen;
	long spawnTime;

	public FloatingText()
	{
		screen = new Vector2f();
	}

	public FloatingText(String text)
	{
		this.text = text;
		screen = new Vector2f();
		spawnTime = System.currentTimeMillis();
	}

	public void setText(String text)
	{
		this.text = text;
	}

	@Override
	public boolean isDead()
	{
		return isDead || spawnTime + 2000 < System.currentTimeMillis();
	}

	public void setDead()
	{
		isDead = true;
	}

	@Override
	public String getName()
	{
		return "text";
	}

	@Override
	public void setPosition(float x, float y, float z)
	{
		super.setPosition(x, y, z);
		update();
	}

	@Override
	public void setPosition(Vector3f position)
	{
		super.setPosition(position);
		update();
	}

	public void update()
	{
		PolyUtil.toScreenPos(getPosition(), screen);
	}

	@Event
	public void renderPos(InGameGuiEvent.PostRender e)
	{
		if (isDead())
			return;
		update();
		renderPosition(screen);
	}

	private void renderPosition(Vector2f screenPos)
	{
		if (!CaveGame.getInstance().frustum.insideFrsutum(getX(), getY(), getZ(), 0.2f)) return;

		String text = this.text == null ? String.format("[%.0f, %.0f, %.0f]", getX(), getY(), getZ()) : this.text;

		int w = Font.getTextWidth(text, 1);

		SpriteRender.fillRect((int) screenPos.x - w / 2.0f, (int) screenPos.y - 2, w + 3, 12, 0.2f, 0.2f, 0.2f, 0.2f);

		Font.render(text, (int) screenPos.x - Font.getTextWidth(text, 1) / 2, (int) screenPos.y);
	}
}
