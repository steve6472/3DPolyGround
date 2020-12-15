package steve6472.polyground.block.blockdata.logic;

import steve6472.polyground.block.blockdata.logic.data.LogicBlockData;
import steve6472.polyground.block.blockdata.micro.AbstractPickableIndexedMicroBlockData;
import steve6472.polyground.gfx.Palette;
import steve6472.polyground.registry.blockdata.BlockDataRegistry;
import steve6472.polyground.registry.palette.PaletteRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.12.2020
 * Project: CaveGame
 *
 ***********************/
public class ChipDesignerData extends AbstractPickableIndexedMicroBlockData
{
	public boolean isColorSelectorOpen;
	public byte selectedColorIndex;
	public OutputType selectedOutputType = OutputType.OUTPUT;
	public InputType selectedInputType = InputType.INPUT;
	public SelectedType selectedType = SelectedType.INPUT;
	public int selectedInputIndex, selectedOutputIndex;
	public LogicBlockData chip;

	public ChipDesignerData()
	{
		grid = new byte[getSize()][getSize() * getSize()];
	}

	public ChipDesignerData(byte[][] grid)
	{
		this.grid = grid;
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
		INPUT, OUTPUT
	}
}
