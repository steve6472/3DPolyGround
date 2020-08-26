package steve6472.polyground.world.biomes;

import org.joml.Vector3f;
import org.json.JSONArray;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.generator.EnumFeatureStage;
import steve6472.polyground.world.generator.feature.BushFeature;
import steve6472.polyground.world.generator.feature.cave.OreVein;
import steve6472.polyground.world.generator.feature.cave.StalaFeature;
import steve6472.polyground.world.generator.feature.components.match.BlockMatch;
import steve6472.polyground.world.generator.feature.components.match.IBlockMatch;
import steve6472.polyground.world.generator.feature.components.provider.IBlockProvider;
import steve6472.polyground.world.generator.feature.components.provider.SimpleStateProvider;

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
		final IBlockMatch stoneMatch = new BlockMatch();
		stoneMatch.load(new JSONArray().put("stone"));

		final IBlockProvider andesiteProvider = new SimpleStateProvider();
		andesiteProvider.load(new JSONArray().put("andesite"));

		final IBlockProvider dioriteProvider = new SimpleStateProvider();
		dioriteProvider.load(new JSONArray().put("diorite"));

		final IBlockProvider graniteProvider = new SimpleStateProvider();
		graniteProvider.load(new JSONArray().put("granite"));

		addFeature(EnumFeatureStage.CAVE_ALTER, 1d / 1024d / 16d, new BushFeature(Blocks.getDefaultState("stone"), Blocks.getDefaultState("crystal_log"), Blocks.getDefaultState("crystal_leaves"), true));
		addFeature(EnumFeatureStage.CAVE_ALTER, 1d / 25000d, new OreVein(stoneMatch, andesiteProvider, 8, 24, 0.9d));
		addFeature(EnumFeatureStage.CAVE_ALTER, 1d / 40000d, new OreVein(stoneMatch, dioriteProvider, 2, 10, 0.4d));
		addFeature(EnumFeatureStage.CAVE_ALTER, 1d / 20000d, new OreVein(stoneMatch, graniteProvider, 4, 16, 0.8d));

		addFeature(EnumFeatureStage.CAVE_ALTER, 1 / 256d, new StalaFeature(Blocks.getDefaultState("stone"), "stone_stala", 0.6f));
		addFeature(EnumFeatureStage.CAVE_ALTER, 1 / 256d, new StalaFeature(Blocks.getDefaultState("andesite"), "andesite_stala", 0.55f));
		addFeature(EnumFeatureStage.CAVE_ALTER, 1 / 256d, new StalaFeature(Blocks.getDefaultState("diorite"), "diorite_stala", 0.2f));
		addFeature(EnumFeatureStage.CAVE_ALTER, 1 / 256d, new StalaFeature(Blocks.getDefaultState("granite"), "granite_stala", 0.45f));
	}

	@Override
	public float[] getParameters()
	{
		return new float[4];
	}

	@Override
	public String getName()
	{
		return "crystal_cave";
	}

	@Override
	public BlockState getTopBlock()
	{
		return Blocks.getBlockByName("stone").getDefaultState();
	}

	@Override
	public BlockState getUnderBlock()
	{
		return Blocks.getBlockByName("stone").getDefaultState();
	}

	@Override
	public BlockState getCaveBlock()
	{
		return Blocks.getBlockByName("air").getDefaultState();
	}

	/**
	 * Indicates how high the biome will stretch above surface
	 *
	 * @return biome height
	 */
	@Override
	public int getBiomeHeight()
	{
		return 9;
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
	public boolean generatesNaturally()
	{
		return false;
	}

	@Override
	public String toString()
	{
		return "CrystalCaveBiome{}";
	}
}
