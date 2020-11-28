package steve6472.polyground.block.blockdata.logic;

import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import org.joml.Vector3i;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.blockdata.AbstractPickableMicroBlockData;
import steve6472.polyground.block.blockdata.logic.other.Chip;
import steve6472.polyground.registry.blockdata.BlockDataRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2020
 * Project: CaveGame
 *
 ***********************/
public class LogicBlockData extends AbstractPickableMicroBlockData
{
	public List<AbstractGate> components;
	public boolean updateModel;

	public LogicBlockData()
	{
		super();
		components = new ArrayList<>();

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				grid[0][i + j * 16] = 0x009536;
			}
		}

		grid[0][0] = 0x008328;
	}

	public void placeWire(int x, int y, int z)
	{
		List<Vector3i> inputs = new ArrayList<>();
		List<Vector3i> outputs = new ArrayList<>();

		List<Vector3i> pos = new ArrayList<>();
		pos.add(new Vector3i(x, y, z));

		List<Vector3i> newNodes = new ArrayList<>();

		int iterations = 0;

		while (iterations < 10)
		{
			for (Vector3i n : pos)
			{
				for (EnumFace f : EnumFace.getFaces())
				{
					Vector3i newNode = new Vector3i(n.x + f.getXOffset(), n.y + f.getYOffset(), n.z + f.getZOffset());

					// Check if node exists and is not already listed
					if (isWire(newNode) && !pos.contains(newNode))
					{
						newNodes.add(newNode);
					}

					if (isInput(newNode) && !inputs.contains(newNode))
					{
						inputs.add(newNode);
					}

					if (isOutput(newNode) && !outputs.contains(newNode))
					{
						outputs.add(newNode);
					}
				}
			}

			if (newNodes.isEmpty())
				break;

			pos.addAll(newNodes);
			newNodes.clear();

			iterations++;
		}

		if (outputs.size() > 1)
		{
			System.err.println("Too many outputs! Wires will not be connected.");
		}

		if (outputs.size() == 0 || inputs.size() == 0)
			return;

		AbstractGate outputComponent = getComponent(outputs.get(0).x, outputs.get(0).y, outputs.get(0).z);
		int outputIndex = outputComponent.getOutputIndex(outputs.get(0));

		for (Vector3i input : inputs)
		{
			AbstractGate inputComponent = getComponent(input.x, input.y, input.z);
			int inputIndex = inputComponent.getInputIndex(input);

			if (inputComponent.inputConnections[inputIndex] != null)
				continue;

			connect(outputComponent, inputComponent, outputIndex, inputIndex);
			outputComponent.updateModel(grid);
			inputComponent.updateModel(grid);
		}
	}

	private static void connect(AbstractGate from, AbstractGate to, int fromOutput, int toInput)
	{
		from.connectOutput(fromOutput, to, toInput);
		to.connectInput(toInput, from, fromOutput);
	}

	public AbstractGate getComponent(int x, int y, int z)
	{
		for (AbstractGate c : components)
		{
			if (x >= c.getPosition().x && x < c.getPosition().x + c.getSize().x && y >= c.getPosition().y && y < c.getPosition().y + c.getSize().y && z >= c.getPosition().z && z < c.getPosition().z + c.getSize().z)
			{
				return c;
			}
		}

		return null;
	}

	public void removeComponent(AbstractGate gate)
	{
		gate.disconnectAll();
		components.remove(gate);
	}

	private boolean isWire(Vector3i pos)
	{
		return grid[pos.y][pos.x + pos.z * 16] == 0x909090;
	}

	private boolean isInput(Vector3i pos)
	{
		return grid[pos.y][pos.x + pos.z * 16] == 0xffffff;
	}

	private boolean isOutput(Vector3i pos)
	{
		return grid[pos.y][pos.x + pos.z * 16] == 0x010101;
	}

	public void placeComponent(int x, int y, int z, String component)
	{
		if (GateReg.canFit(component, x, y, z, grid))
		{
			AbstractGate gate = GateReg.createGate(component, x, y, z);
			gate.updateModel(grid);
			gate.setLogicData(this);
			components.add(gate);
		}
	}

	public void placeComponent(AbstractGate gate)
	{
		gate.updateModel(grid);
		gate.setLogicData(this);
		components.add(gate);
	}

	public void updateMicroModel()
	{
		updateModel = true;
	}

	@Override
	public CompoundTag write()
	{
		final CompoundTag tag = super.write();

		ListTag<CompoundTag> list = new ListTag<>(CompoundTag.class);

		for (AbstractGate g : components)
		{
			list.add(g.write());
		}

		tag.put("components", list);

		return tag;
	}

	@Override
	public void read(CompoundTag tag)
	{
		super.read(tag);

		this.components = readComponents(tag);
	}

	private List<AbstractGate> readComponents(CompoundTag tag)
	{
		final ListTag<CompoundTag> components = (ListTag<CompoundTag>) tag.getListTag("components");
		final List<AbstractGate> gates = new ArrayList<>();
		for (CompoundTag c : components)
		{
			final String type = c.getString("type");
			if (type.equals("chip"))
			{
				Chip g = new Chip(readComponents(c));
				g.read(c);
				g.setLogicData(this);
				gates.add(g);
			} else
			{
				AbstractGate g = GateReg.newGate(type);
				g.read(c);
				g.setLogicData(this);
				gates.add(g);
			}
		}
		for (CompoundTag c : components)
		{
			final AbstractGate gate = AbstractGate.findGate(gates, UUID.fromString(c.getString("uuid")));
			gate.read(c, gates);
		}

		return gates;
	}

	@Override
	public String getId()
	{
		return BlockDataRegistry.logic.id();
	}
}
