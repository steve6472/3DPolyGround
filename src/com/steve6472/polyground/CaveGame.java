package com.steve6472.polyground;

import com.steve6472.polyground.block.model.BlockModelLoader;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.commands.CommandRegistry;
import com.steve6472.polyground.entity.Player;
import com.steve6472.polyground.events.CancellableEvent;
import com.steve6472.polyground.gui.IGamePause;
import com.steve6472.polyground.gui.InGameGui;
import com.steve6472.polyground.gui.MainMenu;
import com.steve6472.polyground.item.Item;
import com.steve6472.polyground.item.ItemAtlas;
import com.steve6472.polyground.item.registry.ItemRegistry;
import com.steve6472.polyground.particle.ParticleStorage;
import com.steve6472.polyground.shaders.ShaderStorage;
import com.steve6472.polyground.tessellators.BasicTessellator;
import com.steve6472.polyground.tessellators.EntityTessellator;
import com.steve6472.polyground.world.*;
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
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
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

		options = new Options();

		buildHelper = new BuildHelper();
		blockModelLoader = new BlockModelLoader();
		new BlockRegistry(this);

		world = new World(this);
		hitPicker = new HitPicker(world);
		frustum = new Frustum();

		inGameGui = new InGameGui(this);
		mainMenu = new MainMenu(this);
		mainMenu.setVisible(true);

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
		setExitKey(KeyList.ESCAPE);

		hitboxList = new ArrayList<>();
//		hitboxList.add(new ParticleHitbox(0.05f, 0.1f, 0.05f, 0.1f, new Vector4f(2.5f, 2.5f, 2.5f, 1)));

//		getWindow().maximize();
	}

	public Camera getCamera()
	{
		return getPlayer().getCamera();
	}

	public Player getPlayer()
	{
		return player;
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

		tickGui();

		particles.tick();

		t.clear();

		handleCamera();

		if (getCamera().canMoveHead())
			player.tick();

		PolyUtil.updateDirectionRay(player.viewDir, getCamera());

//		particles.testParticle(player.viewDir.x * 4.2f + player.getX(), player.viewDir.y * 4.2f + player.getY() + player.getEyeHeight(), player.viewDir.z * 4.2f + player.getZ());

		world.tick();

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
		Gui[] guis = new Gui[] {mainMenu, inGameGui};
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

	@Override
	public void render()
	{
		frustum.updateFrustum(shaders.getProjectionMatrix(), getCamera().getViewMatrix());

		mainFrameBuffer.bindFrameBuffer(this);
		DepthFrameBuffer.clearCurrentBuffer();

		renderFloor();

		hitPicker.tick(player, this);

		for (AABBf a : t)
		{
			AABBUtil.renderAABBf(a, basicTess, shaders.mainShader);
		}

		world.render();

		particles.render();

//		AABBUtil.renderAABBf(player.getHitbox().getHitbox(), this);

		mainFrameBuffer.unbindCurrentFrameBuffer(this);

		Shader.releaseShader();

		if (options.enablePostProcessing)
			pp.doPostProcessing(mainFrameBuffer.texture);

		glDisable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);
		glViewport(0, 0, getWidth(), getHeight());

		if (options.enablePostProcessing)
			SpriteRender.renderSpriteInverted(0, 0, getWidth(), getHeight(), 0, pp.combine.getOutTexture(), getWidth(), getHeight());
		else
			SpriteRender.renderSpriteInverted(0, 0, getWidth(), getHeight(), 0, mainFrameBuffer.texture, getWidth(), getHeight());

		renderGui();

		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glViewport(0, 0, getWidth(), getHeight());
	}

	private void renderFloor()
	{
		CaveGame.shaders.mainShader.bind();
		BasicTessellator tess = basicTess;
		tess.begin(4);

		tess.pos(+128, -0.0001f, +128).color(0.2f, 0.2f, 0.2f, 1.0f).endVertex();
		tess.pos(+128, -0.0001f, -128).color(0.2f, 0.2f, 0.2f, 1.0f).endVertex();
		tess.pos(-128, -0.0001f, -128).color(0.2f, 0.2f, 0.2f, 1.0f).endVertex();
		tess.pos(-128, -0.0001f, +128).color(0.2f, 0.2f, 0.2f, 1.0f).endVertex();

		tess.loadPos(0);
		tess.loadColor(1);
		tess.loadNormal(2);
		tess.draw(Tessellator.QUADS);
		tess.disable(0, 1, 2);
	}

	@Event
	public void togglePP(KeyEvent e)
	{
		if (!inGameGui.commandBox.isFocused())
		{
			if (e.getKey() == KeyList.P && e.getAction() == KeyList.PRESS)
				options.enablePostProcessing = !options.enablePostProcessing;

			if (e.getKey() == KeyList.O && e.getAction() == KeyList.PRESS)
				options.renderAtlases = !options.renderAtlases;
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
		if (world != null && world.worldName != null)
		{
			try
			{
				WorldSerializer.serialize(world);
			} catch (IOException e)
			{
				System.err.println("Failed to load the world!");
				e.printStackTrace();
			}
		}

		FrameBuffer.cleanUp();
		DepthFrameBuffer.cleanUp();
		VertexObjectCreator.cleanUp();
		getWindow().close();
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
