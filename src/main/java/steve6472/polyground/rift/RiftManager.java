package steve6472.polyground.rift;

import org.joml.Vector3f;
import steve6472.polyground.CaveGame;
import steve6472.polyground.gfx.shaders.RiftShader;
import steve6472.polyground.tessellators.BasicTessellator;
import steve6472.sge.gfx.DepthFrameBuffer;
import steve6472.sge.gfx.Sprite;
import steve6472.sge.gfx.Tessellator;
import steve6472.sge.gfx.Tessellator3D;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.WindowSizeEvent;
import steve6472.sge.main.game.Camera;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static steve6472.sge.gfx.VertexObjectCreator.unbindVAO;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 29.11.2019
 * Project: SJP
 *
 ***********************/
public class RiftManager
{
	private List<Rift> rifts;
	private CaveGame main;
	private Camera camera;

	public RiftManager(CaveGame main)
	{
		rifts = new ArrayList<>();
		this.main = main;
		camera = new Camera();
	}

	public void renderRifts()
	{
		int v = 0;
		for (Rift rift : getRifts())
		{
			int t = ((rift.getModel().getVertices().size() * 2) - 3) * 2;
			v += Math.max(t, 1);
		}

		CaveGame.shaders.mainShader.bind(main.getCamera().getViewMatrix());
		BasicTessellator tess = main.basicTess;
		tess.begin(v);
		tess.color(1, 1, 1, 1);

		for (Rift rift : getRifts())
		{
			List<Vector3f> vertices = rift.getModel().getVertices();

			for (int i = 0; i < vertices.size(); i++)
			{
				Vector3f vec = vertices.get(i);

				tess.pos(vec).endVertex();

				if (i != 0 && i != 1)
				{
					tess.pos(vertices.get(i - 1)).endVertex();
					tess.pos(vec).endVertex();
					tess.pos(vertices.get(i - 2)).endVertex();
				}
			}
		}

		glLineWidth(3f);
		tess.loadPos(0);
		tess.loadColor(1);
		tess.draw(Tessellator.LINES);
		tess.disable(0, 1);
		glLineWidth(1f);
	}

	private void renderToBuffer(Rift rift)
	{
		Camera temp = main.getPlayer().getCamera();

		camera.setPosition(rift.getPosition());
		camera.getPosition().add(temp.getPosition());
		camera.getPosition().add(rift.getCorrection());

		camera.setYaw(temp.getYaw() - rift.getYaw());
		camera.setPitch(temp.getPitch() - rift.getPitch());

		camera.updateViewMatrix();
		main.getPlayer().setCamera(camera);
		main.frustum.updateFrustum(CaveGame.shaders.getProjectionMatrix(), camera.getViewMatrix());

		rift.getBuffer().bindFrameBuffer(main);
		DepthFrameBuffer.clearCurrentBuffer();
		main.renderTheWorld(false);

		main.getPlayer().setCamera(temp);
		rift.getBuffer().unbindCurrentFrameBuffer(main);
	}

	private void renderToWorld(Rift rift)
	{
		CaveGame.shaders.riftShader.bind();
		CaveGame.shaders.riftShader.setView(CaveGame.getInstance().getCamera().getViewMatrix());
		CaveGame.shaders.riftShader.setUniform(RiftShader.TINT, 1f, 1f, 1f);

		Sprite.bind(0, rift.getBuffer().texture);

		glBindVertexArray(rift.getModel().getVao());
		glEnableVertexAttribArray(0);
		glDrawArrays(Tessellator3D.TRIANGLE_STRIP, 0, rift.getModel().getVertCount());

		glDisableVertexAttribArray(0);
		unbindVAO();
	}

	public void render()
	{
		for (Rift rift : rifts)
		{
			renderToBuffer(rift);

			main.getMainFrameBuffer().bindFrameBuffer(main);
			renderToWorld(rift);
			main.getMainFrameBuffer().unbindCurrentFrameBuffer(main);
		}
	}

	@Event
	public void updateFramebuffers(WindowSizeEvent e)
	{
		for (Rift rift : rifts)
		{
			rift.getBuffer().bindFrameBuffer(e.getWidth(), e.getHeight());
			rift.getBuffer().resize(e.getWidth(), e.getHeight());
			rift.getBuffer().unbindCurrentFrameBuffer(e.getWidth(), e.getHeight());
		}
	}

	public void addRift(Rift rift)
	{
		rift.getModel().setupModel();
		rift.getModel().updateModel();

		rifts.add(rift);
	}

	public boolean removeRift(String name)
	{
		for (Iterator<Rift> iterator = rifts.iterator(); iterator.hasNext(); )
		{
			Rift r = iterator.next();

			if (r.getName().equals(name))
			{
				r.getModel().deleteModel();
				iterator.remove();
				return true;
			}
		}

		return false;
	}

	public Rift getRift(String name)
	{
		for (Rift rift : rifts)
		{
			if (rift.getName().equals(name))
				return rift;
		}

		return null;
	}

	public List<Rift> getRifts()
	{
		return rifts;
	}
}
