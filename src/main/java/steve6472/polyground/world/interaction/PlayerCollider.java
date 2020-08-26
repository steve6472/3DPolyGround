package steve6472.polyground.world.interaction;

import org.joml.AABBf;
import org.joml.Intersectionf;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.model.Cube;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.World;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.08.2019
 * Project: SJP
 *
 ***********************/
public class PlayerCollider
{
	private static void check(World world, int x, int y, int z, List<AABBf> aabbs, AABBf hitbox)
	{
		BlockState state;
		if ((state = world.getState(x, y, z)) != Block.air.getDefaultState())
		{
			for (Cube t : state.getBlockModel(world, x, y, z).getCubes())
			{
				if (!t.isCollisionBox())
					continue;

				if (Intersectionf.testAabAab(x + t.getAabb().minX, y + t.getAabb().minY, z + t.getAabb().minZ, x + t.getAabb().maxX, y + t.getAabb().maxY, z + t.getAabb().maxZ,

					hitbox.minX, hitbox.minY, hitbox.minZ, hitbox.maxX, hitbox.maxY, hitbox.maxZ))
				{
					AABBf a = new AABBf(x + t.getAabb().minX, y + t.getAabb().minY, z + t.getAabb().minZ, x + t.getAabb().maxX, y + t.getAabb().maxY, z + t.getAabb().maxZ);
					aabbs.add(a);
				}
			}
		}
	}

	public static void checkPlayerBlockCollisions(World world, List<AABBf> aabbs, AABBf expandedHitbox, float x, float y, float z)
	{
		int ix = (int) Math.floor(x), iy = (int) Math.floor(y) + 1, iz = (int) Math.floor(z);

		for (int i = -1; i < 2; i++)
		{
			for (int j = -1; j < 2; j++)
			{
				for (int k = -1; k < 2; k++)
				{
					check(world, ix + i, iy + j, iz + k, aabbs, expandedHitbox);
				}
			}
		}
	}
}
