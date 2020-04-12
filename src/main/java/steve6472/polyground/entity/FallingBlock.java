package steve6472.polyground.entity;

import steve6472.polyground.CaveGame;
import steve6472.polyground.block.Block;
import steve6472.polyground.registry.BlockRegistry;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 04.09.2019
 * Project: SJP
 *
 ***********************/
public class FallingBlock extends EntityBase
{
	EntityHitbox hitbox;
	private boolean isDead = false;

	public FallingBlock()
	{
		hitbox = new EntityHitbox(0.4999f, 0.4999f, 0.4999f, this, this);
	}

	@Override
	public void tick()
	{
		/* Apply gravity */
		setY(getY() - 0.4999f);

		getMotion().y -= 0.004f;

		getPosition().add(getMotion());

		if (getMotion().y > 9.5f)
			getMotion().y = 9.5f;

		boolean isOnBlock = hitbox.collideWithWorld(CaveGame.getInstance().getWorld());

		getMotion().x *= 0.91f;
		getMotion().y *= 0.98f;
		getMotion().z *= 0.91f;

		hitbox.setHitbox(getX(), getY() + 0.4999f, getZ());
		setY(getY() + 0.4999f);

		if (isOnBlock)
		{
			isDead = true;
			World world = CaveGame.getInstance().world;
			world.setBlock((int) Math.floor(getX()), (int) (getY() - 0.4999f), (int) Math.floor(getZ()), BlockRegistry.getBlockByName("gravel").getId(), b -> b == Block.air.getId());
		}

		if (getY() < -16)
			isDead = true;
	}

	@Override
	public boolean isDead()
	{
		return isDead;
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
}
