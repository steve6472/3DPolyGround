package steve6472.polyground.block.blockdata.logic.other;

import org.joml.Vector3i;
import steve6472.polyground.block.blockdata.logic.AbstractGate;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.11.2020
 * Project: CaveGame
 *
 ***********************/
public class Cross extends AbstractGate
{
	private static final int[][] MODEL =
	{
		{
			0x303030, 0x303030, 0x303030,
			0x303030, 0x303030, 0x303030,
			0x303030, 0x303030, 0x303030
		}
	};

	public Cross()
	{
		super("cross");
	}

	@Override
	protected Vector3i[] inputPositions()
	{
		return new Vector3i[] {new Vector3i(1, 0, 0), new Vector3i(2, 0, 1)};
	}

	@Override
	protected Vector3i[] outputPositions()
	{
		return new Vector3i[] {new Vector3i(1, 0, 2), new Vector3i(0, 0, 1)};
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
		outputStates[1] = inputStates[1];
	}

	@Override
	protected int getInputCount()
	{
		return 2;
	}

	@Override
	protected int getOutputCount()
	{
		return 2;
	}

	@Override
	protected AbstractGate createCopy()
	{
		return new Cross();
	}
}
