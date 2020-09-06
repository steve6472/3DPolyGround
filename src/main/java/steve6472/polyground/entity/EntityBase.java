package steve6472.polyground.entity;

import org.joml.Vector3f;
import steve6472.polyground.entity.interfaces.IRotation;
import steve6472.sge.main.game.mixable.IMotion3f;
import steve6472.sge.main.game.mixable.IPosition3f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 04.09.2019
 * Project: SJP
 *
 ***********************/
public abstract class EntityBase implements IPosition3f, IMotion3f, IRotation
{
	private final Vector3f position, motion, pivotPoint, rotations;

	public EntityBase()
	{
		position = new Vector3f();
		motion = new Vector3f();
		pivotPoint = new Vector3f();
		rotations = new Vector3f();
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

	@Override
	public Vector3f getPivotPoint()
	{
		return pivotPoint;
	}

	@Override
	public Vector3f getRotations()
	{
		return rotations;
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
