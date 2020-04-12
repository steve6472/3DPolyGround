package steve6472.polyground.entity.parkour;

import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import steve6472.polyground.CaveGame;
import steve6472.polyground.entity.EntityHitbox;
import steve6472.sge.main.game.mixable.IMotion3f;
import steve6472.sge.main.game.mixable.IPosition3f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.03.2020
 * Project: CaveGame
 *
 ***********************/
public class ParkourEntity implements IPosition3f, IMotion3f
{
	private static final double RAD_90 = Math.toRadians(90);

	private final Vector3f position, motion;
	private final Brain brain;
	private final EntityHitbox hitbox;
	private boolean isOnBlock;

	private boolean isDead;
	private boolean goalReached;
	private int diedAtStep;
	public int currentCheckpoint;
	public float distanceToLastCheckpoint;
	public float yToLastCheckpoint;

	private float closest = Float.MAX_VALUE;

	public ParkourEntity()
	{
		position = new Vector3f(ParkourTest.START_POSITION);
		motion = new Vector3f();
		brain = new Brain();
		hitbox = new EntityHitbox(0.3f, 0.9f, 0.3f, this, this);
	}

	public ParkourEntity(Brain brain)
	{
		position = new Vector3f(ParkourTest.START_POSITION);
		motion = new Vector3f();
		this.brain = brain;
		hitbox = new EntityHitbox(0.3f, 0.9f, 0.3f, this, this);
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

	public void tick()
	{
		Instruction ins = brain.nextInstruction();
		if (ins == null)
		{
			diedAtStep = brain.getPassedInstructions();
			isDead = true;
			return;
		}

		if (currentCheckpoint == ParkourTest.checkpoints.size() - 1)
		{
			Vector4f c = ParkourTest.checkpoints.get(currentCheckpoint);
			if (getPosition().distance(c.x, c.y, c.z) <= c.w)
			{
				diedAtStep = brain.getPassedInstructions();
				isDead = true;
				goalReached = true;
				return;
			}
		}

		if (ParkourTest.checkpoints.size() > currentCheckpoint)
		{
			Vector4f v = ParkourTest.checkpoints.get(currentCheckpoint);
			if (getPosition().distance(new Vector3f(v.x, v.y, v.z)) <= v.w)
				currentCheckpoint++;
		}

		if (ParkourTest.checkpoints.size() > currentCheckpoint)
		{
			Vector4f v = ParkourTest.checkpoints.get(currentCheckpoint);
			distanceToLastCheckpoint = new Vector2f(getPosition().x, getPosition().z).distance(new Vector2f(v.x, v.z));
		}

		if (getPosition().y < 0.9f)
		{
			diedAtStep = brain.getPassedInstructions();
			isDead = true;
			return;
		}


		if (isDead)
			return;

		double xa = 0, za = 0;
		double speed = getPosition().y <= 0 || isOnBlock ? 0.02d : 0.005d;

		if (ins.forward >= ParkourTest.PRESS_THRESHOLD)
			xa += 1;
		if (ins.left >= ParkourTest.PRESS_THRESHOLD)
			za += -1;
		if (ins.backwards >= ParkourTest.PRESS_THRESHOLD)
			xa += -1;
		if (ins.right >= ParkourTest.PRESS_THRESHOLD)
			za += 1;
		if (ins.jump <= ParkourTest.JUMP_PRESS_THRESHOLD && isOnBlock)
			getMotion().y = 0.13f;
		if (ins.sprint >= ParkourTest.PRESS_THRESHOLD)
			speed *= 1.5d;

		move(xa, za, speed, ins.yaw);

		if (!isOnBlock)
			getMotion().y -= 0.005f;

		getPosition().add(getMotion());

		if (getMotion().y > 9.5f)
			getMotion().y = 9.5f;

		isOnBlock = hitbox.collideWithWorld(CaveGame.getInstance().getWorld());

		getMotion().x *= 0.91f;
		getMotion().y *= 0.98f;
		getMotion().z *= 0.91f;

		if (isOnBlock)
		{
			getMotion().mul(0.8f);
		}

		if (getPosition().y < 0)
			getPosition().y = 0;

		updateHitbox();
	}

	private void move(double xa, double za, double speed, double yaw)
	{
		double dist = xa * xa + za * za;
		if (dist < 0.01F)
		{
			return;
		}
		dist = speed / (float) Math.sqrt(dist);
		xa *= dist;
		za *= dist;

		double sin = Math.sin(yaw + RAD_90);
		double cos = Math.cos(yaw + RAD_90);

		getMotion().x += xa * cos + za * sin;
		getMotion().z += za * cos - xa * sin;
	}

	public void updateHitbox()
	{
		hitbox.setHitbox(getX(), getY() + 0.9f, getZ());
	}

	public EntityHitbox getHitbox()
	{
		return hitbox;
	}

	public boolean isDead()
	{
		return isDead;
	}

	public boolean goalReached()
	{
		return goalReached;
	}

	public Brain getBrain()
	{
		return brain;
	}

	public int getDiedAtStep()
	{
		return diedAtStep;
	}
}
