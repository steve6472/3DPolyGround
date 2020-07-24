package steve6472.polyground.generator;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.properties.enums.EnumAxis;
import steve6472.polyground.block.properties.enums.EnumSlabType;
import steve6472.polyground.block.special.SlabBlock;
import steve6472.polyground.block.special.StalaBlock;
import steve6472.polyground.block.states.States;
import steve6472.polyground.generator.models.*;
import steve6472.polyground.generator.special.SimpleSpecial;
import steve6472.polyground.generator.state.PropertyBuilder;
import steve6472.polyground.generator.state.StateBuilder;
import steve6472.polyground.world.chunk.ModelLayer;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 24.09.2019
 * Project: SJP
 *
 ***********************/
public class DataGenerator
{/*
	static File blocks = new File("src/main/resources/blocks");
	static File items = new File("src/main/resources/items");

	static File blockModels = new File("src/main/resources/models/block");
	static File itemModels = new File("src/main/resources/models/item");*/

	public static File BLOCKS = new File("game/objects/blocks");
	public static File ITEMS = new File("game/objects/items");

	public static File BLOCK_MODELS = new File("game/objects/models/block");
	public static File ITEM_MODELS = new File("game/objects/models/item");

	public static File BLOCK_STATES = new File("game/objects/blockstates");

	public static void main(String[] args)
	{
		new DataGenerator().generate();
//		new DataGenerator().generateDebug();
	}

	private void createFolders()
	{
		if (!BLOCKS.exists())
			if (BLOCKS.mkdirs())
				System.out.println("Created new blocks folder");
			else
				System.err.println("Error while creating new blocks folder");
		if (!ITEMS.exists())
			if (ITEMS.mkdirs())
				System.out.println("Created new items folder");
			else
				System.err.println("Error while creating new items folder");
		if (!BLOCK_MODELS.exists())
			if (BLOCK_MODELS.mkdirs())
				System.out.println("Created new blockModels folder");
			else
				System.err.println("Error while creating new blockModels folder");
		if (!ITEM_MODELS.exists())
			if (ITEM_MODELS.mkdirs())
				System.out.println("Created new itemModels folder");
			else
				System.err.println("Error while creating new itemModels folder");
		if (!BLOCK_STATES.exists())
			if (BLOCK_STATES.mkdirs())
				System.out.println("Created new blockStates folder");
			else
				System.err.println("Error while creating new blockStates folder");
	}

	public void generateDebug()
	{
		createFolders();

//		DataBuilder.create().torch("slime_torch", true).blockSpecial(new SimpleSpecial("state_test")).itemModel(new ItemFromTexture("slime_torch")).generate();
	}

	public void generate()
	{
		createFolders();
//		decorativeFullTintedBlock("full_grass_block", 0.5686275f, 0.7411765f, 0.34901962f, "grass_block_top");

		// Items
		DataBuilder.create().itemModel(new ItemFromTexture("block_inspector")).itemName("block_inspector").itemSpecial(new SimpleSpecial("block_inspector")).generate();

		// Blocks
		DataBuilder.create().fullBlock("smooth_stone").generate();
		DataBuilder.create().fullBlock("bricks").generate();
		DataBuilder.create().fullBlock("iron_block").generate();
		DataBuilder.create().fullBlock("stone").generate();
		DataBuilder.create().fullBlock("cobblestone").generate();
		DataBuilder.create().fullBlock("bedrock").generate();
		DataBuilder.create().fullBlock("sand").generate();
		DataBuilder.create().fullBlock("gravel").generate();
		DataBuilder.create().fullBlock("dirt").generate();
		DataBuilder.create().fullBlock("oak_plank").generate();
		DataBuilder.create().fullBlock("null").noItem().generate();
//		DataBuilder.create().fullBlock("cond_test").blockState(StateBuilder.create().singleModel(new CondTest())).blockModel().generate();

		DataBuilder.create().stairs("stone_stairs", "stone").generate();
		DataBuilder.create().stairs("oak_plank_stairs", "oak_plank").generate();
		DataBuilder.create().stairs("cobblestone_stairs", "cobblestone").generate();
		DataBuilder.create().stairs("brick_stairs", "bricks").generate();

		DataBuilder.create().plusBlock("small_grass", true).generate();

		DataBuilder.create().oreBlock("coal_ore", "stone", "ore_overlay", 77, 77, 77).generate();

		DataBuilder.create().lightOreBlock("magical_coal_ore", "stone", "ore_overlay", 26, 204, 204, "6c7f7a", 1.0f, 0.7f, 1.8f).generate();

		DataBuilder.create().fullLightBlock("blaze_block", "FDEA49", 1.0f, 0.14f, 0.07f).generate();

		DataBuilder.create().transparentFullBlock("glass").generate();
		DataBuilder.create().transparentFullBlock("framed_glass").generate();

		DataBuilder.create().leaves("oak_leaves").generate();
		DataBuilder.create().leaves("crystal_leaves").generate();

		DataBuilder.create().slab("dirt_slab", "dirt").generate();
		DataBuilder.create().slab("stone_slab", "stone").generate();
		DataBuilder.create().slab("oak_plank_slab", "oak_plank").generate();
		DataBuilder.create().slab("cobblestone_slab", "cobblestone").generate();
		DataBuilder.create().slab("brick_slab", "bricks").generate();

		DataBuilder.create().pillarBlock("oak_log", "oak_log_side", "oak_log").generate();
		DataBuilder.create().pillarBlock("crystal_log", "crystal_log_side", "crystal_log").generate();

		DataBuilder.create()
			.blockName("stone_stala")
			.itemName("stone_stala")
			.blockToPlace("stone_stala")
			.blockSpecial(new SimpleSpecial("stala"))
			.itemModel(new ItemFromBlock("stone_stala"))
			.blockState(StateBuilder.create()
				.addState(stalaState(1), stalaModel(1, "stone", "stone"))
				.addState(stalaState(2), stalaModel(2, "stone", "stone"))
				.addState(stalaState(3), stalaModel(3, "stone", "stone"))
				.addState(stalaState(4), stalaModel(4, "stone", "stone"))
				.addState(stalaState(5), stalaModel(5, "stone", "stone"))
				.addState(stalaState(6), stalaModel(6, "stone", "stone"))
				.addState(stalaState(7), stalaModel(7, "stone", "stone"))
			).generate();

		DataBuilder.create().fullBlock("grass")
			.blockState(StateBuilder.create().singleModel(
				BlockModelBuilder.create("grass")
					.addCube(CubeBuilder.create()
						.fullBlock()
						.face(FaceBuilder.create().texture("dirt")))
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
			).generate();

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

		//		DataBuilder.create().doubleSlabBlock("double_smooth_slab", "smooth_stone", "smooth_stone_slab", "smooth_slab_top", "smooth_slab_bottom").generate();
		//		DataBuilder.create().slabBlock("smooth_slab_bottom", "smooth_stone", "smooth_stone_slab", true).generate();
		//		DataBuilder.create().slabBlock("smooth_slab_top", "smooth_stone", "smooth_stone_slab", false).generate();
		//		DataBuilder.create().slabItem("smooth_slab", "smooth_slab_top", "smooth_slab_bottom", "double_smooth_slab").generate();

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

	private static PropertyBuilder stalaState(int width)
	{
		return PropertyBuilder.create().addProperty(StalaBlock.WIDTH, width);
	}

	private static BlockModelBuilder stalaModel(int width, String texture, String path)
	{
		return BlockModelBuilder.create("stala_" + width)
			.modelPath("stala/" + path)
			.addCube(CubeBuilder.create()
				.min(8 - width, 0, 8 - width)
				.max(8 + width, 16, 8 + width)
				.face(FaceBuilder.create()
					.texture(texture)
					.autoUv(true))
		);
	}

}
