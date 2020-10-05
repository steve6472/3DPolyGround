package steve6472.polyground.world;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.Tags;
import steve6472.polyground.block.model.CubeTags;
import steve6472.polyground.block.properties.enums.EnumSlabType;
import steve6472.polyground.block.special.*;
import steve6472.polyground.block.states.BlockState;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.10.2019
 * Project: SJP
 *
 ***********************/
public class Cull
{
	public static boolean renderFace(int x, int y, int z, EnumFace face, String cubeName, BlockState middleState, World world)
	{
		BlockState testedState = world.getState(x + face.getXOffset(), y + face.getYOffset(), z + face.getZOffset());
		Block testedBlock = testedState.getBlock();

		Block middleBlock = middleState.getBlock();

		if (middleBlock instanceof CustomBlock || testedBlock instanceof CustomBlock || testedState.isCustom())
			return true;

		if (middleBlock instanceof StairBlock)
		{
			if (cubeName.equals(CubeTags.STAIR_TOP))
			{
				if (!basicBlock(testedBlock, testedState))
				{
					return middleState.get(StairBlock.FACING) == face;
				} else
				{
					return true;
				}
			} else if (cubeName.equals(CubeTags.STAIR_BOTTOM))
			{
				if (face == EnumFace.UP)
					return true;
			}
			return basicBlock(testedBlock, testedState);
		}

		if (middleBlock instanceof TorchBlock || testedBlock instanceof TorchBlock)
		{
			return true;
		}

		if (middleBlock instanceof LightSourceBlock c)
		{
			if (!c.isFull)
				return true;
		}

		if (middleBlock instanceof TransparentBlock)
		{
			if (testedBlock instanceof TransparentBlock)
			{
				return testedBlock != middleBlock;
			} else
			{
				return basicBlock(testedBlock, testedState);
			}
		}

		/* Slab */

		if (middleBlock instanceof SlabBlock)
		{
			if (middleState.get(SlabBlock.TYPE) == EnumSlabType.DOUBLE)
				return basicBlock(testedBlock, testedState);

			if (middleState.get(SlabBlock.AXIS) == face.getAxis())
			{
				EnumFace cull = switch (face.getAxis())
					{
						case X -> EnumFace.NORTH;
						case Y -> EnumFace.UP;
						case Z -> EnumFace.WEST;
					};

				if (middleState.get(SlabBlock.TYPE) == EnumSlabType.TOP)
					cull = cull.getOpposite();

				if (face == cull)
				{
					return true;
				} else
				{
					return basicBlock(testedBlock, testedState);
				}
			}
		}

		if (testedBlock instanceof SlabBlock)
		{
			if (testedState.get(SlabBlock.TYPE) == EnumSlabType.DOUBLE)
				return !middleBlock.isFull || middleState.hasTag(Tags.TRANSPARENT);

			if (testedState.get(SlabBlock.AXIS) == face.getAxis())
			{
				EnumFace cull = switch (face.getAxis())
					{
						case X -> EnumFace.NORTH;
						case Y -> EnumFace.UP;
						case Z -> EnumFace.WEST;
					};

				if (testedState.get(SlabBlock.TYPE) == EnumSlabType.TOP)
					cull = cull.getOpposite();

				if (face != cull)
				{
					return true;
				} else
				{
					return !basicBlock(testedBlock, testedState);
				}
			}
		}

		return basicBlock(testedBlock, testedState);
	}

	private static boolean basicBlock(Block block, BlockState state)
	{
		return !block.isFull || state.hasTag(Tags.TRANSPARENT);
	}
}





