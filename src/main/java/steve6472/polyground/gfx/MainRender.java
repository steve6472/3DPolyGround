package steve6472.polyground.gfx;

import org.joml.AABBf;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import steve6472.polyground.AABBUtil;
import steve6472.polyground.CaveGame;
import steve6472.polyground.Frustum;
import steve6472.polyground.PolyUtil;
import steve6472.polyground.events.WorldEvent;
import steve6472.polyground.gfx.light.LightManager;
import steve6472.polyground.gfx.particle.BreakParticleStorage;
import steve6472.polyground.gfx.particle.ParticleStorage;
import steve6472.polyground.gfx.shaders.Shaders;
import steve6472.polyground.gfx.stack.Stack;
import steve6472.polyground.teleporter.Teleporter;
import steve6472.polyground.tessellators.BasicTessellator;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.sge.gfx.DepthFrameBuffer;
import steve6472.sge.gfx.*;
import steve6472.sge.gfx.post.PostProcessing;
import steve6472.sge.gfx.shaders.AbstractDeferredShader;
import steve6472.sge.gfx.shaders.Shader;
import steve6472.sge.gui.floatingdialog.DialogManager;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.WindowSizeEvent;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static steve6472.sge.gfx.VertexObjectCreator.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 14.05.2020
 * Project: CaveGame
 *
 ***********************/
public class MainRender
{
	public static List<AABBf> t = new ArrayList<>();
	public static Shaders shaders;

	public static int CHUNK_REBUILT = 0;

	private final CaveGame game;

	/* Deferred */
	public CGGBuffer gBuffer;
	public final int finalRenderQuad;

	/* Frame Buffers */
	private final DepthFrameBuffer mainFrameBuffer;
	private final DepthFrameBuffer waterFrameBuffer;

	public final PostProcessing postProcessing;

	public final ThreadedModelBuilder modelBuilder;

	/* World */
	public final CGSkybox skybox;
	public final Frustum frustum;
	public final ParticleStorage particles;
	public final BreakParticleStorage breakParticles;
	public final DialogManager dialogManager;

	public ModelBuilder buildHelper;
	public BasicTessellator basicTess;
	public BasicTessellator waterTess;
	public Stack stack;

	public MainRender(CaveGame game)
	{
		this.game = game;
		game.getEventHandler().register(this);

		/* Create quad for Final Render */
		finalRenderQuad = createVAO();
		storeFloatDataInAttributeList(0, 2, new float[] {-1, +1, -1, -1, +1, -1, +1, -1, +1, +1, -1, +1});
		storeFloatDataInAttributeList(1, 2, new float[] {0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1});
		unbindVAO();

		buildHelper = new ModelBuilder();

		mainFrameBuffer = new DepthFrameBuffer(game.getWidth(), game.getHeight(), true);
		waterFrameBuffer = new DepthFrameBuffer(game.getWidth(), game.getHeight());
		postProcessing = new PostProcessing(game.getWidth(), game.getHeight());
		game.getEventHandler().register(postProcessing);

		frustum = new Frustum();

		particles = new ParticleStorage(this);
		breakParticles = new BreakParticleStorage(this);
		dialogManager = new DialogManager();

		basicTess = new BasicTessellator(1000000);
		waterTess = new BasicTessellator(16777216);

		gBuffer = new CGGBuffer(game.getWindowWidth(), game.getWindowHeight());
		game.getEventHandler().register(gBuffer);

		shaders = new Shaders();
		game.getEventHandler().register(shaders);

		modelBuilder = new ThreadedModelBuilder();
		modelBuilder.start();

		stack = new Stack();

		skybox = new CGSkybox(StaticCubeMap.fromTextureFaces("skybox", new String[]{"side", "side", "top", "bottom", "side", "side"}, "png"), shaders.getProjectionMatrix());
	}

	@Event
	public void resize(WindowSizeEvent e)
	{
		mainFrameBuffer.resize(e.getWidth(), e.getHeight());
		waterFrameBuffer.resize(e.getWidth(), e.getHeight());
		skybox.updateProjection(PolyUtil.createProjectionMatrix(e.getWidth(), e.getHeight()));
	}

	public void render()
	{
		CHUNK_REBUILT = 0;
		if (game.options.maxChunkRebuild == -1)
		{
			while (modelBuilder.canTake())
			{
				ModelData data = modelBuilder.take();
				SubChunk subChunk = game.getWorld().getChunk(data.x, data.z).getSubChunk(data.layer);
				subChunk.updateModel(data);
			}
		} else
		{
			while (modelBuilder.canTake() && CHUNK_REBUILT < game.options.maxChunkRebuild)
			{
				ModelData data = modelBuilder.take();
				SubChunk subChunk = game.getWorld().getChunk(data.x, data.z).getSubChunk(data.layer);
				subChunk.updateModel(data);
				CHUNK_REBUILT++;
			}
		}

		frustum.updateFrustum(shaders.getProjectionMatrix(), game.getCamera().getViewMatrix());

		if (game.world == null)
			return;

		if (!CaveGame.runGameEvent(new WorldEvent.PreRender(game.world)))
			game.world.render(true, true);
		CaveGame.runGameEvent(new WorldEvent.PostRender(game.world));

		mainFrameBuffer.bindFrameBuffer(game);
		steve6472.sge.gfx.DepthFrameBuffer.clearCurrentBuffer();

		shaders.deferredShader.bind();
		shaders.deferredShader.setUniform(AbstractDeferredShader.cameraPos, game.getCamera().getX(), game.getCamera().getY(), game.getCamera().getZ());
		StaticTexture.bind(0, gBuffer.texture);
		StaticTexture.bind(1, gBuffer.position);
		StaticTexture.bind(2, gBuffer.normal);
		StaticTexture.bind(3, gBuffer.emission);
		StaticTexture.bind(4, gBuffer.emissionPos);
		LightManager.updateLights(shaders.deferredShader, true);
		VertexObjectCreator.basicRender(finalRenderQuad, 2, 6, Tessellator.TRIANGLES);

		shaders.entityShader.bind();
		LightManager.updateLights(shaders.entityShader, false);

		mainFrameBuffer.unbindCurrentFrameBuffer(game);

		copyDepthToBuffer(mainFrameBuffer.frameBuffer);

		// Render floating dialogs

		dialogManager.render(game, MainRender.shaders.dialogShader, game.getCamera().getViewMatrix(), game.getCamera().getYaw(), game.getCamera().getPitch(), mainFrameBuffer.frameBuffer);

		/* Render water */
		waterFrameBuffer.bindFrameBuffer(game);
		DepthFrameBuffer.clearCurrentBuffer();
		copyDepthToBuffer(waterFrameBuffer.frameBuffer);
		waterFrameBuffer.bindFrameBuffer(game);
		shaders.mainShader.bind(game.getCamera().getViewMatrix());
		waterTess.loadPos(0);
		waterTess.loadColor(1);
		GL20.glDrawArrays(Tessellator.TRIANGLES, 0, game.currentWaterCount);
		waterTess.disable(0, 1);

		game.getRifts().render();

		mainFrameBuffer.bindFrameBuffer(game);
		renderTheWorld(false);

		shaders.waterShader.bind();
		StaticTexture.bind(0, waterFrameBuffer.texture);
		VertexObjectCreator.basicRender(finalRenderQuad, 2, 6, Tessellator.TRIANGLES);

		if (game.options.renderLights)
			LightManager.renderLights();

		mainFrameBuffer.unbindCurrentFrameBuffer(game);

		if (game.options.renderRifts)
		{
			mainFrameBuffer.bindFrameBuffer(game);
			glDisable(GL_DEPTH_TEST);
			game.getRifts().renderRifts();
			glEnable(GL_DEPTH_TEST);
			mainFrameBuffer.unbindCurrentFrameBuffer(game);
		}

		game.inGameGui.minimap.renderWorld();

		Shader.releaseShader();

		if (game.options.enablePostProcessing)
			postProcessing.doPostProcessing(mainFrameBuffer.texture);
	}

	public void renderTheWorld(boolean deferred)
	{
		resetFrustum();

		if (game.world == null)
			return;

		game.hitPicker.tick(game.getPlayer(), game);

		/* Render AABBs from t */
		shaders.mainShader.bind(game.getCamera().getViewMatrix());
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

		game.mainRender.stack.render(game.getCamera().getViewMatrix());

		/* END */

		if (game.options.renderTeleporters)
		{
			shaders.mainShader.bind(game.getCamera().getViewMatrix());
			for (Teleporter teleporter : game.getWorld().teleporters.getTeleporters())
			{
				AABBUtil.renderAABBf(teleporter.getAabb(), basicTess, 3f, shaders.mainShader);
				renderTeleporterPair(teleporter);
			}
		}

		shaders.mainShader.bind(game.getCamera().getViewMatrix());

		// render only non-deferred!
		// world would be rendered twice from players perseptive
		if (!deferred)
		{
			if (!CaveGame.runGameEvent(new WorldEvent.PreRender(game.world)))
				game.world.render(false, false);
			CaveGame.runGameEvent(new WorldEvent.PostRender(game.world));
		}

//		miningTool.render(game.getPlayer().getX(), game.getPlayer().getY(), game.getPlayer().getZ()
//			, 0, (float) (game.getPlayer().getCamera().getYaw() + Math.PI / 2f), (game.getPlayer().getCamera().getPitch()), 1f);

		if (game.getPlayer() != null)
			game.getPlayer().render();

		//		CaveGame.shaders.mainShader.bind(getCamera().getViewMatrix());
		//		AABBUtil.renderAABBf(player.getHitbox().getHitbox(), basicTess, 1, shaders.mainShader);

		if (game.options.renderSkybox)
		{
			skybox.setShade(game.world.shade);
			skybox.render(game.getCamera().getViewMatrix());
		}

		breakParticles.render();
		particles.render();
	}

	private void renderTeleporterPair(Teleporter tel)
	{
		if (tel.getOther() == null)
			return;

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

	public DepthFrameBuffer getMainFrameBuffer()
	{
		return mainFrameBuffer;
	}

	public void resetFrustum()
	{
		frustum.updateFrustum(MainRender.shaders.getProjectionMatrix(), game.getCamera().getViewMatrix());
	}

	/**
	 * Copies Depth Buffer from gBuffer to gBufferOutput
	 */
	private void copyDepthToBuffer(int bufferToCopyTo)
	{
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, gBuffer.frameBuffer);
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, bufferToCopyTo);
		GL30.glBlitFramebuffer(0, 0, gBuffer.getWidth(), gBuffer.getHeight(), 0, 0, game.getWidth(), game.getHeight(), GL_DEPTH_BUFFER_BIT, GL_NEAREST);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}

}
