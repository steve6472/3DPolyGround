package steve6472.polyground.generator;

import steve6472.polyground.EnumFace;
import steve6472.polyground.generator.models.*;
import steve6472.polyground.generator.special.SimpleSpecial;
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

	static File blocks = new File("game/objects/blocks");
	static File items = new File("game/objects/items");

	static File blockModels = new File("game/objects/models/block");
	static File itemModels = new File("game/objects/models/item");

	public static void main(String[] args)
	{
//		new DataGenerator().generate();
		new DataGenerator().generateDebug();
	}

	private void createFolders()
	{
		if (!blocks.exists())
			if (blocks.mkdirs())
				System.out.println("Created new blocks folder");
			else
				System.err.println("Error while creating new blocks folder");
		if (!items.exists())
			if (items.mkdirs())
				System.out.println("Created new items folder");
			else
				System.err.println("Error while creating new items folder");
		if (!blockModels.exists())
			if (blockModels.mkdirs())
				System.out.println("Created new blockModels folder");
			else
				System.err.println("Error while creating new blockModels folder");
		if (!itemModels.exists())
			if (itemModels.mkdirs())
				System.out.println("Created new itemModels folder");
			else
				System.err.println("Error while creating new itemModels folder");
	}

	public void generateDebug()
	{
		createFolders();

		DataBuilder.create().fullBlock("stone").generate();

		DataBuilder.create().torch("slime_torch", true).blockSpecial(new SimpleSpecial("state_test")).itemModel(new ItemFromTexture("slime_torch")).generate();
	}

	public void generate()
	{
		createFolders();
//		decorativeFullTintedBlock("full_grass_block", 0.5686275f, 0.7411765f, 0.34901962f, "grass_block_top");

		DataBuilder.create().fullBlock("smooth_stone").generate();
		DataBuilder.create().fullBlock("bricks").generate();
		DataBuilder.create().fullBlock("iron_block").generate();
		DataBuilder.create().fullBlock("stone").generate();
		DataBuilder.create().fullBlock("cobblestone").generate();
		DataBuilder.create().fullBlock("bedrock").generate();
		DataBuilder.create().fullBlock("sand").generate();
		DataBuilder.create().fullBlock("gravel").generate();
		DataBuilder.create().fullBlock("cond_test").blockModel(new CondTest()).generate();

		DataBuilder.create().stairs("stone", "stone");
		DataBuilder.create().stairs("oak", "oak_plank");
		DataBuilder.create().stairs("cobblestone", "cobblestone");
		DataBuilder.create().stairs("brick", "bricks");

		DataBuilder.create().plusBlock("small_grass", true).generate();

		DataBuilder.create().oreBlock("coal_ore", "stone", "ore_overlay", 77, 77, 77).generate();

		DataBuilder.create().lightOreBlock("magical_coal_ore", "stone", "ore_overlay", 26, 204, 204, "6c7f7a", 1.0f, 0.7f, 1.8f).generate();

		DataBuilder.create().fullLightBlock("blaze_block", "FDEA49", 1.0f, 0.14f, 0.07f).generate();

		DataBuilder.create().transparentFullBlock("glass").generate();
		DataBuilder.create().transparentFullBlock("framed_glass").generate();

		DataBuilder.create().slabs("oak_plank", "oak_plank");
		DataBuilder.create().slabs("dirt", "dirt");

		DataBuilder.create().doubleSlabBlock("double_smooth_slab", "smooth_stone", "smooth_stone_slab", "smooth_slab_top", "smooth_slab_bottom").generate();
		DataBuilder.create().slabBlock("smooth_slab_bottom", "smooth_stone", "smooth_stone_slab", true).generate();
		DataBuilder.create().slabBlock("smooth_slab_top", "smooth_stone", "smooth_stone_slab", false).generate();
		DataBuilder.create().slabItem("smooth_slab", "smooth_slab_top", "smooth_slab_bottom", "double_smooth_slab").generate();

		DataBuilder.create().logBlock("oak_log", "oak_log_side", "oak_log").generate();

		for (int i = 1; i <= 7; i++)
		{
			DataBuilder.create().stalaBlock("stala_" + i * 2, "stone", i * 2).blockModelPath("stala").generate();
		}

		DataBuilder.create().torch("slime_torch", true).generate();

		DataBuilder.create().fullBlock("oak_leaves")
			.blockModel(BlockModelBuilder.create()
				.addCube(CubeBuilder.create()
					.fullBlock()
					.face(FaceBuilder.create().texture("oak_leaves").tint(118, 171, 47))
				).build()
			).blockSpecial(new SimpleSpecial("leaves")).generate();

		DataBuilder.create().fullBlock("grass")
			.blockModel(BlockModelBuilder.create()
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
					)).build()
			).generate();

		DataBuilder.create().fullBlock("green_screen")
			.blockModel(BlockModelBuilder.create()
				.addCube(CubeBuilder.create()
					.fullBlock()
					.face(FaceBuilder.create()
						.texture("green_screen")
						.modelLayer(ModelLayer.LIGHT))).build()).generate();

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

}
