package steve6472.polyground.block.blockdata.logic;

import steve6472.polyground.block.blockdata.micro.AbstractPickableIndexedMicroBlockData;
import steve6472.polyground.registry.blockdata.BlockDataRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.12.2020
 * Project: CaveGame
 *
 ***********************/
public class ChipDesignerData extends AbstractPickableIndexedMicroBlockData
{
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
	protected int getSize()
	{
		return 32;
	}

	@Override
	public String getId()
	{
		return BlockDataRegistry.chipDesigner.id();
	}
}
