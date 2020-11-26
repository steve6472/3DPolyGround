package steve6472.polyground.block.blockdata.logic.other;

import org.joml.Vector3i;
import steve6472.polyground.block.blockdata.logic.AbstractGate;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.11.2020
 * Project: CaveGame
 *
 ***********************/
public class Light extends AbstractGate
{
	private static final int[][] MODEL =
	{
		{
			0x303030, 0x303030, 0x303030,
			0x303030, 0x303030, 0x303030,
			0x303030, 0x303030, 0x303030
		},
		{
			0x000000, 0x000000, 0x000000,
			0x000000, 0x808000, 0x000000,
			0x000000, 0x000000, 0x000000
		}
	};

	public Light()
	{
		super("light");
	}

	@Override
	protected Vector3i[] inputPositions()
	{
		return new Vector3i[] {new Vector3i(1, 0, 0)};
	}

	@Override
	protected Vector3i[] outputPositions()
	{
		return new Vector3i[0];
	}

	@Override
	protected int[][] getModel()
	{
		return MODEL;
	}

	@Override
	public void updateModel(int[][] grid)
	{
		super.updateModel(grid);
		grid[getPosition().y() + 1][(getPosition().x() + 1) + (getPosition().z() + 1) * 16] = getInputStates()[0] ? 0xffff00 : 0x808000;
	}

	@Override
	protected void updateOutputState()
	{
		updateModel();
	}

	@Override
	protected int getInputCount()
	{
		return 1;
	}

	@Override
	protected int getOutputCount()
	{
		return 0;
	}

	@Override
	protected AbstractGate createCopy()
	{
		return new Light();
	}
}
