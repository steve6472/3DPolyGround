package steve6472.polyground.world;

import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.HitResult;
import steve6472.polyground.TimedBlockPos;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.ISpecialRender;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.IBlockData;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.IBlockState;
import steve6472.polyground.world.chunk.Chunk;
import steve6472.polyground.world.chunk.SubChunk;

import java.util.function.Function;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.09.2019
 * Project: SJP
 *
 ***********************/
public interface IWorldBlockProvider extends IChunkProvider
{
	default BlockState getState(int x, int y, int z)
	{
		Chunk c = getChunkFromBlockPos(x, z);

		if (c == null)
			return Block.AIR.getDefaultState();
		else
		{
			int cx = Math.floorMod(x, 16);
			int cy = y % 16;
			int cz = Math.floorMod(z, 16);

			if (isOutOfChunkBounds(getWorld(), cx, y, cz))
				return Block.AIR.getDefaultState();

			SubChunk sc = c.getSubChunk(y / 16);
			return sc.getState(cx, cy, cz);
		}
	}

	private static boolean isOutOfChunkBounds(World world, int x, int y, int z)
	{
		return x < 0 || x >= 16 || z < 0 || z >= 16 || y < 0 || y >= world.getHeight() * 16;
	}

	default void setState(IBlockState state, int x, int y, int z)
	{
		setState(state.get(), x, y, z, 1);
	}

	/**
	 * Flags:
	 * 1 - Cause a block update
	 * 2 - Prevent the block from being re-rendered
	 * 4 - validate if state can be placed
	 * 8 - Prevent creating new block data
	 * //16 - Re-render on the main thread
	 *
	 * @param state new State
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 */
	default void setState(IBlockState state, int x, int y, int z, int flags)
	{
		Chunk c = getChunkFromBlockPos(x, z);
		if (c != null)
		{
			int cx = Math.floorMod(x, 16);
			int cy = y % 16;
			int cz = Math.floorMod(z, 16);

			if (isOutOfChunkBounds(getWorld(), cx, y, cz))
				return;

			SubChunk sc = c.getSubChunk(y / 16);

			BlockState oldState = sc.getState(cx, cy, cz);
			if (oldState == state.get())
				return;

			boolean shouldRebuild = (flags & 2) == 0;
			if (shouldRebuild)
			{
//				sc.rebuild();
				//TODO: maybe check if cx || cy || cz are on border?
				for (EnumFace face : EnumFace.getValues())
				{
					SubChunk s = getSubChunkFromBlockCoords(x + face.getXOffset(), y + face.getYOffset(), z + face.getZOffset());
					if (s != null)
						s.rebuild();
				}
			}

			boolean canBePlaced = true;

			// Validate state
			if ((flags & 4) != 0)
			{
				canBePlaced = state.get().getBlock().isValidPosition(state.get(), getWorld(), x, y, z);
			}

			if (canBePlaced)
			{
				sc.setState(state.get(), cx, cy, cz);
				state.get().getBlock().onBlockAdded(state.get(), getWorld(), oldState, x, y, z);
				sc.getTickableBlocks().set(cx, cy, cz, state.get().getBlock().isTickable());

				if ((flags & 8) != 8)
				{
					if (state.get().getBlock() instanceof IBlockData)
					{
						BlockData data = ((IBlockData) state.get().getBlock()).createNewBlockEntity(state.get());
						sc.setBlockData(data, cx, cy, cz);
					} else
					{
						sc.setBlockData(null, cx, cy, cz);
					}

					sc.getSpecialRender().set(cx, cy, cz, state.get().getBlock() instanceof ISpecialRender);
				}

				if ((flags & 1) != 0)
				{
					for (EnumFace face : EnumFace.getFaces())
					{
						BlockState stateToUpdate = getWorld().getState(x + face.getXOffset(), y + face.getYOffset(), z + face.getZOffset());
						stateToUpdate.getBlock().neighbourChange(stateToUpdate, getWorld(), face.getOpposite(), x + face.getXOffset(), y + face.getYOffset(), z + face.getZOffset());

						if (CaveGame.getInstance().options.renderNeighbourChange)
						{
							if (Thread.currentThread().getId() == CaveGame.MAIN_THREAD && !stateToUpdate.isAir())
								CaveGame.getInstance().options.renderNeighbourChangeList.add(new TimedBlockPos(x + face.getXOffset(), y + face.getYOffset(), z + face.getZOffset(), 30));
						}
					}
				}
			}
		}
	}

	default Block getBlock(int x, int y, int z)
	{
		return getState(x, y, z).getBlock();
	}

	default void setBlock(Block block, int x, int y, int z)
	{
		setState(block.getDefaultState(), x, y, z);
	}

	default void setBlock(Block block, int x, int y, int z, int flags)
	{
		setState(block.getDefaultState(), x, y, z, flags);
	}

	/**
	 * @param x      x coordinate
	 * @param y      y coordinate
	 * @param z      z coordinate
	 * @param canSet Type is id of block at xyz
	 */
	default void setBlock(Block block, int x, int y, int z, Function<Block, Boolean> canSet)
	{
		if (canSet.apply(getBlock(x, y, z)))
			setBlock(block, x, y, z);
	}

	default void setState(IBlockState state, int x, int y, int z, Function<BlockState, Boolean> canSet)
	{
		if (canSet.apply(getState(x, y, z)))
			setState(state.get(), x, y, z);
	}

	default void setBlock(HitResult hitResult, Block block)
	{
		setBlock(block, hitResult.getX(), hitResult.getY(), hitResult.getZ());
	}

	default void setState(IBlockState state, HitResult hitResult)
	{
		setState(state.get(), hitResult.getX(), hitResult.getY(), hitResult.getZ());
	}

	/*
	 * Data
	 */

	default void setData(BlockData data, int x, int y, int z)
	{
		Chunk c = getChunkFromBlockPos(x, z);

		if (c != null)
		{
			int cx = Math.floorMod(x, 16);
			int cy = y % 16;
			int cz = Math.floorMod(z, 16);

			if (isOutOfChunkBounds(getWorld(), cx, y, cz))
				return;

			SubChunk sc = c.getSubChunk(y / 16);
			sc.setBlockData(data, cx, cy, cz);
		}
	}

	default BlockData getData(int x, int y, int z)
	{
		Chunk c = getChunkFromBlockPos(x, z);

		if (c == null)
			return null;
		else
		{
			int cx = Math.floorMod(x, 16);
			int cy = y % 16;
			int cz = Math.floorMod(z, 16);

			if (isOutOfChunkBounds(getWorld(), cx, y, cz))
				return null;

			SubChunk sc = c.getSubChunk(y / 16);
			return sc.getBlockData(cx, cy, cz);
		}
	}

	// Water
	default void setLiquidVolume(int x, int y, int z, double amount)
	{
		getSubChunk(x, y, z).setLiquidVolume(Math.floorMod(x, 16), Math.floorMod(y, 16), Math.floorMod(z, 16), amount);
	}

	default double getLiquidVolume(int x, int y, int z)
	{
		return getSubChunk(x, y, z).getLiquidVolume(Math.floorMod(x, 16), Math.floorMod(y, 16), Math.floorMod(z, 16));
	}

	World getWorld();
}
