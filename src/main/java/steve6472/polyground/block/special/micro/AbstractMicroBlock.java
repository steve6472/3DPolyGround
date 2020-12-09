package steve6472.polyground.block.special.micro;

import org.joml.AABBf;
import org.joml.Vector3f;
import org.joml.Vector4i;
import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.blockdata.micro.AbstractMicroBlockData;
import steve6472.polyground.block.blockdata.IBlockData;
import steve6472.polyground.block.model.CubeHitbox;
import steve6472.polyground.block.model.elements.Bakery;
import steve6472.polyground.block.special.CustomBlock;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;

import java.util.ArrayList;
import java.util.List;

import static org.joml.Math.max;
import static org.joml.Math.min;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 28.11.2020
 * Project: CaveGame
 *
 ***********************/
public abstract class AbstractMicroBlock extends CustomBlock implements IBlockData
{
	public AbstractMicroBlock(JSONObject json)
	{
		super(json);
	}

	protected Vector4i getLookedAtPiece(World world, Player player, int x, int y, int z)
	{
		AbstractMicroBlockData data = (AbstractMicroBlockData) world.getData(x, y, z);
		if (data == null)
			return null;

		AABBf box = new AABBf();
		Vector3f res = new Vector3f();

		int cx = 0, cy = 0, cz = 0;
		float closestDistance = 10f;
		int face = 0;
		float inv = 1f / 16f;

		for (int i = 0; i < getSize(); i++)
		{
			for (int j = 0; j < getSize(); j++)
			{
				for (int k = 0; k < getSize(); k++)
				{
					if (data.grid[j][i + k * getSize()] != 0)
					{
						box.setMin(x + i * inv + getOffsetX() * inv, y + j * inv + getOffsetY() * inv, z + k * inv + getOffsetZ() * inv);
						box.setMax(x + i * inv + inv + getOffsetX() * inv, y + j * inv + inv + getOffsetY() * inv, z + k * inv + inv + getOffsetZ() * inv);

						if (intersectsAABB(player.viewDir, player.getCamera().getPosition(), box, res))
						{
							if (res.y < closestDistance)
							{
								closestDistance = res.y;
								cx = i;
								cy = j;
								cz = k;
								face = (int) res.z;
							}
						}
					}
				}
			}
		}

		if (closestDistance == 10f)
			return null;

		return new Vector4i(cx, cy, cz, face);
	}

	private static final Vector3f DIRFRAC = new Vector3f();

	private boolean intersectsAABB(Vector3f dir, Vector3f org, AABBf box, Vector3f res)
	{
		// r.dir is unit direction vector of ray
		DIRFRAC.x = 1.0f / dir.x;
		DIRFRAC.y = 1.0f / dir.y;
		DIRFRAC.z = 1.0f / dir.z;
		// lb is the corner of AABB with minimal coordinates - left bottom, rt is maximal corner
		// r.org is origin of ray
		float t1 = (box.minX - org.x) * DIRFRAC.x;
		float t2 = (box.maxX - org.x) * DIRFRAC.x;
		float t3 = (box.minY - org.y) * DIRFRAC.y;
		float t4 = (box.maxY - org.y) * DIRFRAC.y;
		float t5 = (box.minZ - org.z) * DIRFRAC.z;
		float t6 = (box.maxZ - org.z) * DIRFRAC.z;

		float tmin = max(max(min(t1, t2), min(t3, t4)), min(t5, t6));
		float tmax = min(min(max(t1, t2), max(t3, t4)), max(t5, t6));

		// if tmax < 0, ray (line) is intersecting AABB, but the whole AABB is behind us
		if (tmax < 0)
		{
			return false;
		}

		// if tmin > tmax, ray doesn't intersect AABB
		if (tmin > tmax)
		{
			return false;
		}

		float tminx = min(t1, t2);
		float tminy = min(t3, t4);
		float tminz = min(t5, t6);

		int axis = -1;
		if (tmin == tminx) axis = 0;
		if (tmin == tminy) axis = 1;
		if (tmin == tminz) axis = 2;

		//UP, DOWN, NORTH, EAST, SOUTH, WEST
		//0    1      2     3      4     5

		int face = 2; // north
		if (axis == 0)
		{
			if (tmin == t1) face = 4;
		}
		if (axis == 1)
		{
			if (tmin == t4) face = 0;
			if (tmin == t3) face = 1;
		}
		if (axis == 2)
		{
			if (tmin == t6) face = 3;
			if (tmin == t5) face = 5;
		}

		res.x = tmax;
		res.y = tmin;
		res.z = face;

		return true;
	}

	@Override
	public int createModel(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		if (modelLayer != ModelLayer.NORMAL)
			return 0;

		int tris = 0;
		buildHelper.setSubChunk(world.getSubChunkFromBlockCoords(x, y, z));

		AbstractMicroBlockData data = (AbstractMicroBlockData) world.getData(x, y, z);

		for (int i = 0; i < getSize(); i++)
		{
			for (int j = 0; j < getSize(); j++)
			{
				for (int k = 0; k < getSize(); k++)
				{
					int flags = Bakery.createFaceFlags(
						i != (getSize() - 1) && data.grid[j][(i + 1) + k * getSize()] != 0,
						k != (getSize() - 1) && data.grid[j][i + (k + 1) * getSize()] != 0,
						i != 0 && data.grid[j][(i - 1) + k * getSize()] != 0,
						k != 0 && data.grid[j][i + (k - 1) * getSize()] != 0,
						j != (getSize() - 1) && data.grid[j + 1][i + k * getSize()] != 0,
						j != 0 && data.grid[j - 1][i + k * getSize()] != 0
					);
					if (data.grid[j][i + k * getSize()] != 0)
					{
						tris += Bakery.coloredCube(i + getOffsetX(), j + getOffsetY(), k + getOffsetZ(), 1, 1, 1, data.grid[j][i + k * getSize()], flags);
					}
				}
			}
		}

		return tris;
	}

	protected boolean renderSelectedMicro(World world, BlockState state, int x, int y, int z)
	{
		return true;
	}

	@Override
	public CubeHitbox[] getHitbox(World world, BlockState state, int x, int y, int z)
	{
		AbstractMicroBlockData data = (AbstractMicroBlockData) world.getData(x, y, z);
		if (data == null)
			return new CubeHitbox[0];

		AABBf box = new AABBf();
		AABBf box_ = new AABBf();
		float inv = 1f / 16f;

		for (int i = 0; i < getSize(); i++)
		{
			for (int j = 0; j < getSize(); j++)
			{
				for (int k = 0; k < getSize(); k++)
				{
					if (data.grid[j][i + k * getSize()] != 0)
					{
						box_.setMin(i * inv + getOffsetX() * inv, j * inv + getOffsetY() * inv, k * inv + getOffsetZ() * inv);
						box_.setMax(i * inv + inv + getOffsetX() * inv, j * inv + inv + getOffsetY() * inv, k * inv + inv + getOffsetZ() * inv);
						box.union(box_);
					}
				}
			}
		}

		if (renderSelectedMicro(world, state, x, y, z))
		{
			Vector4i c = getLookedAtPiece(world, CaveGame.getInstance().getPlayer(), x, y, z);

			//TODO: use marching cubes or.. greedy cubes to generate hitbox

			if (c != null)
			{
				List<CubeHitbox> hitboxes = new ArrayList<>();
				hitboxes.add(new CubeHitbox(box));
				hitboxes.add(new CubeHitbox(
					new AABBf(
						c.x * inv + getOffsetX() * inv, c.y * inv + getOffsetY() * inv, c.z * inv + getOffsetZ() * inv,
						c.x * inv + inv + getOffsetX() * inv, c.y * inv + inv + getOffsetY() * inv, c.z * inv + inv + getOffsetZ() * inv)));

				return hitboxes.toArray(new CubeHitbox[0]);
			}
		}
		return new CubeHitbox[] {new CubeHitbox(box)};
	}

	protected int getSize()
	{
		return 16;
	}

	protected int getOffsetX()
	{
		return 8 - getSize() / 2;
	}

	protected int getOffsetY()
	{
		return 0;
	}

	protected int getOffsetZ()
	{
		return 8 - getSize() / 2;
	}
}
