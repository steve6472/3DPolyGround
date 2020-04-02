package steve6472.polyground.world;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.model.CubeFace;
import steve6472.polyground.block.model.faceProperty.condition.ConditionFaceProperty;
import steve6472.polyground.block.model.registry.Cube;
import steve6472.polyground.block.model.registry.face.FaceRegistry;
import steve6472.polyground.block.registry.BlockRegistry;
import steve6472.polyground.block.special.*;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.polyground.world.chunk.SubChunkBuilder;

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
				return ConditionFaceProperty.editProperties(cubeFace.getProperty(FaceRegistry.conditionedTexture), cubeFace, x, y, z, subChunk);
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
		if (x >= 0 && x < 16 && z >= 0 && z < 16 && y >= 0 && y < 16)
		{
			return BlockRegistry.getBlockById(sc.getIds()[x][y][z]);
		} else
		{
			SubChunk subChunk = sc.getNeighbouringSubChunk(x, y, z);
			if (subChunk == null)
				return Block.air;
			return BlockRegistry.getBlockById(subChunk.getIds()[Math.floorMod(x, 16)][Math.floorMod(y, 16)][Math.floorMod(z, 16)]);
		}
	}
}
