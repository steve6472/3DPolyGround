package com.steve6472.polyground.shaders;

import com.steve6472.polyground.PolyUtil;
import com.steve6472.polyground.shaders.particles.*;
import com.steve6472.polyground.shaders.world.DissoveWorldShader;
import com.steve6472.polyground.shaders.world.WorldShader;
import com.steve6472.sge.gfx.Shader;
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
	public FlatTexturedParticleShader flatTexturedParticleShader;
	public ShaderParticleShader softRingShader;

	public WorldShader worldShader;
	public DissoveWorldShader dissoveWorldShader;
	public BreakParticleShader breakParticleShader;
	public ItemTextureShader itemTextureShader;

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
		flatTexturedParticleShader = new FlatTexturedParticleShader();
		softRingShader = new ShaderParticleShader();

		worldShader = new WorldShader();
		dissoveWorldShader = new DissoveWorldShader();
		breakParticleShader = new BreakParticleShader();
		itemTextureShader = new ItemTextureShader();

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

		flatTexturedParticleShader.getShader().bind();
		flatTexturedParticleShader.setProjection(projectionMatrix);

		softRingShader.getShader().bind();
		softRingShader.setProjection(projectionMatrix);

		worldShader.bind();
		worldShader.setProjection(projectionMatrix);

		dissoveWorldShader.bind();
		dissoveWorldShader.setProjection(projectionMatrix);
		dissoveWorldShader.setUniform(DissoveWorldShader.NOISERESOLUTION, 8f);
		dissoveWorldShader.setUniform(DissoveWorldShader.ATLAS, 0);

		breakParticleShader.bind();
		breakParticleShader.setProjection(projectionMatrix);

		Matrix4f ortho = new Matrix4f().ortho(0, event.getWidth(), event.getHeight(), 0, 1, -1);

		itemTextureShader.bind();
		itemTextureShader.setProjection(ortho);

		Shader.releaseShader();
	}

	public Matrix4f getProjectionMatrix()
	{
		return projectionMatrix;
	}
}
