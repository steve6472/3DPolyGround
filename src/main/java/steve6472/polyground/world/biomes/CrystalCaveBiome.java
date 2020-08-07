package steve6472.polyground.world.biomes;

import org.joml.Vector3f;
import steve6472.polyground.block.Block;
import steve6472.polyground.registry.BlockRegistry;
import steve6472.polyground.world.generator.EnumFeatureStage;
import steve6472.polyground.world.generator.feature.BushFeature;
import steve6472.polyground.world.generator.feature.cave.StalaFeature;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.11.2019
 * Project: SJP
 *
 ***********************/
public class CrystalCaveBiome extends Biome
{
	@Override
	public void addFeatures()
	{
		addFeature(EnumFeatureStage.CAVE_ALTER, 1 / 256d, new StalaFeature(BlockRegistry.getBlockByName("stone"), 0.6f));
		addFeature(EnumFeatureStage.CAVE_ALTER, 1d / 1024d / 16d, new BushFeature(BlockRegistry.getBlockByName("stone"), BlockRegistry.getBlockByName("crystal_log"), BlockRegistry.getBlockByName("crystal_leaves"), true));
	}

	@Override
	public float[] getParameters()
	{
		return new float[2];
	}

	@Override
	public String getName()
	{
		return "crystal_cave";
	}

	@Override
	public Block getTopBlock()
	{
		return BlockRegistry.getBlockByName("stone");
	}

	@Override
	public Block getUnderBlock()
	{
		return BlockRegistry.getBlockByName("stone");
	}

	@Override
	public Block getCaveBlock()
	{
		return BlockRegistry.getBlockByName("air");
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
		return 2;
	}

	@Override
	public int getIterationCount()
	{
		return 8;
	}

	@Override
	public float getPersistance()
	{
		return 0.4f;
	}

	@Override
	public float getScale()
	{
		return 0.007f;
	}

	@Override
	public float getLow()
	{
		return 8;
	}

	@Override
	public float getHigh()
	{
		return 31;
	}

	@Override
	public Vector3f getColor()
	{
		return new Vector3f(113 / 255f, 212 / 255f, 255 / 255f);
	}

	@Override
	public String toString()
	{
		return "CrystalCaveBiome{}";
	}
}
