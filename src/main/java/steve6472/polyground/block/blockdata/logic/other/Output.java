package steve6472.polyground.block.blockdata.logic.other;

import org.joml.Vector3i;
import steve6472.polyground.block.blockdata.logic.AbstractGate;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.11.2020
 * Project: CaveGame
 *
 ***********************/
public class Output extends AbstractGate
{
	private static final int[][] MODEL =
	{
		{0, 0},
		{0, 0}
	};

	private boolean isLight = false;

	public Output()
	{
		super("output");
	}

	@Override
	protected Vector3i[] inputPositions()
	{
		return new Vector3i[] {new Vector3i(0, 0, 0)};
	}

	@Override
	protected Vector3i[] outputPositions()
	{
		return new Vector3i[] {new Vector3i(0, 1, 1)};
	}

	@Override
	protected int[][] getModel()
	{
		return MODEL;
	}

	@Override
	protected void updateOutputState()
	{
		outputStates[0] = inputStates[0];
	}

	@Override
	protected int getInputCount()
	{
		return 1;
	}

	@Override
	protected int getOutputCount()
	{
		return 1;
	}

	public boolean isLight()
	{
		return isLight;
	}

	public void setLight(boolean light)
	{
		isLight = light;
	}

	@Override
	protected AbstractGate createCopy()
	{
		return new Output();
	}

	@Override
	public AbstractGate copy()
	{
		final Output copy = (Output) super.copy();
		copy.setLight(isLight());
		return copy;
	}
}
