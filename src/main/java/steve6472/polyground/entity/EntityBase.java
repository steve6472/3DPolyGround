package steve6472.polyground.entity;

import org.joml.Vector3f;
import steve6472.sge.main.game.mixable.IMotion3f;
import steve6472.sge.main.game.mixable.IPosition3f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 04.09.2019
 * Project: SJP
 *
 ***********************/
public abstract class EntityBase implements IPosition3f, IMotion3f
{
	private Vector3f position, motion;

	public EntityBase()
	{
		position = new Vector3f();
		motion = new Vector3f();
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

	public abstract boolean isDead();

	public abstract String getName();

	public boolean sort()
	{
		return true;
	}

	public void tick()
	{
	}
}
