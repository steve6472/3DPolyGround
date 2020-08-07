package steve6472.polyground.world.biomes;

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
//	public static final Mountains MOUNTAINS = addBiome(new Mountains());
//	public static final Desert DESERT = addBiome(new Desert());
//	public static final Plains PLAINS = addBiome(new Plains());
//	public static final Forest FOREST = addBiome(new Forest());
//	public static final Tundra TUNDRA = addBiome(new Tundra());
	public static final DesertBiome DESERT = addBiome(new DesertBiome());
	public static final ForestBiome FOREST = addBiome(new ForestBiome());
	public static final OceanBiome OCEAN = addBiome(new OceanBiome());
	public static final CrystalCaveBiome CRYSTAL_CAVE = addBiome(new CrystalCaveBiome());

//	public static final DataBiome SAVANNA = addBiome(
//		new DataBiome(Tile.GRASS, Tile.DIRT, Tile.STONE,
//			12, 4, 6,
//			new Vector3f(120 / 255f, 190 / 255f, 60 / 255f),
//			0.3f, 0.019f, 12, 23, 0.8f, 0.1f)
//			.feature(EnumFeatureStage.TREE, 1f / 400, new AcaciaTreeFeature(Tile.GRASS, Tile.LOG, Tile.LEAVES, Tile.DIRT)));

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
