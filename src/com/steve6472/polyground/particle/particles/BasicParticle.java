package com.steve6472.polyground.particle.particles;

import com.steve6472.polyground.Particle;
import com.steve6472.polyground.CaveGame;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.07.2019
 * Project: SJP
 *
 ***********************/
public class BasicParticle extends Particle
{
	public BasicParticle(Vector3f motion, Vector3f position, float size, Vector4f color, long lifeTime)
	{
		super(motion, position, size, color, lifeTime);
	}

	@Override
	public void tick()
	{
		getPosition().add(getMotion());
	}

	@Override
	public void applyShader()
	{
		CaveGame.shaders.basicParticleShader.bind();
	}

	@Override
	public void applyInvidualShader()
	{

	}

	@Override
	public boolean sort()
	{
		return false;
	}
}
