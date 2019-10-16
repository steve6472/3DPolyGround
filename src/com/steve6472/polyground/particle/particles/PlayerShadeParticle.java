package com.steve6472.polyground.particle.particles;

import com.steve6472.polyground.Particle;
import com.steve6472.polyground.entity.Player;
import com.steve6472.polyground.CaveGame;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.07.2019
 * Project: SJP
 *
 ***********************/
public class PlayerShadeParticle extends Particle
{
	public PlayerShadeParticle(Vector4f color)
	{
		super(new Vector3f(), new Vector3f(), 0.3f, color, -1);
	}

	@Override
	public void tick()
	{
		Player player = CaveGame.getInstance().getPlayer();
//		setPosition(player.getPosition().x, 0.005f, player.getPosition().z);
		setPosition(player.getPosition().x, player.getPosition().y, player.getPosition().z);
	}

	@Override
	public void applyShader()
	{
		CaveGame.shaders.flatParticleShader.bind();
	}

	@Override
	public void applyInvidualShader()
	{

	}
}
