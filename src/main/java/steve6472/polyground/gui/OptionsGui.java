package steve6472.polyground.gui;

import steve6472.polyground.CaveGame;
import steve6472.polyground.Options;
import steve6472.polyground.world.World;
import steve6472.polyground.world.WorldSerializer;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gfx.font.CustomChar;
import steve6472.sge.gfx.font.Font;
import steve6472.sge.gui.Gui;
import steve6472.sge.gui.components.Button;
import steve6472.sge.gui.components.ComponentRender;
import steve6472.sge.gui.components.ItemList;
import steve6472.sge.gui.components.Slider;
import steve6472.sge.main.MainApp;
import steve6472.sge.test.Fex;

import java.io.IOException;
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
//		Background.createComponent(this);

		Button exit = new Button("Quit Game");
		exit.setLocation(getMainApp().getWidth() - 110, getMainApp().getHeight() - 40);
		exit.setSize(100, 30);
		exit.addClickEvent(c -> mainApp.exit());
		addComponent(exit);

		Button save = new Button("Save World");
		save.setLocation(getMainApp().getWidth() - 110, getMainApp().getHeight() - 80);
		save.setSize(100, 30);
		save.addClickEvent(c ->
		{
			try
			{
				World world = CaveGame.getInstance().getWorld();
				if (world.worldName == null || world.worldName.isEmpty())
				{
					CaveGame.getInstance().inGameGui.chat.addText("[#FF5555]", "World has invalid name, please use command to save the world");
					System.err.println("World has no name!");
				} else
				{
					WorldSerializer.serialize(world);
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		});
		addComponent(save);

		Button menu = new Button("Quit to Menu");
		menu.setLocation(getMainApp().getWidth() - 110, getMainApp().getHeight() - 120);
		menu.setSize(100, 30);
		menu.addClickEvent(c ->
		{
			CaveGame.getInstance().getWorld().clearWorld();
			CaveGame.getInstance().world = null;
			CaveGame.getInstance().inGameGui.setVisible(false);
			CaveGame.getInstance().options.isGamePaused = true;
			CaveGame.getInstance().mainMenu.setVisible(true);
			setVisible(false);
			CaveGame.getInstance().getPlayer().setPosition(-2, 0.05f, 0);
			CaveGame.getInstance().getPlayer().setMotion(0, 0, 0);
			CaveGame.getInstance().getPlayer().updateHitbox();
		});
		addComponent(menu);

		Options options = CaveGame.getInstance().options;

		int x = 20 * (ModelLayer.values().length + 1);
		x /= 30;
		x += 1;

		checkBox("chunkModelDebug", 10, 10 + x++ * 30, () -> options.chunkModelDebug, b -> options.chunkModelDebug = b);
		checkBox("renderAtlases", 10, 10 + x++ * 30, () -> options.renderAtlases, b -> options.renderAtlases = b);
		checkBox("subChunkBuildTime", 10, 10 + x++ * 30, () -> options.subChunkBuildTime, b -> options.subChunkBuildTime = b);
		checkBox("showGCLog", 10, 10 + x++ * 30, () -> options.showGCLog, b -> options.showGCLog = b);
		checkBox("renderTeleporters", 10, 10 + x++ * 30, () -> options.renderTeleporters, b -> options.renderTeleporters = b);
		checkBox("renderRifts", 10, 10 + x++ * 30, () -> options.renderRifts, b -> options.renderRifts = b);
		checkBox("renderChunkOutline", 10, 10 + x++ * 30, () -> options.renderChunkOutline, b -> options.renderChunkOutline = b);
		checkBox("renderDataBlocks", 10, 10 + x++ * 30, () -> options.renderDataBlocks, b -> options.renderDataBlocks = b);
		checkBox("renderLights", 10, 10 + x++ * 30, () -> options.renderLights, b -> options.renderLights = b);
		checkBox("renderSkybox", 10, 10 + x++ * 30, () -> options.renderSkybox, b -> options.renderSkybox = b);
		checkBox("renderItemEntityOutline", 10, 10 + x * 30, () -> options.renderItemEntityOutline, b -> options.renderItemEntityOutline = b);

		x = 0;
		checkBox("enablePostProcessing", 300, 10 + x++ * 30, () -> options.enablePostProcessing, b -> options.enablePostProcessing = b);
		checkBox("renderCrosshair", 300, 10 + x++ * 30, () -> options.renderCrosshair, b -> options.renderCrosshair = b);

		x = 0;
		/* Minimap */
		addComponent(new ComponentRender(() ->
		{
			SpriteRender.fillRect(500, 15, 200, 120, 0.35f, 0.35f, 0.35f, 1f);
			SpriteRender.renderFrame("Minimap", 500, 15, 200, 120, Fex.H31, Fex.H31, Fex.H31);
		}));
		Minimap minimap = CaveGame.getInstance().inGameGui.minimap;
		checkBox("Render", 512, 27 + x++ * 30, minimap::isRender, minimap::setRender);
		checkBox("Rotate", 512, 27 + x++ * 30, minimap::isRotate, minimap::setRotate);
		checkBox("Static Height", 512, 27 + x * 30, minimap::isStaticPosition, minimap::setStaticPosition);

		/* Gameplay */

		x = 0;
//		number("maxChunkRebuild", 720, 10 + x++ * 30, -1, 16, () -> options.maxChunkRebuild, b -> options.maxChunkRebuild = b);
		number("generateDistance", 720, 10 + x++ * 30, -1, 8, () -> options.generateDistance, b -> options.generateDistance = b);
//		number("maxScheduledTicks", 720, 10 + x++ * 30, 0, 4096 * 64, () -> options.maxScheduledTicks, b -> options.maxScheduledTicks = b);
		number("randomTicks", 720, 10 + x++ * 30, 0, 4096, () -> options.randomTicks, b -> options.randomTicks = b);

		/* Time Slider */
		Slider time = new Slider();
		time.setLocation(500, 140);
		time.setSize(200, 10);
		time.setButtonSize(10, 20);
		time.addChangeEvent(c -> CaveGame.getInstance().getWorld().shade = (float) c.getValue() / 100f);
		addComponent(time);

		/* Model Layer */
		ItemList modelLayer = new ItemList(ModelLayer.values().length + 1);
		modelLayer.setLocation(10, 10);
		modelLayer.setSize(200, 20 * (ModelLayer.values().length + 1));
		modelLayer.setMultiselect(false);
		addComponent(modelLayer);
		modelLayer.addItem("ALL");
		for (ModelLayer ml : ModelLayer.values())
		{
			modelLayer.addItem(ml.name());
		}
		modelLayer.select(0);
		modelLayer.addChangeEvent(c -> {
			if (c.getSelectedItemsIndices().size() == 0)
				modelLayer.select(0);

			World.enabled = c.getSelectedItemsIndices().get(0) - 1;
		});
	}

	@Override
	public void showEvent()
	{
		mainApp.runEvent(new ShowEvent());
	}

	private OptNamedCheckBox checkBox(String text, int x, int y, Supplier<Boolean> get, Consumer<Boolean> toggle)
	{
		OptNamedCheckBox box = new OptNamedCheckBox();
		box.sup = get;
		box.setText(text);
		box.setLocation(x, y);
		box.setSize(Font.getTextWidth(text, 1) + 30, 25);
		box.setBoxSize(14, 14);
		box.setSelectedChar(CustomChar.CROSS);
		box.setBoxPadding(5, 5);
		box.addChangeEvent(c -> toggle.accept(c.isToggled()));
		addComponent(box);
		return box;
	}

	private OptNumberSelector number(String text, int x, int y, int min, int max, Supplier<Integer> get, Consumer<Integer> set)
	{
		OptNumberSelector sel = new OptNumberSelector();
		sel.sup = get;
		sel.text = text;
		sel.setLocation(x, y);
		sel.setMin(min);
		sel.setMax(max);
		sel.setSize(Font.getTextWidth(text, 1) + 30, 14);
		sel.setButtonWidth(14);
		sel.addChangeEvent(c -> set.accept(c.getValue()));
		addComponent(sel);
		return sel;
	}

	@Override
	public void guiTick()
	{
//		fastChunkBuildFix.setEnabled(SubChunkModel.toBuildCount == 0 && MainRender.CHUNK_REBUILT == 0);
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
