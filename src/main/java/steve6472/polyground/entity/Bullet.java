package steve6472.polyground.entity;

import org.joml.AABBf;
import org.joml.Vector3f;
import steve6472.polyground.CaveGame;
import steve6472.polyground.PolyUtil;
import steve6472.polyground.block.Block;
import steve6472.polyground.entity.interfaces.ICollideable;
import steve6472.polyground.entity.interfaces.IKillable;
import steve6472.polyground.entity.interfaces.ITickable;
import steve6472.polyground.entity.interfaces.IWorldContainer;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.sge.main.game.mixable.IMotion3f;
import steve6472.sge.main.game.mixable.IPosition3f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 11.04.2020
 * Project: CaveGame
 *
 ***********************/
public class Bullet implements ITickable, IKillable, ICollideable, IPosition3f, IMotion3f, IWorldContainer
{
	private static final float SIZE = 1f / 32f;
	private static final AABBf[] HITBOX = { new AABBf(-SIZE, -SIZE, -SIZE, SIZE, SIZE, SIZE) };

	boolean isDead;
	private final Vector3f position, motion;
	private World world;

	public Bullet()
	{
		position = new Vector3f(CaveGame.getInstance().getCamera().getPosition());
		motion = new Vector3f();
		PolyUtil.toDirectionalVector(CaveGame.getInstance().getCamera(), motion);
	}

	@Override
	public void tick()
	{
		getPosition().add(getMotion());
		world.getPg().particles.addBasicTickParticle(getX(), getY(), getZ(), 1f / 32f, 0.6f, 0.6f, 0.6f, 1.0f);

		if (getY() < -10 || getY() > 100)
			die();
	}

	@Override
	public void collide(SubChunk subChunk, int x, int y, int z)
	{
		subChunk.setBlock(x, y, z, Block.air);
		//TODO: spawn particles
		die();
	}

	@Override
	public AABBf[] getHitbox()
	{
		return HITBOX;
	}

	@Override
	public boolean isDead()
	{
		return isDead;
	}

	@Override
	public void setDead(boolean dead)
	{
		isDead = dead;
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
