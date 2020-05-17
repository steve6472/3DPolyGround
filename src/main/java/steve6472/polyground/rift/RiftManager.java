package steve6472.polyground.rift;

import org.joml.Vector3f;
import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.gfx.shaders.RiftShader;
import steve6472.polyground.teleporter.Teleporter;
import steve6472.polyground.tessellators.BasicTessellator;
import steve6472.polyground.world.World;
import steve6472.sge.gfx.DepthFrameBuffer;
import steve6472.sge.gfx.Sprite;
import steve6472.sge.gfx.Tessellator;
import steve6472.sge.gfx.Tessellator3D;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.WindowSizeEvent;
import steve6472.sge.main.game.Camera;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	private final List<Rift> rifts;
	private final CaveGame main;
	private final Camera camera;
	private final World world;

	public RiftManager(CaveGame main, World world)
	{
		rifts = new ArrayList<>();
		this.main = main;
		camera = new Camera();
		this.world = world;
	}

	public void loadRifts()
	{

	}

	public void saveRifts()
	{
		if (world.worldName == null || world.worldName.isEmpty())
		{
			System.err.println("Can not save teleporters, world name is invalid");
			return;
		}

		JSONObject main = new JSONObject();
		JSONArray array = new JSONArray();

		for (Rift t : rifts)
		{
			JSONObject json = new JSONObject();
			JSONArray box = new JSONArray();
			box.put(t.getAabb().minX).put(t.getAabb().minY).put(t.getAabb().minZ).put(t.getAabb().maxX).put(t.getAabb().maxY).put(t.getAabb().maxZ);
			json.put("aabb", box);
			json.put("uuid", t.getUuid());
			json.put("other", t.getOther().getUuid());
			array.put(json);
		}
		main.put("teleporters", array);

		System.out.println(main.toString(4));

		File f = new File("game/worlds/" + world.worldName + "/teleporters.json");
		try
		{
			FileWriter writer = new FileWriter(f);
			main.write(writer, 4, 4);
			writer.flush();
			writer.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void renderRifts()
	{
		int v = 0;
		for (Rift rift : getRifts())
		{
			int t = ((rift.getModel().getVertices().size() * 2) - 3) * 2;
			v += Math.max(t, 1);
		}

		MainRender.shaders.mainShader.bind(main.getCamera().getViewMatrix());
		BasicTessellator tess = main.mainRender.basicTess;
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
		main.mainRender.frustum.updateFrustum(MainRender.shaders.getProjectionMatrix(), camera.getViewMatrix());

		rift.getBuffer().bindFrameBuffer(main);
		DepthFrameBuffer.clearCurrentBuffer();
		main.mainRender.renderTheWorld(false);

		main.getPlayer().setCamera(temp);
		rift.getBuffer().unbindCurrentFrameBuffer(main);
	}

	private void renderToWorld(Rift rift)
	{
		MainRender.shaders.riftShader.bind();
		MainRender.shaders.riftShader.setView(CaveGame.getInstance().getCamera().getViewMatrix());
		MainRender.shaders.riftShader.setUniform(RiftShader.TINT, 1f, 1f, 1f);

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
