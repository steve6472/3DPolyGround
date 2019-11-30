package com.steve6472.polyground.rift;

import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.shaders.RiftShader;
import com.steve6472.sge.gfx.DepthFrameBuffer;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.gfx.Tessellator3D;
import com.steve6472.sge.main.events.Event;
import com.steve6472.sge.main.events.WindowSizeEvent;
import com.steve6472.sge.main.game.Camera;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.steve6472.sge.gfx.VertexObjectCreator.unbindVAO;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

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

	private void renderToBuffer(Rift rift)
	{
		Camera temp = main.getPlayer().getCamera();

		camera.setPosition(rift.getPosition());
		camera.getPosition().add(temp.getPosition());
		camera.getPosition().add(rift.getCorrection());

		camera.setYaw(temp.getYaw() + rift.getYaw());
		camera.setPitch(temp.getPitch() + rift.getPitch());

		camera.updateViewMatrix();
		main.getPlayer().setCamera(camera);
		main.frustum.updateFrustum(CaveGame.shaders.getProjectionMatrix(), camera.getViewMatrix());

		rift.getBuffer().bindFrameBuffer(main);
		DepthFrameBuffer.clearCurrentBuffer();
		main.renderTheWorld();

		main.getPlayer().setCamera(temp);
		rift.getBuffer().unbindCurrentFrameBuffer(main);
	}

	private void renderToWorld(Rift rift)
	{
		CaveGame.shaders.riftShader.bind();
		CaveGame.shaders.riftShader.setView(CaveGame.getInstance().getCamera().getViewMatrix());
		CaveGame.shaders.riftShader.setUniform(RiftShader.TINT, 0.75f, 0.75f, 0.75f);

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
		for (Iterator<Rift> iterator = rifts.iterator(); iterator.hasNext();)
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
