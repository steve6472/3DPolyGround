package steve6472.polyground.world;

import steve6472.polyground.world.chunk.Chunk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 16.11.2019
 * Project: SJP
 *
 ***********************/
public class WorldSerializer
{
	public static void serialize(World world) throws IOException
	{
		File worldFile = new File("worlds\\" + world.worldName);
		if (!worldFile.exists())
			worldFile.mkdirs();

		world.getChunks().forEach(c ->
		{
			try
			{
				c.saveChunk(world);
			} catch (IOException e)
			{
				System.err.println("Chunk " + c.getX() + "/" + c.getZ() + " failed to save");
				System.err.println("    " + e.getMessage() + "\n");
			}
		});
	}

	public static World deserialize(World world) throws IOException
	{
		File worldFile = new File("worlds\\" + world.worldName);
		if (!worldFile.exists())
		{
			try
			{
				throw new FileNotFoundException("World file has not been found");
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}

		for (File chunkFile : Objects.requireNonNull(worldFile.listFiles()))
		{
			String[] name = chunkFile.getName().split("_");
			Chunk c = new Chunk(Integer.parseInt(name[1]), Integer.parseInt(name[2]), world);
			try
			{
				c.loadChunk(world);
			} catch (IOException e)
			{
				System.err.println("Chunk " + c.getX() + "/" + c.getZ() + " failed to load");
				System.err.println("    " + e.getMessage() + "\n");
			}
			Chunk oldChunk = world.getChunk(c.getX(), c.getZ());
			if (oldChunk != null)
				world.deleteChunk(oldChunk.getX(), oldChunk.getZ());

			world.addChunk(c, true);
		}

		return world;
	}
}
