package com.steve6472.polyground.gui;

import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.block.BlockTextureHolder;
import com.steve6472.polyground.events.InGameGuiEvent;
import com.steve6472.sge.gfx.SpriteRender;
import com.steve6472.sge.gfx.font.CustomChar;
import com.steve6472.sge.gfx.font.Font;
import com.steve6472.sge.gui.Gui;
import com.steve6472.sge.gui.components.GCLog;
import com.steve6472.sge.main.MainApp;
import com.steve6472.sge.main.events.Event;
import com.steve6472.sge.main.events.WindowSizeEvent;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class InGameGui extends Gui implements IGamePause
{
	public InGameGui(MainApp mainApp)
	{
		super(mainApp);
	}

	public ItemBar itemBar;
	public GameChat chat;

	private GCLog gcLog;

	@Override
	public void createGui()
	{
		chat = new GameChat();
		chat.setLocation(2, getMainApp().getHeight() - 14);
		chat.setSize((int) (getMainApp().getWidth() * 0.8), 14);
		addComponent(chat);

		itemBar = new ItemBar();
		addComponent(itemBar);
		gcLog = new GCLog();
	}

	@Event
	public void updatePosition(WindowSizeEvent e)
	{
		chat.setLocation(2, e.getHeight() - 14);
	}

	@Override
	public void guiTick()
	{
		gcLog.tick();

		if (CaveGame.runGameEvent(new InGameGuiEvent.PreTick(this))) return;

		CaveGame.getInstance().options.isMouseFree = chat.isFocused();
		itemBar.setLocation(getMainApp().getWidth() - 38, getMainApp().getHeight() / 2 - (7 * 38) / 2);

		CaveGame.runGameEvent(new InGameGuiEvent.PostTick(this));
	}

	@Override
	public void render()
	{
		if (CaveGame.runGameEvent(new InGameGuiEvent.PreRender(this))) return;

		if (CaveGame.getInstance().options.showGCLog)
			gcLog.render(10, 120);

		renderTheRest();

		renderCrosshair();

		runEvent(new InGameGuiEvent.PostRender(this));
	}

	private void renderCrosshair()
	{
		if (CaveGame.runGameEvent(new InGameGuiEvent.PreRenderCrosshair(this))) return;

		Font.renderCustom(getMainApp().getWidth() / 2 - 9, getMainApp().getHeight() / 2 - 9, 2, "[s0]", CustomChar.UNSELECTED_BOX);

		CaveGame.runGameEvent(new InGameGuiEvent.PostRenderCrosshair(this));
	}

	public void renderTheRest()
	{
		CaveGame main = CaveGame.getInstance();

		Font.renderFps(5, 5, main.getFps());
		Font.renderCustom(5, 15, 1, new Object[] { new Object[] {"XYZ: %.4f %.4f %.4f", new Object[] {main.getPlayer().getX(), main.getPlayer().getY(), main.getPlayer().getZ()}}});
//		Font.render("XYZ: " + main.getPlayer().getX() + " " + main.getPlayer().getY() + " " + main.getPlayer().getZ(), 5, 15);
		Font.render("XYZ: " + (int) Math.floor(main.getPlayer().getX()) + " " + (int) Math.floor(main.getPlayer().getY()) + " " + (int) Math.floor(main.getPlayer().getZ()), 5, 25);

		final int as = BlockTextureHolder.getAtlas().getTotalSize();

		if (main.hitPicker.hit)
		{
			Font.render(5, 45,
				"Distance: " + main.hitPicker.getHitResult().getDistance() + "\n" +
					"Side: " + main.hitPicker.getHitResult().getFace() + "\n" +
					"Particles: " + main.particles.count());
			if (CaveGame.getInstance().options.renderAtlases)
				SpriteRender.renderSprite(0, 80, as, as, 0, BlockTextureHolder.getAtlas().getSpriteId(), as, as);
		} else
		{
			Font.render("Particles: " + main.particles.count(), 5, 45);
			if (CaveGame.getInstance().options.renderAtlases)
				SpriteRender.renderSprite(0, 70, as, as, 0, BlockTextureHolder.getAtlas().getSpriteId(), as, as);
		}

		int ts = CaveGame.getInstance().itemAtlas.totalSize;

		if (CaveGame.getInstance().options.renderAtlases)
			SpriteRender.renderSprite(getMainApp().getWidth() - ts, 15, ts, ts, 0, CaveGame.getInstance().itemAtlas.itemAtlas.texture, ts, ts);
	}

	@Override
	public boolean pauseGameIfOpen()
	{
		return false;
	}
}
