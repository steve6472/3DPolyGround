package com.steve6472.polyground.particle.particles;

import com.steve6472.polyground.Particle;
import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.shaders.particles.FlatTexturedParticleShader;
import com.steve6472.sge.gfx.Sprite;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.07.2019
 * Project: SJP
 *
 ***********************/
public class CircleParticle extends Particle
{
	private int sprite;

	public CircleParticle(float x, float y, float z, float size, int sprite, float rotSpeed, long lifeTime)
	{
		super(new Vector3f(), new Vector3f(x, y, z), size, new Vector4f(0, 0, 0, 0), lifeTime);
		this.sprite = sprite;
		this.rotSpeed = rotSpeed;

		colorSpeed = new Vector4f(0, 0, 0, 0);
	}

	float growingSpeed;
	Vector4f colorSpeed;

	public CircleParticle setGrowingSpeed(float growingSpeed)
	{
		this.growingSpeed = growingSpeed;
		return this;
	}

	public CircleParticle setColorSpeed(Vector4f colorSpeed)
	{
		this.colorSpeed = colorSpeed;
		return this;
	}

	public CircleParticle setMovingSpeed(float x, float y, float z)
	{
		setMotion(x, y, z);
		return this;
	}

	public CircleParticle setColor(float r, float g, float b, float a)
	{
		setColor(new Vector4f(r, g, b, a));
		return this;
	}

	float ang;
	private float rotSpeed;

	@Override
	public void tick()
	{
		ang += rotSpeed;
		setSize(getSize() + growingSpeed);
		getColor().add(colorSpeed);
		getPosition().add(getMotion());
	}

	@Override
	public void applyShader()
	{
		FlatTexturedParticleShader ftps = CaveGame.shaders.flatTexturedParticleShader;

		ftps.bind();
		Sprite.bind(0, sprite);
		ftps.setSampler(0);
		ftps.setTransformation(
				new Matrix4f()
						.translate(getPosition())
						.rotate(ang, 0, 1, 0)
						.translate(-getPosition().x, -getPosition().y, -getPosition().z));
	}

	@Override
	public void applyInvidualShader()
	{

	}
}
