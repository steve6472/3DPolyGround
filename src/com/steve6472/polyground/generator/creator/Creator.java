package com.steve6472.polyground.generator.creator;

import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.main.MainApp;
import com.steve6472.sge.main.MainFlags;

import static org.lwjgl.opengl.GL11.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 10.10.2019
 * Project: SJP
 *
 ***********************/
public class Creator extends MainApp
{
	private BlockCreatorGui blockCreatorGui;
	private ItemCreatorGui itemCreatorGui;

	public static Sprite UI;
	public static Sprite BUTTONS;

	@Override
	public void init()
	{
		UI = new Sprite("*creator\\ui.png");
		BUTTONS = new Sprite("*creator\\buttons.png");

		blockCreatorGui = new BlockCreatorGui(this);
		blockCreatorGui.setVisible(true);

		itemCreatorGui = new ItemCreatorGui(this);
	}

	@Override
	public void tick()
	{
		tickGui();
		tickDialogs();
	}

	@Override
	public void render()
	{
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);
		glViewport(0, 0, getWidth(), getHeight());

		renderGui();
		renderDialogs();

		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glViewport(0, 0, getWidth(), getHeight());
	}

	@Override
	public void setWindowHints()
	{
	}

	@Override
	public int getWindowWidth()
	{
		return 16 * 70;
	}

	@Override
	public int getWindowHeight()
	{
		return 9 * 70;
	}

	@Override
	protected int[] getFlags()
	{
		return new int[] {MainFlags.ADD_BASIC_ORTHO, MainFlags.ENABLE_GL_DEPTH_TEST};
	}

	@Override
	public void exit()
	{
		getWindow().close();
	}

	@Override
	public String getTitle()
	{
		return "Creator";
	}

	public static void main(String[] args)
	{
		System.setProperty("joml.format", "false");
		System.setProperty("joml.fastmath", "true");
		System.setProperty("joml.sinLookup", "true");
		new Creator();
	}
}
