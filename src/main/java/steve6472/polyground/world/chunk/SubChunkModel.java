package steve6472.polyground.world.chunk;

import steve6472.polyground.CaveGame;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.registry.BlockRegistry;
import steve6472.polyground.world.BuildHelper;

import java.util.ArrayList;
import java.util.List;

import static steve6472.sge.gfx.VertexObjectCreator.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.11.2019
 * Project: SJP
 *
 ***********************/
public class SubChunkModel
{
	private static Thread buildThread;
	private static List<SubChunkModel> toBuild;
	private static List<ModelData> modelData;
	private static boolean canAdd;
	public static int toBuildCount;

	static
	{
		toBuild = new ArrayList<>();
		modelData = new ArrayList<>();
		buildThread = new Thread()
		{
			@Override
			public void run()
			{
				while (true)
				{
					if (!toBuild.isEmpty())
					{
						SubChunkModel current = toBuild.get(0);

						if (current == null)
						{
							toBuild.remove(0);
							continue;
						}

						List<Float> vertices = new ArrayList<>(current.triangleCount * 3);
						List<Float> colors = new ArrayList<>(current.triangleCount * 4);
						List<Float> textures = new ArrayList<>(current.triangleCount * 2);
						List<Float> normal = new ArrayList<>(current.triangleCount * 3);

						BuildHelper buildHelper = current.subChunk.getWorld().getGame().mainRender.buildHelper;

						buildHelper.load(vertices, colors, textures, normal);

//						current.triangleCount = 0;
						int triangleCount = 0;

						for (int i = 0; i < current.subChunk.getIds().length; i++)
						{
							for (int j = 0; j < current.subChunk.getIds()[i].length; j++)
							{
								for (int k = 0; k < current.subChunk.getIds()[i][j].length; k++)
								{
									int id = current.subChunk.getIds()[j][i][k];
									BlockData blockData = current.subChunk.getBlockData(i, j, k);

									Block b = BlockRegistry.getBlockById(id);

									try
									{
										if (b != null && b != Block.air)
										{
											buildHelper.load(j, i, k);
											triangleCount += b.createModel(j, i, k, current.subChunk, blockData, buildHelper, current.modelLayer);
										}

									} catch (Exception ex)
									{
										System.err.println("Error while building chunk with block '" + b.getName() + "' id '" + b.getId() + "'");
										ex.printStackTrace();
										try
										{
											triangleCount += Block.error.createModel(j, i, k, current.subChunk, blockData, buildHelper, current.modelLayer);
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
							System.out.println(String.format("LAZY: Layer: %s, Triangle Count: %d, Vertices: %d, Colors: %d, Textures: %d", current.modelLayer, current.triangleCount, vertices.size(), colors.size(), textures.size()));
						}

						while (!canAdd)
						{
							try
							{
								Thread.sleep(1);
							} catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						}

						modelData.add(new ModelData(vertices, colors, textures, normal, current, triangleCount));
						toBuild.remove(current);
					}

					try
					{
						Thread.sleep(1);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		};
		buildThread.start();
	}

	public static void upload()
	{
		canAdd = false;
		try
		{
			// Sleep just in case the buildThread is adding model data AFTER the check
			Thread.sleep(1);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		for (ModelData data : modelData)
		{
			bindVAO(data.model.vao);

			storeFloatDataInAttributeList(0, 3, data.model.positionVbo, data.vert);
			storeFloatDataInAttributeList(1, 4, data.model.colorVbo, data.color);
			storeFloatDataInAttributeList(2, 2, data.model.textureVbo, data.text);
			storeFloatDataInAttributeList(3, 3, data.model.vboNorm, data.normal);
			data.model.triangleCount = data.triangleCount;
		}
		modelData.clear();
		canAdd = true;
		toBuildCount = toBuild.size();
	}

	private static class ModelData
	{
		List<Float> vert, color, text, normal;
		SubChunkModel model;
		int triangleCount;

		public ModelData(List<Float> vert, List<Float> color, List<Float> text, List<Float> normal, SubChunkModel model, int triangleCount)
		{
			this.vert = vert;
			this.color = color;
			this.text = text;
			this.normal = normal;
			this.model = model;
			this.triangleCount = triangleCount;
		}
	}

	/* Model Data */ int vao, positionVbo, colorVbo, textureVbo, vboNorm;
	int triangleCount;

	private boolean shouldUpdate = true;

	private final ModelLayer modelLayer;
	private final SubChunk subChunk;

	public SubChunkModel(ModelLayer modelLayer, SubChunk subChunk)
	{
		this.modelLayer = modelLayer;
		this.subChunk = subChunk;
	}

	public void unload()
	{
		delete(vao, positionVbo, colorVbo, textureVbo, vboNorm);
	}

	public void rebuild(boolean lazy)
	{
		if (!shouldUpdate)
			return;

		shouldUpdate = false;

		if (lazy)
		{
			if (!toBuild.contains(this))
				toBuild.add(this);
			return;
		}

		List<Float> vertices = new ArrayList<>(triangleCount * 3);
		List<Float> colors = new ArrayList<>(triangleCount * 4);
		List<Float> textures = new ArrayList<>(triangleCount * 2);
		List<Float> normal = new ArrayList<>(triangleCount * 3);

		subChunk.getParent().getWorld().getGame().mainRender.buildHelper.load(vertices, colors, textures, normal);

		BuildHelper buildHelper = subChunk.getWorld().getGame().mainRender.buildHelper;

		triangleCount = 0;

		for (int i = 0; i < subChunk.getIds().length; i++)
		{
			for (int j = 0; j < subChunk.getIds()[i].length; j++)
			{
				for (int k = 0; k < subChunk.getIds()[i][j].length; k++)
				{
					int id = subChunk.getIds()[j][i][k];
					BlockData blockData = subChunk.getBlockData(i, j, k);

					Block b = BlockRegistry.getBlockById(id);

					try
					{
						if (b != null && b != Block.air)
						{
							subChunk.getParent().getWorld().getGame().mainRender.buildHelper.load(j, i, k);
							triangleCount += b.createModel(j, i, k, subChunk, blockData, buildHelper, modelLayer);
						}

					} catch (Exception ex)
					{
						System.err.println("Error while building chunk with block '" + b.getName() + "' id '" + b.getId() + "'");
						ex.printStackTrace();
						try
						{
							triangleCount += Block.error.createModel(j, i, k, subChunk, blockData, buildHelper, modelLayer);
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
			System.out.println(String.format("Layer: %s, Triangle Count: %d, Vertices: %d, Colors: %d, Textures: %d", modelLayer, triangleCount, vertices.size(), colors.size(), textures.size()));
		}

		bindVAO(vao);

		storeFloatDataInAttributeList(0, 3, positionVbo, vertices);
		storeFloatDataInAttributeList(1, 4, colorVbo, colors);
		storeFloatDataInAttributeList(2, 2, textureVbo, textures);
		storeFloatDataInAttributeList(3, 3, vboNorm, normal);
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
