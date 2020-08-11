package steve6472.polyground.world.chunk;

import steve6472.polyground.gfx.ThreadedModelBuilder;
import steve6472.polyground.world.World;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class Chunk
{
	private final int x, z;
	private final SubChunk[] subChunks;
	private final World world;
	public final int[][] heightMap;

	public Chunk(int x, int z, World world)
	{
		this.x = x;
		this.z = z;
		this.world = world;
		heightMap = new int[16][16];
		for (int i = 0; i < 16; i++)
		{
			Arrays.fill(heightMap[i], -1);
		}

		subChunks = new SubChunk[world.getHeight()];
		for (int i = 0; i < subChunks.length; i++)
		{
			subChunks[i] = new SubChunk(this, i);
		}
	}

	public void checkRebuild(ThreadedModelBuilder builder)
	{
		for (SubChunk subChunk : subChunks)
		{
			subChunk.tryRebuild(builder);
		}
	}

	public void tick()
	{
		for (SubChunk subChunk : subChunks)
		{
			subChunk.tick();
		}
	}

	public void unload()
	{
		for (SubChunk subChunk : subChunks)
			subChunk.unload();
	}

	public void rebuild()
	{
		for (SubChunk subChunk : subChunks)
		{
			subChunk.rebuild();
		}
	}

	public void saveChunk(World world) throws IOException
	{
		File chunk = new File("game/worlds/" + world.worldName + "/chunk_" + x + "_" + z);
		if (!chunk.exists())
			if (chunk.mkdir())
				System.out.println("Created folder for chunk " + chunk.getPath());

		for (SubChunk subChunk : subChunks)
			subChunk.saveSubChunk();
	}

	public void loadChunk(World world) throws IOException
	{
		File chunk = new File("game/worlds/" + world.worldName + "/chunk_" + x + "_" + z);
		if (!chunk.exists())
			throw new FileNotFoundException();

		for (SubChunk subChunk : subChunks)
		{
			ChunkSerializer.deserialize(subChunk);
		}
	}

	public World getWorld()
	{
		return world;
	}

	public int getX()
	{
		return x;
	}

	public int getZ()
	{
		return z;
	}

	public SubChunk[] getSubChunks()
	{
		return subChunks;
	}

	public SubChunk getSubChunk(int layer)
	{
		return getSubChunks()[layer];
	}

	@Override
	public String toString()
	{
		return "Chunk{" + "x=" + x + ", z=" + z + '}';
	}
}
