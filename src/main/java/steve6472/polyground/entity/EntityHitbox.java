package steve6472.polyground.entity;

import org.joml.AABBf;
import org.joml.Intersectionf;
import steve6472.polyground.AABBUtil;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.model.CubeHitbox;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.World;
import steve6472.sge.main.game.mixable.IMotion3f;
import steve6472.sge.main.game.mixable.IPosition3f;
import steve6472.sge.main.util.Triple;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 25.09.2019
 * Project: SJP
 *
 ***********************/
public class EntityHitbox
{
	private final AABBf hitbox, expandedHitbox, temp;
	private final float w, h, d;
	private final IPosition3f position;
	private final IMotion3f motion;

	public EntityHitbox(float w, float h, float d, IPosition3f position, IMotion3f motion)
	{
		hitbox = new AABBf();
		expandedHitbox = new AABBf();
		temp = new AABBf();
		this.w = w;
		this.h = h;
		this.d = d;
		this.position = position;
		this.motion = motion;

		setHitbox(position.getX(), position.getY(), position.getZ());
	}

	public AABBf getHitbox()
	{
		return hitbox;
	}

	public AABBf getExpandedHitbox()
	{
		return expandedHitbox;
	}

	public void setHitbox(float x, float y, float z)
	{
		hitbox.setMax(w + x, h + y, d + z);
		hitbox.setMin(-w + x, -h + y, -d + z);
	}

	public void moveHitbox(float dx, float dy, float dz)
	{
		hitbox.minX += dx;
		hitbox.maxX += dx;
		hitbox.minY += dy;
		hitbox.maxY += dy;
		hitbox.minZ += dz;
		hitbox.maxZ += dz;
	}

	public void expand(float x, float y, float z)
	{
		expandedHitbox.minX = x < 0.0f ? hitbox.minX + x : hitbox.minX;
		expandedHitbox.maxX = x > 0.0f ? hitbox.maxX + x : hitbox.maxX;
		expandedHitbox.minY = y < 0.0f ? hitbox.minY + y : hitbox.minY;
		expandedHitbox.maxY = y > 0.0f ? hitbox.maxY + y : hitbox.maxY;
		expandedHitbox.minZ = z < 0.0f ? hitbox.minZ + z : hitbox.minZ;
		expandedHitbox.maxZ = z > 0.0f ? hitbox.maxZ + z : hitbox.maxZ;
	}

	public boolean stepUp(World world)
	{
		return testStepUp(world,
			floor(expandedHitbox.minX) - floor(position.getX()) - 1,
			floor(expandedHitbox.minY) - floor(position.getY()) - 1,
			floor(expandedHitbox.minZ) - floor(position.getZ()) - 1,
			ceil(expandedHitbox.maxX) - floor(position.getX()) + 1,
			ceil(expandedHitbox.maxY) - floor(position.getY()) + 1,
			ceil(expandedHitbox.maxZ) - floor(position.getZ()) + 1);
	}

	/**
	 * @param world World entity is in
	 * @return isOnGround
	 */
	public boolean collideWithWorld(World world)
	{
		return test(world, null,
			floor(expandedHitbox.minX) - floor(position.getX()) - 1,
			floor(expandedHitbox.minY) - floor(position.getY()) - 1,
			floor(expandedHitbox.minZ) - floor(position.getZ()) - 1,
			ceil(expandedHitbox.maxX) - floor(position.getX()) + 1,
			ceil(expandedHitbox.maxY) - floor(position.getY()) + 1,
			ceil(expandedHitbox.maxZ) - floor(position.getZ()) + 1);
	}

	public boolean collideWithWorld(World world, EntityBase entity)
	{
		return test(world, entity,
			floor(expandedHitbox.minX) - floor(position.getX()) - 1,
			floor(expandedHitbox.minY) - floor(position.getY()) - 1,
			floor(expandedHitbox.minZ) - floor(position.getZ()) - 1,
			ceil(expandedHitbox.maxX) - floor(position.getX()) + 1,
			ceil(expandedHitbox.maxY) - floor(position.getY()) + 1,
			ceil(expandedHitbox.maxZ) - floor(position.getZ()) + 1);
	}

	private static int floor(float n)
	{
		return (int) Math.floor(n);
	}

	private static int ceil(float n)
	{
		return (int) Math.ceil(n);
	}

	private boolean testStepUp(World world, int sx, int sy, int sz, int ex, int ey, int ez)
	{
		if (!CaveGame.getInstance().getPlayer().isOnGround)
			return false;

		int ix = (int) Math.floor(position.getX());
		int iy = (int) Math.floor(position.getY() + 0.005f);
		int iz = (int) Math.floor(position.getZ());

		float stepUp = 0;
		boolean canStep = true;

		for (int i = sx; i < ex; i++)
		{
			for (int j = sy; j < ey; j++)
			{
				for (int k = sz; k < ez; k++)
				{
					int x = ix + i, y = iy + j, z = iz + k;

					BlockState state;
					if ((state = world.getState(x, y, z)) != Block.AIR.getDefaultState())
					{
						for (CubeHitbox t : state.getBlock().getHitbox(world, state, x, y, z))
						{
							if (!t.isCollisionBox())
								continue;

							if (Intersectionf.testAabAab(x + t.getAabb().minX, y + t.getAabb().minY, z + t.getAabb().minZ, x + t.getAabb().maxX, y + t.getAabb().maxY, z + t.getAabb().maxZ,
								expandedHitbox.minX, expandedHitbox.minY, expandedHitbox.minZ, expandedHitbox.maxX, expandedHitbox.maxY, expandedHitbox.maxZ))
							{
								float f = Math.abs(hitbox.minY - (y + t.getAabb().maxY));
								if (f > 0f && f <= 0.6f)
								{
									stepUp = f;
								} else
								{
									canStep = false;
								}
							}
						}
					}
				}
			}
		}

		if (stepUp <= 0.0f)
			return false;

		if (!canStep)
			return false;

		moveHitbox(0, stepUp, 0);

		position.setY(getHitbox().minY);
		return true;
	}

	private boolean test(World world, EntityBase entity, int sx, int sy, int sz, int ex, int ey, int ez)
	{
		int ix = (int) Math.floor(position.getX());
		int iy = (int) Math.floor(position.getY());
		int iz = (int) Math.floor(position.getZ());

		float xaOrg = motion.getMotionX();
		float yaOrg = motion.getMotionY();
		float zaOrg = motion.getMotionZ();

		float xa = xaOrg, ya = yaOrg, za = zaOrg;

		List<BlockState> queuedEntityCollision = new ArrayList<>();
		List<Triple<Integer, Integer, Integer>> queuedEntityCollisionPos = new ArrayList<>();

		for (int i = sx; i < ex; i++)
		{
			for (int j = sy; j < ey; j++)
			{
				for (int k = sz; k < ez; k++)
				{
					int x = ix + i, y = iy + j, z = iz + k;

					BlockState state;
					if ((state = world.getState(x, y, z)) != Block.AIR.getDefaultState())
					{
						for (CubeHitbox t : state.getBlock().getHitbox(world, state, x, y, z))
						{
							if (!t.isCollisionBox())
								continue;

							if (Intersectionf.testAabAab(x + t.getAabb().minX, y + t.getAabb().minY, z + t.getAabb().minZ, x + t.getAabb().maxX, y + t.getAabb().maxY, z + t.getAabb().maxZ,
								expandedHitbox.minX, expandedHitbox.minY, expandedHitbox.minZ, expandedHitbox.maxX, expandedHitbox.maxY, expandedHitbox.maxZ))
							{
								temp.setMin(x + t.getAabb().minX, y + t.getAabb().minY, z + t.getAabb().minZ);
								temp.setMax(x + t.getAabb().maxX, y + t.getAabb().maxY, z + t.getAabb().maxZ);

								if (entity != null)
								{
									queuedEntityCollision.add(state);
									queuedEntityCollisionPos.add(new Triple<>(x, y, z));
								}

								ya = AABBUtil.clipYCollide(getHitbox(), temp, ya);

								xa = AABBUtil.clipXCollide(getHitbox(), temp, xa);

								za = AABBUtil.clipZCollide(getHitbox(), temp, za);
							}
						}
					}
				}
			}
		}

		moveHitbox(xa, ya, za);

		if (xaOrg != xa)
		{
			motion.setMotionX(0.0f);
		}

		if (yaOrg != ya)
		{
			motion.setMotionY(0.0f);
		}

		if (zaOrg != za)
		{
			motion.setMotionZ(0.0f);
		}

		position.setPosition((getHitbox().minX + getHitbox().maxX) / 2.0F, getHitbox().minY, (getHitbox().minZ + getHitbox().maxZ) / 2.0F);

		for (int i = 0; i < queuedEntityCollision.size(); i++)
		{
			BlockState state = queuedEntityCollision.get(i);
			Triple<Integer, Integer, Integer> pos = queuedEntityCollisionPos.get(i);
			state.getBlock().entityCollision(entity, world, state, pos.getA(), pos.getB(), pos.getC());
		}

		return yaOrg != ya && yaOrg < 0.0F;
	}
}
