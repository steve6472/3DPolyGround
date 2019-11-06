package com.steve6472.polyground.world;

import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.special.TransparentBlock;

import static com.steve6472.sge.gfx.VertexObjectCreator.createVAO;
import static com.steve6472.sge.gfx.VertexObjectCreator.storeDataInAttributeList;

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
		sc.positionVbo = storeDataInAttributeList(0, 3, new float[] {-1, 0, 1, -1, 0, -1, 1, 0, -1});
		sc.colorVbo = storeDataInAttributeList(1, 4, new float[] {1, 1, 1, 1});
		sc.textureVbo = storeDataInAttributeList(2, 2, new float[] {0, 0, 0, 1, 1, 1});
		sc.emissiveVbo = storeDataInAttributeList(3, 1, new float[1]);
	}

	public static boolean cull(SubChunk sc, int x, int y, int z)
	{
		Block b = sc.getBlockEfficiently(x, y, z);
		return !b.isFull || (b instanceof TransparentBlock);
	}
}
