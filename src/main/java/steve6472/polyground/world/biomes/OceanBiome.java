package steve6472.polyground.world.biomes;

import org.joml.Vector3f;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.BlockRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.11.2019
 * Project: SJP
 *
 ***********************/
public class OceanBiome extends Biome
{
	@Override
	public void addFeatures()
	{

	}

	@Override
	public float[] getParameters()
	{
		return new float[2];
	}

	@Override
	public String getName()
	{
		return "ocean";
	}

	@Override
	public BlockState getTopBlock()
	{
		return BlockRegistry.getBlockByName("gravel").getDefaultState();
	}

	@Override
	public BlockState getUnderBlock()
	{
		return BlockRegistry.getBlockByName("sand").getDefaultState();
	}

	@Override
	public BlockState getCaveBlock()
	{
		return BlockRegistry.getBlockByName("stone").getDefaultState();
	}

	/**
	 * Indicates how high the biome will stretch above surface
	 *
	 * @return biome height
	 */
	@Override
	public int getBiomeHeight()
	{
		return 0;
	}

	@Override
	public int getUnderLayerHeight()
	{
		return 4;
	}

	@Override
	public int getIterationCount()
	{
		return 64;
	}

	@Override
	public float getPersistance()
	{
		return 0.4f;
	}

	@Override
	public float getScale()
	{
		return 0.0007f;
	}

	@Override
	public float getLow()
	{
		return 0;
	}

	@Override
	public float getHigh()
	{
		return 20;
	}

	@Override
	public Vector3f getColor()
	{
		return new Vector3f(102 / 255f, 209 / 255f, 154 / 255f);
	}
}
