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
	int vao, positionVbo, colorVbo, textureVbo, lightVbo;
	int triangleCount;

	private boolean shouldUpdate = true;

	private ModelLayer modelLayer;

	public SubChunkModel(ModelLayer modelLayer)
	{
		this.modelLayer = modelLayer;
	}

	public void unload()
	{
		delete(vao, positionVbo, colorVbo, textureVbo, lightVbo);
	}

	public void rebuild(SubChunk sc)
	{
		if (!shouldUpdate) return;

		shouldUpdate = false;

		List<Float> vertices = new ArrayList<>(triangleCount * 3);
		List<Float> colors = new ArrayList<>(triangleCount * 4);
		List<Float> textures = new ArrayList<>(triangleCount * 2);
		List<Float> light = new ArrayList<>(triangleCount * 3);

		sc.getParent().getWorld().getPg().buildHelper.load(vertices, colors, textures, light);

		BuildHelper buildHelper = sc.getWorld().getPg().buildHelper;

		triangleCount = 0;

		setupLight(sc);

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
							sc.getParent().getWorld().getPg().buildHelper.load(j, i, k);
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
							System.exit(2);
						}
						return;
					}
				}
			}
		}

		if (CaveGame.getInstance().options.chunkModelDebug && triangleCount != 0)
		{
			System.out.println(String.format("Layer: %s, Triangle Count: %d, Vertices: %d, Colors: %d, Textures: %d, Light: %d", modelLayer, triangleCount,
				vertices.size(), colors.size(), textures.size(), light.size()));
		}

		bindVAO(vao);

		storeFloatDataInAttributeList(0, 3, positionVbo, vertices);
		storeFloatDataInAttributeList(1, 4, colorVbo, colors);
		storeFloatDataInAttributeList(2, 2, textureVbo, textures);
		storeFloatDataInAttributeList(3, 3, lightVbo, light);
	}

	private void setupLight(SubChunk sc)
	{
		for (int i = 0; i < sc.getIds().length; i++)
		{
			for (int j = 0; j < sc.getIds()[i].length; j++)
			{
				for (int k = 0; k < sc.getIds()[i][j].length; k++)
				{
					int id = sc.getIds()[j][i][k];
					BlockData blockData = sc.getBlockData(i, j, k);

					BlockRegistry.getBlockById(id).createLight(j, i, k, sc, blockData);
				}
			}
		}
	}

	public int getVao()
	{
		return vao;
	}

	public boolean shouldUpdate()
	{
		return shouldUpdate;
	}

	public void setShouldUpdate(boolean shouldUpdate)
	{
		this.shouldUpdate = shouldUpdate;
	}
}
