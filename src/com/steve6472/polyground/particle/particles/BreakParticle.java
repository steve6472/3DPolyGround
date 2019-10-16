package com.steve6472.polyground.particle.particles;

import com.steve6472.polyground.Particle;
import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.block.BlockLoader;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.07.2019
 * Project: SJP
 *
 ***********************/
public class BreakParticle extends Particle
{
	long l, d;

	int life;

	public BreakParticle(Vector3f motion, Vector3f position, float size, Vector4f color, long lifeTime)
	{
		super(motion, position, size, color, -1);
		d = System.currentTimeMillis();
		l = System.currentTimeMillis() + lifeTime;
	}

	float growingSpeed;

	@Override
	public void tick()
	{
		life++;
		setSize(getSize() + growingSpeed);

		if (getSize() < 0)
			setSize(0);

		getPosition().add(new Vector3f(getMotion()).mul((float) (System.currentTimeMillis() - d) / (float) (l - d)));
	}

	@Override
	public boolean shouldDie()
	{
		return getSize() <= 0 || l < System.currentTimeMillis();
	}

	@Override
	public boolean sort()
	{
		return false;
	}

	public BreakParticle setGrowingSpeed(float growingSpeed)
	{
		this.growingSpeed = growingSpeed;
		return this;
	}

	@Override
	public void applyShader()
	{
		CaveGame.shaders.breakParticleShader.bind();
		CaveGame.shaders.breakParticleShader.setView(CaveGame.getInstance().getCamera().getViewMatrix());
		BlockLoader.getAtlas().getSprite().bind(0);
	}

	@Override
	public void applyInvidualShader()
	{

	}
}
