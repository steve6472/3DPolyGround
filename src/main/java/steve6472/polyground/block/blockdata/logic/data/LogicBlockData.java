package steve6472.polyground.block.blockdata.logic.data;

import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import org.joml.Vector3i;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.blockdata.logic.AbstractGate;
import steve6472.polyground.block.blockdata.logic.GateReg;
import steve6472.polyground.block.blockdata.micro.AbstractPickableMicroBlockData;
import steve6472.polyground.registry.blockdata.BlockDataRegistry;
import steve6472.sge.main.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

		for (int i = 0; i < getSize(); i++)
		{
			for (int j = 0; j < getSize(); j++)
			{
				grid[0][i + j * getSize()] = 0x009536;
			}
		}

		grid[0][0] = 0x008328;
	}

	private static class Node
	{
		int x, y, z;
		boolean processed;

		Node(int x, int y, int z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			processed = false;
		}

		@Override
		public boolean equals(Object o)
		{
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			Node node = (Node) o;
			return x == node.x && y == node.y && z == node.z;
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(x, y, z);
		}
	}

	private Pair<List<Node>, List<Node>> findWire(int x, int y, int z, int color)
	{
		List<Node> inputs = new ArrayList<>();
		List<Node> outputs = new ArrayList<>();

		List<Node> pos = new ArrayList<>();
		pos.add(new Node(x, y, z));

		List<Node> newNodes = new ArrayList<>();

		int iterations = 0;

		while (iterations < getSize() * 16)
		{
			boolean processedSomething = false;
			for (Node n : pos)
			{
				if (n.processed)
					continue;

				n.processed = true;
				processedSomething = true;

				for (EnumFace f : EnumFace.getFaces())
				{
					final int x1 = n.x + f.getXOffset();
					final int y1 = n.y + f.getYOffset();
					final int z1 = n.z + f.getZOffset();

					if (x1 < 0 || y1 < 0 || z1 < 0 || x1 >= getSize() || y1 >= getSize() || z1 >= getSize())
						continue;

					Node newNode = new Node(x1, y1, z1);

					// Check if node exists and is not already listed
					if (isWire(newNode, color) && !pos.contains(newNode))
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

			if (!processedSomething)
				break;

			if (newNodes.isEmpty())
				break;

			pos.addAll(newNodes);
			newNodes.clear();

			iterations++;
		}

		return new Pair<>(inputs, outputs);
	}

	public void placeWire(int x, int y, int z, int color)
	{
		final Pair<List<Node>, List<Node>> wire = findWire(x, y, z, color);
		List<Node> inputs = wire.a();
		List<Node> outputs = wire.b();

		if (outputs.size() > 1)
		{
			System.err.println("Too many outputs! Wires will not be connected.");
			return;
		}

		if (outputs.size() == 0 || inputs.size() == 0)
			return;

		AbstractGate outputComponent = getComponent(outputs.get(0).x, outputs.get(0).y, outputs.get(0).z);
		int outputIndex = outputComponent.getOutputIndex(outputs.get(0).x, outputs.get(0).y, outputs.get(0).z);

		for (Node input : inputs)
		{
			AbstractGate inputComponent = getComponent(input.x, input.y, input.z);
			int inputIndex = inputComponent.getInputIndex(input.x, input.y, input.z);

			if (inputComponent.getInputConnections()[inputIndex] != null)
				continue;

			connect(outputComponent, inputComponent, outputIndex, inputIndex);
			outputComponent.updateModel(grid);
			inputComponent.updateModel(grid);
		}
	}

	public void removeWire(int x, int y, int z)
	{
		final Pair<List<Node>, List<Node>> wire = findWire(x, y, z, -1);
		List<Node> inputs = wire.a();
		List<Node> outputs = wire.b();

		if (outputs.size() > 1)
		{
			System.err.println("Too many outputs! Wires will not be connected.");
			return;
		}

		if (outputs.size() == 0 || inputs.size() == 0)
			return;

		AbstractGate outputComponent = getComponent(outputs.get(0).x, outputs.get(0).y, outputs.get(0).z);
		int outputIndex = outputComponent.getOutputIndex(outputs.get(0).x, outputs.get(0).y, outputs.get(0).z);

		for (Node input : inputs)
		{
			AbstractGate inputComponent = getComponent(input.x, input.y, input.z);
			int inputIndex = inputComponent.getInputIndex(input.x, input.y, input.z);

			if (inputComponent.getInputConnections()[inputIndex] != null)
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

	/**
	 *
	 * @param pos position of check
	 * @param color 0x909090 for no color wire <br>
	 *              0xff90ff for magenta wire
	 * @return true if same-colored wire is at pos
	 */
	private boolean isWire(Node pos, int color)
	{
		return grid[pos.y][pos.x + pos.z * getSize()] == color;
	}

	private boolean isInput(Node pos)
	{
		return grid[pos.y][pos.x + pos.z * getSize()] == 0xffffff;
	}

	private boolean isOutput(Node pos)
	{
		return grid[pos.y][pos.x + pos.z * getSize()] == 0x010101;
	}

	public void placeComponent(int x, int y, int z, String component)
	{
		if (GateReg.canFit(component, x, y, z, grid, getSize()))
		{
			AbstractGate gate = GateReg.createGate(component, x, y, z);
			gate.setLogicData(this);
			gate.updateModel(grid);
			components.add(gate);

			for (Vector3i inputPosition : gate.getInputPositions())
			{
				placeWire(gate.getPosition().x + inputPosition.x, gate.getPosition().y + inputPosition.y, gate.getPosition().z + inputPosition.z, 0x909090);
				placeWire(gate.getPosition().x + inputPosition.x, gate.getPosition().y + inputPosition.y, gate.getPosition().z + inputPosition.z, 0xff90ff);
			}
			for (Vector3i outputPosition : gate.getOutputPositions())
			{
				placeWire(gate.getPosition().x + outputPosition.x, gate.getPosition().y + outputPosition.y, gate.getPosition().z + outputPosition.z, 0x909090);
				placeWire(gate.getPosition().x + outputPosition.x, gate.getPosition().y + outputPosition.y, gate.getPosition().z + outputPosition.z, 0xff90ff);
			}
		}
	}

	public void placeComponent(AbstractGate gate)
	{
		gate.setLogicData(this);
		gate.updateModel(grid);
		components.add(gate);

		for (Vector3i inputPosition : gate.getInputPositions())
		{
			placeWire(gate.getPosition().x + inputPosition.x, gate.getPosition().y + inputPosition.y, gate.getPosition().z + inputPosition.z, 0x909090);
			placeWire(gate.getPosition().x + inputPosition.x, gate.getPosition().y + inputPosition.y, gate.getPosition().z + inputPosition.z, 0xff90ff);
		}
		for (Vector3i outputPosition : gate.getOutputPositions())
		{
			placeWire(gate.getPosition().x + outputPosition.x, gate.getPosition().y + outputPosition.y, gate.getPosition().z + outputPosition.z, 0x909090);
			placeWire(gate.getPosition().x + outputPosition.x, gate.getPosition().y + outputPosition.y, gate.getPosition().z + outputPosition.z, 0xff90ff);
		}
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

		this.components = AbstractGate.readComponents(tag, this);
	}

	public int size()
	{
		return getSize();
	}

	@Override
	public String getId()
	{
		return BlockDataRegistry.logic.id();
	}
}
