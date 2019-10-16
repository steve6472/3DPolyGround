package com.steve6472.polyground.world;

import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.block.special.TransparentBlock;

import java.util.ArrayList;
import java.util.List;

import static com.steve6472.sge.gfx.VertexObjectCreator.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.08.2019
 * Project: SJP
 *
 ***********************/
public class SubChunkBuilder
{
	static void init(SubChunk sc)
	{
		sc.vao = createVAO();
		sc.positionVbo = storeDataInAttributeList(0, 3, new float[] {-1, 0, 1, -1, 0, -1, 1, 0, -1});
		sc.colorVbo = storeDataInAttributeList(1, 4, new float[] {1, 1, 1, 1});
		sc.textureVbo = storeDataInAttributeList(2, 2, new float[] {0, 0, 0, 1, 1, 1});
		sc.emissiveVbo = storeDataInAttributeList(3, 1, new float[1]);
	}

	public static boolean cull(SubChunk sc, int x, int y, int z)
	{
		if (y < 0 || y >= 16)
		{
			if (y < 0 && sc.getLayer() == 0)
				return true;
			else if (y >= 16 && sc.getLayer() >= sc.getParent().getSubChunks().length - 1)
				return true;
			else if (y < 0)
				return cull(sc.getParent().getSubChunk(sc.getLayer() - 1), x, 15, z);
			else
			{
				return cull(sc.getParent().getSubChunk(sc.getLayer() + 1), x, 0, z);
			}
		}
		else if (x < 0 || z < 0 || z >= 16 || x >= 16)
			return true;
		else
		{
			Block b = BlockRegistry.getBlockById(sc.getIds()[x][y][z]);
			return !b.isFull || (b instanceof TransparentBlock);
		}
	}

//	public static boolean cull(SubChunk sc, int x, int y, int z, EnumFace face)
//	{
//
//	}

	static void rebuild(SubChunk sc)
	{
		List<Float> vertices = new ArrayList<>();
		List<Float> colors = new ArrayList<>();
		List<Float> textures = new ArrayList<>();
		List<Float> emissive = new ArrayList<>();

		sc.triangleCount = 0;

		for (int i = 0; i < sc.getIds().length; i++)
		{
			for (int j = 0; j < sc.getIds()[i].length; j++)
			{
				for (int k = 0; k < sc.getIds()[i][j].length; k++)
				{
					int id = sc.getIds()[j][i][k];
					BlockData blockData = sc.getBlockData(i, j, k);

					Block b = BlockRegistry.getBlockById(id);

					try
					{
						if (b != null && b != Block.air)
						{
							sc.getParent().getWorld().getPg().buildHelper.load(j, i, k, vertices, colors, textures, emissive);
							sc.triangleCount += b.createModel(j, i, k, sc, blockData, sc.getParent().getWorld().getPg().buildHelper);
						}

					} catch (Exception ex)
					{
						System.err.println("Error while building chunk with block '" + b.getName() + "' id '" + b.getId() + "'");
						ex.printStackTrace();
						CaveGame.getInstance().exit();
						return;
					}
				}
			}
		}

		bindVAO(sc.vao);

		storeDataInAttributeList(0, 3, sc.positionVbo, vertices);
		storeDataInAttributeList(1, 4, sc.colorVbo, colors);
		storeDataInAttributeList(2, 2, sc.textureVbo, textures);
		storeDataInAttributeList(3, 1, sc.emissiveVbo, emissive);

		unbindVAO();
	}
}
