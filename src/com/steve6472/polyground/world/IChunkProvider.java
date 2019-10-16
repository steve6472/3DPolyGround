package com.steve6472.polyground.world;

import com.steve6472.sge.main.game.GridStorage;

import java.util.Collection;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.09.2019
 * Project: SJP
 *
 ***********************/
public interface IChunkProvider
{
	GridStorage<Chunk> getChunkStorage();

	default Collection<Chunk> getChunks()
	{
		return getChunkStorage().getMap().values();
	}

	default Chunk getChunk(int x, int z)
	{
		return getChunkStorage().get(x, z);
	}

	default void deleteChunk(int x, int z)
	{
		if (getChunk(x, z) == null) return;

		getChunk(x, z).unload();
		getChunkStorage().remove(x, z);
	}

	default SubChunk getSubChunk(int x, int y, int z)
	{
		if (getChunk(x, z) == null)
			return null;
		else if (y < 0 || y >= getChunk(x, z).getSubChunks().length)
			return null;
		return getChunk(x, z).getSubChunks()[y];
	}

	default SubChunk getSubChunkFromBlockCoords(int x, int y, int z)
	{
		return getSubChunk(x >> 4, y / 16, z >> 4);
	}

	default Chunk getChunkFromBlockPos(int x, int z)
	{
		return getChunkStorage().get(x >> 4, z >> 4);
	}

	default void addChunk(Chunk chunk, boolean updateNeighbours)
	{
		getChunkStorage().put(chunk.getX(), chunk.getZ(), chunk);
		if (updateNeighbours)
		{
			if (getChunkStorage().get(chunk.getX() + 1, chunk.getZ()) != null) getChunkStorage().get(chunk.getX() + 1, chunk.getZ()).update();
			if (getChunkStorage().get(chunk.getX() - 1, chunk.getZ()) != null) getChunkStorage().get(chunk.getX() - 1, chunk.getZ()).update();
			if (getChunkStorage().get(chunk.getX(), chunk.getZ() + 1) != null) getChunkStorage().get(chunk.getX(), chunk.getZ() + 1).update();
			if (getChunkStorage().get(chunk.getX(), chunk.getZ() - 1) != null) getChunkStorage().get(chunk.getX(), chunk.getZ() - 1).update();
		}
	}
}
