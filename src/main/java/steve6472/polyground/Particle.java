package steve6472.polyground;

import org.joml.Vector3f;
import org.joml.Vector4f;
import steve6472.sge.main.game.mixable.IMotion3f;
import steve6472.sge.main.game.mixable.IPosition3f;
import steve6472.sge.main.game.mixable.ITag;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.07.2019
 * Project: SJP
 *
 ***********************/
public abstract class Particle implements IMotion3f, IPosition3f, Comparable<Particle>, ITag
{
	private Vector3f motion, position;
	private float size;
	private Vector4f color;
	private final long deathTime;

	public boolean forcedDeath = false;

	public Particle(Vector3f motion, Vector3f position, float size, Vector4f color, long lifeTime)
	{
		this.motion = motion;
		this.position = position;
		this.size = size;
		this.color = color;
		if (lifeTime == -1)
		{
			deathTime = -1;
		} else
		{
			deathTime = System.currentTimeMillis() + lifeTime;
		}
	}

	public abstract void tick();

	public abstract void applyShader();

	public abstract void applyInvidualShader();

	public void die() {}

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

	public void setSize(float size)
	{
		this.size = size;
	}

	public Vector4f getColor()
	{
		return color;
	}

	public void setColor(Vector4f color)
	{
		this.color = color;
	}

	public long getDeathTime()
	{
		return deathTime;
	}

	public boolean shouldDie()
	{
		return forcedDeath;
	}

	public boolean sort()
	{
		return true;
	}

	@Override
	public int compareTo(Particle o)
	{
		return Math.round(o.getPosition().distance(getPosition()));
	}
}
