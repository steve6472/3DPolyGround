package com.steve6472.polyground.world;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.model.CubeFace;
import com.steve6472.polyground.block.model.faceProperty.condition.ConditionFaceProperty;
import com.steve6472.polyground.block.model.registry.Cube;
import com.steve6472.polyground.block.model.registry.face.FaceRegistry;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.block.special.SlabBlock;
import com.steve6472.polyground.block.special.StairBlock;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.10.2019
 * Project: SJP
 *
 ***********************/
public class Cull
{
	public static boolean renderFace(int x, int y, int z, Cube cube, EnumFace face, Block middleBlock, SubChunk subChunk)
	{
		Block testedBlock = getBlock(subChunk, x + face.getXOffset(), y + face.getYOffset(), z + face.getZOffset());

		if (middleBlock instanceof StairBlock)
		{
			return true;
		}

		if (!(middleBlock instanceof SlabBlock))
		{
			if (testedBlock instanceof SlabBlock)
			{
				if (!face.isSide())
				{
					SlabBlock slabBLock = (SlabBlock) testedBlock;
					if (slabBLock.slabType == (face == EnumFace.UP ? SlabBlock.EnumSlabType.BOTTOM : SlabBlock.EnumSlabType.TOP))
						return false;
				}
			}

			CubeFace cubeFace = cube.getFace(face);

			if (cubeFace != null && cubeFace.hasProperty(FaceRegistry.conditionedTexture))
			{
				return ConditionFaceProperty.editProperties(cubeFace.getProperty(FaceRegistry.conditionedTexture), cubeFace, face, x, y, z, subChunk);
			}

			return SubChunkBuilder.cull(subChunk, x + face.getXOffset(), y + face.getYOffset(), z + face.getZOffset());
		}

		/* Slab */

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
		}

		return testedBlock == Block.air || !testedBlock.isFull;
	}

	private static Block getBlock(SubChunk sc, int x, int y, int z)
	{
		if (y < 0 || y >= 16)
		{
			if (y < 0 && sc.getLayer() == 0)
				return Block.air;
			else if (y >= 16 && sc.getLayer() >= sc.getParent().getSubChunks().length - 1)
				return Block.air;
			else if (y < 0)
				return getBlock(sc.getParent().getSubChunk(sc.getLayer() - 1), x, 15, z);
			else
			{
				return getBlock(sc.getParent().getSubChunk(sc.getLayer() + 1), x, 0, z);
			}
		}
		else if (x < 0 || z < 0 || z >= 16 || x >= 16)
			return Block.air;
		else
		{
			return BlockRegistry.getBlockById(sc.getIds()[x][y][z]);
		}
	}
}
