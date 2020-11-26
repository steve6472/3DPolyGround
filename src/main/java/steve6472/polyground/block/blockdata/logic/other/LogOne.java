package steve6472.polyground.block.blockdata.logic.other;

import org.joml.Vector3i;
import steve6472.polyground.block.blockdata.logic.AbstractGate;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.11.2020
 * Project: CaveGame
 *
 ***********************/
public class LogOne extends AbstractGate
{
	private static final int[][] MODEL =
	{
		{0},
		{0x00ff00}
	};

	public LogOne()
	{
		super("high_constant");
		outputStates[0] = true;
	}

	@Override
	protected Vector3i[] inputPositions()
	{
		return new Vector3i[0];
	}

	@Override
	protected Vector3i[] outputPositions()
	{
		return new Vector3i[] {new Vector3i(0, 0, 0)};
	}

	@Override
	protected int[][] getModel()
	{
		return MODEL;
	}

	@Override
	protected void updateOutputState()
	{
		throw new IllegalStateException("State can not be changed!");
	}

	@Override
	protected int getInputCount()
	{
		return 0;
	}

	@Override
	protected int getOutputCount()
	{
		return 1;
	}

	@Override
	protected AbstractGate createCopy()
	{
		return new LogOne();
	}
}
