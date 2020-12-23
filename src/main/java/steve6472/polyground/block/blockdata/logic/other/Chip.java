package steve6472.polyground.block.blockdata.logic.other;

import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import org.joml.Vector3i;
import steve6472.polyground.block.blockdata.logic.AbstractGate;
import steve6472.polyground.block.blockdata.logic.GatePair;
import steve6472.polyground.block.blockdata.logic.GatePairList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.11.2020
 * Project: CaveGame
 *
 ***********************/
public class Chip extends AbstractGate
{
	private final List<AbstractGate> gates;
	private final List<Input> inputList;
	private final List<Output> outputList;
	private final Vector3i[] inputs, outputs;
	private final int[][] model;

	public Chip(List<AbstractGate> gates)
	{
		super("chip", true);

		this.gates = gates;

		inputList = gates
			.stream()
			.filter((g) -> g instanceof Input)
			.map((g) -> (Input) g)
			.collect(Collectors.toUnmodifiableList());

		outputList = gates
			.stream()
			.filter((g) -> g instanceof Output)
			.map((g) -> (Output) g)
			.collect(Collectors.toUnmodifiableList());

		int max = Math.max(inputList.size(), outputList.size());
		int height = (max * 2 + 1);
		model = new int[1][height * 3];
		setSize(height, 1, 3);

		Arrays.fill(model[0], 0x003238);

		inputStates = new boolean[inputList.size()];
		outputStates = new boolean[outputList.size()];

		inputConnections = new GatePair[inputList.size()];
		outputConnections = new GatePairList[outputList.size()];

		inputs = new Vector3i[inputList.size()];
		outputs = new Vector3i[outputList.size()];

		for (int i = 0; i < inputList.size(); i++)
		{
			inputs[i] = new Vector3i(height - i * 2 - 2, 0, 0);
		}

		for (int i = 0; i < outputList.size(); i++)
		{
			outputs[i] = new Vector3i(height - i * 2 - 2, 0, 2);
		}

		this.position = new Vector3i();

		inputPositions = inputPositions();
		outputPositions = outputPositions();
		setUUID(UUID.randomUUID());
	}

	public Chip(List<AbstractGate> gates, int[][] model, int width, int height, int depth)
	{
		super("chip", true);

		this.gates = gates;
		this.model = model;
		this.setSize(width, height, depth);

		inputList = gates
			.stream()
			.filter((g) -> g instanceof Input)
			.map((g) -> (Input) g)
			.collect(Collectors.toUnmodifiableList());

		outputList = gates
			.stream()
			.filter((g) -> g instanceof Output)
			.map((g) -> (Output) g)
			.collect(Collectors.toUnmodifiableList());

		inputStates = new boolean[inputList.size()];
		outputStates = new boolean[outputList.size()];

		inputConnections = new GatePair[inputList.size()];
		outputConnections = new GatePairList[outputList.size()];

		inputs = new Vector3i[inputList.size()];
		outputs = new Vector3i[outputList.size()];

		for (int i = 0; i < inputList.size(); i++)
		{
			inputs[i] = new Vector3i(inputList.get(i).getPosition());
		}

		for (int i = 0; i < outputList.size(); i++)
		{
			outputs[i] = new Vector3i(outputList.get(i).getPosition());
		}

		this.position = new Vector3i();

		inputPositions = inputPositions();
		outputPositions = outputPositions();
		setUUID(UUID.randomUUID());
	}

	@Override
	public CompoundTag write()
	{
		final CompoundTag tag = super.write();
		ListTag<CompoundTag> list = new ListTag<>(CompoundTag.class);

		for (AbstractGate g : gates)
		{
			list.add(g.write());
		}

		tag.put("components", list);
		return tag;
	}

	@Override
	protected Vector3i[] inputPositions()
	{
		return inputs;
	}

	@Override
	protected Vector3i[] outputPositions()
	{
		return outputs;
	}

	@Override
	protected int[][] getModel()
	{
		return model;
	}

	@Override
	protected void updateOutputState()
	{
		for (int i = 0; i < inputList.size(); i++)
		{
			Input input = inputList.get(i);
			input.updateInputState(0, inputStates[i]);
		}

		for (int i = 0; i < outputList.size(); i++)
		{
			Output output = outputList.get(i);
			outputStates[i] = output.getOutputStates()[0];
		}
	}

	@Override
	public void updateModel(int[][] grid)
	{
		super.updateModel(grid);

		for (int i = 0; i < outputList.size(); i++)
		{
			Output output = outputList.get(i);
			if (output.isLight())
			{
				grid[getPosition().y() + output.getPosition().y()]
					[(getPosition().x() + output.getPosition().x()) + (getPosition().z() + output.getPosition().z()) * logicData.size()] =
					getOutputStates()[i] ? 0xffff00 : 0x808000;
			}
		}
	}

	@Override
	protected int getInputCount()
	{
		return inputs.length;
	}

	@Override
	protected int getOutputCount()
	{
		return outputs.length;
	}

	@Override
	protected AbstractGate createCopy()
	{
		List<AbstractGate> gatesCopy = new ArrayList<>();
		for (AbstractGate g : gates)
		{
			gatesCopy.add(g.copy());
		}

		AbstractGate.fixConnections(gates, gatesCopy);

		return new Chip(gatesCopy);
	}
}
