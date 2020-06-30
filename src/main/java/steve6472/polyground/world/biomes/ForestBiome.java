package steve6472.polyground.world.biomes;

import org.joml.Vector3f;
import steve6472.polyground.block.Block;
import steve6472.polyground.registry.BlockRegistry;
import steve6472.polyground.world.generator.FeatureStage;
import steve6472.polyground.world.generator.feature.BushFeature;
import steve6472.polyground.world.generator.feature.LakeFeature;
import steve6472.polyground.world.generator.feature.TreeFeature;
import steve6472.polyground.world.generator.feature.VegetationPatchFeature;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.11.2019
 * Project: SJP
 *
 ***********************/
public class ForestBiome extends Biome
{
	@Override
	public void addFeatures()
	{
//		features.add(new FeatureEntry(new BlockPatchFeature(BlockRegistry.getBlockByName("biome_grass"), BlockRegistry.getBlockByName("dirt"), 0.6, 3, false), 2 / 256d));

		addFeature(FeatureStage.LAND_ALTER, new LakeFeature(), 1 / 3000d);
		addFeature(FeatureStage.VEGETATION, new VegetationPatchFeature(BlockRegistry.getBlockByName("biome_grass"), BlockRegistry.getBlockByName("small_grass"), 0.3, 2, true), 1 / 100d);
		addFeature(FeatureStage.TREE, new BushFeature(BlockRegistry.getBlockByName("biome_grass"), BlockRegistry.getBlockByName("oak_log"), BlockRegistry.getBlockByName("oak_leaves")), 1 / 200d);
		addFeature(FeatureStage.TREE, new TreeFeature(BlockRegistry.getBlockByName("biome_grass"), BlockRegistry.getBlockByName("oak_log"), BlockRegistry.getBlockByName("oak_leaves"), BlockRegistry.getBlockByName("dirt")), 1 / 5d);

		//		features.add(new FeatureEntry(new LakeFeature(), 1 / 3000d));
//		features.add(new FeatureEntry(new VegetationPatchFeature(BlockRegistry.getBlockByName("biome_grass"), BlockRegistry.getBlockByName("small_grass"), 0.3, 2, true), 1 / 100d));
//		features.add(new FeatureEntry(new BushFeature(BlockRegistry.getBlockByName("biome_grass"), BlockRegistry.getBlockByName("oak_log"), BlockRegistry.getBlockByName("oak_leaves")), 1 / 200d));
//		features.add(new FeatureEntry(new TreeFeature(BlockRegistry.getBlockByName("biome_grass"), BlockRegistry.getBlockByName("oak_log"), BlockRegistry.getBlockByName("oak_leaves"), BlockRegistry.getBlockByName("dirt")), 1 / 5d));
	}

	@Override
	public String getName()
	{
		return "forest";
	}

	@Override
	public Block getTopBlock()
	{
		return BlockRegistry.getBlockByName("biome_grass");
	}

	@Override
	public Block getUnderBlock()
	{
		return BlockRegistry.getBlockByName("dirt");
	}

	@Override
	public Block getCaveBlock()
	{
		return BlockRegistry.getBlockByName("cobblestone");
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
		return 0.07f;
	}

	@Override
	public float getScale()
	{
		return 0.007f;
	}

	@Override
	public float getLow()
	{
		return 10;
	}

	@Override
	public float getHigh()
	{
		return 50;
	}

	@Override
	public Vector3f getColor()
	{
		return new Vector3f(92 / 255f, 184 / 255f, 64 / 255f);
	}
}
