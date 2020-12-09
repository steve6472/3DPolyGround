package steve6472.polyground.block.blockdata;

import steve6472.polyground.block.blockdata.micro.AbstractIndexedMicroBlockData;
import steve6472.polyground.registry.blockdata.BlockDataRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.12.2020
 * Project: CaveGame
 *
 ***********************/
public class PaletteTestData extends AbstractIndexedMicroBlockData
{
	@Override
	public String getId()
	{
		return BlockDataRegistry.paletteTestData.id();
	}
}
