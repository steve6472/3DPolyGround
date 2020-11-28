package steve6472.polyground.block.blockdata.logic;

import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import org.joml.Vector3i;

import java.util.List;
import java.util.UUID;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.11.2020
 * Project: CaveGame
 *
 ***********************/
public abstract class AbstractGate
{
	protected GatePair[] inputConnections;
	protected GatePairList[] outputConnections;
	private UUID uuid;
	private String type;

	protected boolean[] inputStates;
	protected boolean[] outputStates;
	protected Vector3i position, size;
	protected Vector3i[] inputPositions, outputPositions;

	public AbstractGate(String type)
	{
		this.type = type;
		inputStates = new boolean[getInputCount()];
		outputStates = new boolean[getOutputCount()];

		inputConnections = new GatePair[getInputCount()];
		outputConnections = new GatePairList[getOutputCount()];

		this.position = new Vector3i();

		inputPositions = inputPositions();
		outputPositions = outputPositions();
		this.uuid = UUID.randomUUID();
	}

	public AbstractGate(String type, boolean noInit)
	{
		this.type = type;
	}

	/* Board Data */
	protected LogicBlockData logicData;

	public void setLogicData(LogicBlockData data)
	{
		this.logicData = data;
	}

	protected abstract Vector3i[] inputPositions();
	protected abstract Vector3i[] outputPositions();
	protected abstract int[][] getModel();

	public Vector3i getPosition()
	{
		return position;
	}

	public void setPosition(int x, int y, int z)
	{
		this.position.set(x, y, z);
	}

	public Vector3i[] getInputPositions()
	{
		return inputPositions;
	}

	public Vector3i[] getOutputPositions()
	{
		return outputPositions;
	}

	public void setSize(int w, int d, int h)
	{
		size = new Vector3i(w, d, h);
	}

	public Vector3i getSize()
	{
		return size;
	}

	public void updateModel()
	{
		logicData.updateMicroModel();
	}

	public void updateModel(int[][] grid)
	{
		for (int i = 0; i < getSize().y; i++)
		{
			for (int j = 0; j < getSize().x; j++)
			{
				for (int k = 0; k < getSize().z; k++)
				{
					int i1 = getModel()[i][j + k * getSize().x];
					grid[i + getPosition().y][(j + getPosition().x) + (k + getPosition().z) * 16] = i1;
				}
			}
		}

		for (Vector3i in : getInputPositions())
		{
			grid[in.y + getPosition().y][(in.x + getPosition().x) + (in.z + getPosition().z) * 16] = 0xffffff;
		}
		for (Vector3i in : getOutputPositions())
		{
			grid[in.y + getPosition().y][(in.x + getPosition().x) + (in.z + getPosition().z) * 16] = 0x010101;
		}
	}

	public void clickActivate(Vector3i relPos, int[][] grid)
	{
	}

	public int getInputIndex(Vector3i absPos)
	{
		for (int i = 0; i < getInputPositions().length; i++)
		{
			Vector3i input = getInputPositions()[i];
//			System.out.println("input: " + input.x + " " + input.y + " " + input.z);
//			System.out.println("absPos: " + absPos.x + " " + absPos.y + " " + absPos.z + "  ->  " + (getPosition().x - absPos.x) + " " + (getPosition().y - absPos.y) + " " + (getPosition().z - absPos.z) + "  ->  " + getPosition().x + " " + getPosition().y + " " + getPosition().z);
			if (getPosition().x - absPos.x + input.x == 0 && getPosition().y - absPos.y + input.y == 0 && getPosition().z - absPos.z + input.z == 0)
			{
//				System.out.println("returning input " + i + "\n");
				return i;
			}
//			System.out.println("input -1\n");
		}
		return -1;
	}

	public int getOutputIndex(Vector3i absPos)
	{
		for (int i = 0; i < getOutputPositions().length; i++)
		{
			Vector3i output = getOutputPositions()[i];
//			System.out.println("output: " + output.x + " " + output.y + " " + output.z);
//			System.out.println("absPos: " + absPos.x + " " + absPos.y + " " + absPos.z + "  ->  " + (getPosition().x - absPos.x) + " " + (getPosition().y - absPos.y) + " " + (getPosition().z - absPos.z));
			if (getPosition().x - absPos.x + output.x == 0 && getPosition().y - absPos.y + output.y == 0 && getPosition().z - absPos.z + output.z == 0)
			{
//				System.out.println("returning output " + i + "\n");
				return i;
			}
		}
//		System.out.println("output -1\n");
		return -1;
	}

	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		tag.putString("uuid", uuid.toString());

		ListTag<CompoundTag> inputList = new ListTag<>(CompoundTag.class);

		for (int i = 0; i < inputConnections.length; i++)
		{
			GatePair connection = inputConnections[i];
			if (connection == null)
				continue;

			CompoundTag connectionTag = new CompoundTag();
			connectionTag.putInt("inputIndex", i);
			connectionTag.putInt("index", connection.getIndex());
			connectionTag.putString("uuid", connection.getGate().uuid.toString());
			inputList.add(connectionTag);
		}

		tag.put("inputs", inputList);

		tag.putInt("x", position.x);
		tag.putInt("y", position.y);
		tag.putInt("z", position.z);

		tag.putInt("w", size.x);
		tag.putInt("h", size.y);
		tag.putInt("d", size.z);

		tag.putString("type", type);

		return tag;
	}

	public void read(CompoundTag tag)
	{
		if (!tag.getString("type").equals(type))
			throw new IllegalStateException("Type from tag does not match type from AbstractGate object! (" + tag.getString("type") + " != " + type + ")");

		setUUID(UUID.fromString(tag.getString("uuid")));

		setPosition(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
		setSize(tag.getInt("w"), tag.getInt("h"), tag.getInt("d"));
		//TODO: Save inputStates and outputStates
	}

	public void read(CompoundTag tag, List<AbstractGate> gates)
	{
		ListTag<CompoundTag> listTag = (ListTag<CompoundTag>) tag.getListTag("inputs");
		for (CompoundTag t : listTag)
		{
			UUID otherUUID = UUID.fromString(t.getString("uuid"));
			AbstractGate otherGate = findGate(gates, otherUUID);
			if (otherGate == null)
				throw new RuntimeException("Gate with UUID " + otherUUID.toString() + " not found!");
			connect(otherGate, this, t.getInt("index"), t.getInt("inputIndex"));
		}
	}

	public static void connect(AbstractGate from, AbstractGate to, int fromOutput, int toInput)
	{
		from.connectOutput(fromOutput, to, toInput);
		to.connectInput(toInput, from, fromOutput);
	}

	public static AbstractGate findGate(List<AbstractGate> gates, UUID uuid)
	{
		for (AbstractGate gate : gates)
		{
			if (gate.uuid.equals(uuid))
				return gate;
		}

		return null;
	}

	/* End */

	public UUID getUUID()
	{
		return uuid;
	}

	public void setUUID(UUID uuid)
	{
		this.uuid = uuid;
	}

	public boolean[] getOutputStates()
	{
		return outputStates;
	}

	public boolean[] getInputStates()
	{
		return inputStates;
	}

	public void updateInputState(int index, boolean flag)
	{
//		System.out.println(getClass().getSimpleName() + " updated " + index + " to " + flag);
		if (inputStates[index] != flag)
		{
//			System.out.println("\t" + inputStates[index] + " -> " + flag);
			inputStates[index] = flag;
			updateOutputState();
//			debug();
			updateOutputs();
		}
//		System.out.println("");
	}

	public void debug()
	{
		for (int i = 0; i < outputStates.length; i++)
		{
			boolean b = outputStates[i];
			System.out.printf("\tOutput #%d -> %b\n", i, b);
		}
		System.out.println();
		for (int i = 0; i < inputStates.length; i++)
		{
			boolean b = inputStates[i];
			System.out.printf("\tInput #%d -> %b\n", i, b);
		}
	}

	public void updateOutputs()
	{
		for (int i = 0; i < outputConnections.length; i++)
		{
			GatePairList out = outputConnections[i];
			if (out != null)
			{
				for (GatePair c : out.getList())
				{
					c.getGate().updateInputState(c.getIndex(), outputStates[i]);
				}
			}
		}
	}

	protected abstract void updateOutputState();

	protected abstract int getInputCount();
	protected abstract int getOutputCount();

	public void disconnectOutput(int outputIndex, AbstractGate node, int inputIndex)
	{
		if (outputConnections[outputIndex] != null)
		{
			outputConnections[outputIndex].getList().removeIf(c -> c.getGate() == node && c.getIndex() == inputIndex);
		}
	}

	public void disconnectInput(int inputIndex)
	{
		inputConnections[inputIndex] = null;
	}

	public void connectOutput(int outputIndex, AbstractGate node, int inputIndex)
	{
		if (outputConnections[outputIndex] == null)
			outputConnections[outputIndex] = new GatePairList();
		outputConnections[outputIndex].getList().add(new GatePair(node, inputIndex));
		updateOutputs();
	}

	public void connectInput(int inputIndex, AbstractGate gate, int outputIndex)
	{
		if (inputConnections[inputIndex] != null)
			throw new IllegalStateException("Input join can have only one connection! Check failed!");
		inputConnections[inputIndex] = new GatePair(gate, outputIndex);
	}

	public void disconnectAll()
	{
		for (int i = 0; i < inputConnections.length; i++)
		{
			GatePair inputConnection = inputConnections[i];
			disconnectInput(i);

			if (inputConnection != null && inputConnection.getGate() != null)
				inputConnection.getGate().disconnectOutput(inputConnection.getIndex(), this, i);
		}
	}

	public GatePair[] getInputConnections()
	{
		return inputConnections;
	}

	protected abstract AbstractGate createCopy();

	public AbstractGate copy()
	{
		AbstractGate gate = createCopy();
		gate.uuid = new UUID(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
		System.arraycopy(inputStates, 0, gate.inputStates, 0, inputStates.length);
		System.arraycopy(outputStates, 0, gate.outputStates, 0, outputStates.length);
		for (int i = 0; i < inputPositions.length; i++)
		{
			gate.inputPositions[i] = new Vector3i(inputPositions[i]);
		}
		for (int i = 0; i < outputPositions.length; i++)
		{
			gate.outputPositions[i] = new Vector3i(outputPositions[i]);
		}
		gate.setSize(size.x, size.y, size.z);
		gate.setPosition(-1, -1, -1);

		return gate;
	}
}
