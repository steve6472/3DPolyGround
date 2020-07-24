package steve6472.polyground.world.chunk;

import steve6472.polyground.block.Block;
import steve6472.polyground.block.special.TransparentBlock;
import steve6472.polyground.world.World;

import static steve6472.sge.gfx.VertexObjectCreator.createVAO;
import static steve6472.sge.gfx.VertexObjectCreator.storeFloatDataInAttributeList;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.08.2019
 * Project: SJP
 *
 ***********************/
public class SubChunkBuilder
{
	static void init(SubChunkModel sc)
	{
		sc.vao = createVAO();
		sc.positionVbo = storeFloatDataInAttributeList(0, 3, new float[]{-1, 0, 1, -1, 0, -1, 1, 0, -1});
		sc.colorVbo = storeFloatDataInAttributeList(1, 4, new float[]{1, 1, 1, 1});
		sc.textureVbo = storeFloatDataInAttributeList(2, 2, new float[]{0, 0, 0, 1, 1, 1});
		sc.vboNorm = storeFloatDataInAttributeList(3, 3, new float[]{0, 0, 0, 1, 1, 1, 0, 0, 0});
	}

	public static boolean cull(World world, int x, int y, int z)
	{
		Block b = world.getBlock(x, y, z);
		return !b.isFull || (b instanceof TransparentBlock);
	}
}
