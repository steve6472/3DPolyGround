package steve6472.polyground.entity;

import steve6472.polyground.CaveGame;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.interfaces.IKillable;
import steve6472.polyground.entity.interfaces.IRenderable;
import steve6472.polyground.entity.interfaces.ITickable;
import steve6472.polyground.entity.interfaces.IWorldContainer;
import steve6472.polyground.gfx.DynamicEntityModel;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 04.09.2019
 * Project: SJP
 *
 ***********************/
public class FallingBlock extends EntityBase implements IRenderable, IWorldContainer, ITickable, IKillable
{
	private final BlockState state;
	private final int x, y, z;
	private World world;
	EntityHitbox hitbox;
	private boolean isDead = false;
	private boolean canFall;
	private long delayTime;

	public FallingBlock(BlockState state, int x, int y, int z)
	{
		this.state = state;
		this.x = x;
		this.y = y;
		this.z = z;
		setPosition(x + 0.5f, y + 0.5f, z + 0.5f);
		hitbox = new EntityHitbox(0.4999f, 0.4999f, 0.4999f, this, this);
		canFall = true;
	}

	@Override
	public void tick()
	{
		boolean isOnBlock = false;

		/* Apply gravity */
		if (canFall)
		{
			setY(getY() - 0.4999f);

			getMotion().y -= 0.004f;

			getPosition().add(getMotion());

			if (getMotion().y > 9.5f)
				getMotion().y = 9.5f;

			hitbox.expand(getMotionX(), getMotionY(), getMotionZ());
			isOnBlock = hitbox.collideWithWorld(CaveGame.getInstance().getWorld());

			getMotion().x *= 0.91f;
			getMotion().y *= 0.98f;
			getMotion().z *= 0.91f;

			hitbox.setHitbox(getX(), getY() + 0.4999f, getZ());
			setY(getY() + 0.4999f);
		}

		if (isOnBlock || !canFall && delayTime == 0)
		{
			canFall = false;
			setPosition(getX(), (int) (getY()) + 0.5f, getZ());
			delayTime = System.currentTimeMillis();
			getWorld().setState(state, (int) Math.floor(getX()), (int) (getY() - 0.4999f), (int) Math.floor(getZ()));
		}

		if (!canFall && System.currentTimeMillis() - delayTime >= 60)
			setDead(true);

		if (getY() < -16)
			isDead = true;
	}

	@Override
	public boolean isDead()
	{
		return isDead;
	}

	@Override
	public void setDead(boolean dead)
	{
		this.isDead = dead;
	}

	@Override
	public boolean sort()
	{
		return false;
	}

	@Override
	public String getName()
	{
		return "falling_block";
	}

	@Override
	public void render()
	{
		state.getBlockModel(getWorld(), x, y, z).getModel().render(CaveGame.getInstance().getCamera().getViewMatrix(), DynamicEntityModel.MAT.identity().translate(getX() - 0.5f, getY() - 0.5f, getZ() - 0.5f));
	}

	@Override
	public void setWorld(World world)
	{
		this.world = world;
	}

	@Override
	public World getWorld()
	{
		return world;
	}
}
