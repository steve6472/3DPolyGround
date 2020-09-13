package steve6472.polyground.gui;

import steve6472.polyground.CaveGame;
import steve6472.polyground.block.BlockAtlas;
import steve6472.polyground.entity.player.EnumGameMode;
import steve6472.polyground.entity.player.EnumSlot;
import steve6472.polyground.events.InGameGuiEvent;
import steve6472.polyground.gfx.light.Light;
import steve6472.polyground.gfx.light.LightManager;
import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gfx.font.Font;
import steve6472.sge.gui.Gui;
import steve6472.sge.gui.components.GCLog;
import steve6472.sge.main.MainApp;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.WindowSizeEvent;

import static org.lwjgl.opengl.GL11.*;

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
	public Minimap minimap;

	private GCLog gcLog;

	@Override
	public void createGui()
	{
		chat = new GameChat();
		chat.setLocation(2, getMainApp().getHeight() - 14);
		chat.setSize((int) (getMainApp().getWidth() * 0.8), 14);
		addComponent(chat);

		minimap = new Minimap();
		minimap.setSize(100, 100);
		minimap.setLocation(getMainApp().getWidth() - 110, 20);
		addComponent(minimap);

		itemBar = new ItemBar();
		addComponent(itemBar);
		gcLog = new GCLog();
	}

	@Event
	public void updatePosition(WindowSizeEvent e)
	{
		chat.setLocation(2, e.getHeight() - 14);
		minimap.setLocation(e.getWidth() - 110, 20);
	}

	@Override
	public void guiTick()
	{
		gcLog.tick();

		if (CaveGame.runGameEvent(new InGameGuiEvent.PreTick(this)))
			return;

		CaveGame.getInstance().options.isMouseFree = chat.isFocused();
		itemBar.setVisible(CaveGame.getInstance().getPlayer().getGamemode() == EnumGameMode.CREATIVE);
		itemBar.setLocation(getMainApp().getWidth() - 38, getMainApp().getHeight() / 2 - (7 * 38) / 2);

		CaveGame.runGameEvent(new InGameGuiEvent.PostTick(this));
	}

	@Override
	public void render()
	{
		if (CaveGame.runGameEvent(new InGameGuiEvent.PreRender(this)))
			return;

		if (CaveGame.getInstance().options.showGCLog)
			gcLog.render(10, 120);

		renderTheRest();

		if (CaveGame.getInstance().options.renderCrosshair)
			renderCrosshair();

		runEvent(new InGameGuiEvent.PostRender(this));
	}

	private void renderCrosshair()
	{
		glBlendFunc(GL_ONE_MINUS_DST_COLOR, GL_ZERO);

		if (CaveGame.runGameEvent(new InGameGuiEvent.PreRenderCrosshair(this)))
			return;

		SpriteRender.fillRect(getMainApp().getWidth() / 2f - 7, getMainApp().getHeight() / 2f - 8, 14, 2, 1, 1, 1, 1);
		SpriteRender.fillRect(getMainApp().getWidth() / 2f + 5, getMainApp().getHeight() / 2f - 6, 2, 10, 1, 1, 1, 1);
		SpriteRender.fillRect(getMainApp().getWidth() / 2f - 7, getMainApp().getHeight() / 2f + 4, 14, 2, 1, 1, 1, 1);
		SpriteRender.fillRect(getMainApp().getWidth() / 2f - 7, getMainApp().getHeight() / 2f - 6, 2, 10, 1, 1, 1, 1);

		CaveGame.runGameEvent(new InGameGuiEvent.PostRenderCrosshair(this));

		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	public static int chunks, chunkLayers, waterActive;

	public void renderTheRest()
	{
		CaveGame main = CaveGame.getInstance();

		Font.renderFps(5, 5, main.getFps());
		Font.renderCustom(5, 15, 1, String.format("XYZ: %.4f %.4f %.4f", main.getPlayer().getX(), main.getPlayer().getY(), main.getPlayer().getZ()));
		Font.render("XYZ: " + (int) Math.floor(main.getPlayer().getX()) + " " + (int) Math.floor(main.getPlayer().getY()) + " " + (int) Math.floor(main.getPlayer().getZ()), 5, 25);

		final int as = BlockAtlas.getAtlas().getTileCount();

		StringBuilder sb = new StringBuilder();

		if (main.hitPicker.getHitResult().isHit())
		{
			sb.append("Distance: ").append(main.hitPicker.getHitResult().getDistance()).append("\n");
			sb.append("Side: ").append(main.hitPicker.getHitResult().getFace()).append("/").append(main.hitPicker.getHitResult().getFace().getAxis()).append("\n");
		}

		sb.append("Particles: ").append(main.mainRender.particles.count() + main.mainRender.breakParticles.count()).append("\n");
		sb.append("Chunks: ").append(chunks).append("/").append(chunkLayers).append("\n");
		sb.append("Water Active Chunks: ").append(waterActive).append("\n");

		int activeLightCount = 0;
		for (Light light : LightManager.lights)
		{
			if (!light.isInactive())
				activeLightCount++;
		}

		sb.append("Lights: ").append(activeLightCount).append("\n");
		sb.append("Scheduled Ticks: ").append(CaveGame.getInstance().world.scheduledTicks()).append("/").append(CaveGame.getInstance().world.scheduledTicks_()).append("\n");
		sb.append("OnGround: ").append(CaveGame.getInstance().getPlayer().isOnGround).append("\n");

		sb.append("Left Hand: ").append(CaveGame.getInstance().getPlayer().holdedItems.get(EnumSlot.HAND_LEFT).getName()).append("\n");
		sb.append("Right Hand: ").append(CaveGame.getInstance().getPlayer().holdedItems.get(EnumSlot.HAND_RIGHT).getName());

		Font.render(5, 45, sb.toString());

		if (CaveGame.getInstance().options.renderAtlases)
			SpriteRender.renderSprite(0, 60 + getLineCount(sb) * 8, as, as, 0, BlockAtlas.getAtlas().getSprite().getId());

		int ts = CaveGame.getInstance().itemAtlas.totalSize;

		if (CaveGame.getInstance().options.renderAtlases)
			SpriteRender.renderSprite(getMainApp().getWidth() - ts, 15, ts, ts, 0, CaveGame.getInstance().itemAtlas.itemAtlas.texture);
	}

	private int getLineCount(StringBuilder sb)
	{
		int c = 0;

		for (int i = 0; i < sb.length(); i++)
		{
			if (sb.charAt(i) == '\n')
				c++;
		}

		return c;
	}

	@Override
	public boolean pauseGameIfOpen()
	{
		return false;
	}
}
