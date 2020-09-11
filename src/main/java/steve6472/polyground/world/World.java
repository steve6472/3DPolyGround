package steve6472.polyground.world;

import org.joml.AABBf;
import org.joml.Matrix4f;
import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.BlockAtlas;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.EntityManager;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.gfx.ThreadedModelBuilder;
import steve6472.polyground.gfx.light.LightManager;
import steve6472.polyground.gfx.shaders.CGGShader;
import steve6472.polyground.gfx.shaders.world.WorldShader;
import steve6472.polyground.gui.InGameGui;
import steve6472.polyground.world.biomes.Features;
import steve6472.polyground.rift.RiftManager;
import steve6472.polyground.teleporter.TeleporterManager;
import steve6472.polyground.world.biomes.Biome;
import steve6472.polyground.world.biomes.Biomes;
import steve6472.polyground.world.chunk.Chunk;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.polyground.world.chunk.TickScheduler;
import steve6472.polyground.world.generator.ChunkGenDataStorage;
import steve6472.polyground.world.generator.EnumChunkStage;
import steve6472.polyground.world.generator.ThreadedGenerator;
import steve6472.polyground.world.generator.generators.IBiomeGenerator;
import steve6472.polyground.world.generator.generators.IHeightMapGenerator;
import steve6472.polyground.world.generator.generators.ISetBiomeGenerator;
import steve6472.polyground.world.generator.generators.ISurfaceGenerator;
import steve6472.sge.gfx.GBuffer;
import steve6472.sge.gfx.Tessellator3D;
import steve6472.sge.main.game.GridStorage;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

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

	public String worldName = null;

	private final Matrix4f mat;

	private final ThreadedGenerator generator;

	/**
	 * Create dummy world
	 */
	public World()
	{
		seed = 0;
		height = 0;
		mat = new Matrix4f();
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
	}

	public World(CaveGame game, int height, IBiomeGenerator biomeGenerator, IHeightMapGenerator heightMapGenerator, Function<ChunkGenDataStorage, ISurfaceGenerator> surfaceGenerator)
	{
		seed = biomeGenerator.getSeed();
		this.height = height;
		mat = new Matrix4f();
		this.game = game;
		chunks = new GridStorage<>();
		tickScheduler = new TickScheduler(this);

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

		for (Chunk chunk : chunks.getMap().values())
		{
			chunk.tick();
			chunk.checkRebuild(builder);
		}

		lastWaterTickIndex += game.options.maxWaterTick;

		renderChunkOutlines();

		entityManager.tick();
	}

	private BlockingQueue<TickScheduler.ScheduledTick> offThreadTicks;

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

	private void generateChunks()
	{
		int px = (int) Math.floor(CaveGame.getInstance().getPlayer().getX()) >> 4;
		int pz = (int) Math.floor(CaveGame.getInstance().getPlayer().getZ()) >> 4;

		for (Iterator<Chunk> iter = chunks.getMap().values().iterator(); iter.hasNext(); )
		{
			Chunk next = iter.next();
			if (Math.abs(next.getX() - px) > 6 || Math.abs(next.getZ() - pz) > 6)
			{
				if (worldName != null)
				{
					try
					{
						next.saveChunk(this);
					} catch (IOException e)
					{
						System.err.println("Chunk " + next.getX() + "/" + next.getZ() + " failed to save");
						System.err.println("    " + e.getMessage() + "\n");
					}
				}
				next.unload();
				iter.remove();
			}
		}

		if (getChunk(px, pz) == null)
		{
			generateNewChunk(px, pz);
			return;
		}

		int range = 5;

		for (int i = -range; i <= range; i++)
		{
			for (int j = -range; j <= range; j++)
			{
				if (worldName != null)
				{
					if (getChunk(i + px, j + pz) == null)
					{
						if (new File("worlds\\" + worldName + "\\chunk_" + (i + px) + "_" + (j + pz)).exists())
						{
							Chunk c = new Chunk(i + px, j + pz, this);
							try
							{
								c.loadChunk(this);
								addChunk(c);
							} catch (IOException e)
							{
								System.err.println("Chunk " + c.getX() + "/" + c.getZ() + " failed to load");
								System.err.println("    " + e.getMessage() + "\n");
							}
							continue;
						}
					}
				}

				if (getChunk(i + px, j + pz) == null)
				{
					generateNewChunk(i + px, j + pz);
					return;
				}
			}
		}
	}

	private boolean subChunkFrustum(int x, int y, int z)
	{
		return game.mainRender.frustum.insideFrsutum(x, y, z, x + 16, y + 16, z + 16);
	}

	private boolean chunkFrustum(int x, int z)
	{
		return game.mainRender.frustum.insideFrsutum(x, 0, z, x + 16, 16 * 16, z + 16);
	}

	public float shade = 1f;

	public static int enabled = -1;

	public void render(boolean deferred, boolean countChunks)
	{
		if (countChunks)
		{
			InGameGui.chunks = 0;
			InGameGui.chunkLayers = 0;
		}

		if (deferred)
		{
			MainRender.shaders.gShader.bind(game.getCamera().getViewMatrix());

			game.mainRender.gBuffer.bindFrameBuffer();
			GBuffer.clearCurrentBuffer();
		} else
		{
			MainRender.shaders.worldShader.bind();
			MainRender.shaders.worldShader.setView(game.getCamera().getViewMatrix());
			MainRender.shaders.worldShader.setUniform(WorldShader.SHADE, shade);
		}

		BlockAtlas.getAtlas().getSprite().bind(0);

		for (Chunk chunk : chunks.getMap().values())
		{
			if (chunk == null)
				continue;
			if (!chunkFrustum(chunk.getX() * 16, chunk.getZ() * 16))
				continue;

			for (int k = 0; k < chunk.getSubChunks().length; k++)
			{
				SubChunk sc = chunk.getSubChunk(k);
				if (sc.isEmpty())
					continue;
				if (!subChunkFrustum(chunk.getX() * 16, k * 16, chunk.getZ() * 16))
					continue;

				if (deferred)
				{
					MainRender.shaders.gShader.setTransformation(mat.identity().translate(chunk.getX() * 16, k * 16, chunk.getZ() * 16));
				} else
				{
					MainRender.shaders.worldShader.setTransformation(mat.identity().translate(chunk.getX() * 16, k * 16, chunk.getZ() * 16));
				}

				if (countChunks)
					InGameGui.chunks++;

				/*
				 * Rendered in reverse order to get the desired effect
				 * (layer 0 -> back texture, layer 1 -> overlay texture)
				 */
				for (int i = SubChunk.getModelCount() - 1; i >= 0; i--)
				{
					if (sc.isEmpty(i))
						continue;

					if (enabled != -1 && i != enabled)
						continue;

					if (!deferred)
					{
						if (ModelLayer.EMISSION_NORMAL.ordinal() == i || ModelLayer.EMISSION_OVERLAY.ordinal() == i || ModelLayer.LIGHT.ordinal() == i)
							MainRender.shaders.worldShader.setUniform(WorldShader.SHADE, 1.0f);
						else
							MainRender.shaders.worldShader.setUniform(WorldShader.SHADE, shade);
					} else
					{
						if (ModelLayer.EMISSION_NORMAL.ordinal() == i || ModelLayer.EMISSION_OVERLAY.ordinal() == i || ModelLayer.LIGHT.ordinal() == i)
						{
							MainRender.shaders.gShader.setUniform(CGGShader.EMISSION_TOGGLE, 1.0f);
						} else
						{
							MainRender.shaders.gShader.setUniform(CGGShader.EMISSION_TOGGLE, 0.0f);
						}
					}

					if (countChunks)
						InGameGui.chunkLayers++;

					glBindVertexArray(sc.getModel(i).getVao());

					for (int l = 0; l < 4; l++)
						glEnableVertexAttribArray(l);

					glDrawArrays(Tessellator3D.TRIANGLES, 0, sc.getTriangleCount(i) * 3);
				}
			}
		}

		for (int l = 0; l < 4; l++)
			glDisableVertexAttribArray(l);

		glBindVertexArray(0);

		if (deferred)
		{
			game.mainRender.gBuffer.unbindCurrentFrameBuffer();
		}

		entityManager.render();
	}

	private void renderChunkOutlines()
	{
		for (Chunk chunk : chunks.getMap().values())
		{
			if (chunk == null)
				continue;
			if (!chunkFrustum(chunk.getX() * 16, chunk.getZ() * 16))
				continue;

			for (int k = 0; k < chunk.getSubChunks().length; k++)
			{
				if (!subChunkFrustum(chunk.getX() * 16, k * 16, chunk.getZ() * 16))
					continue;

				if (game.options.renderChunkOutline)
					MainRender.t.add(new AABBf(chunk.getX() * 16, k * 16, chunk.getZ() * 16, chunk.getX() * 16 + 16, k * 16 + 16, chunk.getZ() * 16 + 16));
			}
		}
	}

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

	/**
	 * Used by IBlockProvider
	 * @return this
	 */
	@Override
	public World getWorld()
	{
		return this;
	}
}
