package steve6472.polyground.world.interaction;

import org.joml.AABBf;
import org.joml.Intersectionf;
import org.joml.Math;
import org.joml.Vector2f;
import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.HitResult;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.model.CubeHitbox;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.Chunk;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 20.08.2019
 * Project: SJP
 *
 ***********************/
public class HitPicker
{
	private World world;
	final HitResult hitResult;

	public HitPicker(World world)
	{
		this.world = world;
		hr = new Vector2f();
		hitResult = new HitResult();
	}

	public float closest;

	private final Vector2f hr;

	public void tick(Player player, CaveGame game)
	{
		closest = Float.MAX_VALUE;
		hitResult.setHit(false);
		int ix = (int) Math.floor(player.getX());
		int iy = (int) Math.floor(player.getY());
		int iz = (int) Math.floor(player.getZ());

		Chunk[] chunks = new Chunk[9];
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				chunks[i + j * 3] = world.getChunkFromBlockPos(ix + i * 16 - 16, iz + j * 16 - 16);
			}
		}

		for (Chunk c : chunks)
		{
			if (c == null)
				continue;

			for (int i = ix - 5; i <= ix + 5; i++)
			{
				for (int j = iy - 5; j <= iy + 5; j++)
				{
					for (int k = iz - 5; k <= iz + 5; k++)
					{
						if (i < c.getX() * 16 || i >= c.getX() * 16 + 16 || k < c.getZ() * 16 || k >= c.getZ() * 16 + 16)
							continue;

						BlockState state = c.getWorld().getState(i, j, k);
						if (state.getBlock() != Block.AIR)
						{
							for (CubeHitbox t : state.getBlock().getHitbox(world, state, i, j, k))
							{
								if (!t.isHitbox())
									continue;

								if (Intersectionf.intersectRayAab(player.getX(), player.getY() + player.eyeHeight, player.getZ(), player.viewDir.x, player.viewDir.y, player.viewDir.z, t.getAabb().minX + i, t.getAabb().minY + j, t.getAabb().minZ + k, t.getAabb().maxX + i, t.getAabb().maxY + j, t.getAabb().maxZ + k, hr))
								{
									if (hr.x <= closest)
									{
										hitResult.setHit(true);
										closest = hr.x;
										hitResult.setBlockCoords(i, j, k);
										hitResult.setAabb(t.getAabb());
									}
								}
							}
						}
					}
				}
			}
		}

		hitResult.setHit(closest <= 4.3f);

		if (hitResult.isHit())
		{
			hitResult.setDistance(closest);
			hitResult.setPreciseCoords(player.viewDir.x * closest + player.getX(), player.viewDir.y * closest + player.getY() + player.eyeHeight, player.viewDir.z * closest + player.getZ());

			/* Side detection is gonna break after xyz > 2^8 or 2^10 */
			float epsion = 0.00003f;
			if (Math.abs(hitResult.getPx() - (hitResult.getAabb().maxX + hitResult.getX())) <= epsion)
				hitResult.setFace(EnumFace.NORTH);
			if (Math.abs(hitResult.getPx() - (hitResult.getAabb().minX + hitResult.getX())) <= epsion)
				hitResult.setFace(EnumFace.SOUTH);
			if (Math.abs(hitResult.getPy() - (hitResult.getAabb().maxY + hitResult.getY())) <= epsion)
				hitResult.setFace(EnumFace.UP);
			if (Math.abs(hitResult.getPy() - (hitResult.getAabb().minY + hitResult.getY())) <= epsion)
				hitResult.setFace(EnumFace.DOWN);
			if (Math.abs(hitResult.getPz() - (hitResult.getAabb().maxZ + hitResult.getZ())) <= epsion)
				hitResult.setFace(EnumFace.EAST);
			if (Math.abs(hitResult.getPz() - (hitResult.getAabb().minZ + hitResult.getZ())) <= epsion)
				hitResult.setFace(EnumFace.WEST);

			hitResult.setState(world.getState(hitResult.getX(), hitResult.getY(), hitResult.getZ()));

			for (CubeHitbox t : hitResult.getState().getBlock().getHitbox(world, hitResult.getState(), hitResult.getX(), hitResult.getY(), hitResult.getZ()))
			{
				if (!t.isHitbox() || !t.isVisible())
					continue;

				AABBf aabb = t.getAabb();


				MainRender.shaders.mainShader.bind(game.getCamera().getViewMatrix());

				if (game.options.renderBlockOutline)
					game.mainRender.stack.getLineTess().debugBox(hitResult.getX() - 0.001f + aabb.minX, hitResult.getY() - 0.001f + aabb.minY, hitResult.getZ() - 0.001f + aabb.minZ, hitResult.getX() + 0.001f + aabb.maxX, hitResult.getY() + 0.001f + aabb.maxY, hitResult.getZ() + 0.001f + aabb.maxZ);
			}
		}
	}

	public void setWorld(World world)
	{
		this.world = world;
	}

	public HitResult getHitResult()
	{
		return hitResult;
	}
}
