package steve6472.polyground.gfx.particle;

import steve6472.polyground.CaveGame;
import steve6472.polyground.block.BlockAtlas;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.gfx.particle.particles.BreakParticle;
import steve6472.polyground.tessellators.BreakParticleTessellator;
import steve6472.sge.gfx.Tessellator;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.07.2019
 * Project: SJP
 *
 ***********************/
public class BreakParticleStorage
{
	List<BreakParticle> particles;

	private final BreakParticleTessellator particleTess;
	private final MainRender mainRender;

	public BreakParticleStorage(MainRender mainRender)
	{
		particleTess = new BreakParticleTessellator(1000000);
		this.mainRender = mainRender;

		particles = new ArrayList<>();
	}

	public void tick()
	{
		count = 0;

		particles.removeIf(p -> {
			boolean isDead = isDead(p);
			if (!isDead)
			{
				p.tick();
				count++;
			}

			return isDead;
		});
	}

	private boolean isDead(BreakParticle p)
	{
		return (p.getDeathTime() != -1 && System.currentTimeMillis() >= p.getDeathTime()) || p.shouldDie();
	}

	public void render()
	{
		if (particles.isEmpty())
			return;

		MainRender.shaders.breakParticleShader.bind(CaveGame.getInstance().getCamera().getViewMatrix());
		BlockAtlas.getAtlas().getSprite().bind(0);

		BreakParticleTessellator tess = particleTess;
		tess.begin(particles.size());

		for (BreakParticle p : particles)
		{
			if (mainRender.frustum.insideFrsutum(p.getX() - p.getSize(), p.getY() - p.getSize(), p.getZ() - p.getSize(), p.getX() + p.getSize(), p.getY() + p.getSize(), p.getZ() + p.getSize()))
				tess.pos(p.getX(), p.getY(), p.getZ()).uv(p.getUv()).color(p.getColor()).size(p.getSize()).endVertex();
		}

		tess.loadPos(0);
		tess.loadUv(1);
		tess.loadColor(2);
		tess.loadSize(3);
		tess.draw(Tessellator.POINTS);
		tess.disable(0, 1, 2, 3);
	}

	private int count;

	public int count()
	{
		return count;
	}

	public void addParticle(BreakParticle p)
	{
		particles.add(p);
	}
}
