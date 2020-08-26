package steve6472.polyground.world.generator.generators.impl.cave;

import org.joml.SimplexNoise;
import steve6472.polyground.block.Block;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.biomes.Biome;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.polyground.world.generator.ChunkGenDataStorage;
import steve6472.polyground.world.generator.generators.IHeightMapGenerator;
import steve6472.polyground.world.generator.generators.ISurfaceGenerator;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.08.2020
 * Project: CaveGame
 *
 ***********************/
public class CaveGenerator implements ISurfaceGenerator
{
	private final IHeightMapGenerator heightMapGenerator;
	private final ChunkGenDataStorage chunkDataStorage;

	private float[] heightMap;
	private int cx, cz;

	public CaveGenerator(IHeightMapGenerator heightMapGenerator, ChunkGenDataStorage chunkDataStorage)
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
		Block stone = Blocks.getBlockByName("stone");
		Block cobblestone = Blocks.getBlockByName("cobblestone");
		Block bedrock = Blocks.getBlockByName("bedrock");

		for (int x = 0; x < 16; x++)
		{
			for (int y = 0; y < 16; y++)
			{
				for (int z = 0; z < 16; z++)
				{
					float scale = 3f;

					float s0 = noise(x + subChunk.getX() * 16, y + subChunk.getLayer() * 16, z + subChunk.getZ() * 16, scale * 0.01f);
					float s1 = noise(x + subChunk.getX() * 16, y + subChunk.getLayer() * 16, z + subChunk.getZ() * 16, scale * 0.03f);
					float s2 = noise(x + subChunk.getX() * 16, y + subChunk.getLayer() * 16, z + subChunk.getZ() * 16, scale * 0.03f);

					float mix = s0 * (s1 + 1) * 2f + s2;

					if (subChunk.getLayer() == 0)
					{
						mix *= y / 16f;
						mix -= (-y + 16) / 16f;
					}

					subChunk.getParent().heightMap[x][z] = subChunk.getWorld().getHeight() * 16;

					if (subChunk.getLayer() == 0 && y == 0)
						subChunk.setBlock(bedrock, x, y, z);
					else if (mix < -0.4f)
						subChunk.setBlock(cobblestone, x, y, z);
					else if (mix < 0.3f)
						subChunk.setBlock(stone, x, y, z);

					Biome biome = getHeightMapGenerator().getBiomeGenerator().getBiome(x + cx * 16, y + subChunk.getLayer() * 16, z + cz * 16);
					subChunk.setBiome(biome, x, y, z);
					setChunkData(biome, cx, subChunk.getLayer(), cz);
				}
			}
		}
	}

	private static float noise(float x, float y, float z, float scale)
	{
		return SimplexNoise.noise(x * scale, y * scale, z * scale);
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
