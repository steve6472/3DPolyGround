package steve6472.polyground.world.biomes;

import org.joml.Vector3f;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.BlockRegistry;
import steve6472.polyground.world.generator.EnumFeatureStage;
import steve6472.polyground.world.generator.feature.VegetationPatchFeature;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.11.2019
 * Project: SJP
 *
 ***********************/
public class DesertBiome extends Biome
{
	@Override
	public void addFeatures()
	{
		addFeature(EnumFeatureStage.VEGETATION, 1 / 100d, new VegetationPatchFeature(BlockRegistry.getBlockByName("sand"), BlockRegistry.getBlockByName("small_grass"), 0.5, 2, true));

//				features.add(new FeatureEntry(new VegetationPatchFeature(BlockRegistry.getBlockByName("sand"), BlockRegistry.getBlockByName("small_grass"), 0.5, 2, true), 1 / 100d));
	}

	@Override
	public float[] getParameters()
	{
		return new float[2];
	}

	@Override
	public String getName()
	{
		return "desert";
	}

	@Override
	public BlockState getTopBlock()
	{
		return BlockRegistry.getBlockByName("sand").getDefaultState();
	}

	@Override
	public BlockState getUnderBlock()
	{
		return BlockRegistry.getBlockByName("cobblestone").getDefaultState();
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
		return 8;
	}

	@Override
	public int getUnderLayerHeight()
	{
		return 4;
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
		return new Vector3f(183 / 255f, 212 / 255f, 80 / 255f);
	}
}
