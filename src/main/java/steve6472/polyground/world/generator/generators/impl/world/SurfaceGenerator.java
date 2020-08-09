package steve6472.polyground.world.generator.generators.impl.world;

import steve6472.polyground.world.biomes.Biome;
import steve6472.polyground.world.biomes.Biomes;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.polyground.world.generator.ChunkGenDataStorage;
import steve6472.polyground.world.generator.generators.IHeightMapGenerator;
import steve6472.polyground.world.generator.generators.ISurfaceGenerator;
import steve6472.sge.main.Util;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.07.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public class SurfaceGenerator implements ISurfaceGenerator
{
	private final IHeightMapGenerator heightMapGenerator;
	private final ChunkGenDataStorage chunkDataStorage;

	private float[] heightMap;
	private int cx, cz;

	public SurfaceGenerator(IHeightMapGenerator heightMapGenerator, ChunkGenDataStorage chunkDataStorage)
	{
		this.heightMapGenerator = heightMapGenerator;
		this.chunkDataStorage = chunkDataStorage;
	}

	@Override
	public void load(int cx, int cz)
	{
		this.heightMap = heightMapGenerator.generate(cx, cz);
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
				int height = (int) Math.floor(heightMap[x + z * 16]);

				if (subChunk.getLayer() == 0)
					subChunk.getParent().heightMap[x][z] = height;

				Biome biome = heightMapGenerator.getBiomeGenerator().getBiome(x + cx * 16, 0, z + cz * 16);
				setChunkData(biome, cx, subChunk.getLayer(), cz);

				if (height / 16 == subChunk.getLayer())
					subChunk.setState(biome.getTopBlock(), x, height % 16, z);

				// TODO: Clamp to subchunk
				for (int k = height - 1; k >= height - biome.getUnderLayerHeight(); k--)
					subChunk.getWorld().setState(biome.getUnderBlock(), x + cx * 16, k, z + cz * 16);

				for (int k = height - 1 - biome.getUnderLayerHeight(); k >= 0; k--)
					subChunk.getWorld().setState(biome.getCaveBlock(), x + cx * 16, k, z + cz * 16);

				for (int k = subChunk.getLayer() * 16; k < Util.clamp(subChunk.getLayer() * 16, subChunk.getLayer() * 16 + 16, height + biome.getBiomeHeight()); k++)
					subChunk.getWorld().setBiome(biome, x + cx * 16, k, z + cz * 16);

				// Add air biome if present
				if ((height + biome.getBiomeHeight() + 1) / 16 <= subChunk.getLayer())
					setChunkData(Biomes.AIR_BIOME, cx, subChunk.getLayer(), cz);
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
