package com.steve6472.polyground.world;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.world.chunk.Chunk;
import com.steve6472.polyground.world.chunk.SubChunk;
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
		Chunk chunk = getChunk(x, z);

		if (chunk == null) return;

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

	default void addChunk(Chunk chunk, boolean updateNeighbours)
	{
		getChunkStorage().put(chunk.getX(), chunk.getZ(), chunk);

		updateNeighbours(chunk);

		if (updateNeighbours)
		{
			if (chunk.getNeighbouringChunk(EnumFace.NORTH) != null) chunk.getNeighbouringChunk(EnumFace.NORTH).update();
			if (chunk.getNeighbouringChunk(EnumFace.SOUTH) != null) chunk.getNeighbouringChunk(EnumFace.SOUTH).update();
			if (chunk.getNeighbouringChunk(EnumFace.EAST) != null) chunk.getNeighbouringChunk(EnumFace.EAST).update();
			if (chunk.getNeighbouringChunk(EnumFace.WEST) != null) chunk.getNeighbouringChunk(EnumFace.WEST).update();
		}

//		FloatingText c = new FloatingText(String.format("[%d,%d]", chunk.getX(), chunk.getZ()));
//		c.setPosition(chunk.getX() * 16 + 7.5f, 2.5f, chunk.getZ() * 16 + 7.5f);
//		CaveGame.getInstance().world.addEntity(c);

//		getChunkStorage().get(chunk.getX(), chunk.getZ()).update();
	}

	default void updateNeighbours(Chunk chunk)
	{
		Chunk north = getChunkStorage().get(chunk.getX() + 1, chunk.getZ());
		Chunk south = getChunkStorage().get(chunk.getX() - 1, chunk.getZ());
		Chunk east = getChunkStorage().get(chunk.getX(), chunk.getZ() + 1);
		Chunk west = getChunkStorage().get(chunk.getX(), chunk.getZ() - 1);

		chunk.setNeighbouringChunk(EnumFace.NORTH, north);
		chunk.setNeighbouringChunk(EnumFace.SOUTH, south);
		chunk.setNeighbouringChunk(EnumFace.EAST, east);
		chunk.setNeighbouringChunk(EnumFace.WEST, west);

		if (north != null) north.setNeighbouringChunk(EnumFace.SOUTH, chunk);
		if (south != null) south.setNeighbouringChunk(EnumFace.NORTH, chunk);
		if (east != null) east.setNeighbouringChunk(EnumFace.WEST, chunk);
		if (west != null) west.setNeighbouringChunk(EnumFace.EAST, chunk);
	}
}
