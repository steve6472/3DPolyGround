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
		File worldFile = new File("game/worlds/" + world.worldName);
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
				e.printStackTrace();
			}
		});

		world.teleporters.saveTeleporters();
		world.getRifts().saveRifts();
	}

	public static World deserialize(World world) throws IOException
	{
		File worldFile = new File("game/worlds/" + world.worldName);
		if (!worldFile.exists())
		{
			try
			{
				throw new FileNotFoundException("World file has not been found");
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
				return null;
			}
		}

		for (File chunkFile : Objects.requireNonNull(worldFile.listFiles()))
		{
			if (!chunkFile.isDirectory())
				continue;
			if (chunkFile.listFiles() == null || chunkFile.listFiles().length == 0)
			{
				if (chunkFile.delete())
				{
					System.out.println("Removed empty chunk file " + chunkFile.getName());
				}
				continue;
			}

			String[] name = chunkFile.getName().split("_");
			Chunk c = new Chunk(Integer.parseInt(name[1]), Integer.parseInt(name[2]), world);
			try
			{
				c.loadChunk(world);
			} catch (IOException e)
			{
				System.err.println("Chunk " + c.getX() + "/" + c.getZ() + " failed to load");
				e.printStackTrace();
			}
			Chunk oldChunk = world.getChunk(c.getX(), c.getZ());
			if (oldChunk != null)
				world.deleteChunk(oldChunk.getX(), oldChunk.getZ());

			world.addChunk(c);
		}

		world.teleporters.loadTeleporters();
		world.getRifts().loadRifts();

		return world;
	}
}
