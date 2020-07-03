package steve6472.polyground.block.special;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.model.Cube;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.Player;
import steve6472.polyground.world.chunk.SubChunk;

import java.io.File;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 21.09.2019
 * Project: SJP
 *
 ***********************/
public class StairBlock extends Block
{
	public StairBlock(File f, int id)
	{
		super(f, id);
		isFull = false;
	}

	@Override
	public void onPlace(SubChunk subChunk, BlockState state, Player player, EnumFace placedOn, int x, int y, int z)
	{
		super.onPlace(subChunk, state, player, placedOn, x, y, z);
//		if (!(state instanceof RotationData))
//		{
//			subChunk.rebuild();
//			return;
//		}
//
//		RotationData data = (RotationData) state;
//		data.facing = placedOn;
//
//
//		subChunk.rebuild();
	}

	@Override
	public List<Cube> getCubes(int x, int y, int z)
	{
		//		World world = CaveGame.getInstance().world;
		//
		//		SubChunk subChunk = world.getSubChunkFromBlockCoords(x, y, z);
		//		RotationData data = (RotationData) subChunk.getBlockData(x, y, z);
		//
		return super.getCubes(x, y, z);
	}

//	@Override
//	public BlockData createNewBlockEntity()
//	{
//		return new RotationData(EnumFace.NORTH);
//	}

	@Override
	public boolean rebuildChunkOnPlace()
	{
		return false;
	}
}
