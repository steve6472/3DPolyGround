package steve6472.polyground.world;

import org.joml.AABBf;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.BlockAtlas;
import steve6472.polyground.block.ISpecialRender;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.gfx.shaders.CGGShader;
import steve6472.polyground.gfx.shaders.world.WorldShader;
import steve6472.polyground.gui.InGameGui;
import steve6472.polyground.world.chunk.Chunk;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.sge.gfx.GBuffer;
import steve6472.sge.gfx.Tessellator3D;

import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.11.2020
 * Project: CaveGame
 *
 ***********************/
public class WorldRenderer
{
	public CaveGame game;
	public World world;

	private final Matrix4f mat;

	public WorldRenderer(World world)
	{
		this.game = world.getGame();
		this.world = world;
		mat = new Matrix4f();
	}

	private boolean subChunkFrustum(int x, int y, int z)
	{
		return game.mainRender.frustum.insideFrsutum(x, y, z, x + 16, y + 16, z + 16);
	}

	private boolean chunkFrustum(int x, int z)
	{
		return game.mainRender.frustum.insideFrsutum(x, 0, z, x + 16, 16 * world.getHeight(), z + 16);
	}

	public void renderDeferred(boolean countChunks, boolean specialBlockRender)
	{
		if (countChunks)
		{
			InGameGui.chunks = 0;
			InGameGui.chunkLayers = 0;
		}

		MainRender.shaders.gShader.bind(game.getCamera().getViewMatrix());
		MainRender.shaders.gShader.setUniform(CGGShader.SHADE, world.shade);

		game.mainRender.gBuffer.bindFrameBuffer();
		GBuffer.clearCurrentBuffer();

		BlockAtlas.getAtlas().getSprite().bind(0);

		for (int x = -game.options.renderDistance; x <= game.options.renderDistance; x++)
		{
			for (int z = -game.options.renderDistance; z <= game.options.renderDistance; z++)
			{
				int cx = x + ((int) (Math.floor(game.getPlayer().getX())) >> 4);
				int cz = z + ((int) (Math.floor(game.getPlayer().getZ())) >> 4);

				Chunk chunk = world.getChunk(cx, cz);

				if (chunk == null)
					continue;

				cx *= 16;
				cz *= 16;

				if (Vector2f.distance(cx + 8, cz + 8, game.getPlayer().getX(), game.getPlayer().getZ()) >= game.options.renderDistance * 16)
					continue;

				if (!chunkFrustum(cx, cz))
					continue;

				for (int k = 0; k < chunk.getSubChunks().length; k++)
				{
					SubChunk sc = chunk.getSubChunk(k);

					if (sc.isEmpty())
						continue;

					if (!subChunkFrustum(cx, k * 16, cz))
						continue;

					MainRender.shaders.gShader.setTransformation(mat.identity().translate(cx, k * 16, cz));

					if (specialBlockRender)
					{
						final int finalCx = cx;
						final int finalK = k;
						final int finalCz = cz;
						sc.getSpecialRender().iterate((bx, by, bz) ->
						{
							BlockState state = sc.getState(bx, by, bz);
							Block block = state.getBlock();

							if (block instanceof ISpecialRender sr)
							{
								game.mainRender.stack.pushMatrix();
								game.mainRender.stack.translate(bx + finalCx, by + finalK * 16, bz + finalCz);
								game.mainRender.stack.translate(0.5f, 0, 0.5f);

								sr.render(game.mainRender.stack, world, state, bx + finalCx, by + finalK * 16, bz + finalCz);

								game.mainRender.stack.popMatrix();
							}
						});
					}

					if (countChunks)
						InGameGui.chunks++;

					/*
					 * Rendered in reverse order to get the desired effect
					 * (layer 0 -> back texture, layer 1 -> overlay texture)
					 */
					for (int i = SubChunk.getModelCount() - 1; i >= 0; i--)
					{
						// Do not render the transparent layer
						if (ModelLayer.TRANSPARENT.ordinal() == i)
							continue;

						if (sc.isEmpty(i))
							continue;

						if (World.enabled != -1 && i != World.enabled)
							continue;

						if (ModelLayer.EMISSION_NORMAL.ordinal() == i || ModelLayer.EMISSION_OVERLAY.ordinal() == i || ModelLayer.LIGHT.ordinal() == i)
						{
							MainRender.shaders.gShader.setUniform(CGGShader.EMISSION_TOGGLE, 1.0f);
						} else
						{
							MainRender.shaders.gShader.setUniform(CGGShader.EMISSION_TOGGLE, 0.0f);
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
		}

		for (int l = 0; l < 4; l++)
			glDisableVertexAttribArray(l);

		glBindVertexArray(0);

		//		game.world.getEntityManager().render(game.mainRender.stack);

		game.mainRender.gBuffer.unbindCurrentFrameBuffer();


//		renderTransparentDeferred();
	}

	public void renderNormal(boolean countChunks, boolean specialBlockRender)
	{
		if (countChunks)
		{
			InGameGui.chunks = 0;
			InGameGui.chunkLayers = 0;
		}

		MainRender.shaders.worldShader.bind();
		MainRender.shaders.worldShader.setView(game.getCamera().getViewMatrix());
		MainRender.shaders.worldShader.setUniform(WorldShader.SHADE, world.shade);

		BlockAtlas.getAtlas().getSprite().bind(0);

		for (int x = -game.options.renderDistance; x <= game.options.renderDistance; x++)
		{
			for (int z = -game.options.renderDistance; z <= game.options.renderDistance; z++)
			{
				int cx = x + ((int) (Math.floor(game.getPlayer().getX())) >> 4);
				int cz = z + ((int) (Math.floor(game.getPlayer().getZ())) >> 4);

				Chunk chunk = world.getChunk(cx, cz);

				if (chunk == null)
					continue;

				cx *= 16;
				cz *= 16;

				if (Vector2f.distance(cx + 8, cz + 8, game.getPlayer().getX(), game.getPlayer().getZ()) >= game.options.renderDistance * 16)
					continue;

				if (!chunkFrustum(cx, cz))
					continue;

				for (int k = 0; k < chunk.getSubChunks().length; k++)
				{
					SubChunk sc = chunk.getSubChunk(k);

					if (sc.isEmpty())
						continue;

					if (!subChunkFrustum(cx, k * 16, cz))
						continue;

					MainRender.shaders.worldShader.setTransformation(mat.identity().translate(cx, k * 16, cz));

					if (specialBlockRender)
					{
						final int finalCx = cx;
						final int finalK = k;
						final int finalCz = cz;
						sc.getSpecialRender().iterate((bx, by, bz) ->
						{
							BlockState state = sc.getState(bx, by, bz);
							Block block = state.getBlock();

							if (block instanceof ISpecialRender sr)
							{
								game.mainRender.stack.pushMatrix();
								game.mainRender.stack.translate(bx + finalCx, by + finalK * 16, bz + finalCz);
								game.mainRender.stack.translate(0.5f, 0, 0.5f);

								sr.render(game.mainRender.stack, world, state, bx + finalCx, by + finalK * 16, bz + finalCz);

								game.mainRender.stack.popMatrix();
							}
						});
					}

					if (countChunks)
						InGameGui.chunks++;

					/*
					 * Rendered in reverse order to get the desired effect
					 * (layer 0 -> back texture, layer 1 -> overlay texture)
					 */
					for (int i = SubChunk.getModelCount() - 1; i >= 0; i--)
					{
						// Do not render the transparent layer
						if (ModelLayer.TRANSPARENT.ordinal() == i)
							continue;

						if (sc.isEmpty(i))
							continue;

						if (World.enabled != -1 && i != World.enabled)
							continue;

						if (ModelLayer.EMISSION_NORMAL.ordinal() == i || ModelLayer.EMISSION_OVERLAY.ordinal() == i || ModelLayer.LIGHT.ordinal() == i)
							MainRender.shaders.worldShader.setUniform(WorldShader.SHADE, 1.0f);
						else
							MainRender.shaders.worldShader.setUniform(WorldShader.SHADE, world.shade);

						if (countChunks)
							InGameGui.chunkLayers++;

						glBindVertexArray(sc.getModel(i).getVao());

						for (int l = 0; l < 4; l++)
							glEnableVertexAttribArray(l);

						glDrawArrays(Tessellator3D.TRIANGLES, 0, sc.getTriangleCount(i) * 3);
					}
				}
			}
		}

		for (int l = 0; l < 4; l++)
			glDisableVertexAttribArray(l);

		glBindVertexArray(0);

		//		game.world.getEntityManager().render(game.mainRender.stack);


		renderTransparentNormal();
	}


	private void renderTransparentDeferred()
	{
		MainRender.shaders.gShader.bind(game.getCamera().getViewMatrix());
		MainRender.shaders.gShader.setUniform(CGGShader.EMISSION_TOGGLE, 0.0f);
		MainRender.shaders.gShader.setUniform(CGGShader.SHADE, world.shade);

		game.mainRender.gBuffer.bindFrameBuffer();

		BlockAtlas.getAtlas().getSprite().bind(0);

		for (int x = -game.options.renderDistance; x <= game.options.renderDistance; x++)
		{
			for (int z = -game.options.renderDistance; z <= game.options.renderDistance; z++)
			{
				int cx = x + ((int) (Math.floor(game.getPlayer().getX())) >> 4);
				int cz = z + ((int) (Math.floor(game.getPlayer().getZ())) >> 4);

				Chunk chunk = world.getChunk(cx, cz);

				if (chunk == null)
					continue;

				cx *= 16;
				cz *= 16;

				if (Vector2f.distance(cx + 8, cz + 8, game.getPlayer().getX(), game.getPlayer().getZ()) >= game.options.renderDistance * 16)
					continue;

				if (!chunkFrustum(cx, cz))
					continue;

				for (int k = 0; k < chunk.getSubChunks().length; k++)
				{
					SubChunk sc = chunk.getSubChunk(k);

					if (sc.isEmpty())
						continue;

					if (!subChunkFrustum(chunk.getX() * 16, k * 16, chunk.getZ() * 16))
						continue;

					MainRender.shaders.gShader.setTransformation(mat.identity().translate(chunk.getX() * 16, k * 16, chunk.getZ() * 16));

					ModelLayer TRANSPARENT = ModelLayer.TRANSPARENT;
					int i = TRANSPARENT.ordinal();

					if (sc.isEmpty(TRANSPARENT.ordinal()))
						continue;

					if (World.enabled != -1 && TRANSPARENT.ordinal() != World.enabled)
						continue;

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

		game.mainRender.gBuffer.unbindCurrentFrameBuffer();
	}

	private void renderTransparentNormal()
	{
		MainRender.shaders.worldShader.bind();
		MainRender.shaders.worldShader.setView(game.getCamera().getViewMatrix());
		MainRender.shaders.worldShader.setUniform(WorldShader.SHADE, world.shade);

		BlockAtlas.getAtlas().getSprite().bind(0);

		for (int x = -game.options.renderDistance; x <= game.options.renderDistance; x++)
		{
			for (int z = -game.options.renderDistance; z <= game.options.renderDistance; z++)
			{
				int cx = x + ((int) (Math.floor(game.getPlayer().getX())) >> 4);
				int cz = z + ((int) (Math.floor(game.getPlayer().getZ())) >> 4);

				Chunk chunk = world.getChunk(cx, cz);

				if (chunk == null)
					continue;

				cx *= 16;
				cz *= 16;

				if (Vector2f.distance(cx + 8, cz + 8, game.getPlayer().getX(), game.getPlayer().getZ()) >= game.options.renderDistance * 16)
					continue;

				if (!chunkFrustum(cx, cz))
					continue;

				for (int k = 0; k < chunk.getSubChunks().length; k++)
				{
					SubChunk sc = chunk.getSubChunk(k);

					if (sc.isEmpty())
						continue;

					if (!subChunkFrustum(chunk.getX() * 16, k * 16, chunk.getZ() * 16))
						continue;

					MainRender.shaders.worldShader.setTransformation(mat.identity().translate(chunk.getX() * 16, k * 16, chunk.getZ() * 16));

					ModelLayer TRANSPARENT = ModelLayer.TRANSPARENT;
					int i = TRANSPARENT.ordinal();

					if (sc.isEmpty(TRANSPARENT.ordinal()))
						continue;

					if (World.enabled != -1 && TRANSPARENT.ordinal() != World.enabled)
						continue;

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
	}


	void renderChunkOutlines()
	{
		for (Chunk chunk : world.getChunks())
		{
			if (chunk == null)
				continue;
			if (!chunkFrustum(chunk.getX() * 16, chunk.getZ() * 16))
				continue;

			for (int k = 0; k < chunk.getSubChunks().length; k++)
			{
				if (!subChunkFrustum(chunk.getX() * 16, k * 16, chunk.getZ() * 16))
					continue;

				//TODO: Move up... ??
				if (game.options.renderChunkOutline)
					MainRender.t.add(new AABBf(chunk.getX() * 16, k * 16, chunk.getZ() * 16, chunk.getX() * 16 + 16, k * 16 + 16, chunk.getZ() * 16 + 16));


				if (game.options.renderDataBlocks)
				{
					SubChunk sc = chunk.getSubChunk(k);

					for (int i = 0; i < 16; i++)
					{
						for (int j = 0; j < 16; j++)
						{
							for (int l = 0; l < 16; l++)
							{
								if (sc.getBlockData(i, j, l) != null)
									MainRender.t.add(new AABBf(chunk.getX() * 16 + i, k * 16 + j, chunk.getZ() * 16 + l, chunk.getX() * 16 + i + 1, k * 16 + j + 1, chunk.getZ() * 16 + l + 1));
							}
						}
					}
				}
			}
		}
	}
}
