package steve6472.polyground.block.blockdata.logic.other;

import org.joml.Vector3i;
import steve6472.polyground.block.blockdata.logic.AbstractGate;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 28.11.2020
 * Project: CaveGame
 *
 ***********************/
public class SevenSegmentDisplay extends AbstractGate
{
	private static final int[][] MODEL =
		{
			{
				0x303030, 0x303030, 0x303030, 0x303030,
				0x303030, 0x303030, 0x303030, 0x303030,
				0x303030, 0x303030, 0x303030, 0x303030,
				0x303030, 0x303030, 0x303030, 0x303030,
				0x303030, 0x303030, 0x303030, 0x303030,
				0x303030, 0x303030, 0x303030, 0x303030,
				0x303030, 0x303030, 0x303030, 0x303030
			},
			{
				0x303030, 0x303030, 0x303030, 0x303030,
				0x303030, 0x303030, 0x303030, 0x303030,
				0x303030, 0x303030, 0x303030, 0x303030,
				0x303030, 0x303030, 0x303030, 0x303030,
				0x303030, 0x303030, 0x303030, 0x303030,
				0x303030, 0x303030, 0x303030, 0x303030,
				0x303030, 0x303030, 0x303030, 0x303030
			}
		};

	public SevenSegmentDisplay()
	{
		super("seven_segment_display");
	}

	@Override
	protected Vector3i[] inputPositions()
	{
		return new Vector3i[] {new Vector3i(6, 0, 0), new Vector3i(4, 0, 0), new Vector3i(2, 0, 0), new Vector3i(0, 0, 0), new Vector3i(5, 0, 3), new Vector3i(3, 0, 3), new Vector3i(1, 0, 3)};
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
	protected void updateOutputState()
	{
		updateModel();
	}

	@Override
	public void updateModel(int[][] grid)
	{
		super.updateModel(grid);
		// A (Top)
		pixel(grid, 6, 1, 1, color(0));
		pixel(grid, 6, 1, 2, color(0));
		// B (Top-Right)
		pixel(grid, 5, 1, 3, color(1));
		pixel(grid, 4, 1, 3, color(1));
		// C (Bottom-Right)
		pixel(grid, 2, 1, 3, color(2));
		pixel(grid, 1, 1, 3, color(2));
		// D (Bottom)
		pixel(grid, 0, 1, 1, color(3));
		pixel(grid, 0, 1, 2, color(3));
		// E (Bottom-Left)
		pixel(grid, 1, 1, 0, color(4));
		pixel(grid, 2, 1, 0, color(4));
		// F (Top-Left)
		pixel(grid, 4, 1, 0, color(5));
		pixel(grid, 5, 1, 0, color(5));
		// G (Middle)
		pixel(grid, 3, 1, 1, color(6));
		pixel(grid, 3, 1, 2, color(6));
	}

	private int color(int inputStateIndex)
	{
		return getInputStates()[inputStateIndex] ? 0xf30000 : 0xcccccc;
	}

	private void pixel(int[][] grid, int rx, int ry, int rz, int color)
	{
		grid[getPosition().y() + ry][(getPosition().x() + rx) + (getPosition().z() + rz) * logicData.size()] = color;
	}

	@Override
	protected int getInputCount()
	{
		return 7;
	}

	@Override
	protected int getOutputCount()
	{
		return 0;
	}

	@Override
	protected AbstractGate createCopy()
	{
		return new SevenSegmentDisplay();
	}
}
