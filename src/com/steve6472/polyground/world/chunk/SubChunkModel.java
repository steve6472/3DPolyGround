package com.steve6472.polyground.world.chunk;

import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.world.BuildHelper;

import java.util.ArrayList;
import java.util.List;

import static com.steve6472.sge.gfx.VertexObjectCreator.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.11.2019
 * Project: SJP
 *
 ***********************/
public class SubChunkModel
{
	/* Model Data */
	int vao, positionVbo, colorVbo, textureVbo;
	int triangleCount;

	private ModelLayer modelLayer;

	public SubChunkModel(ModelLayer modelLayer)
	{
		this.modelLayer = modelLayer;
	}

	public void unload()
	{
		delete(vao, positionVbo, colorVbo, textureVbo);
	}

	public void rebuild(SubChunk sc)
	{
		List<Float> vertices = new ArrayList<>();
		List<Float> colors = new ArrayList<>();
		List<Float> textures = new ArrayList<>();
		BuildHelper buildHelper = sc.getWorld().getPg().buildHelper;

		triangleCount = 0;

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
							sc.getParent().getWorld().getPg().buildHelper.load(j, i, k, vertices, colors, textures);
							triangleCount += b.createModel(j, i, k, sc, blockData, buildHelper, modelLayer);
						}

					} catch (Exception ex)
					{
						System.err.println("Error while building chunk with block '" + b.getName() + "' id '" + b.getId() + "'");
						ex.printStackTrace();
						try
						{
							triangleCount += Block.error.createModel(j, i, k, sc, blockData, buildHelper, modelLayer);
						} catch (Exception ex1)
						{
							System.err.println("Error while building chunk error block!\nFrick! This should not happen :(");
							ex.printStackTrace();
							CaveGame.getInstance().exit();
						}
						return;
					}
				}
			}
		}

		bindVAO(vao);

		storeFloatDataInAttributeList(0, 3, positionVbo, vertices);
		storeFloatDataInAttributeList(1, 4, colorVbo, colors);
		storeFloatDataInAttributeList(2, 2, textureVbo, textures);

		unbindVAO();
	}

	public int getVao()
	{
		return vao;
	}
}