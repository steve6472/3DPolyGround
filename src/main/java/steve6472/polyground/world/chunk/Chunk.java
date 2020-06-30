package steve6472.polyground.world.chunk;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.blockdata.IBlockData;
import steve6472.polyground.registry.BlockRegistry;
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
	private Chunk NORTH, EAST, SOUTH, WEST;

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

	public Chunk generate()
	{
		for (SubChunk subChunk : subChunks)
		{
			subChunk.generate();
			subChunk.rebuild();
		}

		return this;
	}

	public Chunk generateNoRender()
	{
		for (SubChunk subChunk : subChunks)
		{
			subChunk.generate();
		}

		return this;
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

	public void update()
	{
		for (SubChunk subChunk : subChunks)
		{
			subChunk.rebuildAllLayers();
		}
	}

	public void saveChunk(World world) throws IOException
	{
		File chunk = new File("game/worlds/" + world.worldName + "/chunk_" + x + "_" + z);
		if (!chunk.exists())
			chunk.mkdir();

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

	public void setBlock(int x, int y, int z, int id)
	{
		if (isOutOfChunkBounds(x, y, z))
			return;

		SubChunk sc = subChunks[y / 16];

		boolean shouldRebuild = sc.getBlockId(x, y % 16, z) != id;

		sc.setBlock(x, y % 16, z, id);

		Block b = BlockRegistry.getBlockById(id);

		sc.getTickableBlocks().set(x, y % 16, z, b.isTickable());
		sc.setBlockEntity(x, y % 16, z, b instanceof IBlockData ? ((IBlockData) b).createNewBlockEntity() : null);

		if (shouldRebuild)
			updateNeighbours(sc, x, y, z);
	}

	public void updateNeighbours(SubChunk sc, int x, int y, int z)
	{
		EnumFace faceX = x == 15 ? EnumFace.NORTH : x == 0 ? EnumFace.SOUTH : EnumFace.NONE;
		EnumFace faceZ = z == 15 ? EnumFace.EAST : z == 0 ? EnumFace.WEST : EnumFace.NONE;
		EnumFace faceY = y % 16 == 15 ? EnumFace.UP : y % 16 == 0 ? EnumFace.DOWN : EnumFace.NONE;

		int layer = y / 16;

		if (layer < 0 || layer >= getSubChunks().length)
			return;

		if (faceX == EnumFace.NONE && faceY == EnumFace.NONE && faceZ == EnumFace.NONE)
		{
			getSubChunk(layer).rebuildAllLayers();
			return;
		} else
		{
			sc.rebuildAllLayers();
		}

		Chunk chunk;

		chunk = getNeighbouringChunk(faceX);
		if (chunk != null)
		{
			SubChunk subChunk = chunk.getSubChunk(layer);
			if (subChunk != null)
				subChunk.rebuildAllLayers();
		}

		if (faceY != EnumFace.NONE)
		{
			int l = layer + faceY.getYOffset();
			if (!(l < 0 || l >= getSubChunks().length))
				sc.getParent().getSubChunk(l).rebuildAllLayers();
		}

		chunk = getNeighbouringChunk(faceZ);
		if (chunk != null)
		{
			SubChunk subChunk = chunk.getSubChunk(layer);
			if (subChunk != null)
				subChunk.rebuildAllLayers();
		}
	}

	public int getBlock(int x, int y, int z)
	{
		if (isOutOfChunkBounds(x, y, z))
			return Block.air.getId();

		SubChunk sc = subChunks[y / 16];
		return sc.getIds()[x][y % 16][z];
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

	private boolean isOutOfChunkBounds(int x, int y, int z)
	{
		return x < 0 || x >= 16 || z < 0 || z >= 16 || y < 0 || y >= 16 * subChunks.length;
	}

	public void setNeighbouringChunk(EnumFace face, Chunk chunk)
	{
		switch (face)
		{
			case NORTH -> NORTH = chunk;
			case EAST -> EAST = chunk;
			case SOUTH -> SOUTH = chunk;
			case WEST -> WEST = chunk;
		}
	}

	public Chunk getNeighbouringChunk(EnumFace face)
	{
		return switch (face)
			{
				case NORTH -> NORTH;
				case EAST -> EAST;
				case SOUTH -> SOUTH;
				case WEST -> WEST;
				default -> null;
			};
	}

	@Override
	public String toString()
	{
		return "Chunk{" + "x=" + x + ", z=" + z + '}';
	}
}
