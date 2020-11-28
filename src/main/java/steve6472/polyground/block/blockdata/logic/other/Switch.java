package steve6472.polyground.block.blockdata.logic.other;

import org.joml.Vector3i;
import steve6472.polyground.block.blockdata.logic.AbstractGate;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.11.2020
 * Project: CaveGame
 *
 ***********************/
public class Switch extends AbstractGate
{
	private static final int[][] MODEL =
		{
			{
				0x303030, 0x303030, 0x303030,
				0x303030, 0x303030, 0x303030,
				0x303030, 0x303030, 0x303030
			},
			{
				0x000000, 0x007fff, 0x000000,
				0x000000, 0x000000, 0x000000,
				0x000000, 0x000000, 0x000000
			}
		};

	public Switch()
	{
		super("switch");
	}

	@Override
	public void clickActivate(Vector3i relPos, int[][] grid)
	{
		if (relPos.equals(1, 1, 0))
		{
			outputStates[0] = !outputStates[0];

			updateModel();
			updateOutputs();
		}
	}

	@Override
	public void updateModel(int[][] grid)
	{
		super.updateModel(grid);
		grid[getPosition().y() + 1][(getPosition().x() + 2) + (getPosition().z() + 2) * logicData.size()] = getOutputStates()[0] ? 0x009000 : 0x900000;
	}

	@Override
	protected Vector3i[] inputPositions()
	{
		return new Vector3i[0];
	}

	@Override
	protected Vector3i[] outputPositions()
	{
		return new Vector3i[] {new Vector3i(1, 0, 2)};
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
		return new Switch();
	}
}
