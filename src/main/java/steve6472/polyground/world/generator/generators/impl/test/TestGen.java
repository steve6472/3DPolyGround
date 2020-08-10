package steve6472.polyground.world.generator.generators.impl.test;

import steve6472.polyground.SSimplexNoise;
import steve6472.polyground.registry.BlockRegistry;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.polyground.world.generator.ChunkGenDataStorage;
import steve6472.polyground.world.generator.generators.IHeightMapGenerator;
import steve6472.polyground.world.generator.generators.ISurfaceGenerator;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 10.08.2020
 * Project: CaveGame
 *
 ***********************/
public class TestGen implements ISurfaceGenerator
{
	private final IHeightMapGenerator heightMapGenerator;
	private final ChunkGenDataStorage chunkDataStorage;
	private final SSimplexNoise noise;

	private int cx, cz;

	public TestGen(IHeightMapGenerator heightMapGenerator, ChunkGenDataStorage chunkDataStorage)
	{
		this.heightMapGenerator = heightMapGenerator;
		this.chunkDataStorage = chunkDataStorage;
//		noise = new SSimplexNoise(3096958501783446189L);
		noise = new SSimplexNoise(-8376544303500664650L);
	}

	@Override
	public void load(int cx, int cz)
	{
		this.cx = cx;
		this.cz = cz;
	}

	@Override
	public void generate(SubChunk subChunk)
	{
		for (int x = 0; x < 16; x++)
		{
			for (int z = 0; z < 16; z++)
			{
				float scale = 0.0025f;
				float X = x + subChunk.getX() * 16;
				float Z = z + subChunk.getZ() * 16;

				float n = noise.noise(X * scale, Z * scale);

				if (n < -0.5f)
					subChunk.setBlock(BlockRegistry.getBlockByName("stone"), x, 0, z);
				else if (n < 0)
					subChunk.setBlock(BlockRegistry.getBlockByName("diorite"), x, 0, z);
				else if (n < 0.5f)
					subChunk.setBlock(BlockRegistry.getBlockByName("dirt"), x, 0, z);
				else
					subChunk.setBlock(BlockRegistry.getBlockByName("bedrock"), x, 0, z);

			}
		}
	}

	@Override
	public IHeightMapGenerator getHeightMapGenerator()
	{
		return heightMapGenerator;
	}

	@Override
	public ChunkGenDataStorage getDataStorage()
	{
		return chunkDataStorage;
	}
}
