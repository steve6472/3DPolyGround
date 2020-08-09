package steve6472.polyground.world.biomes;

import org.joml.Vector3f;
import steve6472.polyground.registry.BlockRegistry;
import steve6472.polyground.world.generator.EnumFeatureStage;
import steve6472.polyground.world.generator.feature.TreeFeature;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.07.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public class Biomes
{
	private static final List<Biome> BIOMES = new ArrayList<>();

	public static final AirBiome AIR_BIOME = addBiome(new AirBiome());
	public static final DesertBiome DESERT = addBiome(new DesertBiome());
	public static final ForestBiome FOREST = addBiome(new ForestBiome());
//	public static final OceanBiome OCEAN = addBiome(new OceanBiome());
	public static final CrystalCaveBiome CRYSTAL_CAVE = addBiome(new CrystalCaveBiome());

	public static final DataBiome SAVANNA = addBiome(
		new DataBiome("savanna", BlockRegistry.getDefaultState("grass"), BlockRegistry.getDefaultState("dirt"), BlockRegistry.getDefaultState("stone"),
			12, 4, 6,
			new Vector3f(120 / 255f, 190 / 255f, 60 / 255f),
			0.3f, 0.019f, 12, 23, 0.8f, 0.1f)
			.feature(EnumFeatureStage.TREE, 1f / 400, new TreeFeature(BlockRegistry.getBlockByName("grass"), BlockRegistry.getBlockByName("oak_log"), BlockRegistry.getBlockByName("oak_leaves"), BlockRegistry.getBlockByName("dirt"))));

	public static final DataBiome FLAT = addBiome(new DataBiome("flat", BlockRegistry.getDefaultState("stone"), BlockRegistry.getDefaultState("stone"), BlockRegistry.getDefaultState("stone"),
		256, 0, 0, new Vector3f(145 / 255f, 189 / 255f, 89 / 255f), 0, 0, 0, 0, 0, 0));

	public static int ID;

	private static <T extends Biome> T addBiome(T biome)
	{
		BIOMES.add(biome);

		return biome;
	}

	public static Biome getBiome(int id)
	{
		return BIOMES.get(id);
	}

	public static int count()
	{
		return BIOMES.size() - 1;
	}
}
