package steve6472.polyground.world;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.special.*;
import steve6472.polyground.world.chunk.SubChunkBuilder;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.10.2019
 * Project: SJP
 *
 ***********************/
public class Cull
{
	public static boolean renderFace(int x, int y, int z, EnumFace face, Block middleBlock, World world)
	{
		Block testedBlock = world.getBlock(x + face.getXOffset(), y + face.getYOffset(), z + face.getZOffset());

		if (middleBlock instanceof StairBlock)
		{
			return true;
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
				return testedBlock == Block.air;
			}
		}

		if (!(middleBlock instanceof SlabBlock))
		{
//			if (testedBlock instanceof SlabBlock)
//			{
//				if (!face.isSide())
//				{
//					SlabBlock slabBLock = (SlabBlock) testedBlock;
//					if (slabBLock.slabType == (face == EnumFace.UP ? SlabBlock.EnumSlabType.BOTTOM : SlabBlock.EnumSlabType.TOP))
//						return false;
//				}
//			}
			/*

			Tested in Block instead!

			CubeFace cubeFace = cube.getFace(face);

			if (cubeFace != null && cubeFace.hasProperty(FaceRegistry.conditionedTexture))
			{
				return ConditionFaceProperty.editProperties(cubeFace.getProperty(FaceRegistry.conditionedTexture), cubeFace, x, y, z, subChunk);
			}
			*/

			return SubChunkBuilder.cull(world, x + face.getXOffset(), y + face.getYOffset(), z + face.getZOffset());
		}

		/* Slab */


		if (testedBlock instanceof SlabBlock)
			return true;
/*
		SlabBlock thisSlabBlock = (SlabBlock) middleBlock;
		if (testedBlock instanceof SlabBlock)
		{
			SlabBlock testedSlabBlock = (SlabBlock) testedBlock;

			if (face.isSide())
			{
				return testedSlabBlock.slabType != thisSlabBlock.slabType;
			} else
			{
				return true;
			}
		} else
		{
			if (face == EnumFace.DOWN)
			{
				if (thisSlabBlock.slabType == SlabBlock.EnumSlabType.TOP)
				{
					return true;
				}
			} else if (face == EnumFace.UP)
			{
				if (thisSlabBlock.slabType == SlabBlock.EnumSlabType.BOTTOM)
				{
					return true;
				}
			}
		}*/

		return testedBlock == Block.air || !testedBlock.isFull;
	}
}





