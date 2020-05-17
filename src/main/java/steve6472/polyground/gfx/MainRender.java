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
import steve6472.polyground.gfx.particle.ParticleStorage;
import steve6472.polyground.gfx.shaders.ShaderStorage;
import steve6472.polyground.teleporter.Teleporter;
import steve6472.polyground.tessellators.BasicTessellator;
import steve6472.polyground.world.BuildHelper;
import steve6472.polyground.world.chunk.water.Water;
import steve6472.polyground.world.light.LightManager;
import steve6472.sge.gfx.DepthFrameBuffer;
import steve6472.sge.gfx.*;
import steve6472.sge.gfx.post.PostProcessing;
import steve6472.sge.gfx.shaders.GenericDeferredShader;
import steve6472.sge.gfx.shaders.Shader;
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
	public static List<Water> water = new ArrayList<>();
	public static ShaderStorage shaders;

	private final CaveGame game;

	/* Deferred */
	public CGGBuffer gBuffer;
	private final int finalRenderQuad;

	/* Frame Buffers */
	private final DepthFrameBuffer mainFrameBuffer;
	private final DepthFrameBuffer waterFrameBuffer;

	public final PostProcessing postProcessing;

	/* World */
	public final CGSkybox skybox;
	public final Frustum frustum;
	public final ParticleStorage particles;

	public BuildHelper buildHelper;
	public BasicTessellator basicTess;
	public BasicTessellator waterTess;

	public MainRender(CaveGame game)
	{
		this.game = game;
		game.getEventHandler().register(this);

		/* Create quad for Final Render */
		finalRenderQuad = createVAO();
		storeFloatDataInAttributeList(0, 2, new float[] {-1, +1, -1, -1, +1, -1, +1, -1, +1, +1, -1, +1});
		storeFloatDataInAttributeList(1, 2, new float[] {0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1});
		unbindVAO();

		buildHelper = new BuildHelper();

		mainFrameBuffer = new DepthFrameBuffer(game.getWidth(), game.getHeight(), true);
		waterFrameBuffer = new DepthFrameBuffer(game.getWidth(), game.getHeight());
		postProcessing = new PostProcessing(game.getWidth(), game.getHeight());
		game.getEventHandler().register(postProcessing);

		frustum = new Frustum();

		particles = new ParticleStorage(this);

		basicTess = new BasicTessellator(1000000);
		waterTess = new BasicTessellator(16777216);

		gBuffer = new CGGBuffer(game.getWindowWidth(), game.getWindowHeight());
		game.getEventHandler().register(gBuffer);

		shaders = new ShaderStorage();
		game.getEventHandler().register(shaders);

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
		frustum.updateFrustum(shaders.getProjectionMatrix(), game.getCamera().getViewMatrix());

		if (game.world != null)
		{
			game.world.shouldRebuild = true;
			game.world.tryRebuild();

			if (!CaveGame.runGameEvent(new WorldEvent.PreRender(game.world)))
				game.world.render(true, true);
			CaveGame.runGameEvent(new WorldEvent.PostRender(game.world));

			mainFrameBuffer.bindFrameBuffer(game);
			steve6472.sge.gfx.DepthFrameBuffer.clearCurrentBuffer();

			shaders.deferredShader.bind();
			shaders.deferredShader.setUniform(GenericDeferredShader.cameraPos, game.getCamera().getX(), game.getCamera().getY(), game.getCamera().getZ());
			Sprite.bind(0, gBuffer.texture);
			Sprite.bind(1, gBuffer.position);
			Sprite.bind(2, gBuffer.normal);
			Sprite.bind(3, gBuffer.emission);
			Sprite.bind(4, gBuffer.emissionPos);
			LightManager.updateLights(shaders.deferredShader);
			VertexObjectCreator.basicRender(finalRenderQuad, 2, 6, Tessellator.TRIANGLES);

			mainFrameBuffer.unbindCurrentFrameBuffer(game);

			copyDepthToBuffer(mainFrameBuffer.frameBuffer);

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
			Sprite.bind(0, waterFrameBuffer.texture);
			VertexObjectCreator.basicRender(finalRenderQuad, 2, 6, Tessellator.TRIANGLES);

			if (game.options.renderLights)
				LightManager.renderLights();

			mainFrameBuffer.unbindCurrentFrameBuffer(game);
		}

		if (game.options.renderRifts)
		{
			mainFrameBuffer.bindFrameBuffer(game);
			glDisable(GL_DEPTH_TEST);
			game.getRifts().renderRifts();
			glEnable(GL_DEPTH_TEST);
			mainFrameBuffer.unbindCurrentFrameBuffer(game);
		}

		if (game.world != null)
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

		if (!deferred)
		{
			if (!CaveGame.runGameEvent(new WorldEvent.PreRender(game.world)))
				game.world.render(false, false);
			CaveGame.runGameEvent(new WorldEvent.PostRender(game.world));
		}

		//		CaveGame.shaders.mainShader.bind(getCamera().getViewMatrix());
		//		AABBUtil.renderAABBf(player.getHitbox().getHitbox(), basicTess, 1, shaders.mainShader);

		skybox.setShade(game.world.shade);
		skybox.render(game.getCamera().getViewMatrix());

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
