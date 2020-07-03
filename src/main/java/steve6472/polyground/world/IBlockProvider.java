package steve6472.polyground.world;

import steve6472.polyground.HitResult;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.chunk.Chunk;

import java.util.function.Function;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.09.2019
 * Project: SJP
 *
 ***********************/
public interface IBlockProvider extends IChunkProvider
{
	default BlockState getState(int x, int y, int z)
	{
		Chunk c = getChunkFromBlockPos(x, z);
		if (c == null)
			return Block.air.getDefaultState();
		else
			return c.getState(Math.floorMod(x, 16), y, Math.floorMod(z, 16));
	}

	default void setState(BlockState state, int x, int y, int z)
	{
		Chunk c = getChunkFromBlockPos(x, z);
		if (c != null)
			c.setState(state, Math.floorMod(x, 16), y, Math.floorMod(z, 16));
	}

	default Block getBlock(int x, int y, int z)
	{
		Chunk c = getChunkFromBlockPos(x, z);
		if (c == null)
			return Block.air;
		else
			return c.getBlock(Math.floorMod(x, 16), y, Math.floorMod(z, 16));
	}

	default void setBlock(Block block, int x, int y, int z)
	{
		Chunk c = getChunkFromBlockPos(x, z);
		if (c != null)
			c.setBlock(block, Math.floorMod(x, 16), y, Math.floorMod(z, 16));
	}

	/**
	 * @param x      x coordinate
	 * @param y      y coordinate
	 * @param z      z coordinate
	 * @param canSet Type is id of block at xyz
	 */
	default void setBlock(Block block, int x, int y, int z, Function<Block, Boolean> canSet)
	{
		Chunk c = getChunkFromBlockPos(x, z);
		if (c != null)
			if (canSet.apply(c.getBlock(Math.floorMod(x, 16), y, Math.floorMod(z, 16))))
				c.setBlock(block, Math.floorMod(x, 16), y, Math.floorMod(z, 16));
	}

	default void setBlock(HitResult hitResult, Block block)
	{
		setBlock(block, hitResult.getX(), hitResult.getY(), hitResult.getZ());
	}
}
