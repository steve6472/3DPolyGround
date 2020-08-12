package steve6472.polyground.gui;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import steve6472.polyground.CaveGame;
import steve6472.polyground.registry.CommandRegistry;
import steve6472.polyground.world.World;
import steve6472.polyground.world.biomes.Biomes;
import steve6472.polyground.world.chunk.Chunk;
import steve6472.polyground.world.generator.ChunkGenDataStorage;
import steve6472.polyground.world.generator.generators.IBiomeGenerator;
import steve6472.polyground.world.generator.generators.IHeightMapGenerator;
import steve6472.polyground.world.generator.generators.ISurfaceGenerator;
import steve6472.polyground.world.generator.generators.impl.cave.CaveGenerator;
import steve6472.polyground.world.generator.generators.impl.flat.FlatHeightMapGen;
import steve6472.polyground.world.generator.generators.impl.flat.SingleBiomeGen;
import steve6472.polyground.world.generator.generators.impl.world.HeightMapGenerator;
import steve6472.polyground.world.generator.generators.impl.world.SurfaceGenerator;
import steve6472.polyground.world.generator.generators.impl.world.VoronoiBiomeGen;
import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gfx.StaticTexture;
import steve6472.sge.gui.Gui;
import steve6472.sge.gui.components.Background;
import steve6472.sge.gui.components.Button;
import steve6472.sge.main.MainApp;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

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

	private StaticTexture main;

	@Override
	public void createGui()
	{
		Background.createComponent(this);

		main = StaticTexture.fromTexture("main_title.png");

		long seed = new Random().nextLong();
//		long seed = 4419941787569665203L;
		System.out.println("Seed: " + seed);
		IBiomeGenerator worldBiomeGenerator = new VoronoiBiomeGen(seed, 16, 8, 2, new ArrayList<>(Set.of(
			Biomes.TUNDRA, Biomes.DESERT, Biomes.FOREST, Biomes.SAVANNA, Biomes.DESERT_HILLS, Biomes.COLD_MOUNTAINS, Biomes.PLAINS, Biomes.SAVANNA_PLATEAU, Biomes.MOUNTAINS
		)));
//		IBiomeGenerator worldBiomeGenerator = new SingleBiomeGen(seed, Biomes.FOREST);
		IHeightMapGenerator worldHeightMapGenerator = new HeightMapGenerator(worldBiomeGenerator, 10, 5);
//		IHeightMapGenerator worldHeightMapGenerator = new FlatHeightMapGen(worldBiomeGenerator, 0);
		Function<ChunkGenDataStorage, ISurfaceGenerator> worldSurfaceGenerator = (cds) -> new SurfaceGenerator(worldHeightMapGenerator, cds);


		IBiomeGenerator flatBiomeGenerator = new SingleBiomeGen(seed, Biomes.FLAT);
		IHeightMapGenerator flatHeightMapGenerator = new FlatHeightMapGen(flatBiomeGenerator, 0);
		Function<ChunkGenDataStorage, ISurfaceGenerator> flatSurfaceGenerator = (cds) -> new SurfaceGenerator(flatHeightMapGenerator, cds);

		Button sandbox = new Button("Sandbox");
		sandbox.setLocation(30, 30);
		sandbox.setSize(100, 30);
		sandbox.addClickEvent(c ->
		{
			setVisible(false);
			CaveGame.getInstance().inGameGui.setVisible(true);
			CaveGame.getInstance().options.isGamePaused = false;
			CaveGame.getInstance().options.enablePostProcessing = false;
			CaveGame.getInstance().options.generateDistance = -1;

			CaveGame.getInstance().setWorld(new World(CaveGame.getInstance(), 4, flatBiomeGenerator, flatHeightMapGenerator, flatSurfaceGenerator));
			CaveGame.getInstance().world.addChunk(new Chunk(0, 0, CaveGame.getInstance().getWorld()));


		});
		addComponent(sandbox);

		Button house = new Button("House");
		house.setLocation(30, 70);
		house.setSize(100, 30);
		house.addClickEvent(c ->
		{

			setVisible(false);
			CaveGame.getInstance().inGameGui.setVisible(true);
			CaveGame.getInstance().options.isGamePaused = false;
			CaveGame.getInstance().options.generateDistance = -1;

			CaveGame.getInstance().setWorld(new World(CaveGame.getInstance(), 4, flatBiomeGenerator, flatHeightMapGenerator, flatSurfaceGenerator));
			CaveGame.getInstance().world.worldName = "house";

			try
			{
				CommandRegistry registry = CaveGame.getInstance().commandRegistry;
				registry.dispatcher.execute("loadworld house", registry.commandSource);
				registry.dispatcher.execute("tp -1 1.005 0.5", registry.commandSource);
			} catch (CommandSyntaxException e)
			{
				e.printStackTrace();
			}

		});
		addComponent(house);

		Button world = new Button("World");
		world.setLocation(30, 110);
		world.setSize(100, 30);
		world.addClickEvent(c ->
		{
			setVisible(false);
			CaveGame.getInstance().inGameGui.setVisible(true);
			CaveGame.getInstance().options.isGamePaused = false;

//			IBiomeGenerator biomeGen = new SingleBiomeGen(seed, Biomes.FLAT);
//			IHeightMapGenerator heightMapGen = new FlatHeightMapGen(biomeGen, 0);
//			Function<ChunkGenDataStorage, ISurfaceGenerator> caveGen = (cds) -> new TestGen(heightMapGen, cds);

			CaveGame.getInstance().setWorld(new World(CaveGame.getInstance(), 4, worldBiomeGenerator, worldHeightMapGenerator, worldSurfaceGenerator));
//			CaveGame.getInstance().setWorld(new World(CaveGame.getInstance(), 1, biomeGen, heightMapGen, caveGen));

			try
			{
				CommandRegistry registry = CaveGame.getInstance().commandRegistry;
				registry.dispatcher.execute("tp 8 32 8", registry.commandSource);
				registry.dispatcher.execute("speed 0.05", registry.commandSource);
			} catch (CommandSyntaxException e)
			{
				e.printStackTrace();
			}

		});
		addComponent(world);

		Button cave = new Button("Cave");
		cave.setLocation(30, 150);
		cave.setSize(100, 30);
		cave.addClickEvent(c ->
		{

			setVisible(false);
			CaveGame.getInstance().inGameGui.setVisible(true);
			CaveGame.getInstance().options.isGamePaused = false;

			IBiomeGenerator biomeGen = new SingleBiomeGen(seed, Biomes.CRYSTAL_CAVE);
			IHeightMapGenerator heightMapGen = new FlatHeightMapGen(biomeGen, 256);
			Function<ChunkGenDataStorage, ISurfaceGenerator> caveGen = (cds) -> new CaveGenerator(heightMapGen, cds);

			CaveGame.getInstance().setWorld(new World(CaveGame.getInstance(), 4, biomeGen, heightMapGen, caveGen));

			try
			{
				CommandRegistry registry = CaveGame.getInstance().commandRegistry;
				registry.dispatcher.execute("tp 8.5 16.005 8.5", registry.commandSource);
				registry.dispatcher.execute("speed 0.025", registry.commandSource);
			} catch (CommandSyntaxException e)
			{
				e.printStackTrace();
			}

		});
		addComponent(cave);


		Button pathfind = new Button("Pathfind");
		pathfind.setLocation(30, 190);
		pathfind.setSize(100, 30);
		pathfind.addClickEvent(c ->
		{

			setVisible(false);
			CaveGame.getInstance().inGameGui.setVisible(true);
			CaveGame.getInstance().options.isGamePaused = false;
			CaveGame.getInstance().options.generateDistance = -1;

			CaveGame.getInstance().setWorld(new World(CaveGame.getInstance(), 0, flatBiomeGenerator, flatHeightMapGenerator, flatSurfaceGenerator));
			try
			{
				CommandRegistry registry = CaveGame.getInstance().commandRegistry;
				registry.dispatcher.execute("loadworld pathfind", registry.commandSource);
				registry.dispatcher.execute("tp 8 32.005 8", registry.commandSource);
				registry.dispatcher.execute("ai", registry.commandSource);
				//				registry.dispatcher.execute("spawn ai", registry.commandSource);
				CaveGame.getInstance().getPlayer().isFlying = true;
			} catch (CommandSyntaxException e)
			{
				e.printStackTrace();
			}

		});
		addComponent(pathfind);
	}

	@Override
	public void guiTick()
	{
	}

	@Override
	public void render()
	{
		float f = (float) (getMainApp().getWidth() - 400) / ((float) getMainApp().getWidth());
		SpriteRender.renderSprite(getMainApp().getWidth() / 2 - (int) (main.getWidth() * f) / 2, 100, (int) (main.getWidth() * f), (int) (main.getHeight() * f), 0, main.getId());
	}

	@Override
	public boolean pauseGameIfOpen()
	{
		return true;
	}
}
