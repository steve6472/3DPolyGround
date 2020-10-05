package steve6472.polyground.block.blockdata;

import steve6472.polyground.block.states.BlockState;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 18.09.2019
 * Project: SJP
 *
 ***********************/
public interface IBlockData
{
	BlockData createNewBlockEntity(BlockState state);
}
