package steve6472.polyground.world.biomes;

import org.joml.Vector3f;
import steve6472.polyground.block.Block;
import steve6472.polyground.registry.BlockRegistry;
import steve6472.polyground.world.generator.FeatureStage;
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
		addFeature(FeatureStage.CAVE_ALTER, new StalaFeature(BlockRegistry.getBlockByName("stone"), 0.6f), 1 / 256d);
		addFeature(FeatureStage.CAVE_ALTER, new BushFeature(BlockRegistry.getBlockByName("stone"), BlockRegistry.getBlockByName("crystal_log"), BlockRegistry.getBlockByName("crystal_leaves"), true), 1d / 1024d / 16d);
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
