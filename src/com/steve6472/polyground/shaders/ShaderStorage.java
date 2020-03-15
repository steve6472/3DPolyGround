package com.steve6472.polyground.shaders;

import com.steve6472.polyground.PolyUtil;
import com.steve6472.polyground.shaders.particles.*;
import com.steve6472.polyground.shaders.world.WorldShader;
import com.steve6472.polyground.shaders.world.FlatTexturedShader;
import com.steve6472.sge.gfx.shaders.Shader;
import com.steve6472.sge.main.events.Event;
import com.steve6472.sge.main.events.WindowSizeEvent;
import org.joml.Matrix4f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.07.2019
 * Project: SJP
 *
 ***********************/
public class ShaderStorage
{
	private Matrix4f projectionMatrix;

	public BasicParticleShader basicParticleShader;
	public FlatParticleShader flatParticleShader;

	public FlatTexturedShader flatTexturedShader;
	public WorldShader worldShader;
	public BreakParticleShader breakParticleShader;
	public ItemTextureShader itemTextureShader;
	public DebugShader debugShader;
	public RiftShader riftShader;

	public MainShader mainShader;

	public ShaderStorage()
	{
		projectionMatrix = new Matrix4f();

		initShaders();
	}

	private void initShaders()
	{
		basicParticleShader = new BasicParticleShader();
		flatParticleShader = new FlatParticleShader();

		flatTexturedShader = new FlatTexturedShader();
		worldShader = new WorldShader();
		breakParticleShader = new BreakParticleShader();
		itemTextureShader = new ItemTextureShader();
		debugShader = new DebugShader();
		riftShader = new RiftShader();

		mainShader = new MainShader();
	}

	@Event
	public void windowResize(WindowSizeEvent event)
	{
		projectionMatrix = PolyUtil.createProjectionMatrix(event.getWidth(), event.getHeight());

		basicParticleShader.getShader().bind();
		basicParticleShader.setProjection(projectionMatrix);

		mainShader.getShader().bind();
		mainShader.setProjection(projectionMatrix);

		flatParticleShader.getShader().bind();
		flatParticleShader.setProjection(projectionMatrix);

		flatTexturedShader.bind();
		flatTexturedShader.setProjection(projectionMatrix);

		worldShader.bind();
		worldShader.setProjection(projectionMatrix);
		worldShader.setUniform(WorldShader.ATLAS, 0);

		breakParticleShader.bind();
		breakParticleShader.setProjection(projectionMatrix);

		Matrix4f ortho = new Matrix4f().ortho(0, event.getWidth(), event.getHeight(), 0, 1, -1);

		itemTextureShader.bind();
		itemTextureShader.setProjection(ortho);

		debugShader.bind();
		debugShader.setProjection(projectionMatrix);

		riftShader.bind();
		riftShader.setProjection(projectionMatrix);
		riftShader.setUniform(RiftShader.TEXTURE, 0);

		Shader.releaseShader();
	}

	public Matrix4f getProjectionMatrix()
	{
		return projectionMatrix;
	}
}
