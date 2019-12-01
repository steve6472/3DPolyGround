package com.steve6472.polyground.gui;

import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.Options;
import com.steve6472.sge.gfx.font.CustomChar;
import com.steve6472.sge.gfx.font.Font;
import com.steve6472.sge.gui.Gui;
import com.steve6472.sge.gui.components.Background;
import com.steve6472.sge.gui.components.Button;
import com.steve6472.sge.main.MainApp;
import com.steve6472.sge.main.game.Tag;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.12.2019
 * Project: SJP
 *
 ***********************/
public class OptionsGui extends Gui implements IGamePause
{
	public OptionsGui(MainApp mainApp)
	{
		super(mainApp);
	}

	@Override
	public void createGui()
	{
		Background.createComponent(this);

		Button exit = new Button("Quit Game");
		exit.setLocation(getMainApp().getWidth() - 110, getMainApp().getHeight() - 40);
		exit.setSize(100, 30);
		exit.addClickEvent(c -> mainApp.exit());
		addComponent(exit);
		
		Button clearLightDebug = new Button("Clear Light Debug");
		clearLightDebug.setLocation(10, getMainApp().getHeight() - 40);
		clearLightDebug.setSize(100, 30);
		clearLightDebug.addClickEvent(c -> CaveGame.getInstance().particles.getMap().values().forEach(list -> list.forEach(particle ->
		{
			List<Tag> tags = particle.getTags();
			for (Tag t : tags)
				if (t.getName().startsWith("DebugLight"))
				{
					particle.forcedDeath = true;
					break;
				}
		})));
		addComponent(clearLightDebug);

		Options options = CaveGame.getInstance().options;

		int x = 0;

		checkBox("chunkModelDebug",     10, 10 + x++ * 30, () -> options.chunkModelDebug, b -> options.chunkModelDebug = b);
		checkBox("renderAtlases",       10, 10 + x++ * 30, () -> options.renderAtlases, b -> options.renderAtlases = b);
		checkBox("lightDebug",          10, 10 + x++ * 30, () -> options.lightDebug, b -> options.lightDebug = b);
		checkBox("subChunkBuildTime",   10, 10 + x++ * 30, () -> options.subChunkBuildTime, b -> options.subChunkBuildTime = b);
		checkBox("showGCLog",           10, 10 + x++ * 30, () -> options.showGCLog, b -> options.showGCLog = b);
		checkBox("renderTeleporters",   10, 10 + x++ * 30, () -> options.renderTeleporters, b -> options.renderTeleporters = b);
		checkBox("renderRifts",         10, 10 + x   * 30, () -> options.renderRifts, b -> options.renderRifts = b);

		checkBox("enablePostProcessing", 300, 10, () -> options.enablePostProcessing, b -> options.enablePostProcessing = b);
	}

	@Override
	public void showEvent()
	{
		mainApp.runEvent(new ShowEvent());
	}

	private void checkBox(String text, int x, int y, Supplier<Boolean> get, Consumer<Boolean> toggle)
	{
		BoxOfNamedChecks box = new BoxOfNamedChecks();
		box.sup = get;
		box.setText(text);
		box.setLocation(x, y);
		box.setSize(Font.getTextWidth(text, 1) + 30, 25);
		box.setBoxSize(14, 14);
		box.setSelectedChar(CustomChar.CROSS);
		box.setBoxPadding(5, 5);
		box.addChangeEvent(c -> toggle.accept(c.isToggled()));
		addComponent(box);
	}

	@Override
	public void guiTick()
	{

	}

	@Override
	public void render()
	{

	}

	@Override
	public boolean pauseGameIfOpen()
	{
		return true;
	}
}
