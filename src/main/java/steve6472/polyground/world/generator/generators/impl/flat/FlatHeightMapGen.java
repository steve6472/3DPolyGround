package steve6472.polyground.world.generator.generators.impl.flat;

import steve6472.polyground.world.generator.generators.IBiomeGenerator;
import steve6472.polyground.world.generator.generators.IHeightMapGenerator;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.08.2020
 * Project: CaveGame
 *
 ***********************/
public class FlatHeightMapGen implements IHeightMapGenerator
{
	private final IBiomeGenerator biomeGenerator;
	private final float[] heightMap;

	public FlatHeightMapGen(IBiomeGenerator biomeGenerator, int height)
	{
		this.biomeGenerator = biomeGenerator;

		heightMap = new float[16 * 16];

		for (int x = 0; x < 16; ++x)
		{
			for (int z = 0; z < 16; ++z)
			{
				heightMap[x + z * 16] = height;
			}
		}
	}

	@Override
	public float[] generate(int cx, int cz)
	{
		return heightMap;
	}

	@Override
	public IBiomeGenerator getBiomeGenerator()
	{
		return biomeGenerator;
	}
}
