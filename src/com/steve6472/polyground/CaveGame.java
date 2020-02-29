package com.steve6472.polyground;

import com.steve6472.polyground.block.model.BlockModelLoader;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.commands.CommandRegistry;
import com.steve6472.polyground.commands.CommandSource;
import com.steve6472.polyground.entity.Player;
import com.steve6472.polyground.events.CancellableEvent;
import com.steve6472.polyground.events.WorldEvent;
import com.steve6472.polyground.gui.IGamePause;
import com.steve6472.polyground.gui.InGameGui;
import com.steve6472.polyground.gui.MainMenu;
import com.steve6472.polyground.gui.OptionsGui;
import com.steve6472.polyground.item.Item;
import com.steve6472.polyground.item.ItemAtlas;
import com.steve6472.polyground.item.registry.ItemRegistry;
import com.steve6472.polyground.particle.ParticleStorage;
import com.steve6472.polyground.rift.Rift;
import com.steve6472.polyground.rift.RiftManager;
import com.steve6472.polyground.rift.RiftModel;
import com.steve6472.polyground.shaders.ShaderStorage;
import com.steve6472.polyground.teleporter.Teleporter;
import com.steve6472.polyground.teleporter.TeleporterManager;
import com.steve6472.polyground.tessellators.BasicTessellator;
import com.steve6472.polyground.tessellators.EntityTessellator;
import com.steve6472.polyground.world.BuildHelper;
import com.steve6472.polyground.world.World;
import com.steve6472.polyground.world.chunk.SubChunk;
import com.steve6472.polyground.world.generator.GeneratorRegistry;
import com.steve6472.polyground.world.interaction.HitPicker;
import com.steve6472.sge.gfx.*;
import com.steve6472.sge.gfx.post.PostProcessing;
import com.steve6472.sge.gui.Gui;
import com.steve6472.sge.main.KeyList;
import com.steve6472.sge.main.MainApp;
import com.steve6472.sge.main.MainFlags;
import com.steve6472.sge.main.Window;
import com.steve6472.sge.main.events.Event;
import com.steve6472.sge.main.events.KeyEvent;
import com.steve6472.sge.main.events.WindowSizeEvent;
import com.steve6472.sge.main.game.Camera;
import org.joml.AABBf;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 29.12.2018
 * Project: PolyGround
 *
 ***********************/
public class CaveGame extends MainApp
{
	private static CaveGame instance;

	/* Game objects */
	public static Item itemInHand;
	private Player player;
	public World world;
	public CommandRegistry commandRegistry;
	public GeneratorRegistry generatorRegistry;
	public BlockModelLoader blockModelLoader;
	public ItemAtlas itemAtlas;
	public ParticleStorage particles;
	public HitPicker hitPicker;
	private RiftManager rifts;
	private TeleporterManager teleporters;

	public static ShaderStorage shaders;
	private DepthFrameBuffer mainFrameBuffer;
	private PostProcessing pp;
	public BuildHelper buildHelper;
	public BasicTessellator basicTess;
	public EntityTessellator entityTessellator;
	public Frustum frustum;

	/* GUI */
	public InGameGui inGameGui;
	public MainMenu mainMenu;
	public OptionsGui optionsGui;

	/* Other */
	public List<ParticleHitbox> hitboxList;
	public Options options;

	@Override
	public void init()
	{
		instance = this;

		mainFrameBuffer = new DepthFrameBuffer(getWidth(), getHeight(), true);
		pp = new PostProcessing(getWidth(), getHeight());
		getEventHandler().register(pp);

		entityTessellator = new EntityTessellator();

		player = new Player(this);
		getEventHandler().register(player);

		buildHelper = new BuildHelper();
		blockModelLoader = new BlockModelLoader();
		new BlockRegistry(this);

		frustum = new Frustum();
		rifts = new RiftManager(this);
		getEventHandler().register(rifts);

		teleporters = new TeleporterManager();

		inGameGui = new InGameGui(this);
		mainMenu = new MainMenu(this);
		mainMenu.setVisible(true);

		options = new Options();
		optionsGui = new OptionsGui(this);

		commandRegistry = new CommandRegistry();
		generatorRegistry = new GeneratorRegistry();
		SubChunk.generator = generatorRegistry.getGenerator("flat");

		particles = new ParticleStorage(this);

		basicTess = new BasicTessellator();

		shaders = new ShaderStorage();
		getEventHandler().register(shaders);

		itemAtlas = new ItemAtlas(this);
		new ItemRegistry(this);

		itemInHand = ItemRegistry.getItemByName("stone");

		getEventHandler().runEvent(new WindowSizeEvent(getWindowWidth(), getWindowHeight()));

		hitboxList = new ArrayList<>();
//		hitboxList.add(new ParticleHitbox(0.05f, 0.1f, 0.05f, 0.1f, new Vector4f(2.5f, 2.5f, 2.5f, 1)));

//		getWindow().maximize();
	}

	public void placeRifts()
	{
		{
			List<Vector3f> vertices = new ArrayList<>();
			vertices.add(new Vector3f(1f, 1, 2));
			vertices.add(new Vector3f(1f, 4, 2));
			vertices.add(new Vector3f(1f, 1, -1));
			vertices.add(new Vector3f(1f, 4, -1));
			vertices.add(new Vector3f(1f, 1, 2));
			vertices.add(new Vector3f(1f, 4, 2));

			Rift portal = new Rift("Garage", new Vector3f(20.0f, 0, -9f), new Vector3f(), 0, 0, new RiftModel(vertices));
			portal.setFinished(true);
			getRifts().addRift(portal);
		}

		{
			List<Vector3f> vertices = new ArrayList<>();
			vertices.add(new Vector3f(20.00f, 1.00f, -10.00f));
			vertices.add(new Vector3f(20.00f, 4.00f, -10.00f));
			vertices.add(new Vector3f(20.00f, 1.00f, -7.00f));
			vertices.add(new Vector3f(20.00f, 4.00f, -7.00f));
			vertices.add(new Vector3f(20.00f, 1.00f, -10.00f));
			vertices.add(new Vector3f(20.00f, 4.00f, -10.00f));
			Rift portal = new Rift("Wild", new Vector3f(0.00f, 1.00f, 0.50f), new Vector3f(-20.00f, -1.00f, 8.50f), 0, 0, new RiftModel(vertices));
			portal.setFinished(true);
			getRifts().addRift(portal);
		}

		Teleporter fromGarage, toGarage;

		toGarage = new Teleporter();
		fromGarage = new Teleporter();

		toGarage.setOther(fromGarage);
		toGarage.setAabb(new AABBf(0.25f, 1f, -1f, 0.75f, 4f, 2f));

		fromGarage.setOther(toGarage);
		fromGarage.setAabb(new AABBf(20.25f, 1f, -10f, 20.75f, 4f, -7f));

		teleporters.getTeleporters().add(toGarage);
		teleporters.getTeleporters().add(fromGarage);
	}

	public Camera getCamera()
	{
		return getPlayer().getCamera();
	}

	public Player getPlayer()
	{
		return player;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}

	public RiftManager getRifts()
	{
		return rifts;
	}

	public World getWorld()
	{
		return world;
	}

	public void setWorld(World world)
	{
		this.world = world;
		this.commandRegistry.commandSource = new CommandSource(player, world, inGameGui.chat);
		hitPicker = new HitPicker(world);
	}

	public DepthFrameBuffer getMainFrameBuffer()
	{
		return mainFrameBuffer;
	}

	public static CaveGame getInstance()
	{
		return instance;
	}

	private int oldx, oldy;

	@Override
	public void tick()
	{
		options.isInMenu = false;
		options.isMouseFree = false;

		if (world != null)
			teleporters.tick(player);

		tickGui();

		if (world != null)
			particles.tick();

		t.clear();

		handleCamera();

		if (world != null)
			if (getCamera().canMoveHead())
				player.tick();

		PolyUtil.updateDirectionRay(player.viewDir, getCamera());

//		particles.testParticle(player.viewDir.x * 4.2f + player.getX(), player.viewDir.y * 4.2f + player.getY() + player.getEyeHeight(), player.viewDir.z * 4.2f + player.getZ());

		if (world != null)
			world.tick();

		if (world != null)
			hitboxList.forEach(c -> c.tick(this));

		tickGui();
		getCamera().updateViewMatrix();
	}

	private void handleCamera()
	{
		options.isInMenu = updateMenuFlag();
		boolean flag = !(options.isInMenu || options.isMouseFree);
		if (flag != getCamera().canMoveHead())
		{
			pickMouse(flag);

			if (getCamera().canMoveHead())
			{
				oldx = getMouseX() % getWidth();
				oldy = getMouseY() % getHeight();
				getMouseHandler().tick();
				GLFW.glfwSetCursorPos(getWindowId(), getWidth() / 2d, getHeight() / 2d);
			} else
			{
				GLFW.glfwSetCursorPos(getWindowId(), oldx, oldy);
				getCamera().head(oldx, oldy, options.mouseSensitivity);
			}
			getMouseHandler().tick();
		} else
		{
			pickMouse(getCamera().canMoveHead());
		}
		getCamera().setCanMoveHead(flag);
	}

	private void pickMouse(boolean flag)
	{
		if (flag)
		{
			GLFW.glfwSetInputMode(getWindowId(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		} else
		{
			GLFW.glfwSetInputMode(getWindowId(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		}
	}

	private boolean updateMenuFlag()
	{
		Gui[] guis = new Gui[] {mainMenu, inGameGui, optionsGui};
		for (Gui g : guis)
		{
			if (g instanceof IGamePause)
			{
				IGamePause g1 = (IGamePause) g;
				if (g.isVisible() && g1.pauseGameIfOpen())
				{
					return true;
				}
			}
		}

		return false;
	}

	public static List<AABBf> t = new ArrayList<>();

	public void renderTheWorld()
	{
		if (world != null)
			hitPicker.tick(player, this);

		/* Render AABBs from t */
		shaders.mainShader.bind();
		basicTess.begin(t.size() * 24);

		for (AABBf a : t)
		{
			AABBUtil.addAABB(a, basicTess);
		}

		GL11.glLineWidth(1);
		basicTess.loadPos(0);
		basicTess.loadColor(1);
		basicTess.loadNormal(2);
		basicTess.draw(Tessellator.LINES);
		basicTess.disable(0, 1, 2);

		/* END */

		if (options.renderTeleporters)
		{
			CaveGame.shaders.mainShader.bind();
			for (Teleporter teleporter : teleporters.getTeleporters())
			{
				AABBUtil.renderAABBf(teleporter.getAabb(), basicTess, 3f, shaders.mainShader);
				renderTeleporterPair(teleporter);
			}
		}

		if (world != null)
		{
			if (!CaveGame.runGameEvent(new WorldEvent.PreRender(world)))
				world.render();
			CaveGame.runGameEvent(new WorldEvent.PostRender(world));
		}

		particles.render();

//		AABBUtil.renderAABBf(player.getHitbox().getHitbox(), this);
		renderFloor();
	}

	private void renderRifts()
	{
		int v = 0;
		for (Rift rift : rifts.getRifts())
		{
			int t = ((rift.getModel().getVertices().size() * 2) - 3) * 2;
			v += Math.max(t, 1);
		}

		CaveGame.shaders.mainShader.bind();
		BasicTessellator tess = basicTess;
		tess.begin(v);
		tess.color(1, 1, 1, 1);

		for (Rift rift : rifts.getRifts())
		{
			List<Vector3f> vertices = rift.getModel().getVertices();

			for (int i = 0; i < vertices.size(); i++)
			{
				Vector3f vec = vertices.get(i);

				if (i == 0 || i == 1)
				{
					tess.pos(vec).endVertex();
				} else
				{
					tess.pos(vec).endVertex();
					tess.pos(vertices.get(i - 1)).endVertex();
					tess.pos(vec).endVertex();
					tess.pos(vertices.get(i - 2)).endVertex();
				}
			}
		}

		glLineWidth(3f);
		tess.loadPos(0);
		tess.loadColor(1);
		tess.draw(Tessellator.LINES);
		tess.disable(0, 1);
		glLineWidth(1f);
	}

	private void renderTeleporterPair(Teleporter tel)
	{
		BasicTessellator tess = basicTess;
		tess.begin(2);

		Vector3f c0 = AABBUtil.getCenter(tel.getAabb());
		Vector3f c1 = AABBUtil.getCenter(tel.getOther().getAabb());

		tess.pos(c0.x, c0.y, c0.z).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(c1.x, c1.y, c1.z).color(1f, 1f, 1f, 1f).endVertex();

		tess.loadPos(0);
		tess.loadColor(1);
		tess.draw(Tessellator.LINES);
		tess.disable(0, 1);
	}

	@Override
	public void render()
	{
		frustum.updateFrustum(shaders.getProjectionMatrix(), getCamera().getViewMatrix());

		mainFrameBuffer.bindFrameBuffer(this);
		DepthFrameBuffer.clearCurrentBuffer();
		if (world != null)
		{
			world.shouldRebuild = true;
			world.tryRebuild();
		}
		renderTheWorld();

		mainFrameBuffer.unbindCurrentFrameBuffer(this);

		getRifts().render();

		if (options.renderRifts)
		{
			mainFrameBuffer.bindFrameBuffer(this);
			glDisable(GL_DEPTH_TEST);
			renderRifts();
			glEnable(GL_DEPTH_TEST);
			mainFrameBuffer.unbindCurrentFrameBuffer(this);
		}

		inGameGui.minimap.renderWorld();

		Shader.releaseShader();

		if (options.enablePostProcessing)
			pp.doPostProcessing(mainFrameBuffer.texture);

		glDisable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);
		glViewport(0, 0, getWidth(), getHeight());

		if (options.enablePostProcessing)
			SpriteRender.renderSpriteInverted(0, 0, getWidth(), getHeight(), 0, pp.combine.getOutTexture());
		else
			SpriteRender.renderSpriteInverted(0, 0, getWidth(), getHeight(), 0, mainFrameBuffer.texture);

		renderGui();

		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glViewport(0, 0, getWidth(), getHeight());
	}

	private void renderFloor()
	{
		glDisable(GL_CULL_FACE);
		CaveGame.shaders.mainShader.bind();
		BasicTessellator tess = basicTess;
		tess.begin(4);

		tess.pos(+128, -0.0001f, +128).color(0.5f, 0.5f, 0.5f, 0.9f).endVertex();
		tess.pos(+128, -0.0001f, -128).color(0.5f, 0.5f, 0.5f, 0.9f).endVertex();
		tess.pos(-128, -0.0001f, -128).color(0.5f, 0.5f, 0.5f, 0.9f).endVertex();
		tess.pos(-128, -0.0001f, +128).color(0.5f, 0.5f, 0.5f, 0.9f).endVertex();

		tess.loadPos(0);
		tess.loadColor(1);
		tess.loadNormal(2);
		tess.draw(Tessellator.QUADS);
		tess.disable(0, 1, 2);
		glEnable(GL_CULL_FACE);
	}

	@Event
	public void keyEvent(KeyEvent e)
	{
		if (!inGameGui.chat.isFocused())
		{
			if (e.getAction() == KeyList.PRESS)
			{
				if (e.getKey() == KeyList.P)
					options.enablePostProcessing = !options.enablePostProcessing;

				if (e.getKey() == KeyList.O)
					options.renderAtlases = !options.renderAtlases;

				if (e.getKey() == KeyList.ESCAPE)
				{
					optionsGui.setVisible(!optionsGui.isVisible());
					inGameGui.setVisible(!optionsGui.isVisible());
				}
			}
		}
	}

	public static boolean runGameEvent(CancellableEvent event)
	{
		getInstance().runEvent(event);
		return event.isCancelled();
	}

	@Event
	public void resize(WindowSizeEvent e)
	{
		mainFrameBuffer.resize(e.getWidth(), e.getHeight());
	}

	@Override
	protected int[] getFlags()
	{
		return new int[] {MainFlags.ADD_BASIC_ORTHO, MainFlags.ENABLE_GL_DEPTH_TEST, MainFlags.ENABLE_EXIT_KEY};
	}

	@Override
	public void setWindowHints()
	{
		Window.setResizable(true);
//		Window.setFloating(true);
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
	public void exit()
	{
//		if (world != null && world.worldName != null)
//		{
//			try
//			{
//				WorldSerializer.serialize(world);
//			} catch (IOException e)
//			{
//				System.err.println("Failed to load the world!");
//				e.printStackTrace();
//			}
//		}

		FrameBuffer.cleanUp();
		DepthFrameBuffer.cleanUp();
		VertexObjectCreator.cleanUp();
		getWindow().close();
		System.exit(0);
	}

	@Override
	public String getTitle()
	{
		return "Cave Game";
	}

	public static void main(String[] args)
	{
		System.setProperty("joml.format", "false");
		System.setProperty("joml.fastmath", "true");
		System.setProperty("joml.sinLookup", "true");

//		System.setProperty("debug_conditions", "true");

		new CaveGame();
	}
}
