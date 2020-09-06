package steve6472.polyground.gfx;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.chunk.SubChunkModel;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.08.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public class ThreadedModelBuilder extends Thread
{
	private final BlockingQueue<SubChunkModel> inputQueue;
	private final BlockingQueue<ModelData> outputQueue;
	private boolean terminateRequested;

	public ThreadedModelBuilder()
	{
		inputQueue = new LinkedBlockingQueue<>();
		outputQueue = new LinkedBlockingQueue<>();
	}

	@Override
	public void run()
	{
		while (!terminateRequested)
		{
			try
			{
				SubChunkModel subChunk = inputQueue.take();
				rebuild(subChunk);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
				terminate();
			}
		}
	}

	public void addToQueue(SubChunkModel model)
	{
		try
		{
			if (model.rebuildInProgress)
				return;
			model.rebuildInProgress = true;
			if (!inputQueue.contains(model))
				inputQueue.put(model);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			terminate();
		}
	}

	public boolean canTake()
	{
		return !outputQueue.isEmpty();
	}

	public ModelData take()
	{
		try
		{
			return outputQueue.take();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void terminate()
	{
		terminateRequested = true;
	}

	private void rebuild(SubChunkModel model)
	{
		long start = System.nanoTime();
		List<Vector3f> vertices = new ArrayList<>();
		List<Vector4f> colors = new ArrayList<>();
		List<Vector2f> textures = new ArrayList<>();
		List<Vector3f> normal = new ArrayList<>();
		int triangleCount = 0;

		model.getSubChunk().getParent().getWorld().getGame().mainRender.buildHelper.load(vertices, colors, textures, normal);

		ModelBuilder buildHelper = model.getSubChunk().getWorld().getGame().mainRender.buildHelper;

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					BlockState state = model.getSubChunk().getState(j, i, k);

					Block b = state.getBlock();

					try
					{
						if (b != null && b != Block.air)
						{
							model.getSubChunk().getParent().getWorld().getGame().mainRender.buildHelper.load(j, i, k);
							triangleCount += b.createModel(j + model.getSubChunk().getX() * 16, i + model.getSubChunk().getLayer() * 16, k + model.getSubChunk().getZ() * 16, model.getSubChunk().getWorld(), state, buildHelper, model.getLayer());
						}

					} catch (Exception ex)
					{
						System.err.println("Error while building chunk with block '" + b.getName() + "' state '" + state + "'");
						ex.printStackTrace();
						try
						{
							triangleCount += Block.error.createModel(j + model.getSubChunk().getX() * 16, i + model.getSubChunk().getLayer() * 16, k + model.getSubChunk().getZ() * 16, model.getSubChunk().getWorld(), state, buildHelper, model.getLayer());
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
			System.out.printf("Layer: %s, Triangle Count: %d, Vertices: %d, Colors: %d, Textures: %d%n", model.getLayer(), triangleCount, vertices.size(), colors.size(), textures.size());
		}

		try
		{
//			outputQueue.put(new ModelData(positions, textures, colors, flags, vertexCount, model.getParent().x, model.layer, model.getParent().z));
			outputQueue.put(new ModelData(model.getLayer(), toFloatBuffer3(vertices), toFloatBuffer4(colors), toFloatBuffer2(textures), toFloatBuffer3(normal), model, triangleCount, model.getSubChunk().getX(), model.getSubChunk().getLayer(), model.getSubChunk().getZ()));
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		if (CaveGame.getInstance().options.subChunkBuildTime)
			CaveGame.getInstance().inGameGui.chat.addText("SubChunk " + model.getSubChunk().getX() + "/" + model.getSubChunk().getLayer() + "/" + model.getSubChunk().getZ() + " Took " + (System.nanoTime() - start) / 1000000.0 + "ms to build");
	}

	public static FloatBuffer toFloatBuffer3(List<Vector3f> arr)
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

	public static FloatBuffer toFloatBuffer4(List<Vector4f> arr)
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

	public static FloatBuffer toFloatBuffer2(List<Vector2f> arr)
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
