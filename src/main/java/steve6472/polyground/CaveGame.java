package steve6472.polyground;

import org.lwjgl.glfw.GLFW;
import steve6472.polyground.audio.SoundMaster;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.BlockAtlas;
import steve6472.polyground.block.model.BlockModel;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.entity.player.EnumGameMode;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.events.CancellableEvent;
import steve6472.polyground.events.MouseClickEvent;
import steve6472.polyground.events.SpecialBlockRegistryEvent;
import steve6472.polyground.generator.DataGenerator;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.gui.IGamePause;
import steve6472.polyground.gui.InGameGui;
import steve6472.polyground.gui.MainMenu;
import steve6472.polyground.gui.OptionsGui;
import steve6472.polyground.item.Item;
import steve6472.polyground.item.groups.Groups;
import steve6472.polyground.knapping.Recipes;
import steve6472.polyground.registry.*;
import steve6472.polyground.registry.model.AnimationRegistry;
import steve6472.polyground.registry.model.ModelRegistry;
import steve6472.polyground.registry.palette.PaletteRegistry;
import steve6472.polyground.rift.RiftManager;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.Chunk;
import steve6472.polyground.world.interaction.HitPicker;
import steve6472.sge.gfx.DepthFrameBuffer;
import steve6472.sge.gfx.FrameBuffer;
import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gfx.VertexObjectCreator;
import steve6472.sge.gui.Gui;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.MainApp;
import steve6472.sge.main.MainFlags;
import steve6472.sge.main.Window;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.KeyEvent;
import steve6472.sge.main.events.MouseEvent;
import steve6472.sge.main.events.WindowSizeEvent;
import steve6472.sge.main.game.Camera;
import steve6472.sge.main.util.MathUtil;

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
	public static long MAIN_THREAD;
	public static boolean DEBUG = false;

	/* Game objects */
	private Player player;
	public World world;
	public CommandRegistry commandRegistry;
	public HitPicker hitPicker;
	public Recipes knappingRecipes;
	public Groups itemGroups;

	public MainRender mainRender;

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
		MAIN_THREAD = Thread.currentThread().getId();

		player = new Player(this);
		getEventHandler().register(player);

		mainRender = new MainRender(this);

		getEventHandler().runEvent(new SpecialBlockRegistryEvent());

		itemGroups = new Groups();

		PaletteRegistry.init();
		ModelRegistry.init();
		AnimationRegistry.init();
		Blocks.register(this);
		WaterRegistry.init();
		Items.register(this);
		Blocks.finish(this);
		ModelRegistry.assignTextures();
		Item.runQueue();

		inGameGui = new InGameGui(this);
		mainMenu = new MainMenu(this);
		mainMenu.setVisible(true);

		options = new Options();
		optionsGui = new OptionsGui(this);

		commandRegistry = new CommandRegistry();

		knappingRecipes = new Recipes();

		inGameGui.creativeWheel.load();

		getEventHandler().runEvent(new WindowSizeEvent(getWindowWidth(), getWindowHeight()));

		hitboxList = new ArrayList<>();
		//		hitboxList.add(new ParticleHitbox(0.05f, 0.1f, 0.05f, 0.1f, new Vector4f(2.5f, 2.5f, 2.5f, 1)));

//				getWindow().maximize();
		Window.enableVSync(true);
		mouseUpdated = true;

		SoundMaster.setup();
		SoundRegistry.init();
	}

	public Camera getCamera()
	{
		return getPlayer().getCamera();
	}

	public Player getPlayer()
	{
		return player;
	}

	public RiftManager getRifts()
	{
		return world.getRifts();
	}

	public World getWorld()
	{
		return world;
	}

	public void setWorld(World world)
	{
		this.world = world;
		player.setWorld(world);
		this.commandRegistry.commandSource = new CommandSource(player, world, inGameGui.chat);
		hitPicker = new HitPicker(world);
	}

	public DepthFrameBuffer getMainFrameBuffer()
	{
		return mainRender.getMainFrameBuffer();
	}

	public static CaveGame getInstance()
	{
		return instance;
	}

	public static int lastWaterCount;
	public int currentWaterCount;
	private boolean oldCrosshair;
	private boolean oldCrosshairFlag;

	@Event
	public void mouseClickEvent(MouseEvent e)
	{
		if (hitPicker != null)
			runEvent(new MouseClickEvent(new MouseClick(e, player.getHitResult(), player), e));
	}

	@Override
	public void tick()
	{
		currentWaterCount = lastWaterCount * 36;
		lastWaterCount = 0;
		mainRender.waterTess.begin(Math.min(currentWaterCount, 16777216));

		options.isInMenu = false;
		options.isMouseFree = false;

		if (world != null)
			world.teleporters.tick(player);

		tickGui();

		mainRender.dialogManager.tick();
		getMouseHandler().tick();

		if (mainRender.dialogManager.isActive() && !oldCrosshairFlag)
		{
			oldCrosshair = options.renderCrosshair;
			oldCrosshairFlag = true;
			options.renderCrosshair = false;
		}

		if (!mainRender.dialogManager.isActive() && oldCrosshairFlag)
		{
			options.renderCrosshair = oldCrosshair;
			oldCrosshairFlag = false;
		}

		if (world != null)
		{
			mainRender.breakParticles.tick();
			mainRender.particles.tick();
		}

		MainRender.t.clear();

		handleCamera();

		if (world != null)
		{
			if (!options.isInMenu && !options.isGamePaused && !mainRender.dialogManager.isActive() && !inGameGui.chat.isFocused())
				player.tick();

			SoundMaster.setListenerPosition(player.getX(), player.getY() + player.eyeHeight, player.getZ());
			SoundMaster.setListenerOrientation(player.getCamera().getViewMatrix());
		}


		PolyUtil.updateDirectionRay(player.viewDir, getCamera());

		//		particles.testParticle(player.viewDir.x * 4.2f + player.getX(), player.viewDir.y * 4.2f + player.getY() + player.getEyeHeight(), player.viewDir.z * 4.2f + player.getZ());

		if (world != null)
			world.tick(mainRender.modelBuilder);

		if (world != null)
			hitboxList.forEach(c -> c.tick(this));

		tickGui();
		getCamera().updateViewMatrix();
	}

	private int oldx, oldy;
	private boolean mouseUpdated, dialogFlag;

	private void handleCamera()
	{
		options.isInMenu = updateMenuFlag();
		boolean flag = !(options.isInMenu || options.isMouseFree);

		if (mainRender.dialogManager.isActive() && !options.isInMenu)
		{
			handleCursorInDialog();
			return;
		}

		// Move mouse to center when exiting a dialog. Do NOT move the cursor when opening the game
		if ((flag && mouseUpdated) || dialogFlag)
		{
			dialogFlag = false;
			GLFW.glfwSetCursorPos(getWindowId(), getWidth() / 2f, getHeight() / 2f);
			getMouseHandler().tick();
		}

		pickMouse(flag);

		if (flag)
		{
			mouseUpdated = false;
			int mx = getMouseX();
			int my = getMouseY();

			int dx = mx - getWidth() / 2;
			int dy = my - getHeight() / 2;

			oldx += dx;
			oldy += dy;

			GLFW.glfwSetCursorPos(getWindowId(), getWidth() / 2f, getHeight() / 2f);
			getMouseHandler().tick();

			getCamera().head(oldx, oldy, options.mouseSensitivity);
			getCamera().setPosition(getCamera().getViewPosition());
		}

		if (!flag && !mouseUpdated)
		{
			mouseUpdated = true;
			GLFW.glfwSetCursorPos(getWindowId(), getWidth() / 2f, getHeight() / 2f);
		}
	}

	private void handleCursorInDialog()
	{
		// Center cursor when activating dialog
		if (!dialogFlag)
		{
			dialogFlag = true;
			pickMouse(true);
			GLFW.glfwSetCursorPos(getWindowId(),
				mainRender.dialogManager.getFirstActiveDialog().getWidth() / 2f,
				mainRender.dialogManager.getFirstActiveDialog().getHeight() / 2f);
			getMouseHandler().tick();
			return;
		}

		// Keep cursor in dialog bounds
		pickMouse(true);
		GLFW.glfwSetCursorPos(getWindowId(),
			MathUtil.clamp(0, mainRender.dialogManager.getFirstActiveDialog().getWidth(), getMouseX()),
			MathUtil.clamp(0, mainRender.dialogManager.getFirstActiveDialog().getHeight(), getMouseY()));
		getMouseHandler().tick();
	}

	private void pickMouse(boolean disableCursor)
	{
		GLFW.glfwSetInputMode(getWindowId(), GLFW.GLFW_CURSOR, disableCursor ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
	}

	private boolean updateMenuFlag()
	{
		Gui[] guis = new Gui[]{mainMenu, inGameGui, optionsGui};
		for (Gui g : guis)
		{
			if (g instanceof IGamePause g1)
			{
				if (g.isVisible() && g1.pauseGameIfOpen())
				{
					return true;
				}
			}
		}

		return inGameGui.isVisible() && inGameGui.creativeWheel.isOpen() && player.gamemode == EnumGameMode.CREATIVE;
	}

	@Override
	public void render()
	{
		mainRender.render();

		glDisable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);
		glViewport(0, 0, getWidth(), getHeight());

		if (options.enablePostProcessing)
			SpriteRender.renderSpriteInverted(0, 0, getWidth(), getHeight(), mainRender.postProcessing.combine.getOutTexture());
		else
			SpriteRender.renderSpriteInverted(0, 0, getWidth(), getHeight(), getMainFrameBuffer().texture);

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

				if (e.getKey() == KeyList.G)
					if (player.getGamemode() == EnumGameMode.CREATIVE)
						player.gamemode = EnumGameMode.SURVIVAL;
					else
						player.gamemode = EnumGameMode.CREATIVE;

				if (e.getKey() == KeyList.F5)
				{
					BlockAtlas.compileTextures(0);
					ModelRegistry.reload();
					ModelRegistry.assignTextures();
					AnimationRegistry.reload();
					PaletteRegistry.reload();

					if (e.getMods() == KeyList.M_CONTROL)
					{
						for (Block b : Blocks.getAllBlocks())
						{
							for (BlockState possibleState : b.getDefaultState().getPossibleStates())
							{
								for (BlockModel blockModel : possibleState.getBlockModels())
								{
									blockModel.reload();

									if (blockModel.getElements() != null)
									{
										for (IElement triangle : blockModel.getElements())
										{
											triangle.fixUv(mainRender.buildHelper.texel);
										}
									}
								}
							}
						}

						world.getChunks().forEach(Chunk::rebuild);
					}
				}

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
		SoundMaster.destroy();
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

		boolean generators = false;

		for (String arg : args)
		{
			if (generators = arg.equals("-generators"))
			{
				try
				{
					DataGenerator gen = new DataGenerator();
					long start = System.nanoTime();
					gen.generateBlocks();
					System.out.println("Generated blocks and items in " + (System.nanoTime() - start) / 1000000.0);
					start = System.nanoTime();
					gen.generateFeatures();
					System.out.println("Generated features in " + (System.nanoTime() - start) / 1000000.0);
					start = System.nanoTime();
					gen.generateBiomes();
					System.out.println("Generated biomes in " + (System.nanoTime() - start) / 1000000.0);
				} catch (Exception ex)
				{
					ex.printStackTrace();
					System.exit(1);
				}
			}
			if (arg.equals("-debug"))
				DEBUG = true;
		}

		if (DEBUG && generators)
			new DataGenerator().generateDebug();

		new CaveGame();
	}

	@Override
	protected boolean enableGLDebug()
	{
		return false;
	}
}
