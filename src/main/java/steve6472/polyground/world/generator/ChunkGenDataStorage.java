package steve6472.polyground.world.generator;

import steve6472.sge.main.game.GridStorage;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.07.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public class ChunkGenDataStorage
{
	public static final ChunkGenData GENERATED = ChunkGenData.createGenerated();
	public static final ChunkGenData NOT_GENERATED = ChunkGenData.createNotGenerated();

	private final int subchunkCount;
	private final GridStorage<ChunkGenData[]> storage;

	public ChunkGenDataStorage(int subchunkCount)
	{
		this.subchunkCount = subchunkCount;
		storage = new GridStorage<>();
	}

	public void createChunk(int x, int z)
	{
		ChunkGenData[] arr = new ChunkGenData[subchunkCount];
		for (int y = 0; y < subchunkCount; y++)
			arr[y] = ChunkGenData.createNewData(this, x, y, z);

		storage.put(x, z, arr);
	}

	public ChunkGenData getData(int x, int y, int z)
	{
		if (isChunkPresent(x, z))
			return storage.get(x, z).get()[y];
		return NOT_GENERATED;
	}

	public boolean isChunkPresent(int x, int z)
	{
		return storage.get(x, z).get() != null;
	}

	public void markSubChunkAsGenerated(int x, int y, int z)
	{
		storage.get(x, z).get()[y] = GENERATED;
	}

	public boolean isSubChunkGenerated(int x, int y, int z)
	{
		if (!isChunkPresent(x, z))
			return false;

		return storage.get(x, z).get()[y] == GENERATED;
	}

	public void clear()
	{
		storage.getMap().clear();
	}
}
