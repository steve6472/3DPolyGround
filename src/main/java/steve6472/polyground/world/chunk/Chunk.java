package steve6472.polyground.world.chunk;

import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
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

	public void setState(BlockState state, int x, int y, int z)
	{
		if (isOutOfChunkBounds(x, y, z))
			return;

		SubChunk sc = subChunks[y / 16];

		boolean shouldRebuild = sc.getState(x, y % 16, z) != state;
		if (shouldRebuild)
			sc.rebuild();

		sc.setState(state, x, y % 16, z);

		sc.getTickableBlocks().set(x, y % 16, z, state.getBlock().isTickable());
		//		sc.setBlockEntity(x, y % 16, z, b instanceof IBlockData ? ((IBlockData) b).createNewBlockEntity() : null);

//		if (shouldRebuild)
//			updateNeighbours(sc, x, y, z);
	}

	/*
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
			getSubChunk(layer).rebuild();
			return;
		} else
		{
			sc.rebuild();
		}

		Chunk chunk;

		chunk = world.getChunk(sc.getX() + faceX.getXOffset(), sc.getZ());
		if (chunk != null)
		{
			SubChunk subChunk = chunk.getSubChunk(layer);
			if (subChunk != null)
				subChunk.rebuild();
		}

		if (faceY != EnumFace.NONE)
		{
			int l = layer + faceY.getYOffset();
			if (!(l < 0 || l >= getSubChunks().length))
				sc.getParent().getSubChunk(l).rebuild();
		}

		chunk = world.getChunk(sc.getX(), sc.getZ() + faceZ.getZOffset());
		if (chunk != null)
		{
			SubChunk subChunk = chunk.getSubChunk(layer);
			if (subChunk != null)
				subChunk.rebuild();
		}
	}*/

	public BlockState getState(int x, int y, int z)
	{
		if (isOutOfChunkBounds(x, y, z))
			return Block.air.getDefaultState();

		SubChunk sc = subChunks[y / 16];
		return sc.getState(x, y % 16, z);
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

	@Override
	public String toString()
	{
		return "Chunk{" + "x=" + x + ", z=" + z + '}';
	}
}
