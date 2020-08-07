package steve6472.polyground.world;

import steve6472.polyground.world.chunk.Chunk;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.sge.main.game.GridStorage;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.09.2019
 * Project: SJP
 *
 ***********************/
public interface IChunkProvider
{
	GridStorage<Chunk> getChunkStorage();

	default Iterable<Chunk> getChunks()
	{
		return getChunkStorage().getMap().values();
	}

	default Chunk getChunk(int x, int z)
	{
		return getChunkStorage().get(x, z);
	}

	default void deleteChunk(int x, int z)
	{
		Chunk chunk = getChunk(x, z);

		if (chunk == null)
			return;

		chunk.unload();
		getChunkStorage().remove(x, z);
	}

	default SubChunk getSubChunk(int x, int y, int z)
	{
		Chunk chunk = getChunk(x, z);
		if (chunk == null)
			return null;
		else if (y < 0 || y >= chunk.getSubChunks().length)
			return null;
		return chunk.getSubChunks()[y];
	}

	default SubChunk getSubChunkFromBlockCoords(int x, int y, int z)
	{
		return getSubChunk(x >> 4, y >> 4, z >> 4);
	}

	default Chunk getChunkFromBlockPos(int x, int z)
	{
		return getChunkStorage().get(x >> 4, z >> 4);
	}

	void addChunk(Chunk chunk);

	/*
	default void addChunk(Chunk chunk)
	{
		getChunkStorage().put(chunk.getX(), chunk.getZ(), chunk);

		// Update neighbouding chunks
		Chunk c = getChunk(chunk.getX() + 1, chunk.getZ());
		if (c != null) c.update();
		c = getChunk(chunk.getX() - 1, chunk.getZ());
		if (c != null) c.update();
		c = getChunk(chunk.getX(), chunk.getZ() + 1);
		if (c != null) c.update();
		c = getChunk(chunk.getX(), chunk.getZ() - 1);
		if (c != null) c.update();
	}*/
}
