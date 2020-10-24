package steve6472.polyground.rift;

import org.joml.Vector3f;
import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.gfx.shaders.RiftShader;
import steve6472.polyground.tessellators.BasicTessellator;
import steve6472.polyground.world.World;
import steve6472.sge.gfx.DepthFrameBuffer;
import steve6472.sge.gfx.Sprite;
import steve6472.sge.gfx.Tessellator;
import steve6472.sge.gfx.Tessellator3D;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.WindowSizeEvent;
import steve6472.sge.main.game.Camera;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;
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
		main.getEventHandler().register(this);
	}

	public void loadRifts()
	{
		if (world.worldName == null || world.worldName.isEmpty())
		{
			System.err.println("Can not load teleporters, world name is invalid");
			return;
		}

		JSONObject main;

		File f = new File("game/worlds/" + world.worldName + "/rifts.json");
		if (!f.exists())
		{
			System.err.println("World has no rifts!");
			return;
		} else
		{
			try
			{
				BufferedReader reader = new BufferedReader(new FileReader(f));
				String line;
				StringBuilder builder = new StringBuilder();
				while ((line = reader.readLine()) != null)
				{
					builder.append(line);
				}

				main = new JSONObject(builder.toString());

			} catch (IOException e)
			{
				e.printStackTrace();
				return;
			}
		}

		JSONArray array = main.getJSONArray("rifts");

		for (int i = 0; i < array.length(); i++)
		{
			JSONObject json = array.getJSONObject(i);
			JSONArray ver = json.getJSONArray("vertices");
			JSONArray pos = json.getJSONArray("position");
			JSONArray cor = json.getJSONArray("correction");

			List<Vector3f> vertices = new ArrayList<>(ver.length());
			for (int j = 0; j < ver.length() / 3; j++)
				vertices.add(new Vector3f(ver.getFloat(j * 3), ver.getFloat(j * 3 + 1), ver.getFloat(j * 3 + 2)));

			float yaw = json.getFloat("yaw");
			float pitch = json.getFloat("pitch");
			Vector3f position = new Vector3f(pos.getFloat(0), pos.getFloat(1), pos.getFloat(2));
			Vector3f correction = new Vector3f(cor.getFloat(0), cor.getFloat(1), cor.getFloat(2));
			String name = json.getString("name");

			Rift rift = new Rift(name, position, correction, yaw, pitch, new RiftModel(vertices));
			rift.getModel().setupModel();
			rift.getModel().updateModel();
			rift.setFinished(true);
			rifts.add(rift);
		}
	}

	public void saveRifts()
	{
		if (world.worldName == null || world.worldName.isEmpty())
		{
			System.err.println("Can not save rifts, world name is invalid");
			return;
		}

		JSONObject main = new JSONObject();
		JSONArray array = new JSONArray();

		for (Rift t : rifts)
		{
			if (!t.isFinished())
			{
				System.err.println("Can not save unfinished rift (" + t.getName() + ")");
				continue;
			}
			JSONObject json = new JSONObject();
			JSONArray vertices = new JSONArray();
			for (Vector3f v : t.getModel().getVertices())
				vertices.put(v.x).put(v.y).put(v.z);

			json.put("vertices", vertices);
			json.put("yaw", t.getYaw());
			json.put("pitch", t.getPitch());
			json.put("position", new JSONArray().put(t.getX()).put(t.getY()).put(t.getZ()));
			json.put("correction", new JSONArray().put(t.getCorrection().x()).put(t.getCorrection().y()).put(t.getCorrection().z()));
			json.put("name", t.getName());
			array.put(json);
		}
		main.put("rifts", array);

		File f = new File("game/worlds/" + world.worldName + "/rifts.json");
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

		main.mainRender.renderTheWorld(true);

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
