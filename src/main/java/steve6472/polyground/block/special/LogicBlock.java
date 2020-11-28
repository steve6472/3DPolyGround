package steve6472.polyground.block.special;

import org.joml.Vector3i;
import org.joml.Vector4i;
import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.logic.AbstractGate;
import steve6472.polyground.block.blockdata.logic.GatePair;
import steve6472.polyground.block.blockdata.logic.GateReg;
import steve6472.polyground.block.blockdata.logic.LogicBlockData;
import steve6472.polyground.block.blockdata.logic.other.Chip;
import steve6472.polyground.block.blockdata.logic.other.Input;
import steve6472.polyground.block.blockdata.logic.other.Output;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.World;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.09.2020
 * Project: CaveGame
 *
 ***********************/
public class LogicBlock extends AbstractMicroBlock
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
					data.updateMicroModel();
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
	public boolean isPickable(BlockState state, Player player)
	{
		return !player.holdsItem();
	}

	@Override
	public boolean isTickable()
	{
		return true;
	}
}
