package steve6472.polyground.world.biomes;

import org.joml.Vector3f;
import steve6472.polyground.block.Block;
import steve6472.polyground.registry.BlockRegistry;
import steve6472.polyground.world.generator.FeatureStage;
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
		addFeature(FeatureStage.VEGETATION, new VegetationPatchFeature(BlockRegistry.getBlockByName("sand"), BlockRegistry.getBlockByName("small_grass"), 0.5, 2, true), 1 / 100d);

//				features.add(new FeatureEntry(new VegetationPatchFeature(BlockRegistry.getBlockByName("sand"), BlockRegistry.getBlockByName("small_grass"), 0.5, 2, true), 1 / 100d));
	}

	@Override
	public String getName()
	{
		return "desert";
	}

	@Override
	public Block getTopBlock()
	{
		return BlockRegistry.getBlockByName("sand");
	}

	@Override
	public Block getUnderBlock()
	{
		return BlockRegistry.getBlockByName("cobblestone");
	}

	@Override
	public Block getCaveBlock()
	{
		return BlockRegistry.getBlockByName("stone");
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
