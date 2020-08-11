package steve6472.polyground.world.biomes;

import org.joml.Vector3f;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.Blocks;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.11.2019
 * Project: SJP
 *
 ***********************/
public class VoidBiome extends Biome
{
	@Override
	public void addFeatures()
	{

	}

	@Override
	public float[] getParameters()
	{
		return new float[0];
	}

	@Override
	public String getName()
	{
		return "void";
	}

	@Override
	public BlockState getTopBlock()
	{
		return Blocks.getDefaultState("bedrock");
	}

	@Override
	public BlockState getUnderBlock()
	{
		return Blocks.getDefaultState("air");
	}

	@Override
	public BlockState getCaveBlock()
	{
		return Blocks.getDefaultState("air");
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
		return 0;
	}

	@Override
	public int getIterationCount()
	{
		return 0;
	}

	@Override
	public float getPersistance()
	{
		return 0.0f;
	}

	@Override
	public float getScale()
	{
		return 0.0f;
	}

	@Override
	public float getLow()
	{
		return 0;
	}

	@Override
	public float getHigh()
	{
		return 0;
	}

	@Override
	public Vector3f getColor()
	{
		return new Vector3f(145 / 255f, 189 / 255f, 89 / 255f);
	}
}
