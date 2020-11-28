package steve6472.polyground.block.special.logic;

import org.joml.AABBf;
import org.joml.Vector3i;
import org.joml.Vector4i;
import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.ISpecialRender;
import steve6472.polyground.block.blockdata.AbstractMicroBlockData;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.logic.AbstractGate;
import steve6472.polyground.block.blockdata.logic.GatePair;
import steve6472.polyground.block.blockdata.logic.GatePairList;
import steve6472.polyground.block.blockdata.logic.GateReg;
import steve6472.polyground.block.blockdata.logic.data.LogicBlockData;
import steve6472.polyground.block.blockdata.logic.other.Chip;
import steve6472.polyground.block.blockdata.logic.other.Input;
import steve6472.polyground.block.blockdata.logic.other.Output;
import steve6472.polyground.block.model.CubeHitbox;
import steve6472.polyground.block.model.elements.Bakery;
import steve6472.polyground.block.special.AbstractMicroBlock;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.player.EnumGameMode;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.gfx.stack.LineTess;
import steve6472.polyground.gfx.stack.Stack;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.09.2020
 * Project: CaveGame
 *
 ***********************/
public class LogicBlock extends AbstractMicroBlock implements ISpecialRender
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
		if (player.getGamemode() == EnumGameMode.CREATIVE)
		{
			player.processNextBlockBreak = false;
			return;
		}

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

				final boolean b = GateReg.canFit(cx, cy, cz, -max, 0, 0, h, 1, 3, data.grid, getSize());

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
					data.updateMicroModel();
				}

				player.processNextBlockPlace = false;
				return;
			}

			if (player.holdsItem())
			{
				if (GateReg.has(player.getItemInHand().name()) || player.getItemInHand().name.equals("wire") || player.getItemInHand().name.equals("fill") || player.getItemInHand().name.equals("board_level"))
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
											data.grid[gate.getPosition().y + i][(gate.getPosition().x + j) + (gate.getPosition().z + k) * getSize()] = 0;
										}
									}
								}
								data.removeComponent(gate);
							} else
							{
								data.grid[c.y][c.x + c.z * getSize()] = 0;
								player.processNextBlockBreak = false;
							}
						}
					} else
					{
						if (player.getItemInHand().name.equals("wire"))
						{
							data.grid[cy][cx + cz * getSize()] = 0x909090;
							data.placeWire(cx, cy, cz);
						} else if (player.getItemInHand().name().equals("fill"))
						{
							data.grid[cy][cx + cz * getSize()] = 0x00b340;
						} else if (player.getItemInHand().name().equals("board_level"))
						{
							for (int i = 0; i < getSize() * getSize(); i++)
							{
								if (data.grid[cy][i] == 0)
									data.grid[cy][i] = 0x00b340;
							}
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
				data.updateModel();
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
			data.updateModel();
			world.getSubChunkFromBlockCoords(x, y, z).rebuild();
			data.updateModel = false;
		}
	}

	@Override
	protected boolean renderSelectedMicro(World world, BlockState state, int x, int y, int z)
	{
		final Player player = CaveGame.getInstance().getPlayer();
		return !(player.holdsItem() && GateReg.has(player.getItemInHand().name()));
	}

	@Override
	public CubeHitbox[] getHitbox(World world, BlockState state, int x, int y, int z)
	{
		CubeHitbox[] hitbox = super.getHitbox(world, state, x, y, z);

		final Player player = CaveGame.getInstance().getPlayer();

		Vector4i c = getLookedAtPiece(world, player, x, y, z);

		if (c != null && player.holdsItem() && GateReg.has(player.getItemInHand().name()))
		{
			EnumFace f = EnumFace.getFaces()[c.w];
			int cx = c.x + f.getXOffset();
			int cy = c.y + f.getYOffset();
			int cz = c.z + f.getZOffset();

			float inv = 1f / 16f;
			final GateReg.GateEntry entry = GateReg.getEntry(player.getItemInHand().name());
			hitbox = Stream
				.concat(Arrays.stream(hitbox), Arrays.stream(new CubeHitbox[]{new CubeHitbox(
					new AABBf(
						cx * inv + entry.offsetX * inv + getOffsetX() * inv,
						cy * inv + entry.offsetY * inv + getOffsetY() * inv,
						cz * inv + entry.offsetZ * inv + getOffsetZ() * inv,
						cx * inv + entry.offsetX * inv + entry.sizeX * inv + getOffsetX() * inv,
						cy * inv + entry.offsetY * inv + entry.sizeY * inv + getOffsetY() * inv,
						cz * inv + entry.offsetZ * inv + entry.sizeZ * inv + getOffsetZ() * inv))}))
				.toArray(CubeHitbox[]::new);
		}

		if (c != null && player.holdsBlock() && !player.holdsItem() && player.getBlockDataInHand() instanceof LogicBlockData d)
		{
			EnumFace f = EnumFace.getFaces()[c.w];
			int cx = c.x + f.getXOffset();
			int cy = c.y + f.getYOffset();
			int cz = c.z + f.getZOffset();

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

			float inv = 1f / 16f;
			hitbox = Stream
				.concat(Arrays.stream(hitbox), Arrays.stream(new CubeHitbox[]{new CubeHitbox(
					new AABBf(
						cx * inv + -max * inv + getOffsetX() * inv,
						cy * inv + getOffsetY() * inv,
						cz * inv + getOffsetZ() * inv,
						cx * inv + -max * inv + h * inv + getOffsetX() * inv,
						cy * inv + inv + getOffsetY() * inv,
						cz * inv + 3 * inv + getOffsetZ() * inv))}))
				.toArray(CubeHitbox[]::new);
		}

		return hitbox;
	}

	@Override
	public boolean isPickable(BlockState state, Player player)
	{
		return !player.holdsItem();
	}

	@Override
	public boolean isTickable()
	{
		return true;
	}

	@Override
	public void render(Stack stack, World world, BlockState state, int x, int y, int z)
	{
		if (!CaveGame.getInstance().options.renderLogicConnections)
			return;

		LogicBlockData data = (LogicBlockData) world.getData(x, y, z);
		if (data == null)
			return;

		stack.translate(-0.5f, 0, -0.5f);
		stack.translate(0.5f - data.size() / 32f, 0, 0.5f - data.size() / 32f);

		final LineTess tess = stack.getLineTess();

		for (AbstractGate component : data.components)
		{
			GatePair[] inputConnections = component.getInputConnections();
			for (int i = 0; i < inputConnections.length; i++)
			{
				GatePair ic = inputConnections[i];
				if (ic == null)
					continue;

				tess.color(0, 1, 0, 1);
				tess.pos(
					component.getPosition().x / 16f + component.getInputPositions()[i].x / 16f + 1f / 32f,
					component.getPosition().y / 16f + component.getInputPositions()[i].y / 16f + 1f / 32f + 1f / 64f,
					component.getPosition().z / 16f + component.getInputPositions()[i].z / 16f + 1f / 32f
				).endVertex();

				tess.color(0, 1, 1, 1);
				tess.pos(
					ic.getGate().getPosition().x / 16f + ic.getGate().getOutputPositions()[ic.getIndex()].x / 16f + 1f / 32f,
					ic.getGate().getPosition().y / 16f + ic.getGate().getOutputPositions()[ic.getIndex()].y / 16f + 1f / 32f + 1f / 64f,
					ic.getGate().getPosition().z / 16f + ic.getGate().getOutputPositions()[ic.getIndex()].z / 16f + 1f / 32f
				).endVertex();
			}

			GatePairList[] outputConnections = component.getOutputConnections();
			for (int i = 0; i < outputConnections.length; i++)
			{
				GatePairList oc = outputConnections[i];
				if (oc == null)
					continue;

				for (int j = 0; j < oc.getList().size(); j++)
				{
					GatePair pair = oc.getList().get(j);

					tess.color(1, 0, 0, 1);
					tess.pos(
						component.getPosition().x / 16f + component.getOutputPositions()[i].x / 16f + 1f / 32f,
						component.getPosition().y / 16f + component.getOutputPositions()[i].y / 16f + 1f / 32f - 1f / 64f,
						component.getPosition().z / 16f + component.getOutputPositions()[i].z / 16f + 1f / 32f
					).endVertex();

					tess.color(1, 0, 1, 1);
					tess.pos(
						pair.getGate().getPosition().x / 16f + pair.getGate().getInputPositions()[pair.getIndex()].x / 16f + 1f / 32f,
						pair.getGate().getPosition().y / 16f + pair.getGate().getInputPositions()[pair.getIndex()].y / 16f + 1f / 32f - 1f / 64f,
						pair.getGate().getPosition().z / 16f + pair.getGate().getInputPositions()[pair.getIndex()].z / 16f + 1f / 32f
					).endVertex();
				}
			}
		}
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
					final int i1 = data.grid[j][i + k * getSize()];
					if (i1 != 0 && !(i1 == 0x909090 && CaveGame.getInstance().options.disableLogicWire))
					{
						int flags = Bakery.createFaceFlags(
							i != (getSize() - 1) && data.grid[j][(i + 1) + k * getSize()] != 0,
							k != (getSize() - 1) && data.grid[j][i + (k + 1) * getSize()] != 0,
							i != 0 && data.grid[j][(i - 1) + k * getSize()] != 0,
							k != 0 && data.grid[j][i + (k - 1) * getSize()] != 0,
							j != (getSize() - 1) && data.grid[j + 1][i + k * getSize()] != 0,
							j != 0 && data.grid[j - 1][i + k * getSize()] != 0
						);
						tris += Bakery.coloredCube(i + getOffsetX(), j + getOffsetY(), k + getOffsetZ(), 1, 1, 1, i1, flags);
					}
				}
			}
		}

		return tris;
	}
}
