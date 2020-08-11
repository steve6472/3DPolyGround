package steve6472.polyground.gfx.particle.particles;

import org.joml.Vector3f;
import org.joml.Vector4f;
import steve6472.sge.main.game.mixable.IMotion3f;
import steve6472.sge.main.game.mixable.IPosition3f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.07.2019
 * Project: SJP
 *
 ***********************/
public class BreakParticle implements IMotion3f, IPosition3f
{
	private final Vector3f motion, position;
	private final Vector3f color;
	private final Vector4f uv;
	private float size;
	private final long deathTime;
	long l, d;

	int life;

	public BreakParticle(Vector3f motion, Vector3f position, float size, Vector4f uv, Vector3f color, long lifeTime)
	{
		this.motion = motion;
		this.position = position;
		this.size = size;
		this.color = color;
		this.uv = uv;
		if (lifeTime == -1)
		{
			deathTime = -1;
		} else
		{
			deathTime = System.currentTimeMillis() + lifeTime;
		}
		d = System.currentTimeMillis();
		l = System.currentTimeMillis() + lifeTime;
	}

	float growingSpeed;

	public void tick()
	{
		life++;
		setSize(getSize() + growingSpeed);

		if (getSize() < 0)
			setSize(0);

		getPosition().add(new Vector3f(getMotion()).mul((float) (System.currentTimeMillis() - d) / (float) (l - d)));
	}

	public boolean shouldDie()
	{
		return getSize() <= 0 || l < System.currentTimeMillis();
	}

	public BreakParticle setGrowingSpeed(float growingSpeed)
	{
		this.growingSpeed = growingSpeed;
		return this;
	}

	public void setSize(float size)
	{
		this.size = size;
	}

	@Override
	public Vector3f getMotion()
	{
		return motion;
	}

	@Override
	public Vector3f getPosition()
	{
		return position;
	}

	public float getSize()
	{
		return size;
	}

	public Vector4f getUv()
	{
		return uv;
	}

	public Vector3f getColor()
	{
		return color;
	}

	public long getDeathTime()
	{
		return deathTime;
	}
}
