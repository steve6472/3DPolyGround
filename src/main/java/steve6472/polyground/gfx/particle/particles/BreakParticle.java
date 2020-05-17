package steve6472.polyground.gfx.particle.particles;

import org.joml.Vector3f;
import org.joml.Vector4f;
import steve6472.polyground.CaveGame;
import steve6472.polyground.Particle;
import steve6472.polyground.block.BlockTextureHolder;
import steve6472.polyground.gfx.MainRender;
import steve6472.sge.main.game.Tag;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.07.2019
 * Project: SJP
 *
 ***********************/
public class BreakParticle extends Particle
{
	private List<Tag> tags;
	long l, d;

	int life;

	public BreakParticle(Vector3f motion, Vector3f position, float size, Vector4f color, long lifeTime)
	{
		super(motion, position, size, color, -1);
		d = System.currentTimeMillis();
		l = System.currentTimeMillis() + lifeTime;
		tags = new ArrayList<>();
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
		MainRender.shaders.breakParticleShader.bind(CaveGame.getInstance().getCamera().getViewMatrix());
		BlockTextureHolder.getAtlas().getSprite().bind(0);
	}

	@Override
	public void applyInvidualShader()
	{

	}

	@Override
	public List<Tag> getTags()
	{
		return tags;
	}
}
