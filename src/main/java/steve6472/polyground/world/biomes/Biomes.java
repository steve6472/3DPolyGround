package steve6472.polyground.world.biomes;

import org.joml.Vector3f;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.generator.EnumFeatureStage;
import steve6472.polyground.world.generator.feature.*;

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
	// Altitude, temperature, weirdness, humidity

	public static final AirBiome AIR_BIOME = addBiome(new AirBiome());
//	public static final OceanBiome OCEAN = addBiome(new OceanBiome());
	public static final CrystalCaveBiome CRYSTAL_CAVE = addBiome(new CrystalCaveBiome());

	public static final DataBiome DESERT = addBiome(new DataBiome("desert", Blocks.getDefaultState("sand"), Blocks.getDefaultState("sandstone"), Blocks.getDefaultState("stone"),
		8, 4, 4, new Vector3f(183 / 255f, 212 / 255f, 80 / 255f), 0.3f, 0.03f, 8, 18)
		.altitude(0.05f).temperature(1f).weirdness(0f).humidity(-1f)

		.feature(EnumFeatureStage.VEGETATION, 1 / 100d, new VegetationPatchFeature(Blocks.getDefaultState("sand"), Blocks.getDefaultState("small_grass"), 0.5, 2, true))
		.feature(EnumFeatureStage.VEGETATION, 1d / 256d, new PillarFeature(Blocks.getDefaultState("sand"), Blocks.getDefaultState("cactus"), 1, 4))
		.feature(EnumFeatureStage.VEGETATION, 1d / 256d / 4d, new StackablePillarFeature(Blocks.getDefaultState("sand"), Blocks.getDefaultState("cactus"), 0.5, 22))
		.create());

	public static final DataBiome DESERT_HILLS = addBiome(new DataBiome("desert_hills", Blocks.getDefaultState("sand"), Blocks.getDefaultState("sandstone"), Blocks.getDefaultState("stone"),
		8, 4, 6, new Vector3f(183 / 255f, 212 / 255f, 80 / 255f), 0.5f, 0.05f, 9, 33)
		.altitude(0.65f).temperature(0.95f).weirdness(0f).humidity(-0.99f)

		.feature(EnumFeatureStage.VEGETATION, 1 / 100d, new VegetationPatchFeature(Blocks.getDefaultState("sand"), Blocks.getDefaultState("small_grass"), 0.5, 2, true))
		.feature(EnumFeatureStage.VEGETATION, 1d / 256d, new PillarFeature(Blocks.getDefaultState("sand"), Blocks.getDefaultState("cactus"), 1, 4))
		.feature(EnumFeatureStage.VEGETATION, 1d / 256d / 4d, new StackablePillarFeature(Blocks.getDefaultState("sand"), Blocks.getDefaultState("cactus"), 0.5, 22))
		.create());

	public static final DataBiome FOREST = addBiome(new DataBiome("forest", Blocks.getDefaultState("grass"), Blocks.getDefaultState("dirt"), Blocks.getDefaultState("stone"),
		14, 4, 8, new Vector3f(92 / 255f, 184 / 255f, 64 / 255f), 0.07f, 0.007f, 12, 22)
		.altitude(0.1f).temperature(0.25f).weirdness(0f).humidity(0.6f)

		.feature(EnumFeatureStage.LAND_ALTER, 1 / 3000d, new LakeFeature())
		.feature(EnumFeatureStage.VEGETATION, 1 / 100d, new VegetationPatchFeature(Blocks.getDefaultState("grass"), Blocks.getDefaultState("small_grass"), 0.3, 2, true))
		.feature(EnumFeatureStage.TREE, 1 / 200d, new BushFeature(Blocks.getBlockByName("grass"), Blocks.getBlockByName("oak_log"), Blocks.getBlockByName("oak_leaves")))
		.feature(EnumFeatureStage.TREE, 1 / 5d, new TreeFeature(Blocks.getDefaultState("grass"), Blocks.getDefaultState("oak_log"), Blocks.getDefaultState("oak_leaves"), Blocks.getDefaultState("dirt")))

		.create());

	public static final DataBiome PLAINS = addBiome(new DataBiome("plains", Blocks.getDefaultState("grass"), Blocks.getDefaultState("dirt"), Blocks.getDefaultState("stone"),
		12, 4, 7, new Vector3f(92 / 255f, 200 / 255f, 64 / 255f), 0.07f, 0.006f, 10, 20)
		.altitude(0f).temperature(0.2f).weirdness(0f).humidity(0.3f)

		.feature(EnumFeatureStage.VEGETATION, 1 / 120d, new VegetationPatchFeature(Blocks.getDefaultState("grass"), Blocks.getDefaultState("small_grass"), 0.75, 4, true))
		.feature(EnumFeatureStage.VEGETATION, 1 / 120d, new VegetationPatchFeature(Blocks.getDefaultState("grass"), Blocks.getDefaultState("tall_grass"), 0.75, 4, true))
		.feature(EnumFeatureStage.TREE, 1 / 300d, new BushFeature(Blocks.getBlockByName("grass"), Blocks.getBlockByName("oak_log"), Blocks.getBlockByName("oak_leaves")))

		.create());

	public static final DataBiome TUNDRA = addBiome(new DataBiome("tundra", Blocks.getDefaultState("grass"), Blocks.getDefaultState("dirt"), Blocks.getDefaultState("stone"),
		12, 4, 7, new Vector3f(92 / 255f, 200 / 255f, 64 / 255f), 0.06f, 0.005f, 10, 18)
		.altitude(0f).temperature(-1f).weirdness(0f).humidity(0.25f)

		.feature(EnumFeatureStage.VEGETATION, 1 / 120d, new VegetationPatchFeature(Blocks.getDefaultState("grass"), Blocks.getDefaultState("small_grass"), 0.75, 4, true))
		.feature(EnumFeatureStage.VEGETATION, 1 / 120d, new VegetationPatchFeature(Blocks.getDefaultState("grass"), Blocks.getDefaultState("tall_grass"), 0.75, 4, true))
		.feature(EnumFeatureStage.TREE, 1 / 300d, new TreeFeature(Blocks.getDefaultState("grass"), Blocks.getDefaultState("oak_log"), Blocks.getDefaultState("oak_leaves"), Blocks.getDefaultState("dirt")))
		.feature(EnumFeatureStage.TOP_MODIFICATION, 1, new TopSnowFeature(Blocks.getDefaultState("grass"), Blocks.getBlockByName("snow_layer"), 0, 0))

		.create());

	public static final DataBiome SAVANNA = addBiome(
		new DataBiome("savanna", Blocks.getDefaultState("grass"), Blocks.getDefaultState("dirt"), Blocks.getDefaultState("stone"),
			14, 4, 6,
			new Vector3f(120 / 255f, 190 / 255f, 60 / 255f),
			0.3f, 0.019f, 12, 23)

			.altitude(0.1f).temperature(0.65f).weirdness(0.0f).humidity(0.05f)

			.feature(EnumFeatureStage.TREE, 1f / 400, new TreeFeature(Blocks.getDefaultState("grass"), Blocks.getDefaultState("oak_log"), Blocks.getDefaultState("oak_leaves"), Blocks.getDefaultState("dirt")))
			.feature(EnumFeatureStage.VEGETATION, 1 / 100d, new VegetationPatchFeature(Blocks.getDefaultState("grass"), Blocks.getDefaultState("small_grass"), 0.5, 2, true))
			.create());


	public static final DataBiome SAVANNA_PLATEAU = addBiome(
		new DataBiome("savanna_plateau", Blocks.getDefaultState("grass"), Blocks.getDefaultState("dirt"), Blocks.getDefaultState("stone"),
			14, 4, 6,
			new Vector3f(120 / 255f, 190 / 255f, 60 / 255f),
			0.3f, 0.003f, 23, 30)

			.altitude(0.8f).temperature(0.60f).weirdness(0.3f).humidity(0f)

			.feature(EnumFeatureStage.TREE, 1f / 400, new TreeFeature(Blocks.getDefaultState("grass"), Blocks.getDefaultState("oak_log"), Blocks.getDefaultState("oak_leaves"), Blocks.getDefaultState("dirt")))
			.feature(EnumFeatureStage.VEGETATION, 1 / 200d, new VegetationPatchFeature(Blocks.getDefaultState("grass"), Blocks.getDefaultState("small_grass"), 0.5, 2, true)).create());

	public static final DataBiome MOUNTAINS = addBiome(new DataBiome("mountains", Blocks.getDefaultState("stone"), Blocks.getDefaultState("stone"), Blocks.getDefaultState("stone"),
		12, 0, 3, new Vector3f(92 / 255f, 160 / 255f, 64 / 255f), 0.2f, 0.025f, 15, 50)
		.altitude(1f).temperature(0.1f).weirdness(0.0f).humidity(0.17f)

		.feature(EnumFeatureStage.TOP_MODIFICATION, 1.0 / 444.4, new TileReplacePatchFeature(Blocks.getDefaultState("stone"), Blocks.getDefaultState("grass"), 0.9, 5, true, true))
		.create());


	public static final DataBiome COLD_MOUNTAINS = addBiome(new DataBiome("cold_mountains", Blocks.getDefaultState("stone"), Blocks.getDefaultState("stone"), Blocks.getDefaultState("stone"),
		12, 0, 3, new Vector3f(92 / 255f, 160 / 255f, 64 / 255f), 0.2f, 0.025f, 15, 55)
		.altitude(1f).temperature(-1f).weirdness(0.0f).humidity(0.15f)

		.feature(EnumFeatureStage.TOP_MODIFICATION, 1.0 / 500.0, new TileReplacePatchFeature(Blocks.getDefaultState("stone"), Blocks.getDefaultState("snow_block"), 1, 4, true, false))
		.feature(EnumFeatureStage.TOP_MODIFICATION, 1, new TopSnowFeature(Blocks.getDefaultState("stone"), Blocks.getBlockByName("snow_layer"), 46, 52)).create());

	public static final DataBiome FLAT = addBiome(new DataBiome("flat", Blocks.getDefaultState("stone"), Blocks.getDefaultState("stone"), Blocks.getDefaultState("stone"),
		256, 0, 0, new Vector3f(145 / 255f, 189 / 255f, 89 / 255f), 0, 0, 0, 0).create());

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
