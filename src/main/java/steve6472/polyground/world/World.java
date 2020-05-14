package steve6472.polyground.world;

import org.joml.AABBf;
import org.joml.Matrix4f;
import org.joml.Random;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.BlockTextureHolder;
import steve6472.polyground.entity.EntityManager;
import steve6472.polyground.gfx.shaders.CGGShader;
import steve6472.polyground.gfx.shaders.world.WorldShader;
import steve6472.polyground.gui.InGameGui;
import steve6472.polyground.world.chunk.Chunk;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.sge.gfx.GBuffer;
import steve6472.sge.gfx.Tessellator3D;
import steve6472.sge.main.game.GridStorage;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

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
public class World implements IBlockProvider
{
	private final int HEIGHT = 8;

	private GridStorage<Chunk> chunks;
	private EntityManager entityManager;

	private CaveGame pg;

	private Random random;

	public boolean shouldRebuild = false;
	public String worldName = null;

	private Matrix4f mat;

	public World(CaveGame pg)
	{
		mat = new Matrix4f();
		this.pg = pg;
		chunks = new GridStorage<>();

		random = new Random(4);
		entityManager = new EntityManager(this);

		//
		//		if (worldName == null)
		//			addChunk(new Chunk(0, 0, this).generate(), false);
	}

	//	private byte delay = 0;

	public void tick()
	{
		InGameGui.waterActive = 0;
		for (Chunk chunk : chunks.getMap().values())
		{
			chunk.tick();
		}

		renderChunkOutlines();

		entityManager.tick();

		//		delay++;
		//		if (delay >= 30)
		//		{
		//			generateChunks();
		//			delay = 0;
		//		}
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
								addChunk(c, true);
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
		return pg.frustum.insideFrsutum(x, y, z, x + 16, y + 16, z + 16);
	}

	private boolean chunkFrustum(int x, int z)
	{
		return pg.frustum.insideFrsutum(x, 0, z, x + 16, 16 * 16, z + 16);
	}

	public float shade = 1f;

	public static int enabled = -1;

	public void tryRebuild()
	{
		if (shouldRebuild)
		{
			rebuild();
			shouldRebuild = false;
		}
	}

	public void render(boolean deferred, boolean countChunks)
	{
		if (countChunks)
		{
			InGameGui.chunks = 0;
			InGameGui.chunkLayers = 0;
		}

		if (deferred)
		{
			CaveGame.shaders.gShader.bind(CaveGame.getInstance().getCamera().getViewMatrix());

			CaveGame.getInstance().gBuffer.bindFrameBuffer();
			GBuffer.clearCurrentBuffer();
		} else
		{
			CaveGame.shaders.worldShader.bind();
			CaveGame.shaders.worldShader.setView(CaveGame.getInstance().getCamera().getViewMatrix());
			CaveGame.shaders.worldShader.setUniform(WorldShader.SHADE, shade);
		}

		BlockTextureHolder.getAtlas().getSprite().bind(0);

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
					CaveGame.shaders.gShader.setTransformation(mat.identity().translate(chunk.getX() * 16, k * 16, chunk.getZ() * 16));
				} else
				{
					CaveGame.shaders.worldShader.setTransformation(mat.identity().translate(chunk.getX() * 16, k * 16, chunk.getZ() * 16));
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
							CaveGame.shaders.worldShader.setUniform(WorldShader.SHADE, 1.0f);
						else
							CaveGame.shaders.worldShader.setUniform(WorldShader.SHADE, shade);
					} else
					{
						if (ModelLayer.EMISSION_NORMAL.ordinal() == i || ModelLayer.EMISSION_OVERLAY.ordinal() == i || ModelLayer.LIGHT.ordinal() == i)
						{
							CaveGame.shaders.gShader.setUniform(CGGShader.EMISSION_TOGGLE, 1.0f);
						} else
						{
							CaveGame.shaders.gShader.setUniform(CGGShader.EMISSION_TOGGLE, 0.0f);
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
			CaveGame.getInstance().gBuffer.unbindCurrentFrameBuffer();
		}
	}

	private void rebuild()
	{
		for (Chunk chunk : chunks.getMap().values())
		{
			if (chunk == null)
				continue;

			for (int k = 0; k < chunk.getSubChunks().length; k++)
			{
				SubChunk sc = chunk.getSubChunk(k);
				if (sc == null)
					continue;

				sc.rebuild();
			}
		}
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

				if (pg.options.renderChunkOutline)
					CaveGame.t.add(new AABBf(chunk.getX() * 16, k * 16, chunk.getZ() * 16, chunk.getX() * 16 + 16, k * 16 + 16, chunk.getZ() * 16 + 16));
			}
		}
	}

	public Random getRandom()
	{
		return random;
	}

	public CaveGame getPg()
	{
		return pg;
	}

	public void generateNewChunk(int x, int z)
	{
		if (getChunk(x, z) != null)
			return;

		addChunk(new Chunk(x, z, this).generate(), true);
	}

	@Override
	public GridStorage<Chunk> getChunkStorage()
	{
		return chunks;
	}

	public void clearWorld()
	{
		getChunks().forEach(Chunk::unload);
		chunks = new GridStorage<>();
	}

	public int getHeight()
	{
		return HEIGHT;
	}

	public EntityManager getEntityManager()
	{
		return entityManager;
	}
}
