package steve6472.polyground.block.special.logic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**********************
 * Edited by steve6472 (Mirek Jozefek)
 * On date: 09.12.2020
 * Project: CaveGame
 *
 * Author: https://gist.github.com/code-disaster/3c2719a0c06254074f4c
 *
 ***********************/
class VoxelFileReader
{
	@FunctionalInterface
	interface VoxelReader
	{
		void voxel(int x, int y, int z, byte colorIndex);
	}

	private static class Chunk
	{
		long id;
		int contentSize;
		int childrenSize;
	}

	private byte[] buf = new byte[4];

	void read(File path, VoxelReader onVoxel) throws IOException
	{

		BufferedInputStream input = new BufferedInputStream(new FileInputStream(path));
		Chunk chunk = new Chunk();

		if (read32(input) != magicValue('V', 'O', 'X', ' '))
		{
			throw new IllegalArgumentException("Not a valid .vox file.");
		}

		if (read32(input) < 150)
		{
			throw new IllegalArgumentException("Unsupported version.");
		}

		readChunk(input, chunk);
		if (chunk.id != magicValue('M', 'A', 'I', 'N'))
		{
			throw new IllegalArgumentException("Main chunk expected.");
		}

		if (input.skip(chunk.contentSize) != chunk.contentSize)
		{
			throw new IllegalArgumentException("Invalid number of bytes skipped.");
		}

		for (; ; )
		{
			try
			{
				readChunk(input, chunk);
			} catch (IOException ignored)
			{
				break;
			}

			if (chunk.id == magicValue('X', 'Y', 'Z', 'I'))
			{

				int numVoxels = (int) read32(input);

				int x, y, z, c;

				for (int v = 0; v < numVoxels; v++)
				{
					x = input.read();
					y = input.read();
					z = input.read();
					c = input.read();

					onVoxel.voxel(x, y, z, (byte) (c - 128));
				}
			}
		}

		input.close();
	}

	private void readChunk(BufferedInputStream input, Chunk chunk) throws IOException
	{
		chunk.id = read32(input);
		chunk.contentSize = (int) read32(input);
		chunk.childrenSize = (int) read32(input);
	}

	private long read32(BufferedInputStream input) throws IOException
	{
		if (input.read(buf) < 4)
		{
			throw new IOException();
		}
		return (buf[0] & 0xff) | ((buf[1] & 0xff) << 8) | ((buf[2] & 0xff) << 16) | ((long) (buf[3] & 0xff) << 24);
	}

	private long magicValue(char c0, char c1, char c2, char c3)
	{
		return (((long) c3 << 24) & 0xff000000) | (((long) c2 << 16) & 0x00ff0000) | ((c1 << 8) & 0x0000ff00) | (c0 & 0x000000ff);
	}
}