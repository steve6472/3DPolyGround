package steve6472.polyground.block.special;

import org.joml.AABBf;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4i;
import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.IBlockData;
import steve6472.polyground.block.blockdata.logic.AbstractGate;
import steve6472.polyground.block.blockdata.logic.GatePair;
import steve6472.polyground.block.blockdata.logic.GateReg;
import steve6472.polyground.block.blockdata.logic.LogicBlockData;
import steve6472.polyground.block.blockdata.logic.other.Chip;
import steve6472.polyground.block.blockdata.logic.other.Input;
import steve6472.polyground.block.blockdata.logic.other.Output;
import steve6472.polyground.block.model.CubeHitbox;
import steve6472.polyground.block.model.elements.Bakery;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.player.Player;
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
public class LogicBlock extends CustomBlock implements IBlockData
{
	public LogicBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public BlockData createNewBlockEntity(BlockState state)
	{
		return new LogicBlockData();
	}

	@Override
	public void onClick(BlockState state, World world, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getButton() != KeyList.MMB && click.getAction() == KeyList.PRESS)
		{
			LogicBlockData data = (LogicBlockData) world.getData(x, y, z);
			if (data == null)
				return;

			Vector4i c = getLookedAtPiece(world, player, x, y, z);

			if (c == null)
				return;

			if (click.getButton() == KeyList.LMB && c.y == 0)
				return;

			AbstractGate gate = data.getComponent(c.x, c.y, c.z);
			if (gate != null)
			{
				if (click.getButton() == KeyList.RMB)
				{
					gate.clickActivate(new Vector3i(c.x, c.y, c.z).sub(gate.getPosition()), data.grid);
				}
			}

			EnumFace f = EnumFace.getFaces()[c.w];
			int cx = c.x + f.getXOffset();
			int cy = c.y + f.getYOffset();
			int cz = c.z + f.getZOffset();

			if (player.holdsBlock() && player.getBlockDataInHand() instanceof LogicBlockData d)
			{
				long inputCount = d.components
					.stream()
					.filter((g) -> g instanceof Input)
					.count();

				long outputCount = d.components
					.stream()
					.filter((g) -> g instanceof Output)
					.count();

				int max = (int) Math.max(inputCount, outputCount);
				int h = (max * 2 + 1);

				final boolean b = GateReg.canFit(cx, cy, cz, -max, 0, 0, h, 1, 3, data.grid);

				if (b)
				{
					List<AbstractGate> gatesCopy = new ArrayList<>();
					for (AbstractGate g : d.components)
					{
						gatesCopy.add(g.copy());
					}

					for (AbstractGate g : d.components)
					{
						GatePair[] inputConnections = g.getInputConnections();
						for (int i = 0; i < inputConnections.length; i++)
						{
							GatePair inputConnection = inputConnections[i];
							if (inputConnection == null)
								continue;

							final AbstractGate from = AbstractGate.findGate(gatesCopy, inputConnection.getGate().getUUID());
							final AbstractGate to = AbstractGate.findGate(gatesCopy, g.getUUID());

							AbstractGate.connect(from, to, inputConnection.getIndex(), i);
						}
					}

					Chip chip = new Chip(gatesCopy);
					chip.setPosition(cx - max, cy, cz);
					data.placeComponent(chip);
					data.updateModel();
				}

				player.processNextBlockPlace = false;
				return;
			}

			if (player.holdsItem())
			{
				if (GateReg.has(player.getItemInHand().name()) || player.getItemInHand().name.equals("wire"))
				{
					if (click.getButton() == KeyList.LMB)
					{
						if (c.y > 0)
						{
							if (gate != null)
							{
								for (int i = 0; i < gate.getSize().y; i++)
								{
									for (int j = 0; j < gate.getSize().x; j++)
									{
										for (int k = 0; k < gate.getSize().z; k++)
										{
											data.grid[gate.getPosition().y + i][(gate.getPosition().x + j) + (gate.getPosition().z + k) * 16] = 0;
										}
									}
								}
								data.removeComponent(gate);
							} else
							{
								data.grid[c.y][c.x + c.z * 16] = 0;
								player.processNextBlockBreak = false;
							}
						}
					} else
					{
						if (player.getItemInHand().name.equals("wire"))
						{
							data.grid[cy][cx + cz * 16] = 0x909090;
							data.placeWire(cx, cy, cz);
						} else
						{
							data.placeComponent(cx, cy, cz, player.getItemInHand().name());
						}
					}
				}

				if (data.updateModel)
				{
					data.components.forEach(a -> a.updateModel(data.grid));
					data.updateModel = false;
				}
				data.updateHandModel();
				world.getSubChunkFromBlockCoords(x, y, z).rebuild();
			}
		}
	}

	@Override
	public void tick(BlockState state, World world, int x, int y, int z)
	{
		LogicBlockData data = (LogicBlockData) world.getData(x, y, z);
		if (data == null)
			return;

		if (data.updateModel)
		{
			data.components.forEach(c -> c.updateModel(data.grid));
			data.updateHandModel();
			world.getSubChunkFromBlockCoords(x, y, z).rebuild();
			data.updateModel = false;
		}
	}

	@Override
	public boolean isPickable(BlockState state, Player player)
	{
		return !player.holdsItem();
	}

	private Vector4i getLookedAtPiece(World world, Player player, int x, int y, int z)
	{
		LogicBlockData data = (LogicBlockData) world.getData(x, y, z);
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

		LogicBlockData data = (LogicBlockData) world.getData(x, y, z);

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
		LogicBlockData data = (LogicBlockData) world.getData(x, y, z);
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

	@Override
	public boolean isTickable()
	{
		return true;
	}
}
