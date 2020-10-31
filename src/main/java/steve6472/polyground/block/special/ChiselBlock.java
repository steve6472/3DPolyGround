package steve6472.polyground.block.special;

import org.joml.AABBf;
import org.joml.Vector3f;
import org.joml.Vector4i;
import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.ChiselBlockData;
import steve6472.polyground.block.blockdata.IBlockData;
import steve6472.polyground.block.model.CubeHitbox;
import steve6472.polyground.block.model.elements.Bakery;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.item.itemdata.BrushData;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

import java.util.ArrayList;
import java.util.List;

import static org.joml.Math.max;
import static org.joml.Math.min;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.09.2020
 * Project: CaveGame
 *
 ***********************/
public class ChiselBlock extends CustomBlock implements IBlockData
{
	public ChiselBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public BlockData createNewBlockEntity(BlockState state)
	{
		return new ChiselBlockData();
	}

	@Override
	public void onClick(BlockState state, World world, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getButton() == KeyList.RMB && click.getAction() == KeyList.PRESS)
		{
			ChiselBlockData data = (ChiselBlockData) world.getData(x, y, z);
			if (data == null)
				return;

			Vector4i c = getLookedAtPiece(world, player, x, y, z);

			if (c != null && player.holdsItem())
			{
				if (player.getItemDataInHand() instanceof BrushData bd)
				{
					int cx = c.x;
					int cy = c.y;
					int cz = c.z;
					data.grid[cy][cx + cz * 16] = bd.color;

					data.updateModel();
					world.getSubChunkFromBlockCoords(x, y, z).rebuild();
					return;

				}
			}
		}

		if (click.getButton() != KeyList.MMB && click.getAction() == KeyList.PRESS)
		{
			ChiselBlockData data = (ChiselBlockData) world.getData(x, y, z);
			if (data == null)
				return;

			Vector4i c = getLookedAtPiece(world, player, x, y, z);

			if (c != null && player.holdsItem() && player.getItemInHand().name().equals("chisel_tool"))
			{
				if (click.getButton() == KeyList.LMB)
				{
					data.grid[c.y][c.x + c.z * 16] = 0;
					data.pieceCount--;
					player.processNextBlockBreak = false;
				} else
				{
					EnumFace f = EnumFace.getFaces()[c.w];
					int cx = c.x + f.getXOffset();
					int cy = c.y + f.getYOffset();
					int cz = c.z + f.getZOffset();
					if (cx >= 0 && cx < 16 && cy >= 0 && cy < 16 && cz >= 0 && cz < 16)
					{
						data.grid[cy][cx + cz * 16] = 0x303030;
						data.pieceCount++;
					}
					player.processNextBlockPlace = false;
				}

				if (data.pieceCount == 0)
				{
					world.setBlock(Block.AIR, x, y, z);
					world.getSubChunkFromBlockCoords(x, y, z).rebuild();
					return;
				}

				data.updateModel();
				world.getSubChunkFromBlockCoords(x, y, z).rebuild();
			}
		}
	}

	@Override
	public boolean isPickable(BlockState state, Player player)
	{
		return !player.holdsItem() || !player.getItemInHand().name().equals("chisel_tool");
	}

	private Vector4i getLookedAtPiece(World world, Player player, int x, int y, int z)
	{
		ChiselBlockData data = (ChiselBlockData) world.getData(x, y, z);
		if (data == null)
			return null;

		AABBf box = new AABBf();
		Vector3f res = new Vector3f();

		int cx = 0, cy = 0, cz = 0;
		float closestDistance = 10f;
		int face = 0;

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					if (data.grid[j][i + k * 16] != 0)
					{
						box.setMin(x + i * 1f / 16f, y + j * 1f / 16f, z + k * 1f / 16f);
						box.setMax(x + i * 1f / 16f + 1f / 16f, y + j * 1f / 16f + 1f / 16f, z + k * 1f / 16f + 1f / 16f);

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

	private boolean intersectsAABB(Vector3f dir, Vector3f org, AABBf box, Vector3f res)
	{
		// r.dir is unit direction vector of ray
		Vector3f dirfrac = new Vector3f();

		dirfrac.x = 1.0f / dir.x;
		dirfrac.y = 1.0f / dir.y;
		dirfrac.z = 1.0f / dir.z;
		// lb is the corner of AABB with minimal coordinates - left bottom, rt is maximal corner
		// r.org is origin of ray
		float t1 = (box.minX - org.x) * dirfrac.x;
		float t2 = (box.maxX - org.x) * dirfrac.x;
		float t3 = (box.minY - org.y) * dirfrac.y;
		float t4 = (box.maxY - org.y) * dirfrac.y;
		float t5 = (box.minZ - org.z) * dirfrac.z;
		float t6 = (box.maxZ - org.z) * dirfrac.z;

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

		ChiselBlockData data = (ChiselBlockData) world.getData(x, y, z);

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					int flags = Bakery.createFaceFlags(
						i != 15 && data.grid[j][(i + 1) + k * 16] != 0,
						k != 15 && data.grid[j][i + (k + 1) * 16] != 0,
						i != 0 && data.grid[j][(i - 1) + k * 16] != 0,
						k != 0 && data.grid[j][i + (k - 1) * 16] != 0,
						j != 15 && data.grid[j + 1][i + k * 16] != 0,
						j != 0 && data.grid[j - 1][i + k * 16] != 0
					);
					if (data.grid[j][i + k * 16] != 0)
					{
						tris += Bakery.coloredCube_1x1(i, j, k, data.grid[j][i + k * 16], flags);
					}
				}
			}
		}

		return tris;
	}

	@Override
	public CubeHitbox[] getHitbox(World world, BlockState state, int x, int y, int z)
	{
		ChiselBlockData data = (ChiselBlockData) world.getData(x, y, z);
		if (data == null)
			return new CubeHitbox[0];

		AABBf box = new AABBf();
		AABBf box_ = new AABBf();

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					if (data.grid[j][i + k * 16] != 0)
					{
						box_.setMin(i * 1f / 16f, j * 1f / 16f, k * 1f / 16f);
						box_.setMax(i * 1f / 16f + 1f / 16f, j * 1f / 16f + 1f / 16f, k * 1f / 16f + 1f / 16f);
						box.union(box_);
					}
				}
			}
		}

		Vector4i c = getLookedAtPiece(world, CaveGame.getInstance().getPlayer(), x, y, z);

		//TODO: use marching cubes or.. greedy cubes to generate hitbox

		if (c != null)
		{
			List<CubeHitbox> hitboxes = new ArrayList<>();
			hitboxes.add(new CubeHitbox(box));
			hitboxes.add(new CubeHitbox(
				new AABBf(
					c.x * 1f / 16f, c.y * 1f / 16f, c.z * 1f / 16f,
					c.x * 1f / 16f + 1f / 16f, c.y * 1f / 16f + 1f / 16f, c.z * 1f / 16f + 1f / 16f)));

			return hitboxes.toArray(new CubeHitbox[0]);
		}
		return new CubeHitbox[] {new CubeHitbox(box)};
	}
}
