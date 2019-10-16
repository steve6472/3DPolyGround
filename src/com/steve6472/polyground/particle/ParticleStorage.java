package com.steve6472.polyground.particle;

import com.steve6472.polyground.Particle;
import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.QuickSort;
import com.steve6472.polyground.particle.particles.BasicParticle;
import com.steve6472.polyground.tessellators.ParticleTessellator;
import com.steve6472.sge.gfx.Tessellator;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.07.2019
 * Project: SJP
 *
 ***********************/
public class ParticleStorage
{
	HashMap<Class<? extends Particle>, List<Particle>> particles;
	HashMap<Class<? extends Particle>, List<Particle>> tickParticles;

	private ParticleTessellator particleTess;
	private CaveGame pg;

	public ParticleStorage(CaveGame pg)
	{
		this.pg = pg;
		particleTess = new ParticleTessellator();

		particles = new HashMap<>();
		tickParticles = new HashMap<>();
	}

	public void tick()
	{
		count = 0;
		for (List<Particle> list : this.particles.values())
		{
			for (Particle p : list)
			{
				p.tick();

				count++;
			}

			list.removeIf(this::isDead);
		}

		tickParticles.clear();
	}

	private boolean isDead(Particle p)
	{
		return p.getDeathTime() != -1 && System.currentTimeMillis() >= p.getDeathTime() || p.shouldDie();
	}

	public void render()
	{
		subRender(particles);
		subRender(tickParticles);
	}

	private int count;

	public int count()
	{
		return count;
	}

	private void subRender(HashMap<Class<? extends Particle>, List<Particle>> map)
	{
		for (Class<? extends Particle> type : map.keySet())
		{
			List<Particle> list = map.get(type);

			if (list.isEmpty())
				continue;

			if (list.get(0).sort())
				QuickSort.sortEntitiesByDistance(list, CaveGame.getInstance().getCamera().getPosition());

			list.get(0).applyShader();

			ParticleTessellator tess = particleTess;
			tess.begin(list.size());

			for (Particle p : list)
			{
				p.applyInvidualShader();
				tess.pos(p.getX(), p.getY(), p.getZ()).color(p.getColor()).size(p.getSize()).endVertex();
			}

			tess.loadPos(0);
			tess.loadColor(1);
			tess.loadSize(2);
			tess.draw(Tessellator.POINTS);
			tess.disable(0, 1, 2);
		}
	}


	/* Particles */

	private void put(Particle p)
	{
		List<Particle> pl = particles.get(p.getClass());
		if (pl == null)
		{
			pl = new ArrayList<>();
			pl.add(p);
			particles.put(p.getClass(), pl);
		} else
		{
			pl.add(p);
		}
	}

	private void putTick(Particle p)
	{
		List<Particle> pl = tickParticles.get(p.getClass());
		if (pl == null)
		{
			pl = new ArrayList<>();
			pl.add(p);
			tickParticles.put(p.getClass(), pl);
		} else
		{
			pl.add(p);
		}
	}

	public HashMap<Class<? extends Particle>, List<Particle>> getMap()
	{
		return particles;
	}

	public void addParticle(Particle p)
	{
		put(p);
	}

	public void addBasicParticle(float x, float y, float z, float size, float r, float g, float b, float a)
	{
		put(new BasicParticle(new Vector3f(), new Vector3f(x, y, z), size, new Vector4f(r, g, b, a), 2000));
	}

	public void addBasicParticle(float x, float y, float z, float size, float r, float g, float b, float a, long lifeTime)
	{
		put(new BasicParticle(new Vector3f(), new Vector3f(x, y, z), size, new Vector4f(r, g, b, a), lifeTime));
	}

	/* FrameParticles */

	public void addTickParticle(Particle p)
	{
		putTick(p);
	}

	public void addBasicTickParticle(float x, float y, float z, float size, float r, float g, float b, float a)
	{
		putTick(new BasicParticle(new Vector3f(), new Vector3f(x, y, z), size, new Vector4f(r, g, b, a), -1));
	}

	public void testParticle(float x, float y, float z)
	{
		addBasicTickParticle(x, y, z, 0.05f, 0, 1, 1, 1);
	}

	public void testParticle(float x, float y, float z, long life)
	{
		addBasicParticle(x, y, z, 0.05f, 0, 1, 1, 1, life);
	}
}
