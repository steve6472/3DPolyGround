package steve6472.polyground.world.biomes;

import org.joml.Vector3f;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.08.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public class AirBiome extends Biome
{
	@Override
	public void addFeatures()
	{
//		addFeature(EnumFeatureStage.AIR, 1d / 4096d / 8d, new CloudFeature());
	}

	@Override
	public float[] getParameters()
	{
		return new float[] {0f, 0, 0, 0};
	}

	@Override
	public String getName()
	{
		return "sky";
	}

	@Override
	public BlockState getTopBlock()
	{
		return Block.AIR.getDefaultState();
	}

	@Override
	public BlockState getUnderBlock()
	{
		return Block.AIR.getDefaultState();
	}

	@Override
	public BlockState getCaveBlock()
	{
		return Block.AIR.getDefaultState();
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

	private final Vector3f FOLIAGE_COLOR = new Vector3f(0.8f, 0.8f, 1);
	@Override
	public Vector3f getColor()
	{
		return FOLIAGE_COLOR;
	}

	@Override
	public boolean generatesNaturally()
	{
		return false;
	}

	@Override
	public int getIterationCount()
	{
		return 0;
	}

	@Override
	public float getPersistance()
	{
		return 0;
	}

	@Override
	public float getScale()
	{
		return 0;
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
}
