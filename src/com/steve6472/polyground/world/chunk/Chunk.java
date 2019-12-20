package com.steve6472.polyground.world.chunk;

import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.blockdata.IBlockData;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.world.World;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class Chunk
{
	private int x, z;

	private SubChunk[] subChunks;
	private World world;

	public Chunk(int x, int z, World world)
	{
		this.x = x;
		this.z = z;
		this.world = world;

		subChunks = new SubChunk[4];
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
			subChunk.rebuild();
		}
	}

	public void saveChunk(World world) throws IOException
	{
		File chunk = new File("worlds\\" + world.worldName + "\\chunk_" + x + "_" + z);
		if (!chunk.exists())
			chunk.mkdir();

		for (SubChunk subChunk : subChunks)
			subChunk.saveSubChunk();
	}

	public void loadChunk(World world) throws IOException
	{
		File chunk = new File("worlds\\" + world.worldName + "\\chunk_" + x + "_" + z);
		if (!chunk.exists()) throw new FileNotFoundException();

		for (SubChunk subChunk : subChunks)
			ChunkSerializer.deserialize(subChunk);
	}

	public void setBlock(int x, int y, int z, Block block)
	{
		setBlock(x, y, z, block.getId());
	}

	public void setBlock(int x, int y, int z, Block block, boolean rebuild)
	{
		setBlock(x, y, z, block.getId(), rebuild);
	}

	public void setBlock(int x, int y, int z, int id)
	{
		setBlock(x, y, z, id, true);
	}

	public void setBlock(int x, int y, int z, int id, boolean rebuild)
	{
		if (isOutOfChunkBounds(x, y, z)) return;

		SubChunk sc = subChunks[y / 16];
		sc.setBlock(x, y % 16, z, id);

		Block b = BlockRegistry.getBlockById(id);

		if (b.isTickable())
		{
			sc.getTickableBlocks().add(x, y % 16, z);
		} else
		{
			sc.getTickableBlocks().remove(x, y % 16, z);
		}

		if (b instanceof IBlockData)
		{
			sc.setBlockEntity(x, y, z, ((IBlockData) b).createNewBlockEntity());
		} else
		{
			sc.setBlockEntity(x, y, z, null);
		}

		if (rebuild)
		{
			sc.rebuild();

			if (x == 15)
			{
				SubChunk subChunk = world.getSubChunk(getX() + 1, y / 16, getZ());
				if (subChunk != null) subChunk.rebuild();
			} else if (x == 0)
			{
				SubChunk subChunk = world.getSubChunk(getX() - 1, y / 16, getZ());
				if (subChunk != null) subChunk.rebuild();
			}

			if (z == 15)
			{
				SubChunk subChunk = world.getSubChunk(getX(), y / 16, getZ() + 1);
				if (subChunk != null) subChunk.rebuild();
			} else if (z == 0)
			{
				SubChunk subChunk = world.getSubChunk(getX(), y / 16, getZ() - 1);
				if (subChunk != null) subChunk.rebuild();
			}

			if (y % 16 == 0 && y - 1 < 16 * subChunks.length && y - 1 > 0)
				subChunks[(y - 1) / 16].rebuild();
			if (y % 16 == 15 && y + 1 < 16 * subChunks.length)
				subChunks[(y + 1) / 16].rebuild();
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

	public Block getBlock(int x, int y, int z)
	{
		if (isOutOfChunkBounds(x, y, z)) return Block.air;

		SubChunk sc = subChunks[y / 16];
		return BlockRegistry.getBlockById(sc.getIds()[x][y % 16][z]);
	}

	public int getBlockId(int x, int y, int z)
	{
		if (isOutOfChunkBounds(x, y, z)) return Block.air.getId();

		SubChunk sc = subChunks[y / 16];
		return sc.getIds()[x][y % 16][z];
	}

	private boolean isOutOfChunkBounds(int x, int y, int z)
	{
		return x < 0 || x >= 16 || z < 0 || z >= 16 || y < 0 || y >= 16 * subChunks.length;
	}
}
