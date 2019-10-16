package com.steve6472.polyground.gui;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.commands.CommandRegistry;
import com.steve6472.polyground.world.Chunk;
import com.steve6472.polyground.world.SubChunk;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.gfx.SpriteRender;
import com.steve6472.sge.gui.Gui;
import com.steve6472.sge.gui.components.Background;
import com.steve6472.sge.gui.components.Button;
import com.steve6472.sge.main.MainApp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.09.2019
 * Project: SJP
 *
 ***********************/
public class MainMenu extends Gui implements IGamePause
{
	public MainMenu(MainApp mainApp)
	{
		super(mainApp);
	}

	private Sprite main;

	@Override
	public void createGui()
	{
		Background.createComponent(this);

		main = new Sprite("main_title.png");

		Button sandbox = new Button("Sandbox");
		sandbox.setLocation(30, 30);
		sandbox.setSize(100, 30);
		sandbox.addClickEvent(c -> {

			setVisible(false);
			CaveGame.getInstance().inGameGui.setVisible(true);
			CaveGame.getInstance().options.isGamePaused = false;

			CaveGame.getInstance().world.addChunk(new Chunk(0, 0, CaveGame.getInstance().world).generate(), false);

		});
		addComponent(sandbox);

		Button house = new Button("House");
		house.setLocation(30, 70);
		house.setSize(100, 30);
		house.addClickEvent(c -> {

			setVisible(false);
			CaveGame.getInstance().inGameGui.setVisible(true);
			CaveGame.getInstance().options.isGamePaused = false;

			CaveGame.getInstance().world.addChunk(new Chunk(0, 0, CaveGame.getInstance().world).generate(), false);
			try
			{
				CommandRegistry registry = CaveGame.getInstance().commandRegistry;
				registry.dispatcher.execute("loadworld house", registry.commandSource);
				registry.dispatcher.execute("tp 8 1.005 8", registry.commandSource);
			} catch (CommandSyntaxException e)
			{
				e.printStackTrace();
			}

		});
		addComponent(house);

		Button world = new Button("World");
		world.setLocation(30, 110);
		world.setSize(100, 30);
		world.addClickEvent(c -> {

			setVisible(false);
			CaveGame.getInstance().inGameGui.setVisible(true);
			CaveGame.getInstance().options.isGamePaused = false;

			SubChunk.generator = CaveGame.getInstance().generatorRegistry.getGenerator("world");

			int r = 3;
			for (int i = -r; i <= r; i++)
			{
				for (int j = -r; j <= r; j++)
				{
					CaveGame.getInstance().world.addChunk(new Chunk(i, j, CaveGame.getInstance().world).generate(), false);
				}
			}

			try
			{
				CommandRegistry registry = CaveGame.getInstance().commandRegistry;
				registry.dispatcher.execute("tp 8 32.005 8", registry.commandSource);
			} catch (CommandSyntaxException e)
			{
				e.printStackTrace();
			}

		});
		addComponent(world);
	}

	@Override
	public void guiTick()
	{
	}

	@Override
	public void render()
	{
		float f = (float) (getMainApp().getWidth() - 400) / ((float) getMainApp().getWidth());
		SpriteRender.renderSprite(200, 100, (int) (main.getWidth() * f), (int) (main.getHeight() * f), 0, main);
	}

	@Override
	public boolean pauseGameIfOpen()
	{
		return true;
	}
}
