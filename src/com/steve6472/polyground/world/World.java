package com.steve6472.polyground.world;

import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.block.BlockTextureHolder;
import com.steve6472.polyground.entity.EntityBase;
import com.steve6472.polyground.entity.EntityStorage;
import com.steve6472.polyground.entity.FloatingText;
import com.steve6472.polyground.shaders.world.DissoveWorldShader;
import com.steve6472.polyground.world.chunk.Chunk;
import com.steve6472.polyground.world.chunk.ModelLayer;
import com.steve6472.polyground.world.chunk.SubChunk;
import com.steve6472.sge.gfx.Tessellator3D;
import com.steve6472.sge.main.game.GridStorage;
import org.joml.AABBf;
import org.joml.Matrix4f;
import org.joml.Random;

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
	private GridStorage<Chunk> chunks;
	private CaveGame pg;

	private EntityStorage entityStorage;

	private Random random;

	public boolean shouldRebuild = false;
	public String worldName = null;
	private final int HEIGHT = 4;

	public World()
	{

	}

	public World(CaveGame pg)
	{
		this.pg = pg;
		chunks = new GridStorage<>();
		entityStorage = new EntityStorage();
		entityStorage.fillList();

		random = new Random(4);

//
//		if (worldName == null)
//			addChunk(new Chunk(0, 0, this).generate(), false);
	}

//	private byte delay = 0;

	public void tick()
	{
		chunks.getMap().values().forEach(Chunk::tick);

		entityStorage.tickEntities();

		renderChunkOutlines();

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

	public void render()
	{
		if (shouldRebuild)
		{
			rebuild();
			shouldRebuild = false;
		}

		CaveGame.shaders.dissoveWorldShader.bind();
		CaveGame.shaders.dissoveWorldShader.setView(CaveGame.getInstance().getCamera().getViewMatrix());
		CaveGame.shaders.dissoveWorldShader.setUniform(DissoveWorldShader.SHADE, shade);

		BlockTextureHolder.getAtlas().getSprite().bind(0);

		for (Chunk chunk : chunks.getMap().values())
		{
			if (chunk == null) continue;
			if (!chunkFrustum(chunk.getX() * 16, chunk.getZ() * 16)) continue;

			for (int k = 0; k < chunk.getSubChunks().length; k++)
			{
				SubChunk sc = chunk.getSubChunk(k);
				if (sc.isEmpty()) continue;
				if (!subChunkFrustum(chunk.getX() * 16, k * 16, chunk.getZ() * 16)) continue;

				CaveGame.shaders.dissoveWorldShader.setUniform(DissoveWorldShader.TIME, sc.getRenderTime());
				CaveGame.shaders.dissoveWorldShader.setTransformation(new Matrix4f().translate(chunk.getX() * 16, k * 16, chunk.getZ() * 16));

				/*
				 * Rendered in reverse order to get the desired effect
				 * (layer 0 -> back texture, layer 1 -> overlay texture)
				 */
				for (int i = SubChunk.getModelCount() - 1; i >= 0; i--)
				{

					if (sc.isEmpty(i)) continue;

					if (enabled != -1 && i != enabled)
						continue;

					if (ModelLayer.EMISSION_NORMAL.ordinal() == i || ModelLayer.EMISSION_OVERLAY.ordinal() == i)
						CaveGame.shaders.dissoveWorldShader.setUniform(DissoveWorldShader.SHADE, 1.0f);
					else
						CaveGame.shaders.dissoveWorldShader.setUniform(DissoveWorldShader.SHADE, shade);

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

		renderEntities();
	}

	private void rebuild()
	{
		for (Chunk chunk : chunks.getMap().values())
		{
			if (chunk == null) continue;

			for (int k = 0; k < chunk.getSubChunks().length; k++)
			{
				SubChunk sc = chunk.getSubChunk(k);
				if (sc == null) continue;

				sc.rebuild();
			}
		}
	}

	private void renderChunkOutlines()
	{
		for (Chunk chunk : chunks.getMap().values())
		{
			if (chunk == null) continue;
			if (!chunkFrustum(chunk.getX() * 16, chunk.getZ() * 16)) continue;

			for (int k = 0; k < chunk.getSubChunks().length; k++)
			{
				if (!subChunkFrustum(chunk.getX() * 16, k * 16, chunk.getZ() * 16)) continue;

				if (pg.options.renderChunkOutline)
					CaveGame.t.add(new AABBf(
						chunk.getX() * 16, k * 16, chunk.getZ() * 16,
						chunk.getX() * 16 + 16, k * 16 + 16, chunk.getZ() * 16 + 16));
			}
		}
	}

	public void addEntity(EntityBase e)
	{
		if (e instanceof FloatingText)
			pg.getEventHandler().register(e);
		entityStorage.addEntity(e);
	}

	public void renderEntities()
	{
		entityStorage.renderEntities();
	}

	public EntityStorage getEntityStorage()
	{
		return entityStorage;
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
		if (getChunk(x, z) != null) return;

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
}
