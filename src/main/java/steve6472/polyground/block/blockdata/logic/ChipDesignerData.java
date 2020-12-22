package steve6472.polyground.block.blockdata.logic;

import steve6472.polyground.block.blockdata.logic.other.Input;
import steve6472.polyground.block.blockdata.logic.other.Output;
import steve6472.polyground.block.blockdata.micro.AbstractPickableIndexedMicroBlockData;
import steve6472.polyground.block.model.elements.Bakery;
import steve6472.polyground.gfx.Palette;
import steve6472.polyground.registry.blockdata.BlockDataRegistry;
import steve6472.polyground.registry.palette.PaletteRegistry;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.12.2020
 * Project: CaveGame
 *
 ***********************/
public class ChipDesignerData extends AbstractPickableIndexedMicroBlockData
{
	public byte selectedColorIndex;
	public OutputType selectedOutputType = OutputType.OUTPUT;
	public InputType selectedInputType = InputType.INPUT;
	public SelectedType selectedType = SelectedType.INPUT;
	public int selectedInputIndex, selectedOutputIndex;
	public List<AbstractGate> chipComponents, inputComponents, outputComponents;
	public int[][] chipModel;

	public ChipDesignerData()
	{
		grid = new byte[getSize()][getSize() * getSize()];
		chipModel = new int[22][22 * 22];
	}

	public ChipDesignerData(byte[][] grid)
	{
		this.grid = grid;
		chipModel = new int[22][22 * 22];
		updateModel();
	}

	@Override
	public Palette getPalette()
	{
		return PaletteRegistry.RAINBOW;
	}

	@Override
	protected int getSize()
	{
		return 32;
	}

	/**
	 * Creates COPY of the list
	 * @param components Components to copy
	 */
	public void setChipComponents(List<AbstractGate> components)
	{
		chipComponents = new ArrayList<>();
		inputComponents = new ArrayList<>();
		outputComponents = new ArrayList<>();

		for (AbstractGate component : components)
		{
			final AbstractGate copy = component.copy();
			chipComponents.add(copy);

			if (copy instanceof Input)
				inputComponents.add(copy);
			if (copy instanceof Output)
				outputComponents.add(copy);
		}

		AbstractGate.fixConnections(chipComponents, components);
	}

	public AbstractGate getInputGateAt(int x, int y, int z)
	{
		return getGateIndex(x, y, z, inputComponents);
	}

	public AbstractGate getOutputGateAt(int x, int y, int z)
	{
		return getGateIndex(x, y, z, outputComponents);
	}

	protected AbstractGate getGateIndex(int x, int y, int z, List<AbstractGate> components)
	{
		for (AbstractGate g : components)
		{
			if (g.getPosition().equals(x, y, z))
				return g;
		}

		return null;
	}

	@Override
	public void updateModel()
	{
		model.load((modelBuilder) -> {

			Bakery.tempBuilder(modelBuilder);

			int tris = 0;

			for (int i = 0; i < getSize(); i++)
			{
				for (int j = 0; j < getSize(); j++)
				{
					for (int k = 0; k < getSize(); k++)
					{
						if (grid[j][i + k * getSize()] != -128)
						{
							int flags = Bakery.createFaceFlags(
								i != (getSize() - 1) && grid[j][(i + 1) + k * getSize()] != -128,
								k != (getSize() - 1) && grid[j][i + (k + 1) * getSize()] != -128,
								i != 0 && grid[j][(i - 1) + k * getSize()] != -128,
								k != 0 && grid[j][i + (k - 1) * getSize()] != -128,
								j != (getSize() - 1) && grid[j + 1][i + k * getSize()] != -128,
								j != 0 && grid[j - 1][i + k * getSize()] != -128
							);
							int color = getPalette().getColors()[grid[j][i + k * getSize()] + 128];
							tris += Bakery.coloredCube_1x1(i, j, k, color, flags);
						}
					}
				}
			}

			if (chipModel != null)
			{
				for (int i = 0; i < 22; i++)
				{
					for (int j = 0; j < 22; j++)
					{
						for (int k = 0; k < 22; k++)
						{
							if (chipModel[j][i + k * 22] != 0)
							{
								int flags = Bakery.createFaceFlags(
									i != (22 - 1) && chipModel[j][(i + 1) + k * 22] != 0,
									k != (22 - 1) && chipModel[j][i + (k + 1) * 22] != 0,
									i != 0 && chipModel[j][(i - 1) + k * 22] != 0,
									k != 0 && chipModel[j][i + (k - 1) * 22] != 0,
									j != (22 - 1) && chipModel[j + 1][i + k * 22] != 0,
									j != 0 && chipModel[j - 1][i + k * 22] != 0
								);
								tris += Bakery.coloredCube_1x1(i + 2, j + 10, k + 2, chipModel[j][i + k * 22], flags);
							}
						}
					}
				}
			}

			Bakery.worldBuilder();

			return tris;
		});
	}

	@Override
	public String getId()
	{
		return BlockDataRegistry.chipDesigner.id();
	}

	public enum OutputType
	{
		OUTPUT, LIGHT;

		private static final OutputType[] TYPES = {OUTPUT, LIGHT};

		public OutputType next()
		{
			return TYPES[(this.ordinal() + 1) % TYPES.length];
		}
	}

	public enum InputType
	{
		INPUT, SWITCH;

		private static final InputType[] TYPES = {INPUT, SWITCH};

		public InputType next()
		{
			return TYPES[(this.ordinal() + 1) % TYPES.length];
		}
	}

	public enum SelectedType
	{
		INPUT, OUTPUT, COLOR
	}
}
