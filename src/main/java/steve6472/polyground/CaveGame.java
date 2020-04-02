package steve6472.polyground;

import org.joml.AABBf;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import steve6472.polyground.block.model.BlockModelLoader;
import steve6472.polyground.block.registry.BlockRegistry;
import steve6472.polyground.commands.CommandRegistry;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.entity.Player;
import steve6472.polyground.events.CancellableEvent;
import steve6472.polyground.events.WorldEvent;
import steve6472.polyground.gfx.CGGBuffer;
import steve6472.polyground.gfx.particle.ParticleStorage;
import steve6472.polyground.gfx.shaders.ShaderStorage;
import steve6472.polyground.gui.IGamePause;
import steve6472.polyground.gui.InGameGui;
import steve6472.polyground.gui.MainMenu;
import steve6472.polyground.gui.OptionsGui;
import steve6472.polyground.item.Item;
import steve6472.polyground.item.ItemAtlas;
import steve6472.polyground.item.registry.ItemRegistry;
import steve6472.polyground.rift.Rift;
import steve6472.polyground.rift.RiftManager;
import steve6472.polyground.rift.RiftModel;
import steve6472.polyground.teleporter.Teleporter;
import steve6472.polyground.teleporter.TeleporterManager;
import steve6472.polyground.tessellators.BasicTessellator;
import steve6472.polyground.tessellators.EntityTessellator;
import steve6472.polyground.world.BuildHelper;
import steve6472.polyground.world.CGSkybox;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.polyground.world.generator.GeneratorRegistry;
import steve6472.polyground.world.interaction.HitPicker;
import steve6472.polyground.world.light.LightManager;
import steve6472.sge.gfx.*;
import steve6472.sge.gfx.post.PostProcessing;
import steve6472.sge.gfx.shaders.GenericDeferredShader;
import steve6472.sge.gfx.shaders.Shader;
import steve6472.sge.gui.Gui;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.MainApp;
import steve6472.sge.main.MainFlags;
import steve6472.sge.main.Window;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.KeyEvent;
import steve6472.sge.main.events.WindowSizeEvent;
import steve6472.sge.main.game.Camera;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static steve6472.sge.gfx.VertexObjectCreator.*;

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
	public CGSkybox skybox;

	/* Deferred */
	public CGGBuffer gBuffer;
	private int finalRenderQuad;

	/* Sun Shadow */
	private steve6472.polyground.gfx.DepthFrameBuffer shadowBuffer;


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

		/* Create quad for Final Render */
		finalRenderQuad = createVAO();
		storeFloatDataInAttributeList(0, 2, new float[] {-1, +1, -1, -1, +1, -1, +1, -1, +1, +1, -1, +1});
		storeFloatDataInAttributeList(1, 2, new float[] {0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1});
		unbindVAO();

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

		itemInHand = ItemRegistry.getItemByName("slime_torch");

		skybox = new CGSkybox(StaticCubeMap.fromTextureFaces("skybox", new String[]{"side", "side", "top", "bottom", "side", "side"}, "png"), shaders.getProjectionMatrix());

		gBuffer = new CGGBuffer(getWindowWidth(), getWindowHeight());
		getEventHandler().register(gBuffer);

		getEventHandler().runEvent(new WindowSizeEvent(getWindowWidth(), getWindowHeight()));

		shadowBuffer = new steve6472.polyground.gfx.DepthFrameBuffer(getWidth(), getHeight());

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
		{
			if (getCamera().canMoveHead())
				player.tick();
		}


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
		Gui[] guis = new Gui[]{mainMenu, inGameGui, optionsGui};
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

	public void renderTheWorld(boolean deferred)
	{
		frustum.updateFrustum(shaders.getProjectionMatrix(), getCamera().getViewMatrix());

		if (world == null)
			return;

		hitPicker.tick(player, this);

		/* Render AABBs from t */
		shaders.mainShader.bind(getCamera().getViewMatrix());
		basicTess.begin(t.size() * 24);

		for (AABBf a : t)
		{
			AABBUtil.addAABB(a, basicTess);
		}

		GL11.glLineWidth(1);
		basicTess.loadPos(0);
		basicTess.loadColor(1);
		basicTess.draw(Tessellator.LINES);
		basicTess.disable(0, 1);

		/* END */

		if (options.renderTeleporters)
		{
			CaveGame.shaders.mainShader.bind(getCamera().getViewMatrix());
			for (Teleporter teleporter : teleporters.getTeleporters())
			{
				AABBUtil.renderAABBf(teleporter.getAabb(), basicTess, 3f, shaders.mainShader);
				renderTeleporterPair(teleporter);
			}
		}

		CaveGame.shaders.mainShader.bind(getCamera().getViewMatrix());

		if (!deferred)
		{
			if (!CaveGame.runGameEvent(new WorldEvent.PreRender(world)))
				world.render(false);
			CaveGame.runGameEvent(new WorldEvent.PostRender(world));
		}

//		CaveGame.shaders.mainShader.bind(getCamera().getViewMatrix());
//		AABBUtil.renderAABBf(player.getHitbox().getHitbox(), basicTess, 1, shaders.mainShader);

		skybox.setShade(world.shade);
		skybox.render(getCamera().getViewMatrix());

		particles.render();
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

	/**
	 * Copies Depth Buffer from gBuffer to gBufferOutput
	 */
	private void copyDepthToForwardBuffer()
	{
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, gBuffer.frameBuffer);
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, mainFrameBuffer.frameBuffer);
		GL30.glBlitFramebuffer(0, 0, gBuffer.getWidth(), gBuffer.getHeight(), 0, 0, getWidth(), getHeight(), GL_DEPTH_BUFFER_BIT, GL_NEAREST);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}

	@Override
	public void render()
	{
		frustum.updateFrustum(shaders.getProjectionMatrix(), getCamera().getViewMatrix());

		if (world != null)
		{
			world.shouldRebuild = true;
			world.tryRebuild();

			if (!CaveGame.runGameEvent(new WorldEvent.PreRender(world)))
				world.render(true);
			CaveGame.runGameEvent(new WorldEvent.PostRender(world));

			mainFrameBuffer.bindFrameBuffer(this);
			DepthFrameBuffer.clearCurrentBuffer();

			shaders.deferredShader.bind();
			shaders.deferredShader.setUniform(GenericDeferredShader.cameraPos, getCamera().getX(), getCamera().getY(), getCamera().getZ());
			Sprite.bind(0, gBuffer.texture);
			Sprite.bind(1, gBuffer.position);
			Sprite.bind(2, gBuffer.normal);
			Sprite.bind(3, gBuffer.emission);
			Sprite.bind(4, gBuffer.emissionPos);
			LightManager.updateLights(shaders.deferredShader);
			VertexObjectCreator.basicRender(finalRenderQuad, 2, 6, Tessellator.TRIANGLES);

			mainFrameBuffer.unbindCurrentFrameBuffer(this);

			copyDepthToForwardBuffer();

		}

		getRifts().render();

		mainFrameBuffer.bindFrameBuffer(this);
		renderTheWorld(false);

		if (options.renderLights)
			LightManager.renderLights();

		mainFrameBuffer.unbindCurrentFrameBuffer(this);

		if (options.renderRifts)
		{
			mainFrameBuffer.bindFrameBuffer(this);
			glDisable(GL_DEPTH_TEST);
			rifts.renderRifts();
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
		skybox.updateProjection(PolyUtil.createProjectionMatrix(e.getWidth(), e.getHeight()));
	}

	@Override
	protected int[] getFlags()
	{
		return new int[]{MainFlags.ADD_BASIC_ORTHO, MainFlags.ENABLE_GL_DEPTH_TEST, MainFlags.ENABLE_EXIT_KEY};
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
