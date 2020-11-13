package steve6472.polyground.gui;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;
import steve6472.polyground.CaveGame;
import steve6472.polyground.PolyUtil;
import steve6472.polyground.events.WorldEvent;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.world.World;
import steve6472.sge.gfx.DepthFrameBuffer;
import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gui.Component;
import steve6472.sge.main.MainApp;
import steve6472.sge.main.game.Camera;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 27.12.2019
 * Project: SJP
 *
 ***********************/
public class Minimap extends Component
{
	private CaveGame game;
	private DepthFrameBuffer buffer;
	private Camera camera;
	private Matrix4f projectionMatrix;

	private boolean render = true;
	private boolean rotate = true;
	private boolean staticPosition = false;
	private float renderHeight = 10;

	@Override
	public void init(MainApp main)
	{
		this.game = (CaveGame) main;
		buffer = new DepthFrameBuffer(game);
		buffer.bindFrameBuffer(getWidth(), getHeight());
		buffer.resize(getWidth(), getHeight());
		camera = new Camera();
		projectionMatrix = PolyUtil.createProjectionMatrix(getWidth(), getHeight(), 512, 70);
		GL30.glBindFramebuffer(36160, 0);
//		projectionMatrix = new Matrix4f().ortho(0, 0, getWidth(), getHeight(), 0.1f, 512);
	}

	public void tick()
	{
	}

	public void renderWorld()
	{
		if (!render)
			return;

		renderToBuffer();
	}

	public void render()
	{
		if (!render)
			return;

		SpriteRender.fillRect(getX(), getY(), getWidth(), getHeight(), 0.2f, 0.2f, 0.2f, 1f);
		SpriteRender.renderSpriteInverted(getX(), getY(), getWidth(), getHeight(), buffer.texture);
		SpriteRender.drawSoftCircle(getX() + getWidth() / 2f, getY() + getHeight() / 2f, 4, 0.2f, 1, 0, 0, 1);
	}

	private void renderToBuffer()
	{
		Camera temp = game.getPlayer().getCamera();

		if (staticPosition)
		{
			camera.setPosition(temp.getX(), getRenderHeight(), temp.getZ());
		} else
		{
			camera.setPosition(temp.getPosition());
			camera.addPosition(0, getRenderHeight(), 0);
		}

		if (rotate)
			camera.setYaw(temp.getYaw());
		else
			camera.setYaw((float) (Math.PI * -0.5f));
		camera.setPitch((float) (Math.PI * -0.5f));

		camera.updateViewMatrix();
		game.getPlayer().setCamera(camera);
		game.mainRender.frustum.updateFrustum(projectionMatrix, camera.getViewMatrix());

		MainRender.shaders.worldShader.bind();
		MainRender.shaders.worldShader.setProjection(projectionMatrix);
		MainRender.shaders.entityShader.setProjection(projectionMatrix);

		buffer.bindFrameBuffer(getWidth(), getHeight());
		DepthFrameBuffer.clearCurrentBuffer();

		World world = game.getWorld();
		if (world != null)
		{
			if (!CaveGame.runGameEvent(new WorldEvent.PreRender(world)))
				world.getRenderer().renderNormal(false, false);
			CaveGame.runGameEvent(new WorldEvent.PostRender(world));
		}

		MainRender.shaders.worldShader.bind();
		MainRender.shaders.worldShader.setProjection(MainRender.shaders.getProjectionMatrix());
		MainRender.shaders.entityShader.setProjection(MainRender.shaders.getProjectionMatrix());

		game.getPlayer().setCamera(temp);
		buffer.unbindCurrentFrameBuffer(game);
		game.mainRender.resetFrustum();
	}

	public boolean isRender()
	{
		return render;
	}

	public void setRender(boolean render)
	{
		this.render = render;
	}

	public boolean isRotate()
	{
		return rotate;
	}

	public void setRotate(boolean rotate)
	{
		this.rotate = rotate;
	}

	public boolean isStaticPosition()
	{
		return staticPosition;
	}

	public void setStaticPosition(boolean staticPosition)
	{
		this.staticPosition = staticPosition;
	}

	public float getRenderHeight()
	{
		return renderHeight;
	}

	public void setRenderHeight(float renderHeight)
	{
		this.renderHeight = renderHeight;
	}
}
