package steve6472.polyground.generator;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Tags;
import steve6472.polyground.block.properties.enums.EnumAxis;
import steve6472.polyground.block.properties.enums.EnumHalf;
import steve6472.polyground.block.properties.enums.EnumSlabType;
import steve6472.polyground.block.special.*;
import steve6472.polyground.block.states.States;
import steve6472.polyground.generator.biome.BiomeBuilder;
import steve6472.polyground.generator.biome.feature.BlockStateBuilder;
import steve6472.polyground.generator.biome.feature.FeatureBuilder;
import steve6472.polyground.generator.models.*;
import steve6472.polyground.generator.special.SimpleSpecial;
import steve6472.polyground.generator.special.SpecialBuilder;
import steve6472.polyground.generator.state.PropertyBuilder;
import steve6472.polyground.generator.state.StateBuilder;
import steve6472.polyground.registry.feature.FeatureRegistry;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.polyground.world.generator.EnumFeatureStage;

import java.io.File;
import java.util.function.BiFunction;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 24.09.2019
 * Project: SJP
 *
 ***********************/
public class DataGenerator
{
	public static File BLOCKS = new File("game/objects/blocks");
	public static File ITEMS = new File("game/objects/items");
	public static File BIOMES = new File("game/objects/biomes");
	public static File FEATURES = new File("game/objects/features");

	public static File BLOCK_MODELS = new File("game/objects/models/block");
	public static File ITEM_MODELS = new File("game/objects/models/item");

	public static File BLOCK_STATES = new File("game/objects/blockstates");

	public static void main(String[] args)
	{
		new DataGenerator().generateBlocksItems();
		new DataGenerator().generateFeatures();
		new DataGenerator().generateBiomes();
		new DataGenerator().generateDebug();
	}

	private void createFolders()
	{
		folder(BLOCKS, "blocks");
		folder(ITEMS, "items");
		folder(BLOCK_MODELS, "blockModels");
		folder(ITEM_MODELS, "itemModels");
		folder(BLOCK_STATES, "blockStates");
		folder(BIOMES, "biomes");
		folder(FEATURES, "features");
	}

	private void folder(File file, String name)
	{
		if (!file.exists())
			if (file.mkdirs())
				System.out.println("Created new " + name + " folder");
			else
				System.err.println("Error while creating new " + name + " folder");
	}

	public void generateDebug()
	{
		createFolders();

		BiFunction<Boolean, Integer, PropertyBuilder> leavesProperty = (b, i) -> PropertyBuilder.create().addProperty(LeavesBlock.PERSISTENT, b).addProperty(LeavesBlock.DISTANCE, i);
		BiFunction<Boolean, Integer, BlockModelBuilder> leavesModel = (b, i) -> BlockModelBuilder.create("leaves" + (b ? "_persistent" : "") + "_" + i).addCube(CubeBuilder.create().fullBlock().face(FaceBuilder.create().texture("number/" + i).biomeTint(b)));

		DataBuilder.create().fullBlock("visual_leaves")
			.blockSpecial(new SimpleSpecial("leaves"))
			.blockState(StateBuilder.create()
				.addState(leavesProperty.apply(false, 1), leavesModel.apply(false, 1))
				.addState(leavesProperty.apply(false, 2), leavesModel.apply(false, 2))
				.addState(leavesProperty.apply(false, 3), leavesModel.apply(false, 3))
				.addState(leavesProperty.apply(false, 4), leavesModel.apply(false, 4))
				.addState(leavesProperty.apply(false, 5), leavesModel.apply(false, 5))
				.addState(leavesProperty.apply(false, 6), leavesModel.apply(false, 6))
				.addState(leavesProperty.apply(false, 7), leavesModel.apply(false, 7))
				.addState(leavesProperty.apply(true, 1), leavesModel.apply(true, 1))
				.addState(leavesProperty.apply(true, 2), leavesModel.apply(true, 2))
				.addState(leavesProperty.apply(true, 3), leavesModel.apply(true, 3))
				.addState(leavesProperty.apply(true, 4), leavesModel.apply(true, 4))
				.addState(leavesProperty.apply(true, 5), leavesModel.apply(true, 5))
				.addState(leavesProperty.apply(true, 6), leavesModel.apply(true, 6))
				.addState(leavesProperty.apply(true, 7), leavesModel.apply(true, 7))
			).debug().generate();

//		DataBuilder.create().torch("slime_torch", true).blockSpecial(new SimpleSpecial("state_test")).itemModel(new ItemFromTexture("slime_torch")).generate();
	}

	public void generateFeatures()
	{
		FeatureBuilder.create(FeatureRegistry.STACKABLE_PILLAR.name())
			.path("desert")
			.name("tall_cactus")
			.matchTags("block_under", Tags.CACTUS_TOP)
			.blockState("block", BlockStateBuilder.create().block("cactus"))
			.doubleArg("next_chance", 0.1)
			.integer("max_height", 22)
			.generate();

		FeatureBuilder.create(FeatureRegistry.PILLAR.name())
			.path("desert")
			.name("cactus")
			.matchTags("block_under", Tags.CACTUS_TOP)
			.provideStates("block", BlockStateBuilder.create().block("cactus"))
			.integer("min_height", 1)
			.integer("max_height", 3)
			.generate();

		FeatureBuilder.create(FeatureRegistry.PILLAR.name())
			.path("common")
			.name("top_snow")
//			.matchBlocks("block_under", "grass", "dirt", "stone", "sand")
			.matchAlwaysTrue("block_under")
			.provideStates("block",
				BlockStateBuilder.create().block("snow_layer").state(SnowLayerBlock.SNOW_LAYER, 1),
				BlockStateBuilder.create().block("snow_layer").state(SnowLayerBlock.SNOW_LAYER, 2),
				BlockStateBuilder.create().block("snow_layer").state(SnowLayerBlock.SNOW_LAYER, 3),
				BlockStateBuilder.create().block("snow_layer").state(SnowLayerBlock.SNOW_LAYER, 4))
			.integer("min_height", 1)
			.integer("max_height", 1)
			.generate();

		FeatureBuilder.create(FeatureRegistry.VEGETATION_PATCH.name())
			.path("desert")
			.name("shrubs")
			.matchTags("block_under", Tags.SHRUBS_TOP)
			.blockState("block", BlockStateBuilder.create().block("shrub"))
			.integer("radius", 2)
			.doubleArg("chance", 0.5)
			.bool("decay", true)
			.generate();

		FeatureBuilder.create(FeatureRegistry.VEGETATION_PATCH.name())
			.path("common")
			.name("grass_patch")
			.matchTags("block_under", Tags.FLOWER_TOP)
			.blockState("block", BlockStateBuilder.create().block("small_grass"))
			.integer("radius", 2)
			.doubleArg("chance", 0.3)
			.bool("decay", true)
			.generate();

		FeatureBuilder.create(FeatureRegistry.VEGETATION_PATCH.name())
			.path("common")
			.name("tall_grass_patch")
			.matchTags("block_under", Tags.FLOWER_TOP)
			.blockState("block", BlockStateBuilder.create().block("tall_grass"))
			.integer("radius", 4)
			.doubleArg("chance", 0.5)
			.bool("decay", true)
			.generate();

		FeatureBuilder.create(FeatureRegistry.VEGETATION_PATCH.name())
			.path("common")
			.name("grass_patch_mixed")
			.matchTags("block_under", Tags.FLOWER_TOP)
			.blockState("block", BlockStateBuilder.create().block("tall_grass"))
			.integer("radius", 4)
			.doubleArg("chance", 0.75)
			.bool("decay", true)
			.generate();

		FeatureBuilder.create(FeatureRegistry.VEGETATION_PATCH.name())
			.path("common")
			.name("pebbles")
			.matchBlocks("block_under", "sand", "grass", "dirt", "stone")
			.blockState("block", BlockStateBuilder.create().block("pebble"))
			.integer("radius", 3)
			.doubleArg("chance", 0.3)
			.bool("decay", true)
			.generate();

		FeatureBuilder.create(FeatureRegistry.TREE.name())
			.path("trees")
			.name("oak_tree")
			.matchBlocks("match_under", "grass", "dirt")
			.blockState("log", BlockStateBuilder.create().block("oak_log"))
			.blockState("leaves", BlockStateBuilder.create().block("oak_leaves"))
			.blockState("set_under", BlockStateBuilder.create().block("dirt"))
			.integer("height_min", 5)
			.integer("height_max", 7)
			.generate();

		FeatureBuilder.create(FeatureRegistry.TOP_SNOW.name())
			.path("mountain")
			.name("top_mountain_snow")
			.matchAlwaysTrue("target")
			.provideStates("snow",
				BlockStateBuilder.create().block("snow_layer").state(SnowLayerBlock.SNOW_LAYER, 1),
				BlockStateBuilder.create().block("snow_layer").state(SnowLayerBlock.SNOW_LAYER, 2),
				BlockStateBuilder.create().block("snow_layer").state(SnowLayerBlock.SNOW_LAYER, 3),
				BlockStateBuilder.create().block("snow_layer").state(SnowLayerBlock.SNOW_LAYER, 4))
			.integer("height_start", 46)
			.integer("height_max", 52)
			.generate();

		FeatureBuilder.create(FeatureRegistry.TILE_REPLACE_PATCH.name())
			.path("mountain")
			.name("mountain_snow_patches")
			.matchBlocks("target", "stone")
			.provideStates("block", BlockStateBuilder.create().block("snow_block"))
			.doubleArg("chance", 1)
			.integer("radius", 4)
			.bool("decay_from_center", true)
			.bool("only_top", false)
			.generate();
	}

	public void generateBiomes()
	{
		BiomeBuilder.create()
			.name("desert")
			.surface("sand", "sandstone", "stone", 4, 8)
			.heightMap(4, 0.3f, 0.03f, 8, 18)
			.climate(0.05f, 1f, 0f, -1f)
			.foliageColor(183 / 255f, 212 / 255f, 80 / 255f)
			.feature(EnumFeatureStage.VEGETATION, 1d / 100d, "shrubs")
			.feature(EnumFeatureStage.VEGETATION, 1d / 256d / 4d, "tall_cactus")
			.feature(EnumFeatureStage.VEGETATION, 1d / 256d, "cactus")
			.feature(EnumFeatureStage.VEGETATION, 1d / 256d / 8d, "pebbles")
			.generate();

		BiomeBuilder.create()
			.name("desert_hills")
			.surface("sand", "sandstone", "stone", 4, 8)
			.heightMap(6, 0.5f, 0.05f, 9, 33)
			.climate(0.65f, 0.95f, 0f, -0.99f)
			.foliageColor(183 / 255f, 212 / 255f, 80 / 255f)
			.feature(EnumFeatureStage.VEGETATION, 1d / 100d, "shrubs")
			.feature(EnumFeatureStage.VEGETATION, 1d / 256d / 4d, "tall_cactus")
			.feature(EnumFeatureStage.VEGETATION, 1d / 256d, "cactus")
			.feature(EnumFeatureStage.VEGETATION, 1d / 256d / 8d, "pebbles")
			.generate();

		BiomeBuilder.create()
			.name("forest")
			.surface("grass", "dirt", "stone", 4, 14)
			.heightMap(8, 0.07f, 0.007f, 12, 22)
			.climate(0.1f, 0.25f, 0f, -0.6f)
			.foliageColor(92 / 255f, 184 / 255f, 64 / 255f)
			.feature(EnumFeatureStage.VEGETATION, 1d / 100d, "grass_patch")
			.feature(EnumFeatureStage.VEGETATION, 1d / 256d, "pebbles")
			.feature(EnumFeatureStage.TREE, 1d / 5d, "oak_tree")
			.generate();

		BiomeBuilder.create()
			.name("plains")
			.surface("grass", "dirt", "stone", 4, 12)
			.heightMap(7, 0.07f, 0.006f, 10, 20)
			.climate(0f, 0.2f, 0f, -0.3f)
			.foliageColor(92 / 255f, 200 / 255f, 64 / 255f)
			.feature(EnumFeatureStage.VEGETATION, 1d / 100d, "grass_patch")
			.feature(EnumFeatureStage.VEGETATION, 1d / 220d, "tall_grass_patch")
			.feature(EnumFeatureStage.VEGETATION, 1d / 512d, "pebbles")
			.generate();

		BiomeBuilder.create()
			.name("tundra")
			.surface("grass", "dirt", "stone", 4, 12)
			.heightMap(7, 0.06f, 0.005f, 10, 18)
			.climate(0f, -1f, 0f, 0.25f)
			.foliageColor(92 / 255f, 200 / 255f, 64 / 255f)
			.feature(EnumFeatureStage.TOP_MODIFICATION, 1, "top_snow")
			.feature(EnumFeatureStage.TREE, 1d / 300d, "oak_tree")
			.generate();

		BiomeBuilder.create()
			.name("savanna")
			.surface("grass", "dirt", "stone", 4, 14)
			.heightMap(6, 0.3f, 0.019f, 12, 23)
			.climate(0.1f, 0.65f, 0.0f, 0.05f)
			.foliageColor(120 / 255f, 190 / 255f, 60 / 255f)
			.feature(EnumFeatureStage.TREE, 1d / 400d, "oak_tree")
			.feature(EnumFeatureStage.VEGETATION, 1d / 256d, "pebbles")
			.feature(EnumFeatureStage.VEGETATION, 1d / 100d, "grass_patch")
			.generate();

		BiomeBuilder.create()
			.name("savanna_plateau")
			.surface("grass", "dirt", "stone", 4, 14)
			.heightMap(6, 0.3f, 0.003f, 23, 30)
			.climate(0.8f, 0.60f, 0.3f, 0.0f)
			.foliageColor(120 / 255f, 190 / 255f, 60 / 255f)
			.feature(EnumFeatureStage.TREE, 1d / 400d, "oak_tree")
			.feature(EnumFeatureStage.VEGETATION, 1d / 256d, "pebbles")
			.feature(EnumFeatureStage.VEGETATION, 1d / 100d, "grass_patch")
			.generate();

		BiomeBuilder.create()
			.name("mountains")
			.surface("stone", "stone", "stone", 0, 12)
			.heightMap(3, 0.2f, 0.025f, 15, 50)
			.climate(1f, 0.1f, 0.0f, 0.17f)
			.foliageColor(92 / 255f, 160 / 255f, 64 / 255f)
			.generate();

		BiomeBuilder.create()
			.name("cold_mountains")
			.surface("stone", "stone", "stone", 0, 12)
			.heightMap(3, 0.2f, 0.025f, 15, 50)
			.climate(1f, -1f, 0.0f, 0.15f)
			.foliageColor(92 / 255f, 160 / 255f, 64 / 255f)
			.feature(EnumFeatureStage.TOP_MODIFICATION, 1d, "top_mountain_snow")
			.feature(EnumFeatureStage.TOP_MODIFICATION, 1.0 / 500.0, "mountain_snow_patches")
			.generate();
	}

	public void generateBlocksItems()
	{
		createFolders();
//		decorativeFullTintedBlock("full_grass_block", 0.5686275f, 0.7411765f, 0.34901962f, "grass_block_top");

		// Items
		DataBuilder.create().itemModel(new ItemFromTexture("block_inspector")).itemName("block_inspector").itemSpecial(new SimpleSpecial("block_inspector")).generate();
		DataBuilder.create().itemModel(new ItemFromTexture("rift_placer")).itemName("rift_placer").itemSpecial(new SimpleSpecial("riftplacer")).generate();
		DataBuilder.create().itemModel(new ItemFromTexture("speedometer")).itemName("speedometer").itemSpecial(new SimpleSpecial("speedometer")).generate();

		// Blocks
		DataBuilder.create().fullBlock("null").noItem().generate();
		DataBuilder.create().fullBlock("smooth_stone").generate();
		DataBuilder.create().fullBlock("bricks").generate();
		DataBuilder.create().fullBlock("iron_block").generate();
		DataBuilder.create().fullBlock("stone").generate();
		DataBuilder.create().fullBlock("cobblestone").generate();
		DataBuilder.create().fullBlock("bedrock").generate();
		DataBuilder.create().fullBlock("gravel").generate();
		DataBuilder.create().fullBlock("oak_plank").generate();
		DataBuilder.create().fullBlock("andesite").generate();
		DataBuilder.create().fullBlock("diorite").generate();
		DataBuilder.create().fullBlock("granite").generate();
		DataBuilder.create().fullBlock("sand").addTags(Tags.CACTUS_TOP, Tags.SHRUBS_TOP).generate();
		DataBuilder.create().fullBlock("dirt").addTag(Tags.FLOWER_TOP).generate();

		DataBuilder.create().fullBlock("snow_block", "snow").generate();

		DataBuilder.create().fullBlock("sandstone", "sandstone_top", "sandstone", "sandstone_bottom").generate();

//		DataBuilder.create().fullBlock("cond_test").blockState(StateBuilder.create().singleModel(new CondTest())).blockModel().generate();

		DataBuilder.create().stairs("stone_stairs", "stone").generate();
		DataBuilder.create().stairs("oak_plank_stairs", "oak_plank").generate();
		DataBuilder.create().stairs("cobblestone_stairs", "cobblestone").generate();
		DataBuilder.create().stairs("brick_stairs", "bricks").generate();

		DataBuilder.create().plusBlock("small_grass", true).blockSpecial(new SimpleSpecial("flower")).addTag(Tags.TRANSPARENT).generate();
		DataBuilder.create().plusBlock("shrub", true).blockSpecial(new SimpleSpecial("shrub")).addTag(Tags.TRANSPARENT).generate();

		DataBuilder.create().oreBlock("coal_ore", "stone", "ore_overlay", 77, 77, 77).generate();

		DataBuilder.create().lightOreBlock("magical_coal_ore", "stone", "ore_overlay", 26, 204, 204, "6c7f7a", 1.0f, 0.7f, 1.8f).generate();

		DataBuilder.create().fullLightBlock("blaze_block", "FDEA49", 1.0f, 0.14f, 0.07f).generate();

		DataBuilder.create().transparentFullBlock("glass").generate();
		DataBuilder.create().transparentFullBlock("framed_glass").generate();

		DataBuilder.create().leaves("oak_leaves").generate();
		DataBuilder.create().leaves("crystal_leaves").generate();

		DataBuilder.create().slab("dirt_slab", "dirt")
			.addTagToState().with(SlabBlock.TYPE, EnumSlabType.TOP).with(SlabBlock.AXIS, EnumAxis.Y).finish(Tags.FLOWER_TOP)
			.addTagToState().with(SlabBlock.TYPE, EnumSlabType.DOUBLE).with(SlabBlock.AXIS, EnumAxis.X).finish(Tags.FLOWER_TOP)
			.addTagToState().with(SlabBlock.TYPE, EnumSlabType.DOUBLE).with(SlabBlock.AXIS, EnumAxis.Y).finish(Tags.FLOWER_TOP)
			.addTagToState().with(SlabBlock.TYPE, EnumSlabType.DOUBLE).with(SlabBlock.AXIS, EnumAxis.Z).finish(Tags.FLOWER_TOP)
			.generate();

		DataBuilder.create().slab("stone_slab", "stone").generate();
		DataBuilder.create().slab("oak_plank_slab", "oak_plank").generate();
		DataBuilder.create().slab("cobblestone_slab", "cobblestone").generate();
		DataBuilder.create().slab("brick_slab", "bricks").generate();

		DataBuilder.create().pillarBlock("oak_log", "oak_log_side", "oak_log")
			.addTagToState().with(PilliarBlock.AXIS, EnumAxis.X).finish(Tags.LOG)
			.addTagToState().with(PilliarBlock.AXIS, EnumAxis.Y).finish(Tags.LOG)
			.addTagToState().with(PilliarBlock.AXIS, EnumAxis.Z).finish(Tags.LOG)
			.generate();
		DataBuilder.create().pillarBlock("crystal_log", "crystal_log_side", "crystal_log")
			.addTagToState().with(PilliarBlock.AXIS, EnumAxis.X).finish(Tags.LOG)
			.addTagToState().with(PilliarBlock.AXIS, EnumAxis.Y).finish(Tags.LOG)
			.addTagToState().with(PilliarBlock.AXIS, EnumAxis.Z).finish(Tags.LOG)
			.generate();

		DataBuilder.create().stala("stone_stala", "stone", "stone").generate();
		DataBuilder.create().stala("granite_stala", "granite", "granite").generate();
		DataBuilder.create().stala("andesite_stala", "andesite", "andesite").generate();
		DataBuilder.create().stala("diorite_stala", "diorite", "diorite").generate();

		DataBuilder.create()
			.fullBlock("corrupted_stone")
			.blockSpecial(new SimpleSpecial("corrupted_stone"))
			.addTag(Tags.CORRUPTED)
			.generate();

		DataBuilder.create()
			.fullBlock("pebble")
			.blockSpecial(new SimpleSpecial("custom"))
			.blockState(StateBuilder.create()
				.singleModel(BlockModelBuilder.create("pebble")
					.addCube(CubeBuilder.create()
						.size(1, 0, 2, 7, 1, 4)
						.face(FaceBuilder.create().texture("stone").autoUv())
					).addCube(CubeBuilder.create()
						.size(10, 0, 4, 5, 1, 7)
						.face(FaceBuilder.create().texture("stone").autoUv())
					).addCube(CubeBuilder.create()
						.size(2, 0, 9, 5, 1, 5)
						.face(FaceBuilder.create().texture("stone").autoUv())
					)
				)
			).generate();

		DataBuilder.create()
			.blockName("tall_grass")
			.itemName("tall_grass")
			.blockToPlace("tall_grass")
			.itemModel(new ItemFromTexture("tall_grass_top"))
			.blockSpecial(new SimpleSpecial("double_block"))
			.blockState(StateBuilder.create()
				.addState(PropertyBuilder.create().addProperty(DoubleBlock.HALF, EnumHalf.BOTTOM).tag(Tags.TRANSPARENT), BlockModelBuilder.create("tall_grass_bottom")
					.addCube(CubeBuilder.create()
						.fullBlock()
						.collisionBox(false)
					).addCube(CubeBuilder.create()
						.collisionBox(false)
						.hitbox(false)
						.min(8, 0, 0)
						.max(8, 16, 16)
						.face(FaceBuilder.create().texture("tall_grass_bottom").biomeTint(true), EnumFace.SOUTH, EnumFace.NORTH)
					).addCube(CubeBuilder.create()
						.collisionBox(false)
						.hitbox(false)
						.min(0, 0, 8)
						.max(16, 16, 8)
						.face(FaceBuilder.create().texture("tall_grass_bottom").biomeTint(true), EnumFace.EAST, EnumFace.WEST)
					)
				)
				.addState(PropertyBuilder.create().addProperty(DoubleBlock.HALF, EnumHalf.TOP).tag(Tags.TRANSPARENT), BlockModelBuilder.create("tall_grass_top")
					.addCube(CubeBuilder.create()
						.fullBlock()
						.collisionBox(false)
					).addCube(CubeBuilder.create()
						.collisionBox(false)
						.hitbox(false)
						.min(8, 0, 0)
						.max(8, 16, 16)
						.face(FaceBuilder.create().texture("tall_grass_top").biomeTint(true), EnumFace.SOUTH, EnumFace.NORTH)
					).addCube(CubeBuilder.create()
						.collisionBox(false)
						.hitbox(false)
						.min(0, 0, 8)
						.max(16, 16, 8)
						.face(FaceBuilder.create().texture("tall_grass_top").biomeTint(true), EnumFace.EAST, EnumFace.WEST)
					)
				)
			).generate();

		DataBuilder.create()
			.blockName("cactus")
			.itemName("cactus")
			.blockToPlace("cactus")
			.itemModel(new ItemFromBlock("cactus"))
			.blockSpecial(new SimpleSpecial("cactus"))
			.blockState(StateBuilder.create().singleModel(BlockModelBuilder.create("cactus")
				.addCube(CubeBuilder.create()
					.min(1, 0, 1)
					.max(15, 16, 15)
					.face(FaceBuilder.create()
						.texture("cactus_top")
						.autoUv(true), EnumFace.UP)
					.face(FaceBuilder.create()
						.texture("cactus_bottom")
						.autoUv(true), EnumFace.DOWN)
				).addCube(CubeBuilder.create()
					.collisionBox(false)
					.hitbox(false)
					.min(0, 0, 1)
					.max(16, 16, 15)
					.face(FaceBuilder.create()
						.texture("cactus_side")
						.autoUv(true), EnumFace.EAST, EnumFace.WEST)
				).addCube(CubeBuilder.create()
					.collisionBox(false)
					.hitbox(false)
					.min(1, 0, 0)
					.max(15, 16, 16)
					.face(FaceBuilder.create()
						.texture("cactus_side")
						.autoUv(true), EnumFace.NORTH, EnumFace.SOUTH)
				)
			).tag(Tags.CACTUS_TOP))
			.generate();

		DataBuilder.create()
			.blockName("snow_layer")
			.itemName("snow_layer")
			.blockToPlace("snow_layer")
			.itemModel(new ItemFromBlock("snow_layer"))
			.blockSpecial(new SimpleSpecial("snow_layer"))
			.blockState(StateBuilder.create()
				.addState(snowState(1), snowLayer(1, "snow"))
				.addState(snowState(2), snowLayer(2, "snow"))
				.addState(snowState(3), snowLayer(3, "snow"))
				.addState(snowState(4), snowLayer(4, "snow"))
				.addState(snowState(5), snowLayer(5, "snow"))
				.addState(snowState(6), snowLayer(6, "snow"))
				.addState(snowState(7), snowLayer(7, "snow"))
				.addState(snowState(8), snowLayer(8, "snow"))
			).generate();

		DataBuilder.create().fullBlock("grass")
			.blockState(StateBuilder.create().singleModel(
				BlockModelBuilder.create("grass")
					.addCube(CubeBuilder.create()
						.fullBlock()
						.face(FaceBuilder.create().texture("dirt"), EnumFace.NORTH, EnumFace.EAST, EnumFace.SOUTH, EnumFace.WEST, EnumFace.DOWN))
					.addCube(CubeBuilder.create()
						.fullBlock()
						.hitbox(false)
						.collisionBox(false)
						.face(FaceBuilder.create()
								.texture("grass_block_side_overlay")
								.biomeTint(true)
								.modelLayer(ModelLayer.OVERLAY_0),
							CubeBuilder.SIDE)
						.face(FaceBuilder.create()
							.texture("grass_block_top")
							.biomeTint(true)
							.modelLayer(ModelLayer.OVERLAY_0), EnumFace.UP
						)
					)
				)
				.tag(Tags.FLOWER_TOP)
			)
			.blockSpecial(SpecialBuilder.create("spreadable").addValue("target", "dirt").addValue("target_set", "grass"))
			.generate();

		DataBuilder.create().fullBlock("green_screen")
			.blockState(
				StateBuilder.create().singleModel(
				BlockModelBuilder.create("green_screen")
					.addCube(CubeBuilder.create()
						.fullBlock()
						.face(FaceBuilder.create()
							.texture("green_screen")
							.modelLayer(ModelLayer.LIGHT)
						)
					)
				)
			).generate();

		DataBuilder.create()
			.itemName("slime_torch")
			.itemModel(new ItemFromTexture("slime_torch"))
			.blockToPlace("slime_torch")
			.blockName("slime_torch")
			.blockSpecial(new SimpleSpecial("state_test"))
			.blockState(StateBuilder.create()
				.addState(PropertyBuilder.create()
						.addProperty(States.LIT, true),
					BlockModelBuilder.create("slime_torch")
						.addCube(CubeBuilder.create()
							.torch()
							.collisionBox(false)
							.face(FaceBuilder.create()
									.texture("slime_torch")
									.modelLayer(ModelLayer.LIGHT)
								,CubeBuilder.SIDE
							)
							.face(FaceBuilder.create()
									.texture("slime_torch")
									.modelLayer(ModelLayer.LIGHT)
									.uv(7,  6, 9, 8)
								, EnumFace.UP
							)
							.face(FaceBuilder.create()
									.texture("slime_torch")
									.modelLayer(ModelLayer.LIGHT)
									.uv(7,  11, 9, 13)
								, EnumFace.DOWN
							)
						)
				).addState(PropertyBuilder.create()
						.addProperty(States.LIT, false),
					BlockModelBuilder.create("slime_torch_off")
						.addCube(CubeBuilder.create()
							.torch()
							.collisionBox(false)
							.face(FaceBuilder.create()
									.texture("slime_torch_off")
								,CubeBuilder.SIDE
							)
							.face(FaceBuilder.create()
									.texture("slime_torch_off")
									.modelLayer(ModelLayer.LIGHT)
									.uv(7,  6, 9, 8)
								, EnumFace.UP
							)
							.face(FaceBuilder.create()
									.texture("slime_torch_off")
									.modelLayer(ModelLayer.LIGHT)
									.uv(7,  11, 9, 13)
								, EnumFace.DOWN
							)
						)
				)
			).generate();

		DataBuilder.create()
			.blockName("smooth_slab")
			.itemName("smooth_slab")
			.blockToPlace("smooth_slab")
			.blockSpecial(new SimpleSpecial("slab"))
			.itemModel(new ItemFromBlock("smooth_slab"))
			.itemSpecial(new SimpleSpecial("slab"))
			.blockState(StateBuilder.create()
				.addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.BOTTOM)
						.addProperty(SlabBlock.AXIS, EnumAxis.Y),
					BlockModelBuilder.create("smooth_slab" + "_bottom")
						.modelPath("slab")
						.addCube(CubeBuilder.create()
							.bottomSlab()
							.face(FaceBuilder.create()
								.texture("smooth_stone")
								.uvlock(true), CubeBuilder.TOP_BOTTOM)
							.face(FaceBuilder.create()
								.texture("smooth_stone_slab")
								.uvlock(true), CubeBuilder.SIDE)
						)
				).addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.TOP)
						.addProperty(SlabBlock.AXIS, EnumAxis.Y),
					BlockModelBuilder.create("smooth_slab" + "_top")
						.modelPath("slab")
						.addCube(CubeBuilder.create()
							.topSlab()
							.face(FaceBuilder.create()
								.texture("smooth_stone")
								.uvlock(true), CubeBuilder.TOP_BOTTOM)
							.face(FaceBuilder.create()
								.texture("smooth_stone_slab")
								.uvlock(true), CubeBuilder.SIDE)
						)
				).addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.DOUBLE)
						.addProperty(SlabBlock.AXIS, EnumAxis.Y),
					BlockModelBuilder.create("smooth_slab" + "_double")
						.modelPath("slab")
						.addCube(CubeBuilder.create()
							.fullBlock()
							.face(FaceBuilder.create()
								.texture("smooth_stone")
								.uvlock(true), CubeBuilder.TOP_BOTTOM)
							.face(FaceBuilder.create()
								.texture("smooth_stone_slab")
								.uvlock(true), CubeBuilder.SIDE)
						)
				).addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.BOTTOM)
						.addProperty(SlabBlock.AXIS, EnumAxis.X),
					BlockModelBuilder.create("smooth_slab" + "_side_x")
						.modelPath("slab")
						.addCube(CubeBuilder.create()
							.min(0, 0, 0)
							.max(8, 16, 16)
							.face(FaceBuilder.create()
								.texture("smooth_stone_slab_side")
								, CubeBuilder.TOP_BOTTOM)
							.face(FaceBuilder.create()
								.texture("smooth_stone")
								, EnumFace.NORTH, EnumFace.SOUTH)
							.face(FaceBuilder.create()
								.texture("smooth_stone_slab_side")
								.uv(0, 8, 16, 0)
								.rotation(90)
								, EnumFace.EAST, EnumFace.WEST)
						)
				).addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.TOP)
						.addProperty(SlabBlock.AXIS, EnumAxis.X).rotation(180),
					BlockModelBuilder.noGen("smooth_slab" + "_side_x").modelPath("slab")
				).addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.BOTTOM)
						.addProperty(SlabBlock.AXIS, EnumAxis.Z),
					BlockModelBuilder.create("smooth_slab" + "_side_z")
						.modelPath("slab")
						.addCube(CubeBuilder.create()
							.min(0, 0, 8)
							.max(16, 16, 16)
							.face(FaceBuilder.create()
								.texture("smooth_stone_slab_side")
								.uv(0, 0, 16, 8)
								.rotation(90)
								, CubeBuilder.TOP_BOTTOM)
							.face(FaceBuilder.create()
								.texture("smooth_stone_slab_side")
								.uv(0, 8, 16, 0)
								.rotation(90)
								, EnumFace.NORTH, EnumFace.SOUTH)
							.face(FaceBuilder.create()
								.texture("smooth_stone")
								, EnumFace.EAST, EnumFace.WEST)
						)
				).addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.TOP)
						.addProperty(SlabBlock.AXIS, EnumAxis.Z).rotation(180),
					BlockModelBuilder.noGen("smooth_slab" + "_side_z").modelPath("slab")
				).addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.DOUBLE)
						.addProperty(SlabBlock.AXIS, EnumAxis.Z)
						.rotation(90),
					BlockModelBuilder.noGen("smooth_slab" + "_double_x").modelPath("slab")
				).addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.DOUBLE)
						.addProperty(SlabBlock.AXIS, EnumAxis.X),
					BlockModelBuilder.create("smooth_slab" + "_double_x")
						.modelPath("slab")
						.addCube(CubeBuilder.create()
							.fullBlock()
							.face(FaceBuilder.create()
								.texture("smooth_stone")
								.uvlock(true), EnumFace.NORTH, EnumFace.SOUTH)
							.face(FaceBuilder.create()
								.texture("smooth_stone_slab")
								, EnumFace.UP, EnumFace.DOWN)
							.face(FaceBuilder.create()
								.texture("smooth_stone_slab")
								.rotation(90)
								.uvlock(true), EnumFace.EAST, EnumFace.WEST)
						)
				)
			).generate();

/*
		DataBuilder.create().fullBlock("bedrock").generate();
		DataBuilder.create().fullBlock("bricks").generate();
		DataBuilder.create().fullBlock("cobblestone").generate();
		DataBuilder.create().fullBlock("green_screen").generate();
		DataBuilder.create().fullBlock("oak_plank").generate();
		DataBuilder.create().fullBlock("iron_block").generate();
		DataBuilder.create().fullBlock("sand").generate();
		DataBuilder.create().fullBlock("stone").generate();

		DataBuilder.create().doubleSlabBlock("dirt", "dirt_slab_top", "dirt_slab_bottom").generate();
		DataBuilder.create().doubleSlabBlock("double_smooth_slab", "smooth_slab_top", "smooth_slab_bottom").generate();

		DataBuilder.create().transparentFullBlock("glass").generate();
		DataBuilder.create().transparentFullBlock("framed_glass").generate();

		decorativeFullLeavesBlock("oak_leaves", 48f / 104f, 67f / 100f, 19f / 104f);
		decorativeFullLeavesBlock("dark_oak_leaves", 48f / 104f, 67f / 100f, 19f / 104f);
		decorativeFullLeavesBlock("birch_leaves", 56f / 112f, 71f / 109f, 37f / 112f);
		decorativeFullLeavesBlock("jungle_leaves", 52f / 112f, 73f / 109f, 21f / 112f);
		decorativeFullLeavesBlock("spruce_leaves", 32f / 85f, 51f / 85f, 32f / 85f);
		decorativeFullLeavesBlock("acacia_leaves", 74f / 158f, 106f / 158f, 29f / 158f);*/
/*
//		decorativeFullSidedBlock("oak_log", "oak_log", "oak_log", "oak_log_side");

//		grassBlock("grass", 0.5686275f, 0.7411765f, 0.34901962f);
//		grassBlock("red_grass", 1f, 0f, 0f);
//		grassBlock("green_grass", 0f, 1f, 0f);
//		grassBlock("blue_grass", 0f, 0f, 1f);

		/* Smooth Slab */
//		sidedSlabFamily("smooth_slab_top", "smooth_slab_bottom", "double_smooth_slab", "smooth_slab", "smooth_stone", "smooth_stone", "smooth_stone_slab");

		/* Old Grass Block Slab */
//		sidedSlabFamily("old_grass_block_slab_top", "old_grass_block_slab_bottom", "old_grass_block", "old_grass_block_slab", "old_grass_block_top", "dirt", "old_grass_block_slab_side", "old_grass_block_side");

		/* Dirt Slab */
//		fullSlabFamily("dirt_slab_top", "dirt_slab_bottom", "dirt", "dirt_slab", "dirt");

		/* Old Full Grass Block Slab */
//		fullSlabFamily("old_full_grass_block_slab_top", "old_full_grass_block_slab_bottom", "old_full_grass_block", "old_full_grass_block_slab", "old_grass_block_top");

		/* Grass Block Slab */
//		topSlabGrassTinted("grass_block_slab_top", 0.5686275f, 0.7411765f, 0.34901962f);
//		bottomSlabGrassTinted("grass_block_slab_bottom", 0.5686275f, 0.7411765f, 0.34901962f);
//		item("grass_block_slab", new ItemFromBlock("grass_block_slab_bottom"), new SlabSpecial("grass_block_slab_top", "grass_block_slab_bottom", "grass"), "grass_block_slab_bottom");


//		fallingFullBlock("sand");
//		fallingFullBlock("gravel");

//		specialItem("speedometer", new SimpleSpecial("speedometer"));
//		specialItem("block_inspector", new SimpleSpecial("block_inspector"));
//		specialItem("wooden_axe", new SimpleSpecial("worldedit"));

//		stairFamily("stone_stair", "stone");

//		tintedOreBlock("coal", 0.3f, 0.3f, 0.3f);

		/*
		 * Dead Blocks
		 */
/*
		DataBuilder.create().fullBlock("blue_grass")
			.blockModel(BlockModelBuilder.create()
				.addCube(CubeBuilder.create()
					.fullBlock()
					.face(FaceBuilder.create().texture("dirt"), CubeBuilder.NO_TOP))
				.addCube(CubeBuilder.create()
					.fullBlock()
					.hitbox(false)
					.collisionBox(false)
					.face(FaceBuilder.create()
							.texture("grass_block_side_overlay")
							.tint(0, 0, 255)
							.modelLayer(ModelLayer.OVERLAY_0),
						CubeBuilder.SIDE)
					.face(FaceBuilder.create()
						.texture("grass_block_top")
						.tint(0, 0, 255)
						.modelLayer(ModelLayer.OVERLAY_0), EnumFace.UP
					)).build()
			).generate();

		DataBuilder.create().fullBlock("red_grass")
			.blockModel(BlockModelBuilder.create()
				.addCube(CubeBuilder.create()
					.fullBlock()
					.face(FaceBuilder.create().texture("dirt"), CubeBuilder.NO_TOP))
				.addCube(CubeBuilder.create()
					.fullBlock()
					.hitbox(false)
					.collisionBox(false)
					.face(FaceBuilder.create()
							.texture("grass_block_side_overlay")
							.tint(255, 0, 0)
							.modelLayer(ModelLayer.OVERLAY_0),
						CubeBuilder.SIDE)
					.face(FaceBuilder.create()
						.texture("grass_block_top")
						.tint(255, 0, 0)
						.modelLayer(ModelLayer.OVERLAY_0), EnumFace.UP
					)).build()
			).generate();

		DataBuilder.create().fullBlock("green_grass")
			.blockModel(BlockModelBuilder.create()
				.addCube(CubeBuilder.create()
					.fullBlock()
					.face(FaceBuilder.create().texture("dirt"), CubeBuilder.NO_TOP))
				.addCube(CubeBuilder.create()
					.fullBlock()
					.hitbox(false)
					.collisionBox(false)
					.face(FaceBuilder.create()
							.texture("grass_block_side_overlay")
							.tint(0, 254, 0)
							.modelLayer(ModelLayer.OVERLAY_0),
						CubeBuilder.SIDE)
					.face(FaceBuilder.create()
						.texture("grass_block_top")
						.tint(0, 255, 0)
						.modelLayer(ModelLayer.OVERLAY_0), EnumFace.UP
					)).build()
			).generate();*/

	}

	private static PropertyBuilder snowState(int height)
	{
		return PropertyBuilder.create().addProperty(States.SNOW_LAYERS, height);
	}

	private static BlockModelBuilder snowLayer(int height, String texture)
	{
		return BlockModelBuilder.create("snow_layer_" + height)
			.modelPath("snow_layer")
			.addCube(CubeBuilder.create()
				.min(0, 0, 0)
				.max(16, height * 2, 16)
				.face(FaceBuilder.create()
					.texture(texture)
					.autoUv(true))
			);
	}

}
