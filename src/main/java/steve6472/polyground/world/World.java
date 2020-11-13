package steve6472.polyground.world;

import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.audio.Source;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.EntityManager;
import steve6472.polyground.gfx.ThreadedModelBuilder;
import steve6472.polyground.gfx.light.LightManager;
import steve6472.polyground.gui.InGameGui;
import steve6472.polyground.rift.RiftManager;
import steve6472.polyground.teleporter.TeleporterManager;
import steve6472.polyground.world.biomes.Biome;
import steve6472.polyground.world.biomes.Biomes;
import steve6472.polyground.world.biomes.Features;
import steve6472.polyground.world.chunk.Chunk;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.polyground.world.chunk.TickScheduler;
import steve6472.polyground.world.generator.ChunkGenDataStorage;
import steve6472.polyground.world.generator.EnumChunkStage;
import steve6472.polyground.world.generator.ThreadedGenerator;
import steve6472.polyground.world.generator.generators.IBiomeGenerator;
import steve6472.polyground.world.generator.generators.IHeightMapGenerator;
import steve6472.polyground.world.generator.generators.ISetBiomeGenerator;
import steve6472.polyground.world.generator.generators.ISurfaceGenerator;
import steve6472.sge.main.game.GridStorage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class World implements IWorldBlockProvider
{
	private final int height;
	private final long seed;

	public int lastWaterTickIndex = 0;
	public int currentWaterTickIndex = 0;
	public boolean reachedMax = false;

	public final TeleporterManager teleporters;
	public final Features features;
	public final Biomes biomes;

	private final GridStorage<Chunk> chunks;
	private final TickScheduler tickScheduler;
	private final EntityManager entityManager;
	private final RiftManager rifts;
	private final CaveGame game;
	private final Random random;
	private final List<Source> soundSources;
	private final WorldRenderer renderer;

	public String worldName = null;

	private final ThreadedGenerator generator;

	/**
	 * Create dummy world
	 */
	public World()
	{
		seed = 0;
		height = 0;
		game = null;
		chunks = new GridStorage<>();
		tickScheduler = null;
		features = null;
		biomes = null;
		random = new Random(0);
		entityManager = null;
		teleporters = null;
		rifts = null;
		generator = null;
		offThreadTicks = null;
		soundSources = null;
		renderer = null;
	}

	public World(CaveGame game, int height, IBiomeGenerator biomeGenerator, IHeightMapGenerator heightMapGenerator, Function<ChunkGenDataStorage, ISurfaceGenerator> surfaceGenerator)
	{
		seed = biomeGenerator.getSeed();
		this.height = height;
		this.game = game;
		chunks = new GridStorage<>();
		tickScheduler = new TickScheduler(this);
		soundSources = new ArrayList<>();

		features = new Features();
		features.load();

		biomes = new Biomes(game);
		if (biomeGenerator instanceof ISetBiomeGenerator setBiomeGenerator)
		{
			for (Biome b : setBiomeGenerator.biomes())
			{
				biomes.addBiome(b);
			}
		} else
		{
			biomes.load(features);
			biomeGenerator.setBiomes(biomes.getBiomes());
		}

		random = new Random(biomeGenerator.getSeed());
		entityManager = new EntityManager(this);

		teleporters = new TeleporterManager(this);
		rifts = new RiftManager(game, this);

		this.generator = new ThreadedGenerator(this, biomeGenerator, heightMapGenerator, surfaceGenerator);
		generator.start();

		offThreadTicks = new LinkedBlockingQueue<>();
		LightManager.init();

		renderer = new WorldRenderer(this);
	}

	public void addSound(int soundId, float x, float y, float z, float volume, float pitch)
	{
		Source source = new Source();
		source.setVolume(volume);
		source.setPitch(pitch);
		source.setPosition(x, y, z);
		source.play(soundId);
		soundSources.add(source);

		if (game.options.debugSoundPos)
			game.mainRender.particles.addBasicParticle(x, y, z, 0.1f, 1, 1, 1, 1, 390);
	}

	public void tick(ThreadedModelBuilder builder)
	{
		if (game.options.generateDistance > -1)
			generateInRadius(game.options.generateDistance);

		if (lastWaterTickIndex >= InGameGui.waterActive)
			lastWaterTickIndex = 0;
		reachedMax = false;
		currentWaterTickIndex = 0;
		InGameGui.waterActive = 0;

		for (Iterator<Source> iterator = soundSources.iterator(); iterator.hasNext(); )
		{
			Source source = iterator.next();
			if (!source.isPlaying())
			{
				source.delete();
				iterator.remove();
			}
		}

		while(!offThreadTicks.isEmpty())
		{
			try
			{
				tickScheduler.scheduleTick(offThreadTicks.take());
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		tickScheduler.tick();

		for (int x = -game.options.renderDistance; x <= game.options.renderDistance; x++)
		{
			for (int z = -game.options.renderDistance; z <= game.options.renderDistance; z++)
			{
				int cx = x + ((int) (Math.floor(game.getPlayer().getX())) >> 4);
				int cz = z + ((int) (Math.floor(game.getPlayer().getZ())) >> 4);

				Chunk chunk = getChunk(cx, cz);

				if (chunk == null)

					continue;
				chunk.tick();
				chunk.checkRebuild(builder);
			}
		}

		lastWaterTickIndex += game.options.maxWaterTick;

		renderer.renderChunkOutlines();

		entityManager.tick();
	}

	private final BlockingQueue<TickScheduler.ScheduledTick> offThreadTicks;

	public void scheduleUpdate(BlockState state, EnumFace from, int x, int y, int z, int tickIn)
	{
		if (Thread.currentThread().getName().equals("Generator"))
		{
			offThreadTicks.add(new TickScheduler.ScheduledTick(x, y, z, state, from, tickIn));
		} else
			tickScheduler.scheduleTick(state, from, x, y, z, tickIn);
	}

	public int scheduledTicks()
	{
		return tickScheduler.scheduledTicks();
	}

	public int scheduledTicks_()
	{
		return tickScheduler.scheduledTicks_();
	}

	private void generateInRadius(int generateDistance)
	{
		int x = (int) Math.floor(game.getPlayer().getPosition().x) >> 4;
		int z = (int) Math.floor(game.getPlayer().getPosition().z) >> 4;
		for (int i = -generateDistance; i <= generateDistance; i++)
		{
			for (int j = -generateDistance; j <= generateDistance; j++)
			{
				gen(i + x, j + z);
			}
		}
	}

	private void gen(int x, int z)
	{
		Chunk chunk = getChunk(x, z);
		if (chunk == null)
		{
			chunk = new Chunk(x, z, this);
			addChunk(chunk);
			for (SubChunk sc : chunk.getSubChunks())
				generator.addToQueue(sc);
		} else
		{
			for (SubChunk sc : chunk.getSubChunks())
				if (sc.stage != EnumChunkStage.FINISHED)
					generator.addToQueue(sc);
		}
	}

	public float shade = 1f;

	public static int enabled = -1;

	public Random getRandom()
	{
		return random;
	}

	public CaveGame getGame()
	{
		return game;
	}

	public void generateNewChunk(int x, int z)
	{
		if (getChunk(x, z) == null)
			addChunk(new Chunk(x, z, this));
	}

	@Override
	public GridStorage<Chunk> getChunkStorage()
	{
		return chunks;
	}

	@Override
	public void addChunk(Chunk chunk)
	{
		getChunkStorage().put(chunk.getX(), chunk.getZ(), chunk);
		for (SubChunk sc : chunk.getSubChunks())
			generator.addToQueue(sc);
	}

	public void clearWorld()
	{
		getChunks().forEach(Chunk::unload);
		chunks.getMap().clear();
	}

	public int getHeight()
	{
		return height;
	}

	public long getSeed()
	{
		return seed;
	}

	public EntityManager getEntityManager()
	{
		return entityManager;
	}

	public RiftManager getRifts()
	{
		return rifts;
	}

	public void setBiome(Biome biome, int x, int y, int z)
	{
		Chunk c = getChunk(x >> 4, z >> 4);
		if (c != null)
			if (y < c.getSubChunks().length * 16)
			{
				c.getSubChunk(y / 16).setBiome(biome, Math.floorMod(x, 16), Math.floorMod(y, 16), Math.floorMod(z, 16));
			}
	}

	public Biome getBiome(int x, int y, int z)
	{
		Chunk c = getChunk(x >> 4, z >> 4);
		if (c != null)
			if (y < c.getSubChunks().length * 16)
			{
				return c.getSubChunk(y / 16).getBiome(Math.floorMod(x, 16), Math.floorMod(y, 16), Math.floorMod(z, 16));
			}

		return null;
	}

	public WorldRenderer getRenderer()
	{
		return renderer;
	}

	/**
	 * Used by IBlockProvider
	 * @return this
	 */
	@Override
	public World getWorld()
	{
		return this;
	}

	public int getSoundCount()
	{
		return soundSources.size();
	}
}
