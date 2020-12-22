package steve6472.polyground.generator;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Tags;
import steve6472.polyground.block.properties.enums.*;
import steve6472.polyground.block.special.*;
import steve6472.polyground.block.states.States;
import steve6472.polyground.generator.biome.BiomeBuilder;
import steve6472.polyground.generator.biome.feature.BlockStateBuilder;
import steve6472.polyground.generator.biome.feature.FeatureBuilder;
import steve6472.polyground.generator.models.BlockModelBuilder;
import steve6472.polyground.generator.models.CubeBuilder;
import steve6472.polyground.generator.models.FaceBuilder;
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
		new DataGenerator().generateBlocks();
		new DataGenerator().generateFeatures();
		new DataGenerator().generateBiomes();
		new DataGenerator().generateDebug();
		System.out.println("\n\n\n/*\n * Generating Items\n */\n\n");
		new DataGenerator().generateItems();
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

		BiFunction<Boolean, Integer, PropertyBuilder> leavesProperty = (b, i) -> PropertyBuilder
			.create()
			.addProperty(LeavesBlock.PERSISTENT, b)
			.addProperty(LeavesBlock.DISTANCE, i);
		BiFunction<Boolean, Integer, BlockModelBuilder> leavesModel = (b, i) -> BlockModelBuilder
			.create("leaves" + (b ? "_persistent" : "") + "_" + i)
			.addCube(CubeBuilder
				.create()
				.fullBlock()
				.face(FaceBuilder.create().texture("block/number/" + i).biomeTint(b)));

		BlockBuilder
			.create()
			.fullBlock("visual_leaves")
			.blockSpecial(new SimpleSpecial("leaves"))
			.blockState(StateBuilder
				.create()
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
				.addState(leavesProperty.apply(true, 7), leavesModel.apply(true, 7)))
			.generate();

		//		DataBuilder.create().fullBlock("model_test").generate();

		//		DataBuilder.create().torch("slime_torch", true).blockSpecial(new SimpleSpecial("state_test")).itemModel(new ItemFromTexture("slime_torch")).generate();
	}

	private SpecialBuilder tagBelowSpecial(boolean and, String... tags)
	{
		return SpecialBuilder.create("tag_below").addValue("operation", and ? "and" : "or").addValue("tags", tags);
	}

	public void generateItems()
	{
		String[] powders = {"red", "green", "blue", "cyan", "magenta", "yellow", "black", "white"};

		ItemBuilder.itemModelPath("flint_axe", "axe", "full_axe", "items");
		ItemBuilder.itemModel("chisel_tool", "chisel", "items");
		ItemBuilder.itemModel("hemp_string", "hemp", "items");
		ItemBuilder.item("flint_axe_head", "items");
		ItemBuilder.item("hammerstone_limestone", "items");
		ItemBuilder.item("hammerstone_sandstone", "items");
		ItemBuilder.item("hammerstone_quartzite", "items");
		ItemBuilder.item("pebble", "items");
		ItemBuilder.item("stick", "items");

		// Logic
		ItemBuilder.itemModelPath("high_constant", "logic", "high_constant", "items.logic");
		ItemBuilder.itemModelPath("low_constant", "logic", "low_constant", "items.logic");
		ItemBuilder.itemModelPath("and", "logic", "and", "items.logic.gates");
		ItemBuilder.itemModelPath("nand", "logic", "nand", "items.logic.gates");
		ItemBuilder.itemModelPath("not", "logic", "not", "items.logic.gates");
		ItemBuilder.itemModelPath("or", "logic", "or", "items.logic.gates");
		ItemBuilder.itemModelPath("nor", "logic", "nor", "items.logic.gates");
		ItemBuilder.itemModelPath("xor", "logic", "xor", "items.logic.gates");
		ItemBuilder.itemModelPath("wire", "logic", "wire", "items.logic");
		ItemBuilder.itemModelPath("magenta_wire", "logic", "magenta_wire", "items.logic");
		ItemBuilder.itemModelPath("light", "logic", "light", "items.logic");
		ItemBuilder.itemModelPath("input", "logic", "input", "items.logic");
		ItemBuilder.itemModelPath("output", "logic", "output", "items.logic");
		ItemBuilder.itemModelPath("switch", "logic", "switch", "items.logic");
		ItemBuilder.itemModelPath("cross", "logic", "cross", "items.logic");
		ItemBuilder.itemModelPath("connector_to_up", "logic", "connector_to_up", "items.logic");
		ItemBuilder.itemModelPath("connector_to_down", "logic", "connector_to_down", "items.logic");
		ItemBuilder.itemModelPath("board_level", "logic", "board_level", "items.logic");
		ItemBuilder.itemModelPath("fill", "logic", "fill", "items.logic");
		ItemBuilder.itemModelPath("seven_segment_display", "logic", "seven_segment_display", "items.logic");

		ItemBuilder
			.create()
			.name("brush")
			.model("brush")
			.groupPath("items")
			.special(new SimpleSpecial("brush"))
			.generate();

		ItemBuilder
			.create()
			.name("chip")
			.model("chip")
			.modelPath("logic")
			.special(new SimpleSpecial("chip"))
			.generate();

		for (String p : powders)
		{
			ItemBuilder.itemModelPath(p + "_powder", "powders", p + "_powder", "items.powders");
		}
/*
		item("flint_axe_head", "items");
		item("flint_axe", "axe/full_axe", "items");
		item("hemp_string", "hemp", "items");
		item("hammerstone_limestone", "items");
		item("hammerstone_sandstone", "items");
		item("hammerstone_quartzite", "items");
		item("red_powder", "powders/red_powder", "items.powders");
		item("green_powder", "powders/green_powder", "items.powders");
		item("blue_powder", "powders/blue_powder", "items.powders");
		item("cyan_powder", "powders/cyan_powder", "items.powders");
		item("magenta_powder", "powders/magenta_powder", "items.powders");
		item("yellow_powder", "powders/yellow_powder", "items.powders");
		item("black_powder", "powders/black_powder", "items.powders");
		item("white_powder", "powders/white_powder", "items.powders");
		item("pebble", "items");
		item("stick_item", "stick", "items");*/
	}

	public void generateBlocks()
	{
		createFolders();
		//		decorativeFullTintedBlock("full_grass_block", 0.5686275f, 0.7411765f, 0.34901962f, "grass_block_top");

		// Items
		/*BlockBuilder
			.create()
			.itemModel(new ItemFromTexture("block_inspector"))
			.itemName("block_inspector")
			.itemSpecial(new SimpleSpecial("block_inspector"))
			.generate();
		BlockBuilder
			.create()
			.itemModel(new ItemFromTexture("rift_placer"))
			.itemName("rift_placer")
			.itemSpecial(new SimpleSpecial("riftplacer"))
			.generate();
		BlockBuilder
			.create()
			.itemModel(new ItemFromTexture("tele_placer"))
			.itemName("tele_placer")
			.itemSpecial(new SimpleSpecial("tele_placer"))
			.generate();
		BlockBuilder
			.create()
			.itemModel(new ItemFromTexture("speedometer"))
			.itemName("speedometer")
			.itemSpecial(new SimpleSpecial("speedometer"))
			.generate();*/

		// Blocks
		BlockBuilder.create().fullBlock("null").addTags(Tags.SOLID, Tags.ERROR).generate();
		BlockBuilder
			.create()
			.fullBlock("smooth_stone")
			.addTags(Tags.SOLID)
			.placerGroupPath("building.stone")
			.generate();
		BlockBuilder.create().fullBlock("bricks").addTags(Tags.SOLID).placerGroupPath("building.bricks").generate();
		BlockBuilder.create().fullBlock("iron_block").addTags(Tags.SOLID).placerGroupPath("building.metal").generate();
		BlockBuilder.create().fullBlock("gold_block").addTags(Tags.SOLID).placerGroupPath("building.metal").generate();
		BlockBuilder
			.create()
			.fullBlock("cobblestone")
			.addTags(Tags.SOLID)
			.placerGroupPath("building.stone.cobblestone")
			.generate();
		BlockBuilder.create().fullBlock("bedrock").addTags(Tags.SOLID).placerGroupPath("building.stone").generate();

		BlockBuilder
			.create()
			.fullBlock("oak_planks", "wood/planks/oak_planks")
			.placerGroupPath("building.wood.oak")
			.addTags(Tags.SOLID, Tags.PICKABLE)
			.generate();
		BlockBuilder
			.create()
			.fullBlock("spruce_planks", "wood/planks/spruce_planks")
			.placerGroupPath("building.wood.spruce")
			.addTags(Tags.SOLID, Tags.PICKABLE)
			.generate();
		BlockBuilder
			.create()
			.fullBlock("birch_planks", "wood/planks/birch_planks")
			.placerGroupPath("building.wood.birch")
			.addTags(Tags.SOLID, Tags.PICKABLE)
			.generate();
		BlockBuilder
			.create()
			.fullBlock("acacia_planks", "wood/planks/acacia_planks")
			.placerGroupPath("building.wood.acacia")
			.addTags(Tags.SOLID, Tags.PICKABLE)
			.generate();
		BlockBuilder
			.create()
			.fullBlock("dark_oak_planks", "wood/planks/dark_oak_planks")
			.placerGroupPath("building.wood.dark_oak")
			.addTags(Tags.SOLID, Tags.PICKABLE)
			.generate();
		BlockBuilder
			.create()
			.fullBlock("jungle_planks", "wood/planks/jungle_planks")
			.placerGroupPath("building.wood.jungle")
			.addTags(Tags.SOLID, Tags.PICKABLE)
			.generate();

		BlockBuilder
			.create()
			.fullBlock("stone")
			.addTags(Tags.SOLID)
			.placerGroupPath("building.stone.stone")
			.placerGroupPath("nature.hills")
			.generate();
		BlockBuilder
			.create()
			.fullBlock("underground_sandstone", "sandstone_bottom")
			.addTags(Tags.SOLID)
			.placerGroupPath("building.stone")
			.generate();
		BlockBuilder.create().fullBlock("limestone").addTags(Tags.SOLID).placerGroupPath("building.stone").generate();
		BlockBuilder.create().fullBlock("quartzite").addTags(Tags.SOLID).placerGroupPath("building.stone").generate();
		BlockBuilder.create().fullBlock("andesite").addTags(Tags.SOLID).placerGroupPath("building.stone").generate();
		BlockBuilder.create().fullBlock("diorite").addTags(Tags.SOLID).placerGroupPath("building.stone").generate();
		BlockBuilder.create().fullBlock("granite").addTags(Tags.SOLID).placerGroupPath("building.stone").generate();
		BlockBuilder.create().fullBlock("dirt").addTags(Tags.FLOWER_TOP, Tags.SOLID).generate();
		BlockBuilder.create().fullBlock("coarse_dirt").addTags(Tags.FLOWER_TOP, Tags.SOLID).generate();

		BlockBuilder
			.create()
			.fullBlock("sand")
			.blockSpecial(new SimpleSpecial("falling_block"))
			.addTags(Tags.CACTUS_TOP, Tags.SHRUBS_TOP, Tags.SOLID)
			.placerGroupPath("nature.desert")
			.generate();
		BlockBuilder
			.create()
			.fullBlock("gravel")
			.blockSpecial(new SimpleSpecial("falling_block"))
			.addTags(Tags.SOLID)
			.placerGroupPath("nature.hills")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("snow_block", "snow")
			.placerGroupPath("nature.taiga")
			.addTags(Tags.SOLID)
			.placerGroupPath("nature.hills")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("sandstone", "sandstone_top", "sandstone", "sandstone_bottom")
			.addTags(Tags.SOLID)
			.generate();

		//		DataBuilder.create().fullBlock("cond_test").blockState(StateBuilder.create().singleModel(new CondTest())).blockModel().generate();

		BlockBuilder.create().stairs("stone_stairs", "stone").placerGroupPath("building.stone.stone").generate();
		BlockBuilder
			.create()
			.stairs("cobblestone_stairs", "cobblestone")
			.placerGroupPath("building.stone.cobblestone")
			.generate();
		BlockBuilder.create().stairs("brick_stairs", "bricks").placerGroupPath("building.bricks").generate();

		BlockBuilder
			.create()
			.stairs("oak_stairs", "wood/planks/oak_planks")
			.placerGroupPath("building.wood.oak")
			.generate();
		BlockBuilder
			.create()
			.stairs("spruce_stairs", "wood/planks/spruce_planks")
			.placerGroupPath("building.wood.spruce")
			.generate();
		BlockBuilder
			.create()
			.stairs("birch_stairs", "wood/planks/birch_planks")
			.placerGroupPath("building.wood.birch")
			.generate();
		BlockBuilder
			.create()
			.stairs("acacia_stairs", "wood/planks/acacia_planks")
			.placerGroupPath("building.wood.acacia")
			.generate();
		BlockBuilder
			.create()
			.stairs("dark_oak_stairs", "wood/planks/dark_oak_planks")
			.placerGroupPath("building.wood.dark_oak")
			.generate();
		BlockBuilder
			.create()
			.stairs("jungle_stairs", "wood/planks/jungle_planks")
			.placerGroupPath("building.wood.jungle")
			.generate();

		//		DataBuilder.create().plusBlock("small_grass", true).blockSpecial(tagSpecial(true, Tags.FLOWER_TOP)).addTag(Tags.TRANSPARENT).generate();
		BlockBuilder
			.create()
			.plusBlock("shrub", true)
			.blockSpecial(tagBelowSpecial(true, Tags.SHRUBS_TOP))
			.addTag(Tags.TRANSPARENT)
			.placerGroupPath("nature.desert")
			.generate();

		BlockBuilder
			.create()
			.oreBlock("coal_ore", "stone", "ore_overlay", 77, 77, 77)
			.addTags(Tags.SOLID)
			.placerGroupPath("nature.cave")
			.generate();

		BlockBuilder
			.create()
			.lightOreBlock("magical_coal_ore", "stone", "ore_overlay", 26, 204, 204, "6c7f7a", 1.0f, 0.35f, 0.44f)
			.addTags(Tags.SOLID)
			.placerGroupPath("nature.cave")
			.generate();

		BlockBuilder
			.create()
			.fullLightBlock("blaze_block", "FDEA49", 1.0f, 0.14f, 0.07f)
			.addTags(Tags.SOLID)
			.generate();

		BlockBuilder.create().transparentFullBlock("glass").generate();
		BlockBuilder.create().transparentFullBlock("framed_glass").generate();

		BlockBuilder
			.create()
			.leaves("oak_leaves", "leaves/oak_leaves")
			.placerGroupPath("nature.forest")
			.placerGroupPath("nature.leaves")
			.generate();
		BlockBuilder
			.create()
			.leaves("spruce_leaves", "leaves/spruce_leaves")
			.placerGroupPath("nature.leaves")
			.generate();
		BlockBuilder.create().leaves("birch_leaves", "leaves/birch_leaves").placerGroupPath("nature.leaves").generate();
		BlockBuilder
			.create()
			.leaves("acacia_leaves", "leaves/acacia_leaves")
			.placerGroupPath("nature.leaves")
			.generate();
		BlockBuilder
			.create()
			.leaves("dark_oak_leaves", "leaves/dark_oak_leaves")
			.placerGroupPath("nature.leaves")
			.generate();
		BlockBuilder
			.create()
			.leaves("jungle_leaves", "leaves/jungle_leaves")
			.placerGroupPath("nature.leaves")
			.generate();
		BlockBuilder
			.create()
			.leaves("crystal_leaves", "leaves/crystal_leaves")
			.placerGroupPath("nature.cave")
			.generate();

		BlockBuilder
			.create()
			.slab("dirt_slab", "dirt")
			.addTagToState()
			.with(SlabBlock.TYPE, EnumSlabType.TOP)
			.with(SlabBlock.AXIS, EnumAxis.Y)
			.finish(Tags.FLOWER_TOP)
			.addTagToState()
			.with(SlabBlock.TYPE, EnumSlabType.DOUBLE)
			.finish(Tags.FLOWER_TOP, Tags.SOLID)
			.generate();
		BlockBuilder
			.create()
			.slab("stone_slab", "stone")
			.addTagToState()
			.with(SlabBlock.TYPE, EnumSlabType.DOUBLE)
			.finish(Tags.SOLID)
			.placerGroupPath("building.stone.stone")
			.generate();
		BlockBuilder
			.create()
			.slab("underground_sandstone_slab", "sandstone_bottom")
			.addTagToState()
			.with(SlabBlock.TYPE, EnumSlabType.DOUBLE)
			.finish(Tags.SOLID)
			.generate();
		BlockBuilder
			.create()
			.slab("limestone_slab", "limestone")
			.addTagToState()
			.with(SlabBlock.TYPE, EnumSlabType.DOUBLE)
			.finish(Tags.SOLID)
			.generate();
		BlockBuilder
			.create()
			.slab("quartzite_slab", "quartzite")
			.addTagToState()
			.with(SlabBlock.TYPE, EnumSlabType.DOUBLE)
			.finish(Tags.SOLID)
			.generate();
		BlockBuilder
			.create()
			.slab("cobblestone_slab", "cobblestone")
			.addTagToState()
			.with(SlabBlock.TYPE, EnumSlabType.DOUBLE)
			.finish(Tags.SOLID)
			.placerGroupPath("building.stone.cobblestone")
			.generate();
		BlockBuilder
			.create()
			.slab("brick_slab", "bricks")
			.addTagToState()
			.with(SlabBlock.TYPE, EnumSlabType.DOUBLE)
			.finish(Tags.SOLID)
			.placerGroupPath("building.bricks")
			.generate();

		BlockBuilder
			.create()
			.slab("oak_slab", "wood/planks/oak_planks")
			.placerGroupPath("building.wood.oak")
			.addTagToState()
			.with(SlabBlock.TYPE, EnumSlabType.DOUBLE)
			.finish(Tags.SOLID)
			.generate();
		BlockBuilder
			.create()
			.slab("spruce_slab", "wood/planks/spruce_planks")
			.placerGroupPath("building.wood.spruce")
			.addTagToState()
			.with(SlabBlock.TYPE, EnumSlabType.DOUBLE)
			.finish(Tags.SOLID)
			.generate();
		BlockBuilder
			.create()
			.slab("birch_slab", "wood/planks/birch_planks")
			.placerGroupPath("building.wood.birch")
			.addTagToState()
			.with(SlabBlock.TYPE, EnumSlabType.DOUBLE)
			.finish(Tags.SOLID)
			.generate();
		BlockBuilder
			.create()
			.slab("acacia_slab", "wood/planks/acacia_planks")
			.placerGroupPath("building.wood.acacia")
			.addTagToState()
			.with(SlabBlock.TYPE, EnumSlabType.DOUBLE)
			.finish(Tags.SOLID)
			.generate();
		BlockBuilder
			.create()
			.slab("dark_oak_slab", "wood/planks/dark_oak_planks")
			.placerGroupPath("building.wood.dark_oak")
			.addTagToState()
			.with(SlabBlock.TYPE, EnumSlabType.DOUBLE)
			.finish(Tags.SOLID)
			.generate();
		BlockBuilder
			.create()
			.slab("jungle_slab", "wood/planks/jungle_planks")
			.placerGroupPath("building.wood.jungle")
			.addTagToState()
			.with(SlabBlock.TYPE, EnumSlabType.DOUBLE)
			.finish(Tags.SOLID)
			.generate();

		BlockBuilder
			.create()
			.pillarBlock("oak_log", "wood/log/oak_log", "wood/log/oak_log_top")
			.placerGroupPath("building.wood.oak")
			.addTagToState()
			.finish(Tags.LOG, Tags.SOLID, Tags.PICKABLE)
			.generate();
		BlockBuilder
			.create()
			.pillarBlock("spruce_log", "wood/log/spruce_log", "wood/log/spruce_log_top")
			.placerGroupPath("building.wood.spruce")
			.addTagToState()
			.finish(Tags.LOG, Tags.SOLID, Tags.PICKABLE)
			.generate();
		BlockBuilder
			.create()
			.pillarBlock("birch_log", "wood/log/birch_log", "wood/log/birch_log_top")
			.placerGroupPath("building.wood.birch")
			.addTagToState()
			.finish(Tags.LOG, Tags.SOLID, Tags.PICKABLE)
			.generate();
		BlockBuilder
			.create()
			.pillarBlock("acacia_log", "wood/log/acacia_log", "wood/log/acacia_log_top")
			.placerGroupPath("building.wood.acacia")
			.addTagToState()
			.finish(Tags.LOG, Tags.SOLID, Tags.PICKABLE)
			.generate();
		BlockBuilder
			.create()
			.pillarBlock("dark_oak_log", "wood/log/dark_oak_log", "wood/log/dark_oak_log_top")
			.placerGroupPath("building.wood.dark_oak")
			.addTagToState()
			.finish(Tags.LOG, Tags.SOLID, Tags.PICKABLE)
			.generate();
		BlockBuilder
			.create()
			.pillarBlock("jungle_log", "wood/log/jungle_log", "wood/log/jungle_log_top")
			.placerGroupPath("building.wood.jungle")
			.addTagToState()
			.finish(Tags.LOG, Tags.SOLID, Tags.PICKABLE)
			.generate();

		BlockBuilder
			.create()
			.pillarBlock("crystal_log", "crystal_log_side", "crystal_log")
			.addTagToState()
			.finish(Tags.LOG, Tags.SOLID)
			.placerGroupPath("nature.cave")
			.generate();

		BlockBuilder.create().stala("stone_stala", "stone", "stone").placerGroupPath("building.stone.stone").generate();
		BlockBuilder.create().stala("granite_stala", "granite", "granite").placerGroupPath("building.stone").generate();
		BlockBuilder
			.create()
			.stala("andesite_stala", "andesite", "andesite")
			.placerGroupPath("building.stone")
			.generate();
		BlockBuilder.create().stala("diorite_stala", "diorite", "diorite").placerGroupPath("building.stone").generate();

		BlockBuilder
			.create()
			.fullBlock("dir_light", "blaze_block")
			.blockSpecial(SpecialBuilder
				.create("dir_light")
				.addValue("color", "e6e6e6")
				.addValue("constant", 1)
				.addValue("linear", 0.14f)
				.addValue("quadratic", 0.07f)
				.addValue("cutOff", 30))
			.addTags(Tags.SOLID)
			.generate();

		BlockBuilder
			.create()
			.fullBlock("flint_knapping")
			.blockSpecial(new SimpleSpecial("flint_knapping"))
			.blockState(StateBuilder.create().emptyModel())
			.placerGroupPath("misc")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("root")
			.blockSpecial(new SimpleSpecial("root"))
			.blockState(StateBuilder
				.create()
				.singleModel(BlockModelBuilder
					.create("root")
					.addCube(CubeBuilder
						.create()
						.fullBlock()
						.face(FaceBuilder.create().texture("block/root_side"), CubeBuilder.SIDE)
						.face(FaceBuilder.create().texture("block/dirt"), EnumFace.DOWN)
						.face(FaceBuilder
							.create()
							.texture("block/grass_block_top")
							.biomeTint(true)
							.modelLayer(ModelLayer.OVERLAY_0), EnumFace.UP))))
			.placerGroupPath("nature")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("chisel")
			.blockSpecial(new SimpleSpecial("chisel"))
			.blockState(StateBuilder.create().emptyModel())
			.placerGroupPath("misc")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("statue_grass")
			.blockSpecial(SpecialBuilder.create("vox_statue").addValue("path", "statues/grass").addValue("palette", "rainbow"))
			.blockState(StateBuilder.create().emptyModel())
			.placerGroupPath("building.statues")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("statue_megumin")
			.blockSpecial(SpecialBuilder.create("vox_statue").addValue("path", "statues/statue").addValue("palette", "meg"))
			.blockState(StateBuilder.create().emptyModel())
			.placerGroupPath("building.statues")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("statue_treasure")
			.blockSpecial(SpecialBuilder.create("vox_statue").addValue("path", "statues/treasure").addValue("palette", "treasure"))
			.blockState(StateBuilder.create().emptyModel())
			.placerGroupPath("building.statues")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("chip_designer")
			.blockSpecial(new SimpleSpecial("chip_designer"))
			.blockState(StateBuilder.create().emptyModel())
			.placerGroupPath("misc")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("logic")
			.blockSpecial(new SimpleSpecial("logic"))
			.blockState(StateBuilder.create().emptyModel())
			.placerGroupPath("misc")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("advanced_logic")
			.blockSpecial(new SimpleSpecial("advanced_logic"))
			.blockState(StateBuilder.create().emptyModel())
			.placerGroupPath("misc")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("pro_logic")
			.blockSpecial(new SimpleSpecial("pro_logic"))
			.blockState(StateBuilder.create().emptyModel())
			.placerGroupPath("misc")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("small_grass")
			.blockSpecial(tagBelowSpecial(true, Tags.FLOWER_TOP))
			.addTag(Tags.TRANSPARENT)
			.blockState(StateBuilder
				.create()
				.singleModel(BlockModelBuilder.create("small_grass").externalPath("custom_models/small_grass.bbmodel")))
			.placerGroupPath("nature")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("corrupted_stone")
			.blockSpecial(new SimpleSpecial("corrupted_stone"))
			.addTags(Tags.CORRUPTED, Tags.SOLID)
			.placerGroupPath("nature.cave")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("amethine")
			.blockSpecial(new SimpleSpecial("custom"))
			.blockState(StateBuilder
				.create()
				.singleModel(BlockModelBuilder.create("amethine").externalPath("custom_models/amethine.bbmodel"))
				.custom(true))
			.placerGroupPath("nature.cave")
			.addTags(Tags.PICKABLE)
			.generate();

		BlockBuilder
			.create()
			.fullBlock("companion_cube")
			.blockState(StateBuilder
				.create()
				.singleModel(BlockModelBuilder
					.create("companion_cube")
					.externalPath("custom_models/companion_cube.bbmodel")))
			.blockSpecial(new SimpleSpecial("custom"))
			.addTags(Tags.PICKABLE)
			.placerGroupPath("misc")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("mining_tool")
			.blockState(StateBuilder
				.create()
				.singleModel(BlockModelBuilder.create("mining_tool").externalPath("custom_models/mining_tool.bbmodel")))
			.blockSpecial(new SimpleSpecial("custom"))
			.placerGroupPath("misc")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("block_inspector")
			.blockState(StateBuilder
				.create()
				.singleModel(BlockModelBuilder
					.create("block_inspector")
					.externalPath("custom_models/block_inspector.bbmodel")))
			.blockSpecial(new SimpleSpecial("custom"))
			.placerGroupPath("misc")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("small_palette")
			.blockState(StateBuilder
				.create()
				.singleModel(BlockModelBuilder
					.create("small_palette")
					.externalPath("custom_models/small_palette.bbmodel")))
			.blockSpecial(new SimpleSpecial("custom"))
			.addTags(Tags.PICKABLE)
			.placerGroupPath("misc")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("wooden_box")
			.blockState(StateBuilder
				.create()
				.singleModel(BlockModelBuilder.create("wooden_box").externalPath("custom_models/wooden_box.bbmodel")))
			.blockSpecial(new SimpleSpecial("custom"))
			.addTags(Tags.SOLID, Tags.PICKABLE)
			.placerGroupPath("misc")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("icicle")
			.blockState(StateBuilder
				.create()
				.singleModel(BlockModelBuilder.create("icicle").externalPath("custom_models/icicle.bbmodel")))
			.blockSpecial(new SimpleSpecial("custom"))
			.placerGroupPath("misc")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("dirt_path")
			.blockSpecial(new SimpleSpecial("custom"))
			.blockState(StateBuilder
				.create()
				.singleModel(BlockModelBuilder
					.create("dirt_path")
					.addCube(CubeBuilder
						.create()
						.max(16, 15, 16)
						.face(FaceBuilder.create().texture("block/dirt_path_top"), EnumFace.UP)
						.face(FaceBuilder.create().texture("block/dirt"), EnumFace.DOWN)
						.face(FaceBuilder.create().texture("block/dirt_path_side"), CubeBuilder.SIDE))))
			.placerGroupPath("nature")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("liq_extractor")
			.blockState(StateBuilder
				.create()
				.addState(PropertyBuilder
					.create()
					.addProperty(LiqExtractorBlock.FACING, EnumFace.NORTH)
					.tags(Tags.PICKABLE, Tags.LIQ_CONNECT), BlockModelBuilder
					.create("liq_extractor")
					.externalPath("custom_models/liq_extractor.bbmodel"))
				.addState(PropertyBuilder
					.create()
					.addProperty(LiqExtractorBlock.FACING, EnumFace.EAST)
					.tags(Tags.PICKABLE, Tags.LIQ_CONNECT)
					.rot(0, -90, 0), BlockModelBuilder.noGen("liq_extractor"))
				.addState(PropertyBuilder
					.create()
					.addProperty(LiqExtractorBlock.FACING, EnumFace.SOUTH)
					.tags(Tags.PICKABLE, Tags.LIQ_CONNECT)
					.rot(0, -180, 0), BlockModelBuilder.noGen("liq_extractor"))
				.addState(PropertyBuilder
					.create()
					.addProperty(LiqExtractorBlock.FACING, EnumFace.WEST)
					.tags(Tags.PICKABLE, Tags.LIQ_CONNECT)
					.rot(0, -270, 0), BlockModelBuilder.noGen("liq_extractor")))
			.blockSpecial(new SimpleSpecial("liq_extractor"))
			.placerGroupPath("misc")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("paint_bucket")
			.blockState(StateBuilder
				.create()
				.singleModel(BlockModelBuilder
					.create("paint_bucket")
					.externalPath("custom_models/paint_bucket.bbmodel")))
			.blockSpecial(new SimpleSpecial("paint_bucket"))
			.placerGroupPath("misc")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("axe_assembly")
			.blockSpecial(new SimpleSpecial("axe_block"))
			.blockState(StateBuilder
				.create()
				.singleModel((BlockModelBuilder
					.create("axe_template")
					.externalPath("custom_models/axe_template.bbmodel"))))
			.placerGroupPath("misc")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("branch")
			.blockSpecial(new SimpleSpecial("branch"))
			.blockState(StateBuilder.create().emptyModel())
			.placerGroupPath("building.wood")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("amethine_core")
			.blockSpecial(new SimpleSpecial("amethine_core"))
			.blockState(StateBuilder.create().emptyModel())
			.placerGroupPath("misc")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("pipe")
			.blockSpecial(new SimpleSpecial("pipe"))
			.blockState(StateBuilder.create().emptyModel())
			.placerGroupPath("misc")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("sapling")
			.blockSpecial(new SimpleSpecial("sapling"))
			.blockState(StateBuilder
				.create()
				.singleModel(BlockModelBuilder
					.create("sapling")
					.addCube(CubeBuilder.create().min(4, 0, 4).max(12, 12, 12))))
			.placerGroupPath("nature")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("flower")
			.blockSpecial(new SimpleSpecial("flower"))
			.blockState(StateBuilder
				.create()
				.addState(PropertyBuilder
					.create()
					.addProperty(FlowerBlock.FLOWER_COLOR, EnumFlowerColor.RED)
					.tags(Tags.PICKABLE), BlockModelBuilder
					.create("red_flower")
					.externalPath("custom_models/flowers/red_flower.bbmodel"))
				.addState(PropertyBuilder
					.create()
					.addProperty(FlowerBlock.FLOWER_COLOR, EnumFlowerColor.GREEN)
					.tags(Tags.PICKABLE), BlockModelBuilder
					.create("green_flower")
					.externalPath("custom_models/flowers/green_flower.bbmodel"))
				.addState(PropertyBuilder
					.create()
					.addProperty(FlowerBlock.FLOWER_COLOR, EnumFlowerColor.BLUE)
					.tags(Tags.PICKABLE), BlockModelBuilder
					.create("blue_flower")
					.externalPath("custom_models/flowers/blue_flower.bbmodel"))
				.addState(PropertyBuilder
					.create()
					.addProperty(FlowerBlock.FLOWER_COLOR, EnumFlowerColor.CYAN)
					.tags(Tags.PICKABLE), BlockModelBuilder
					.create("cyan_flower")
					.externalPath("custom_models/flowers/cyan_flower.bbmodel"))
				.addState(PropertyBuilder
					.create()
					.addProperty(FlowerBlock.FLOWER_COLOR, EnumFlowerColor.MAGENTA)
					.tags(Tags.PICKABLE), BlockModelBuilder
					.create("magenta_flower")
					.externalPath("custom_models/flowers/magenta_flower.bbmodel"))
				.addState(PropertyBuilder
					.create()
					.addProperty(FlowerBlock.FLOWER_COLOR, EnumFlowerColor.YELLOW)
					.tags(Tags.PICKABLE), BlockModelBuilder
					.create("yellow_flower")
					.externalPath("custom_models/flowers/yellow_flower.bbmodel"))
				.addState(PropertyBuilder
					.create()
					.addProperty(FlowerBlock.FLOWER_COLOR, EnumFlowerColor.WHITE)
					.tags(Tags.PICKABLE), BlockModelBuilder
					.create("white_flower")
					.externalPath("custom_models/flowers/white_flower.bbmodel"))
				.addState(PropertyBuilder
					.create()
					.addProperty(FlowerBlock.FLOWER_COLOR, EnumFlowerColor.BLACK)
					.tags(Tags.PICKABLE), BlockModelBuilder
					.create("black_flower")
					.externalPath("custom_models/flowers/black_flower.bbmodel")))
			.placerGroupPath("nature")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("hemp")
			.blockSpecial(new SimpleSpecial("hemp"))
			.blockState(StateBuilder
				.create()
				.addState(PropertyBuilder.create().addProperty(HempBlock.STAGE, 3), BlockModelBuilder
					.create("hemp_3")
					.externalPath("custom_models/hemp_full.bbmodel"))
				.addState(PropertyBuilder.create().addProperty(HempBlock.STAGE, 2), BlockModelBuilder
					.create("hemp_2")
					.externalPath("custom_models/hemp_half.bbmodel"))
				.addState(PropertyBuilder.create().addProperty(HempBlock.STAGE, 1), BlockModelBuilder
					.create("hemp_1")
					.externalPath("custom_models/hemp_empty.bbmodel")))
			.placerGroupPath("nature")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("flint")
			.blockState(StateBuilder
				.create()
				.addState(PropertyBuilder
					.create()
					.addProperty(FourDirectionalBlock.FACING, EnumFace.NORTH)
					.tags(Tags.PICKABLE, Tags.TRANSPARENT)
					.custom(true), BlockModelBuilder.create("flint_1").externalPath("custom_models/flint_1.bbmodel"))

				.addState(PropertyBuilder
					.create()
					.addProperty(FourDirectionalBlock.FACING, EnumFace.EAST)
					.tags(Tags.PICKABLE, Tags.TRANSPARENT)
					.custom(true)
					.rot(0, -90, 0), BlockModelBuilder.noGen("flint_1"))

				.addState(PropertyBuilder
					.create()
					.addProperty(FourDirectionalBlock.FACING, EnumFace.SOUTH)
					.tags(Tags.PICKABLE, Tags.TRANSPARENT)
					.custom(true)
					.rot(0, -180, 0), BlockModelBuilder.noGen("flint_1"))

				.addState(PropertyBuilder
					.create()
					.addProperty(FourDirectionalBlock.FACING, EnumFace.WEST)
					.tags(Tags.PICKABLE, Tags.TRANSPARENT)
					.custom(true)
					.rot(0, -270, 0), BlockModelBuilder.noGen("flint_1")))

			.blockSpecial(new SimpleSpecial("flint"))
			.placerGroupPath("nature")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("log_stack")
			.blockSpecial(new SimpleSpecial("log_stack"))
			.blockState(StateBuilder
				.create()
				.addState(PropertyBuilder
					.create()
					.addProperty(LogStackBlock.LOGS, 1)
					.tags(Tags.PICKABLE), BlockModelBuilder
					.create("log_stack_1")
					.modelPath("log_stack")
					.externalPath("custom_models/log_stack_1.bbmodel"))
				.addState(PropertyBuilder
					.create()
					.addProperty(LogStackBlock.LOGS, 2)
					.tags(Tags.PICKABLE), BlockModelBuilder
					.create("log_stack_2")
					.modelPath("log_stack")
					.externalPath("custom_models/log_stack_2.bbmodel"))
				.addState(PropertyBuilder
					.create()
					.addProperty(LogStackBlock.LOGS, 3)
					.tags(Tags.PICKABLE), BlockModelBuilder
					.create("log_stack_3")
					.modelPath("log_stack")
					.externalPath("custom_models/log_stack_3.bbmodel"))
				.addState(PropertyBuilder
					.create()
					.addProperty(LogStackBlock.LOGS, 4)
					.tags(Tags.PICKABLE), BlockModelBuilder
					.create("log_stack_4")
					.modelPath("log_stack")
					.externalPath("custom_models/log_stack_4.bbmodel"))
				.addState(PropertyBuilder
					.create()
					.addProperty(LogStackBlock.LOGS, 5)
					.tags(Tags.PICKABLE), BlockModelBuilder
					.create("log_stack_5")
					.modelPath("log_stack")
					.externalPath("custom_models/log_stack_5.bbmodel"))
				.addState(PropertyBuilder
					.create()
					.addProperty(LogStackBlock.LOGS, 6)
					.tags(Tags.PICKABLE), BlockModelBuilder
					.create("log_stack_6")
					.modelPath("log_stack")
					.externalPath("custom_models/log_stack_6.bbmodel")))
			.placerGroupPath("building.wood")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("conveyor_belt")
			.blockSpecial(new SimpleSpecial("conveyor_belt"))
			.blockState(StateBuilder
				.create()
				.addState(PropertyBuilder.create().addProperty(ConveyorBeltBlock.FACING, EnumFace.UP), BlockModelBuilder
					.create("conveyor_belt")
					.externalPath("custom_models/conveyor_belt.bbmodel"))
				.addState(PropertyBuilder
					.create()
					.addProperty(ConveyorBeltBlock.FACING, EnumFace.DOWN), BlockModelBuilder.noGen("conveyor_belt"))
				.addState(PropertyBuilder
					.create()
					.addProperty(ConveyorBeltBlock.FACING, EnumFace.NORTH), BlockModelBuilder.noGen("conveyor_belt"))
				.addState(PropertyBuilder
					.create()
					.addProperty(ConveyorBeltBlock.FACING, EnumFace.EAST)
					.rot(0, 270, 0), BlockModelBuilder.noGen("conveyor_belt"))
				.addState(PropertyBuilder
					.create()
					.addProperty(ConveyorBeltBlock.FACING, EnumFace.SOUTH)
					.rot(0, 180, 0), BlockModelBuilder.noGen("conveyor_belt"))
				.addState(PropertyBuilder
					.create()
					.addProperty(ConveyorBeltBlock.FACING, EnumFace.WEST)
					.rot(0, 90, 0), BlockModelBuilder.noGen("conveyor_belt")))
			.placerGroupPath("misc")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("pebbles")
			.blockState(StateBuilder
				.create()
				.singleModel(BlockModelBuilder
					.create("pebbles_1")
					.modelPath("pebbles")
					.externalPath("custom_models/pebbles_1.bbmodel"), BlockModelBuilder
					.create("pebbles_2")
					.modelPath("pebbles")
					.externalPath("custom_models/pebbles_2.bbmodel"), BlockModelBuilder
					.create("pebbles_3")
					.modelPath("pebbles")
					.externalPath("custom_models/pebbles_3.bbmodel"), BlockModelBuilder
					.create("pebbles_4")
					.modelPath("pebbles")
					.externalPath("custom_models/pebbles_4.bbmodel")))
			.blockSpecial(SpecialBuilder
				.create("breakable")
				.addValue("item", "pebble")
				.addValue("random_count", true)
				.addValue("min", 1)
				.addValue("max", 4)
				.addValue("distance", 0.4f)
				.addValue("random_rotation", true))
			.placerGroupPath("nature")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("hammerstone")
			.blockState(StateBuilder
				.create()
				.addState(PropertyBuilder
					.create()
					.addProperty(HammerstoneBlock.STONE_TYPE, EnumStoneType.SANDSTONE)
					.tags(Tags.PICKABLE, Tags.TRANSPARENT)
					.custom(true), BlockModelBuilder
					.create("hammerstone_sandstone_1")
					.modelPath("hammerstones")
					.externalPath("custom_models/hammerstone_sandstone_1.bbmodel"))

				.addState(PropertyBuilder
					.create()
					.addProperty(HammerstoneBlock.STONE_TYPE, EnumStoneType.LIMESTONE)
					.tags(Tags.PICKABLE, Tags.TRANSPARENT)
					.custom(true), BlockModelBuilder
					.create("hammerstone_limestone_1")
					.modelPath("hammerstones")
					.externalPath("custom_models/hammerstone_limestone_1.bbmodel"))

				.addState(PropertyBuilder
					.create()
					.addProperty(HammerstoneBlock.STONE_TYPE, EnumStoneType.QUARTZITE)
					.tags(Tags.PICKABLE, Tags.TRANSPARENT)
					.custom(true), BlockModelBuilder
					.create("hammerstone_quartzite_1")
					.modelPath("hammerstones")
					.externalPath("custom_models/hammerstone_quartzite_1.bbmodel")))

			.blockSpecial(new SimpleSpecial("hammerstone"))
			.placerGroupPath("nature")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("stick")
			.blockSpecial(tagBelowSpecial(true, Tags.SOLID))
			.blockState(StateBuilder
				.create()
				.singleModel((BlockModelBuilder
					.create("stick_4")
					.modelPath("stick")
					.externalPath("custom_models/stick_5.bbmodel")))
				.tags(Tags.PICKABLE))
			.placerGroupPath("nature.forest")
			.blockSpecial(SpecialBuilder
				.create("breakable")
				.addValue("item", "stick")
				.addValue("count", 1)
				.addValue("distance", 0.1f)
				.addValue("random_rotation", true))
			.generate();

		BlockBuilder
			.create()
			.blockName("tall_grass")
			.blockSpecial(new SimpleSpecial("double_block"))
			.blockState(StateBuilder
				.create()
				.addState(PropertyBuilder
					.create()
					.addProperty(DoubleBlock.HALF, EnumHalf.BOTTOM)
					.tag(Tags.TRANSPARENT), BlockModelBuilder
					.create("tall_grass_bottom")
					.addCube(CubeBuilder.create().fullBlock().collisionBox(false))
					.addCube(CubeBuilder
						.create()
						.collisionBox(false)
						.hitbox(false)
						.min(8, 0, 0)
						.max(8, 16, 16)
						.rotation(0, 45, 0)
						.face(FaceBuilder
							.create()
							.texture("block/tall_grass_bottom")
							.biomeTint(true), EnumFace.SOUTH, EnumFace.NORTH))
					.addCube(CubeBuilder
						.create()
						.collisionBox(false)
						.hitbox(false)
						.min(0, 0, 8)
						.max(16, 16, 8)
						.rotation(0, 45, 0)
						.face(FaceBuilder
							.create()
							.texture("block/tall_grass_bottom")
							.biomeTint(true), EnumFace.EAST, EnumFace.WEST)))
				.addState(PropertyBuilder
					.create()
					.addProperty(DoubleBlock.HALF, EnumHalf.TOP)
					.tag(Tags.TRANSPARENT), BlockModelBuilder
					.create("tall_grass_top")
					.addCube(CubeBuilder.create().fullBlock().collisionBox(false))
					.addCube(CubeBuilder
						.create()
						.collisionBox(false)
						.hitbox(false)
						.min(8, 0, 0)
						.max(8, 16, 16)
						.rotation(0, 45, 0)
						.face(FaceBuilder
							.create()
							.texture("block/tall_grass_top")
							.biomeTint(true), EnumFace.SOUTH, EnumFace.NORTH))
					.addCube(CubeBuilder
						.create()
						.collisionBox(false)
						.hitbox(false)
						.min(0, 0, 8)
						.max(16, 16, 8)
						.rotation(0, 45, 0)
						.face(FaceBuilder
							.create()
							.texture("block/tall_grass_top")
							.biomeTint(true), EnumFace.EAST, EnumFace.WEST))))
			.generate();

		BlockBuilder
			.create()
			.blockName("oak_door")
			.blockSpecial(SpecialBuilder.create("door").addValue("model", "door/oak"))
			.blockState(StateBuilder.create().emptyModel())
			.placerGroupPath("building.wood.oak")
			.generate();

		BlockBuilder
			.create()
			.blockName("mechanical_oak_door")
			.blockSpecial(SpecialBuilder.create("door").addValue("model", "door/mechanical_oak"))
			.blockState(StateBuilder.create().emptyModel())
			.placerGroupPath("building.wood.oak")
			.generate();

		BlockBuilder
			.create()
			.blockName("cactus")
			.blockSpecial(tagBelowSpecial(true, Tags.CACTUS_TOP))
			.blockState(StateBuilder
				.create()
				.singleModel(BlockModelBuilder
					.create("cactus")
					.addCube(CubeBuilder
						.create()
						.min(1, 0, 1)
						.max(15, 16, 15)
						.face(FaceBuilder.create().texture("block/cactus_top").autoUv(true), EnumFace.UP)
						.face(FaceBuilder.create().texture("block/cactus_bottom").autoUv(true), EnumFace.DOWN))
					.addCube(CubeBuilder
						.create()
						.collisionBox(false)
						.hitbox(false)
						.min(0, 0, 1)
						.max(16, 16, 15)
						.face(FaceBuilder
							.create()
							.texture("block/cactus_side")
							.autoUv(true), EnumFace.EAST, EnumFace.WEST))
					.addCube(CubeBuilder
						.create()
						.collisionBox(false)
						.hitbox(false)
						.min(1, 0, 0)
						.max(15, 16, 16)
						.face(FaceBuilder
							.create()
							.texture("block/cactus_side")
							.autoUv(true), EnumFace.NORTH, EnumFace.SOUTH)))
				.tag(Tags.CACTUS_TOP))
			.placerGroupPath("nature.desert")
			.generate();

		BlockBuilder
			.create()
			.blockName("snow_layer")
			.blockSpecial(new SimpleSpecial("snow_layer"))
			.blockState(StateBuilder
				.create()
				.addState(snowState(1), snowLayer(1, "block/snow"))
				.addState(snowState(2), snowLayer(2, "block/snow"))
				.addState(snowState(3), snowLayer(3, "block/snow"))
				.addState(snowState(4), snowLayer(4, "block/snow"))
				.addState(snowState(5), snowLayer(5, "block/snow"))
				.addState(snowState(6), snowLayer(6, "block/snow"))
				.addState(snowState(7), snowLayer(7, "block/snow"))
				.addState(snowState(8).tag(Tags.SOLID), snowLayer(8, "block/snow")))
			.placerGroupPath("nature")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("grass")
			.blockState(StateBuilder
				.create()
				.singleModel(BlockModelBuilder
					.create("grass")
					.addCube(CubeBuilder
						.create()
						.fullBlock()
						.face(FaceBuilder
							.create()
							.texture("block/dirt"), EnumFace.NORTH, EnumFace.EAST, EnumFace.SOUTH, EnumFace.WEST, EnumFace.DOWN))
					.addCube(CubeBuilder
						.create()
						.fullBlock()
						.hitbox(false)
						.collisionBox(false)
						.face(FaceBuilder
							.create()
							.texture("block/grass_block_side_overlay")
							.biomeTint(true)
							.modelLayer(ModelLayer.OVERLAY_0), CubeBuilder.SIDE)
						.face(FaceBuilder
							.create()
							.texture("block/grass_block_top")
							.biomeTint(true)
							.modelLayer(ModelLayer.OVERLAY_0), EnumFace.UP)))
				.tags(Tags.FLOWER_TOP, Tags.SOLID))
			.blockSpecial(SpecialBuilder
				.create("spreadable")
				.addValue("target", "dirt")
				.addValue("target_set", "grass"))
			.placerGroupPath("nature")
			.generate();

		BlockBuilder
			.create()
			.fullBlock("green_screen")
			.blockState(StateBuilder
				.create()
				.singleModel(BlockModelBuilder
					.create("green_screen")
					.addCube(CubeBuilder
						.create()
						.fullBlock()
						.face(FaceBuilder.create().texture("block/green_screen").modelLayer(ModelLayer.LIGHT))))
				.tag(Tags.SOLID))
			.placerGroupPath("misc")
			.generate();

		BlockBuilder
			.create()
			.blockName("slime_torch")
			.blockSpecial(new SimpleSpecial("state_test"))
			.blockState(StateBuilder
				.create()
				.addState(PropertyBuilder.create().addProperty(States.LIT, true), BlockModelBuilder
					.create("slime_torch")
					.addCube(CubeBuilder
						.create()
						.torch()
						.collisionBox(false)
						.face(FaceBuilder
							.create()
							.texture("block/slime_torch")
							.modelLayer(ModelLayer.LIGHT), CubeBuilder.SIDE)
						.face(FaceBuilder
							.create()
							.texture("block/slime_torch")
							.modelLayer(ModelLayer.LIGHT)
							.uv(7, 6, 9, 8), EnumFace.UP)
						.face(FaceBuilder
							.create()
							.texture("block/slime_torch")
							.modelLayer(ModelLayer.LIGHT)
							.uv(7, 11, 9, 13), EnumFace.DOWN)))
				.addState(PropertyBuilder.create().addProperty(States.LIT, false), BlockModelBuilder
					.create("slime_torch_off")
					.addCube(CubeBuilder
						.create()
						.torch()
						.collisionBox(false)
						.face(FaceBuilder.create().texture("block/slime_torch_off"), CubeBuilder.SIDE)
						.face(FaceBuilder
							.create()
							.texture("block/slime_torch_off")
							.modelLayer(ModelLayer.LIGHT)
							.uv(7, 6, 9, 8), EnumFace.UP)
						.face(FaceBuilder
							.create()
							.texture("block/slime_torch_off")
							.modelLayer(ModelLayer.LIGHT)
							.uv(7, 11, 9, 13), EnumFace.DOWN))))
			.placerGroupPath("misc")
			.generate();

		BlockBuilder
			.create()
			.blockName("smooth_slab")
			.blockSpecial(new SimpleSpecial("slab"))
			//			.itemSpecial(SpecialBuilder
			//				.create("slab")
			//				.addValue("block", "smooth_slab"))
			.blockState(StateBuilder
				.create()
				.addState(PropertyBuilder
					.create()
					.addProperty(SlabBlock.TYPE, EnumSlabType.BOTTOM)
					.addProperty(SlabBlock.AXIS, EnumAxis.Y), BlockModelBuilder
					.create("smooth_slab" + "_bottom")
					.modelPath("slab")
					.addCube(CubeBuilder
						.create()
						.bottomSlab()
						.face(FaceBuilder.create().texture("block/smooth_stone").uvlock(true), CubeBuilder.TOP_BOTTOM)
						.face(FaceBuilder.create().texture("block/smooth_stone_slab").uvlock(true), CubeBuilder.SIDE)))
				.addState(PropertyBuilder
					.create()
					.addProperty(SlabBlock.TYPE, EnumSlabType.TOP)
					.addProperty(SlabBlock.AXIS, EnumAxis.Y), BlockModelBuilder
					.create("smooth_slab" + "_top")
					.modelPath("slab")
					.addCube(CubeBuilder
						.create()
						.topSlab()
						.face(FaceBuilder.create().texture("block/smooth_stone").uvlock(true), CubeBuilder.TOP_BOTTOM)
						.face(FaceBuilder.create().texture("block/smooth_stone_slab").uvlock(true), CubeBuilder.SIDE)))
				.addState(PropertyBuilder
					.create()
					.addProperty(SlabBlock.TYPE, EnumSlabType.DOUBLE)
					.addProperty(SlabBlock.AXIS, EnumAxis.Y)
					.tag(Tags.SOLID), BlockModelBuilder
					.create("smooth_slab" + "_double")
					.modelPath("slab")
					.addCube(CubeBuilder
						.create()
						.fullBlock()
						.face(FaceBuilder.create().texture("block/smooth_stone").uvlock(true), CubeBuilder.TOP_BOTTOM)
						.face(FaceBuilder.create().texture("block/smooth_stone_slab").uvlock(true), CubeBuilder.SIDE)))
				.addState(PropertyBuilder
					.create()
					.addProperty(SlabBlock.TYPE, EnumSlabType.BOTTOM)
					.addProperty(SlabBlock.AXIS, EnumAxis.X), BlockModelBuilder
					.create("smooth_slab" + "_side_x")
					.modelPath("slab")
					.addCube(CubeBuilder
						.create()
						.min(0, 0, 0)
						.max(8, 16, 16)
						.face(FaceBuilder.create().texture("block/smooth_stone_slab_side"), CubeBuilder.TOP_BOTTOM)
						.face(FaceBuilder.create().texture("block/smooth_stone"), EnumFace.NORTH, EnumFace.SOUTH)
						.face(FaceBuilder
							.create()
							.texture("block/smooth_stone_slab_side")
							.uv(0, 8, 16, 0)
							.rotation(90), EnumFace.EAST, EnumFace.WEST)))
				.addState(PropertyBuilder
					.create()
					.addProperty(SlabBlock.TYPE, EnumSlabType.TOP)
					.addProperty(SlabBlock.AXIS, EnumAxis.X)
					.rot(0, 180, 0), BlockModelBuilder.noGen("smooth_slab" + "_side_x").modelPath("slab"))
				.addState(PropertyBuilder
					.create()
					.addProperty(SlabBlock.TYPE, EnumSlabType.BOTTOM)
					.addProperty(SlabBlock.AXIS, EnumAxis.Z), BlockModelBuilder
					.create("smooth_slab" + "_side_z")
					.modelPath("slab")
					.addCube(CubeBuilder
						.create()
						.min(0, 0, 8)
						.max(16, 16, 16)
						.face(FaceBuilder
							.create()
							.texture("block/smooth_stone_slab_side")
							.uv(0, 0, 16, 8)
							.rotation(90), CubeBuilder.TOP_BOTTOM)
						.face(FaceBuilder
							.create()
							.texture("block/smooth_stone_slab_side")
							.uv(0, 8, 16, 0)
							.rotation(90), EnumFace.NORTH, EnumFace.SOUTH)
						.face(FaceBuilder.create().texture("block/smooth_stone"), EnumFace.EAST, EnumFace.WEST)))
				.addState(PropertyBuilder
					.create()
					.addProperty(SlabBlock.TYPE, EnumSlabType.TOP)
					.addProperty(SlabBlock.AXIS, EnumAxis.Z)
					.rot(0, 180, 0), BlockModelBuilder.noGen("smooth_slab" + "_side_z").modelPath("slab"))
				.addState(PropertyBuilder
					.create()
					.addProperty(SlabBlock.TYPE, EnumSlabType.DOUBLE)
					.addProperty(SlabBlock.AXIS, EnumAxis.Z)
					.tag(Tags.SOLID)
					.rot(0, 90, 0), BlockModelBuilder.noGen("smooth_slab" + "_double_x").modelPath("slab"))
				.addState(PropertyBuilder
					.create()
					.addProperty(SlabBlock.TYPE, EnumSlabType.DOUBLE)
					.addProperty(SlabBlock.AXIS, EnumAxis.X)
					.tag(Tags.SOLID), BlockModelBuilder
					.create("smooth_slab" + "_double_x")
					.modelPath("slab")
					.addCube(CubeBuilder
						.create()
						.fullBlock()
						.face(FaceBuilder
							.create()
							.texture("block/smooth_stone")
							.uvlock(true), EnumFace.NORTH, EnumFace.SOUTH)
						.face(FaceBuilder.create().texture("block/smooth_stone_slab"), EnumFace.UP, EnumFace.DOWN)
						.face(FaceBuilder
							.create()
							.texture("block/smooth_stone_slab")
							.rotation(90)
							.uvlock(true), EnumFace.EAST, EnumFace.WEST))))
			.placerGroupPath("building.stone")
			.generate();

/*
		DataBuilder.create().doubleSlabBlock("dirt", "dirt_slab_top", "dirt_slab_bottom").generate();
		DataBuilder.create().doubleSlabBlock("double_smooth_slab", "smooth_slab_top", "smooth_slab_bottom").generate();

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
		return BlockModelBuilder
			.create("snow_layer_" + height)
			.modelPath("snow_layer")
			.addCube(CubeBuilder
				.create()
				.min(0, 0, 0)
				.max(16, height * 2, 16)
				.face(FaceBuilder.create().texture(texture).autoUv(true)));
	}


	public void generateFeatures()
	{
		FeatureBuilder
			.create(FeatureRegistry.STACKABLE_PILLAR.name())
			.path("desert")
			.name("tall_cactus")
			.matchTags("block_under", Tags.CACTUS_TOP)
			.blockState("block", BlockStateBuilder.create().block("cactus"))
			.doubleArg("next_chance", 0.1)
			.integer("max_height", 22)
			.generate();

		FeatureBuilder
			.create(FeatureRegistry.PILLAR.name())
			.path("desert")
			.name("cactus")
			.matchTags("block_under", Tags.CACTUS_TOP)
			.provideStates("block", BlockStateBuilder.create().block("cactus"))
			.integer("min_height", 1)
			.integer("max_height", 3)
			.generate();

		FeatureBuilder
			.create(FeatureRegistry.PILLAR.name())
			.path("common")
			.name("top_snow")
			.matchAlwaysTrue("block_under")
			.provideStates("block", BlockStateBuilder
				.create()
				.block("snow_layer")
				.state(SnowLayerBlock.SNOW_LAYER, 1), BlockStateBuilder
				.create()
				.block("snow_layer")
				.state(SnowLayerBlock.SNOW_LAYER, 2), BlockStateBuilder
				.create()
				.block("snow_layer")
				.state(SnowLayerBlock.SNOW_LAYER, 3), BlockStateBuilder
				.create()
				.block("snow_layer")
				.state(SnowLayerBlock.SNOW_LAYER, 4))
			.integer("min_height", 1)
			.integer("max_height", 1)
			.generate();

		FeatureBuilder
			.create(FeatureRegistry.BOULDER.name())
			.path("common/boulders")
			.name("limestone_boulder")
			.provideStates("block", BlockStateBuilder.create().block("limestone"))
			.provideStates("slab", BlockStateBuilder.create().block("limestone_slab"))
			.provideStates("deco", BlockStateBuilder
				.create()
				.block("hammerstone")
				.state(HammerstoneBlock.STONE_TYPE, EnumStoneType.LIMESTONE))
			.matchAlwaysTrue("block_under")
			.generate();

		FeatureBuilder
			.create(FeatureRegistry.BOULDER.name())
			.path("common/boulders")
			.name("sandstone_boulder")
			.provideStates("block", BlockStateBuilder.create().block("sandstone"))
			.provideStates("slab", BlockStateBuilder.create().block("sandstone_slab"))
			.provideStates("deco", BlockStateBuilder
				.create()
				.block("hammerstone")
				.state(HammerstoneBlock.STONE_TYPE, EnumStoneType.SANDSTONE))
			.matchAlwaysTrue("block_under")
			.generate();

		FeatureBuilder
			.create(FeatureRegistry.BOULDER.name())
			.path("common/boulders")
			.name("quartzite_boulder")
			.provideStates("block", BlockStateBuilder.create().block("quartzite"))
			.provideStates("slab", BlockStateBuilder.create().block("quartzite_slab"))
			.provideStates("deco", BlockStateBuilder
				.create()
				.block("hammerstone")
				.state(HammerstoneBlock.STONE_TYPE, EnumStoneType.QUARTZITE))
			.matchAlwaysTrue("block_under")
			.generate();

		FeatureBuilder
			.create(FeatureRegistry.BOULDER.name())
			.path("common/boulders")
			.name("stone_boulder")
			.provideStates("block", BlockStateBuilder.create().block("stone"))
			.provideStates("slab", BlockStateBuilder.create().block("stone_slab"))
			.provideStates("deco", BlockStateBuilder.create().block("pebbles"))
			.matchAlwaysTrue("block_under")
			.generate();

		FeatureBuilder
			.create(FeatureRegistry.VEGETATION_PATCH.name())
			.path("desert")
			.name("shrubs")
			.matchTags("block_under", Tags.SHRUBS_TOP)
			.provideStates("block", BlockStateBuilder.create().block("shrub"))
			.integer("radius", 2)
			.doubleArg("chance", 0.5)
			.bool("decay", true)
			.generate();

		FeatureBuilder
			.create(FeatureRegistry.VEGETATION_PATCH.name())
			.path("common")
			.name("grass_patch")
			.matchTags("block_under", Tags.FLOWER_TOP)
			.provideStates("block", BlockStateBuilder.create().block("small_grass"))
			.integer("radius", 2)
			.doubleArg("chance", 0.3)
			.bool("decay", true)
			.generate();

		FeatureBuilder
			.create(FeatureRegistry.VEGETATION_PATCH.name())
			.path("common")
			.name("hemp")
			.matchTags("block_under", Tags.FLOWER_TOP)
			.provideStates("block", BlockStateBuilder
				.create()
				.block("hemp")
				.state(HempBlock.STAGE, 3), BlockStateBuilder.create().block("hemp").state(HempBlock.STAGE, 2))
			.integer("radius", 5)
			.doubleArg("chance", 0.05)
			.bool("decay", true)
			.generate();

		FeatureBuilder
			.create(FeatureRegistry.VEGETATION_PATCH.name())
			.path("common")
			.name("tall_grass_patch")
			.matchTags("block_under", Tags.FLOWER_TOP)
			.provideStates("block", BlockStateBuilder.create().block("tall_grass"))
			.integer("radius", 4)
			.doubleArg("chance", 0.5)
			.bool("decay", true)
			.generate();

		FeatureBuilder
			.create(FeatureRegistry.VEGETATION_PATCH.name())
			.path("common")
			.name("grass_patch_mixed")
			.matchTags("block_under", Tags.FLOWER_TOP)
			.provideStates("block", BlockStateBuilder.create().block("tall_grass"))
			.integer("radius", 4)
			.doubleArg("chance", 0.75)
			.bool("decay", true)
			.generate();

		FeatureBuilder
			.create(FeatureRegistry.VEGETATION_PATCH.name())
			.path("common")
			.name("pebbles")
			.matchBlocks("block_under", "sand", "grass", "dirt", "stone")
			.provideStates("block", BlockStateBuilder.create().block("pebbles"))
			.integer("radius", 3)
			.doubleArg("chance", 0.3)
			.bool("decay", true)
			.generate();

		FeatureBuilder
			.create(FeatureRegistry.VEGETATION_PATCH.name())
			.path("common")
			.name("flowers")
			.matchBlocks("block_under", "grass", "dirt")
			.provideStates("block", BlockStateBuilder
				.create()
				.block("flower")
				.state(FlowerBlock.FLOWER_COLOR, EnumFlowerColor.RED), BlockStateBuilder
				.create()
				.block("flower")
				.state(FlowerBlock.FLOWER_COLOR, EnumFlowerColor.GREEN), BlockStateBuilder
				.create()
				.block("flower")
				.state(FlowerBlock.FLOWER_COLOR, EnumFlowerColor.BLUE), BlockStateBuilder
				.create()
				.block("flower")
				.state(FlowerBlock.FLOWER_COLOR, EnumFlowerColor.CYAN), BlockStateBuilder
				.create()
				.block("flower")
				.state(FlowerBlock.FLOWER_COLOR, EnumFlowerColor.MAGENTA), BlockStateBuilder
				.create()
				.block("flower")
				.state(FlowerBlock.FLOWER_COLOR, EnumFlowerColor.YELLOW), BlockStateBuilder
				.create()
				.block("flower")
				.state(FlowerBlock.FLOWER_COLOR, EnumFlowerColor.WHITE), BlockStateBuilder
				.create()
				.block("flower")
				.state(FlowerBlock.FLOWER_COLOR, EnumFlowerColor.BLACK))
			.integer("radius", 4)
			.doubleArg("chance", 0.4)
			.bool("decay", true)
			.generate();

		FeatureBuilder
			.create(FeatureRegistry.VEGETATION_PATCH.name())
			.path("common")
			.name("sticks")
			.matchBlocks("block_under", "sand", "grass", "dirt", "stone")
			.provideStates("block", BlockStateBuilder.create().block("stick"))
			.integer("radius", 3)
			.doubleArg("chance", 0.3)
			.bool("decay", true)
			.generate();

		FeatureBuilder
			.create(FeatureRegistry.VEGETATION_PATCH.name())
			.path("common")
			.name("flint")
			.matchBlocks("block_under", "sand", "grass", "dirt", "stone")
			.provideStates("block", BlockStateBuilder
				.create()
				.block("flint")
				.state(FourDirectionalBlock.FACING, EnumFace.NORTH), BlockStateBuilder
				.create()
				.block("flint")
				.state(FourDirectionalBlock.FACING, EnumFace.EAST), BlockStateBuilder
				.create()
				.block("flint")
				.state(FourDirectionalBlock.FACING, EnumFace.SOUTH), BlockStateBuilder
				.create()
				.block("flint")
				.state(FourDirectionalBlock.FACING, EnumFace.WEST))
			.integer("radius", 4)
			.doubleArg("chance", 0.5)
			.bool("decay", true)
			.generate();

		FeatureBuilder
			.create(FeatureRegistry.SAPLING.name())
			.path("trees")
			.name("oak_sapling")
			.matchBlocks("match_under", "grass", "dirt")
			.generate();

		FeatureBuilder
			.create(FeatureRegistry.TOP_SNOW.name())
			.path("mountain")
			.name("top_mountain_snow")
			.matchAlwaysTrue("target")
			.provideStates("snow", BlockStateBuilder
				.create()
				.block("snow_layer")
				.state(SnowLayerBlock.SNOW_LAYER, 1), BlockStateBuilder
				.create()
				.block("snow_layer")
				.state(SnowLayerBlock.SNOW_LAYER, 2), BlockStateBuilder
				.create()
				.block("snow_layer")
				.state(SnowLayerBlock.SNOW_LAYER, 3), BlockStateBuilder
				.create()
				.block("snow_layer")
				.state(SnowLayerBlock.SNOW_LAYER, 4))
			.integer("height_start", 46)
			.integer("height_max", 52)
			.generate();

		FeatureBuilder
			.create(FeatureRegistry.TILE_REPLACE_PATCH.name())
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
		BiomeBuilder
			.create()
			.name("desert")
			.surface("sand", "sandstone", "stone", 4, 16)
			.heightMap(4, 0.3f, 0.03f, 8, 18)
			.climate(0.05f, 1f, 0f, -1f)
			.foliageColor(183 / 255f, 212 / 255f, 80 / 255f)
			.feature(EnumFeatureStage.VEGETATION, 1d / 100d, "shrubs")
			.feature(EnumFeatureStage.VEGETATION, 1d / 256d / 4d, "tall_cactus")
			.feature(EnumFeatureStage.VEGETATION, 1d / 256d, "cactus")
			.feature(EnumFeatureStage.VEGETATION, 1d / 256d / 8d, "pebbles")
			.feature(EnumFeatureStage.TOP_MODIFICATION, 1d / 3072, "sandstone_boulder")
			.generate();

		BiomeBuilder
			.create()
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

		BiomeBuilder
			.create()
			.name("forest")
			.surface("grass", "dirt", "stone", 4, 20)
			.heightMap(8, 0.07f, 0.007f, 12, 22)
			.climate(0.1f, 0.25f, 0f, -0.6f)
			.foliageColor(92 / 255f, 184 / 255f, 64 / 255f)
			.feature(EnumFeatureStage.TOP_MODIFICATION, 1d / 2048, "stone_boulder")
			.feature(EnumFeatureStage.VEGETATION, 1d / 75d, "grass_patch")
			.feature(EnumFeatureStage.VEGETATION, 1d / 512d, "pebbles")
			.feature(EnumFeatureStage.VEGETATION, 1d / 150d, "sticks")
			.feature(EnumFeatureStage.VEGETATION, 1d / 512d, "flowers")
			.feature(EnumFeatureStage.TREE, 1d / 5d, "oak_sapling")
			.generate();

		BiomeBuilder
			.create()
			.name("plains")
			.surface("grass", "dirt", "stone", 4, 20)
			.heightMap(7, 0.07f, 0.006f, 10, 20)
			.climate(0f, 0.2f, 0f, -0.3f)
			.foliageColor(92 / 255f, 200 / 255f, 64 / 255f)
			.feature(EnumFeatureStage.VEGETATION, 1d / 100d, "grass_patch")
			.feature(EnumFeatureStage.VEGETATION, 1d / 220d, "tall_grass_patch")
			.feature(EnumFeatureStage.VEGETATION, 1d / 512d, "pebbles")
			.feature(EnumFeatureStage.VEGETATION, 1d / 256d, "flowers")
			.generate();

		BiomeBuilder
			.create()
			.name("tundra")
			.surface("grass", "dirt", "stone", 4, 24)
			.heightMap(7, 0.06f, 0.005f, 10, 18)
			.climate(0f, -1f, 0f, 0.25f)
			.foliageColor(92 / 255f, 200 / 255f, 64 / 255f)
			.feature(EnumFeatureStage.TOP_MODIFICATION, 1d / 4096 / 2d, "limestone_boulder")
			.feature(EnumFeatureStage.TOP_MODIFICATION, 1d / 4096 / 2d, "quartzite_boulder")
			.feature(EnumFeatureStage.TOP_MODIFICATION, 1, "top_snow")
			.feature(EnumFeatureStage.TREE, 1d / 300d, "oak_sapling")
			.generate();

		BiomeBuilder
			.create()
			.name("savanna")
			.surface("grass", "dirt", "stone", 4, 16)
			.heightMap(6, 0.3f, 0.019f, 12, 23)
			.climate(0.1f, 0.65f, 0.0f, 0.05f)
			.foliageColor(120 / 255f, 190 / 255f, 60 / 255f)
			.feature(EnumFeatureStage.TOP_MODIFICATION, 1d / 4096 / 2d, "limestone_boulder")
			.feature(EnumFeatureStage.TOP_MODIFICATION, 1d / 4096 / 2d, "quartzite_boulder")
			.feature(EnumFeatureStage.TREE, 1d / 400d, "oak_sapling")
			.feature(EnumFeatureStage.VEGETATION, 1d / 256d, "pebbles")
			.feature(EnumFeatureStage.VEGETATION, 1d / 100d, "grass_patch")
			.feature(EnumFeatureStage.VEGETATION, 1d / 4096d, "hemp")
			.generate();

		BiomeBuilder
			.create()
			.name("savanna_plateau")
			.surface("grass", "dirt", "stone", 4, 16)
			.heightMap(6, 0.3f, 0.003f, 23, 30)
			.climate(0.8f, 0.60f, 0.3f, 0.0f)
			.foliageColor(120 / 255f, 190 / 255f, 60 / 255f)
			.feature(EnumFeatureStage.TOP_MODIFICATION, 1d / 4096 / 2d, "limestone_boulder")
			.feature(EnumFeatureStage.TOP_MODIFICATION, 1d / 4096 / 2d, "quartzite_boulder")
			.feature(EnumFeatureStage.TREE, 1d / 400d, "oak_sapling")
			.feature(EnumFeatureStage.VEGETATION, 1d / 2048d, "flint")
			.feature(EnumFeatureStage.VEGETATION, 1d / 256d, "pebbles")
			.feature(EnumFeatureStage.VEGETATION, 1d / 100d, "grass_patch")
			.generate();

		BiomeBuilder
			.create()
			.name("mountains")
			.surface("stone", "stone", "stone", 0, 12)
			.heightMap(3, 0.2f, 0.025f, 15, 50)
			.climate(1f, 0.1f, 0.0f, 0.17f)
			.foliageColor(92 / 255f, 160 / 255f, 64 / 255f)
			.generate();

		BiomeBuilder
			.create()
			.name("cold_mountains")
			.surface("stone", "stone", "stone", 0, 12)
			.heightMap(3, 0.2f, 0.025f, 15, 50)
			.climate(1f, -1f, 0.0f, 0.15f)
			.foliageColor(92 / 255f, 160 / 255f, 64 / 255f)
			.feature(EnumFeatureStage.TOP_MODIFICATION, 1d, "top_mountain_snow")
			.feature(EnumFeatureStage.TOP_MODIFICATION, 1.0 / 500.0, "mountain_snow_patches")
			.generate();
	}


}
