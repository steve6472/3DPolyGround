package steve6472.polyground.world.chunk;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.BuildHelper;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static steve6472.sge.gfx.VertexObjectCreator.bindVAO;
import static steve6472.sge.gfx.VertexObjectCreator.delete;

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

						List<Vector3f> vertices = new ArrayList<>(current.triangleCount);
						List<Vector4f> colors = new ArrayList<>(current.triangleCount);
						List<Vector2f> textures = new ArrayList<>(current.triangleCount);
						List<Vector3f> normal = new ArrayList<>(current.triangleCount);

						BuildHelper buildHelper = current.subChunk.getWorld().getGame().mainRender.buildHelper;

						buildHelper.load(vertices, colors, textures, normal);

						int triangleCount = 0;

						for (int i = 0; i < 16; i++)
						{
							for (int j = 0; j < 16; j++)
							{
								for (int k = 0; k < 16; k++)
								{
									BlockState state = current.subChunk.getState(j, i, k);

									Block b = state.getBlock();

									try
									{
										if (b != null && b != Block.air)
										{
											buildHelper.load(j, i, k);
											triangleCount += b.createModel(j + current.subChunk.getX() * 16, i + current.subChunk.getLayer() * 16, k + current.subChunk.getZ() * 16, current.subChunk.getWorld(), state, buildHelper, current.modelLayer);
										}

									} catch (Exception ex)
									{
										System.err.println("Error while building chunk with block '" + b.getName() + "' state '" + state + "'");
										ex.printStackTrace();
										try
										{
											triangleCount += Block.error.createModel(j + current.subChunk.getX() * 16, i + current.subChunk.getLayer() * 16, k + current.subChunk.getZ() * 16, current.subChunk.getWorld(), state, buildHelper, current.modelLayer);
										} catch (Exception ex1)
										{
											System.err.println("Error while building chunk error block!\nFrick! This should not happen :(");
											ex.printStackTrace();
											CaveGame.getInstance().exit();
											System.exit(2);
										}
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

			storeFloatDataInAttributeList3(0, 3, data.model.positionVbo, data.vert);
			storeFloatDataInAttributeList4(1, 4, data.model.colorVbo, data.color);
			storeFloatDataInAttributeList2(2, 2, data.model.textureVbo, data.text);
			storeFloatDataInAttributeList3(3, 3, data.model.vboNorm, data.normal);
			data.model.triangleCount = data.triangleCount;
		}
		modelData.clear();
		canAdd = true;
		toBuildCount = toBuild.size();
	}

	private static class ModelData
	{
		List<Vector3f> vert, normal;
		List<Vector4f> color;
		List<Vector2f> text;
		SubChunkModel model;
		int triangleCount;

		public ModelData(List<Vector3f> vert, List<Vector4f> color, List<Vector2f> text, List<Vector3f> normal, SubChunkModel model, int triangleCount)
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

		List<Vector3f> vertices = new ArrayList<>(triangleCount);
		List<Vector4f> colors = new ArrayList<>(triangleCount);
		List<Vector2f> textures = new ArrayList<>(triangleCount);
		List<Vector3f> normal = new ArrayList<>(triangleCount);

		subChunk.getParent().getWorld().getGame().mainRender.buildHelper.load(vertices, colors, textures, normal);

		BuildHelper buildHelper = subChunk.getWorld().getGame().mainRender.buildHelper;

		triangleCount = 0;

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					BlockState state = subChunk.getState(j, i, k);

					Block b = state.getBlock();

					try
					{
						if (b != null && b != Block.air)
						{
							subChunk.getParent().getWorld().getGame().mainRender.buildHelper.load(j, i, k);
							triangleCount += b.createModel(j + subChunk.getX() * 16, i + subChunk.getLayer() * 16, k + subChunk.getZ() * 16, subChunk.getWorld(), state, buildHelper, modelLayer);
						}

					} catch (Exception ex)
					{
						System.err.println("Error while building chunk with block '" + b.getName() + "' state '" + state + "'");
						ex.printStackTrace();
						try
						{
							triangleCount += Block.error.createModel(j + subChunk.getX() * 16, i + subChunk.getLayer() * 16, k + subChunk.getZ() * 16, subChunk.getWorld(), state, buildHelper, modelLayer);
						} catch (Exception ex1)
						{
							System.err.println("Error while building chunk error block!\nFrick! This should not happen :(");
							ex.printStackTrace();
							CaveGame.getInstance().exit();
							System.exit(2);
						}
					}
				}
			}
		}

		if (CaveGame.getInstance().options.chunkModelDebug && triangleCount != 0)
		{
			System.out.printf("Layer: %s, Triangle Count: %d, Vertices: %d, Colors: %d, Textures: %d%n", modelLayer, triangleCount, vertices.size(), colors.size(), textures.size());
		}

		bindVAO(vao);

		storeFloatDataInAttributeList3(0, 3, positionVbo, vertices);
		storeFloatDataInAttributeList4(1, 4, colorVbo, colors);
		storeFloatDataInAttributeList2(2, 2, textureVbo, textures);
		storeFloatDataInAttributeList3(3, 3, vboNorm, normal);
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





	private static void storeFloatDataInAttributeList3(int attributeNumber, int size, int vboId, List<Vector3f> data)
	{
		FloatBuffer buffer = toFloatBuffer3(data);
		GL30.glBindBuffer(34962, vboId);
		GL30.glBufferData(34962, buffer, 35044);
		GL30.glVertexAttribPointer(attributeNumber, size, 5126, false, 0, 0L);
		GL30.glBindBuffer(34962, 0);
	}

	private static void storeFloatDataInAttributeList4(int attributeNumber, int size, int vboId, List<Vector4f> data)
	{
		FloatBuffer buffer = toFloatBuffer4(data);
		GL30.glBindBuffer(34962, vboId);
		GL30.glBufferData(34962, buffer, 35044);
		GL30.glVertexAttribPointer(attributeNumber, size, 5126, false, 0, 0L);
		GL30.glBindBuffer(34962, 0);
	}

	private static void storeFloatDataInAttributeList2(int attributeNumber, int size, int vboId, List<Vector2f> data)
	{
		FloatBuffer buffer = toFloatBuffer2(data);
		GL30.glBindBuffer(34962, vboId);
		GL30.glBufferData(34962, buffer, 35044);
		GL30.glVertexAttribPointer(attributeNumber, size, 5126, false, 0, 0L);
		GL30.glBindBuffer(34962, 0);
	}

	private static FloatBuffer toFloatBuffer3(List<Vector3f> arr)
	{
		FloatBuffer buff = BufferUtils.createFloatBuffer(arr.size() * 3);

		for (Vector3f i : arr)
		{
			buff.put(i.x);
			buff.put(i.y);
			buff.put(i.z);
		}

		buff.flip();
		return buff;
	}

	private static FloatBuffer toFloatBuffer4(List<Vector4f> arr)
	{
		FloatBuffer buff = BufferUtils.createFloatBuffer(arr.size() * 4);

		for (Vector4f i : arr)
		{
			buff.put(i.x);
			buff.put(i.y);
			buff.put(i.z);
			buff.put(i.w);
		}

		buff.flip();
		return buff;
	}

	private static FloatBuffer toFloatBuffer2(List<Vector2f> arr)
	{
		FloatBuffer buff = BufferUtils.createFloatBuffer(arr.size() * 2);

		for (Vector2f i : arr)
		{
			buff.put(i.x);
			buff.put(i.y);
		}

		buff.flip();
		return buff;
	}
}
