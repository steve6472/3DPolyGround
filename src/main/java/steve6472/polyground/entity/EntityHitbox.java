package steve6472.polyground.entity;

import org.joml.AABBf;
import org.joml.Intersectionf;
import steve6472.polyground.AABBUtil;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.model.Cube;
import steve6472.polyground.world.World;
import steve6472.sge.main.game.mixable.IMotion3f;
import steve6472.sge.main.game.mixable.IPosition3f;

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

	/**
	 * @param world World entity is in
	 * @return isOnGround
	 */
	public boolean collideWithWorld(World world)
	{
		expand(motion.getMotionX(), motion.getMotionY(), motion.getMotionZ());

		return test(world, floor(expandedHitbox.minX) - floor(position.getX()) - 1, floor(expandedHitbox.minY) - floor(position.getY()) - 1, floor(expandedHitbox.minZ) - floor(position.getZ()) - 1, ceil(expandedHitbox.maxX) - floor(position.getX()) + 1, ceil(expandedHitbox.maxY) - floor(position.getY()) + 1, ceil(expandedHitbox.maxZ) - floor(position.getZ()) + 1);
	}

	private static int floor(float n)
	{
		return (int) Math.floor(n);
	}

	private static int ceil(float n)
	{
		return (int) Math.ceil(n);
	}

	private boolean test(World world, int sx, int sy, int sz, int ex, int ey, int ez)
	{
		int ix = (int) Math.floor(position.getX());
		int iy = (int) Math.floor(position.getY()) + 1;
		int iz = (int) Math.floor(position.getZ());

		float xaOrg = motion.getMotionX();
		float yaOrg = motion.getMotionY();
		float zaOrg = motion.getMotionZ();

		float xa = xaOrg, ya = yaOrg, za = zaOrg;

		for (int i = sx; i < ex; i++)
		{
			for (int j = sy; j < ey; j++)
			{
				for (int k = sz; k < ez; k++)
				{
					int x = ix + i, y = iy + j, z = iz + k;

					Block block;
					if ((block = world.getBlock(x, y, z)) != Block.air)
					{
						for (Cube t : block.getCubes(x, y, z))
						{
							if (!t.isCollisionBox())
								continue;

							if (Intersectionf.testAabAab(x + t.getAabb().minX, y + t.getAabb().minY, z + t.getAabb().minZ, x + t.getAabb().maxX, y + t.getAabb().maxY, z + t.getAabb().maxZ,
								expandedHitbox.minX, expandedHitbox.minY, expandedHitbox.minZ, expandedHitbox.maxX, expandedHitbox.maxY, expandedHitbox.maxZ))
							{
								temp.setMin(x + t.getAabb().minX, y + t.getAabb().minY, z + t.getAabb().minZ);
								temp.setMax(x + t.getAabb().maxX, y + t.getAabb().maxY, z + t.getAabb().maxZ);

								ya = AABBUtil.clipYCollide(getHitbox(), temp, ya);

								xa = AABBUtil.clipXCollide(getHitbox(), temp, xa);

								za = AABBUtil.clipZCollide(getHitbox(), temp, za);
							}
						}
					}
				}
			}
		}

		moveHitbox(0.0f, ya, 0.0f);
		moveHitbox(xa, 0.0F, 0.0F);
		moveHitbox(0.0F, 0.0F, za);

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

		position.setX((getHitbox().minX + getHitbox().maxX) / 2.0F);
		position.setY(getHitbox().minY + 0.0F);
		position.setZ((getHitbox().minZ + getHitbox().maxZ) / 2.0F);

		return yaOrg != ya && yaOrg < 0.0F;
	}
}
