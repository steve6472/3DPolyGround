package steve6472.polyground.entity;

import org.joml.AABBf;
import org.joml.Vector3f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.11.2020
 * Project: CaveGame
 *
 ***********************/
public abstract class HitboxEntityBase extends EntityBase
{
	private final EntityHitbox entityHitbox;

	public HitboxEntityBase(float w, float h, float d)
	{
		entityHitbox = new EntityHitbox(w, h, d, this, this);
	}

	public EntityHitbox getEntityHitbox()
	{
		return entityHitbox;
	}

	public AABBf getHitbox()
	{
		return entityHitbox.getHitbox();
	}

	public void updateHitbox()
	{
		entityHitbox.setHitbox(getX(), getY() + 0.25f / 2f, getZ());
	}

	@Override
	public void setPosition(Vector3f position)
	{
		super.setPosition(position);
		updateHitbox();
	}

	@Override
	public void setPosition(float x, float y, float z)
	{
		super.setPosition(x, y, z);
		updateHitbox();
	}

	@Override
	public void addPosition(Vector3f position)
	{
		super.addPosition(position);
		updateHitbox();
	}

	@Override
	public void addPosition(float x, float y, float z)
	{
		super.addPosition(x, y, z);
		updateHitbox();
	}

	@Override
	public void setX(float x)
	{
		super.setX(x);
		updateHitbox();
	}

	@Override
	public void setY(float y)
	{
		super.setY(y);
		updateHitbox();
	}

	@Override
	public void setZ(float z)
	{
		super.setZ(z);
		updateHitbox();
	}
}
