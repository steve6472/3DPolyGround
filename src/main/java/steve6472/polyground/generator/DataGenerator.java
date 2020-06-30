package steve6472.polyground.generator;

import steve6472.polyground.generator.models.stair.StairBlock;
import steve6472.SSS;
import org.json.JSONObject;
import steve6472.polyground.generator.models.*;
import steve6472.polyground.generator.models.slab.*;
import steve6472.polyground.generator.special.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 24.09.2019
 * Project: SJP
 *
 ***********************/
public class DataGenerator
{
	private File blocks = new File("src/main/resources/blocks");
	private File items = new File("src/main/resources/items");

	private File blockModels = new File("src/main/resources/models/block");
	private File itemModels = new File("src/main/resources/models/item");

	public static void main(String[] args)
	{
		try
		{
			new DataGenerator().generate();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void generate() throws IOException
	{
//		decorativeFullBlock("smooth_stone");
//		decorativeFullBlock("bricks");
//		decorativeFullBlock("oak_plank");
//		decorativeFullBlock("iron_block");
//		decorativeFullBlock("stone");
//		decorativeFullBlock("cobblestone");
//		decorativeFullBlock("bedrock");
//		decorativeFullBlock("green_screen");

//		decorativeFullTintedBlock("full_grass_block", 0.5686275f, 0.7411765f, 0.34901962f, "grass_block_top");

//		decorativeFullTransparentBlock("glass");
//		decorativeFullTransparentBlock("framed_glass");

		blockWithModelSpecial("stala_14", new StalBlock(1), new SimpleSpecial("custom"));
		blockWithModelSpecial("stala_12", new StalBlock(2), new SimpleSpecial("custom"));
		blockWithModelSpecial("stala_10", new StalBlock(3), new SimpleSpecial("custom"));
		blockWithModelSpecial("stala_8", new StalBlock(4), new SimpleSpecial("custom"));
		blockWithModelSpecial("stala_6", new StalBlock(5), new SimpleSpecial("custom"));
		blockWithModelSpecial("stala_4", new StalBlock(6), new SimpleSpecial("custom"));
		blockWithModelSpecial("stala_2", new StalBlock(7), new SimpleSpecial("custom"));

/*
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
	}

	/* Ores */

	private void tintedOreBlock(String name, float red, float green, float blue) throws IOException
	{
		block(name, new TintedFullBlock("stone_cut", "ore_overlay", red, green, blue), new SimpleSpecial("tinted"));
		item(name, new ItemFromBlock(name), null, name);
	}

	/* Stair */

	private void stairFamily(String name, String texture) throws IOException
	{
		block(name + "_north", new StairBlock(texture), new SimpleSpecial("stair"));

		item(name, new ItemFromBlock(name + "_north"), null, name + "_north");
	}

	/* Grass Block */

	private void grassBlock(String name, float red, float green, float blue) throws IOException
	{
		block(name, new TintedGrassBlock(red, green, blue, "dirt_cut", "dirt", "grass_block_side_overlay", "grass_block_top"), new SimpleSpecial("tinted"));
		item(name, new ItemFromBlock(name), null, name);
	}

	private void bottomSlabGrassTinted(String name, float red, float green, float blue) throws IOException
	{
		block(name, new SlabBottomTintedGrass(red, green, blue, "dirt_cut", "dirt", "grass_block_side_overlay", "grass_block_top"), new TintedSlabBlockSpecial("bottom"));
	}

	private void topSlabGrassTinted(String name, float red, float green, float blue) throws IOException
	{
		block(name, new SlabTopTintedGrass(red, green, blue, "dirt_cut", "dirt", "grass_block_side_overlay", "grass_block_top"), new TintedSlabBlockSpecial("top"));
	}

	/* Full Slab */

	private void topSlabFull(String name, String texture) throws IOException
	{
		block(name, new SlabTopFullBlock(texture), new SlabBlockSpecial("top"));
	}

	private void bottomSlabFull(String name, String texture) throws IOException
	{
		block(name, new SlabBottomFullBlock(texture), new SlabBlockSpecial("bottom"));
	}

	private void doubleSlabFull(String name, String slabTop, String slabBottom) throws IOException
	{
		block(name, new FullBlock(name), new DoubleSlabSpecial(slabTop, slabBottom));
		item(name, new ItemFromBlock(name), null, name);
	}

	private void doubleSlabFull(String name, String texture, String slabTop, String slabBottom) throws IOException
	{
		block(name, new FullBlock(texture), new DoubleSlabSpecial(slabTop, slabBottom));
		item(name, new ItemFromBlock(name), null, name);
	}

	private void fullSlabFamily(String topName, String bottomName, String bothName, String itemName, String texture) throws IOException
	{
		topSlabFull(topName, texture);
		bottomSlabFull(bottomName, texture);
		doubleSlabFull(bothName, texture, topName, bottomName);
		item(itemName, new ItemFromBlock(bottomName), new SlabSpecial(topName, bottomName, bothName), bottomName);
	}

	/* Sided Slab */

	private void topSlabSided(String name, String top, String bottom, String side) throws IOException
	{
		block(name, new SlabTopSidedBlock(top, bottom, side), new SlabBlockSpecial("top"));
	}

	private void bottomSlabSided(String name, String top, String bottom, String side) throws IOException
	{
		block(name, new SlabBottomSidedBlock(top, bottom, side), new SlabBlockSpecial("bottom"));
	}

	private void doubleSlabSided(String name, String top, String bottom, String side, String slabTop, String slabBottom) throws IOException
	{
		doubleSlabSided(name, new FullSidedBlock(top, bottom, side), new DoubleSlabSpecial(slabTop, slabBottom));
		item(name, new ItemFromBlock(name), null, name);
	}

	private void doubleSlabSided(String name, IModel model, DoubleSlabSpecial doubleSlabSpecial) throws IOException
	{
		block(name, model, doubleSlabSpecial);
		item(name, new ItemFromBlock(name), null, name);
	}

	private void sidedSlabFamily(String topName, String bottomName, String bothName, String itemName, String topTexture, String bottomTexture, String sideTexture) throws IOException
	{
		topSlabSided(topName, topTexture, bottomTexture, sideTexture);
		bottomSlabSided(bottomName, topTexture, bottomTexture, sideTexture);
		doubleSlabSided(bothName, topTexture, bottomTexture, sideTexture, topName, bottomName);
		item(itemName, new ItemFromBlock(bottomName), new SlabSpecial(topName, bottomName, bothName), bottomName);
	}

	private void sidedSlabFamily(String topName, String bottomName, String bothName, String itemName, String topTexture, String bottomTexture, String slabSideTexture, String blockSideTexture) throws IOException
	{
		topSlabSided(topName, topTexture, bottomTexture, slabSideTexture);
		bottomSlabSided(bottomName, topTexture, bottomTexture, slabSideTexture);
		doubleSlabSided(bothName, topTexture, bottomTexture, blockSideTexture, topName, bottomName);
		item(itemName, new ItemFromBlock(bottomName), new SlabSpecial(topName, bottomName, bothName), bottomName);
	}

	/* Decorative */

	private void decorativeFullSidedBlock(String name, String top, String bottom, String side) throws IOException
	{
		block(name, new FullSidedBlock(top, bottom, side), null);
		item(name, new ItemFromBlock(name), null, name);
	}

	private void decorativeFullBlock(String name) throws IOException
	{
		block(name, new FullBlock(name), null);
		item(name, new ItemFromBlock(name), null, name);
	}

	private void blockWithModel(String name, IModel model) throws IOException
	{
		block(name, model, null);
		item(name, new ItemFromBlock(name), null, name);
	}

	private void blockWithModelSpecial(String name, IModel model, ISpecial special) throws IOException
	{
		block(name, model, special);
		item(name, new ItemFromBlock(name), null, name);
	}

	private void decorativeFullBlock(String name, String texture) throws IOException
	{
		block(name, new FullBlock(texture), null);
		item(name, new ItemFromBlock(name), null, name);
	}

	private void decorativeFullTintedBlock(String name, float r, float g, float b, String texture) throws IOException
	{
		block(name, new TintedBlock(r, g, b, texture), new SimpleSpecial("tinted"));
		item(name, new ItemFromBlock(name), null, name);
	}

	private void decorativeFullLeavesBlock(String name, float r, float g, float b) throws IOException
	{
		block(name, new TintedBlock(r, g, b, name), new SimpleSpecial("leaves"));
		item(name, new ItemFromBlock(name), null, name);
	}

	private void decorativeFullTransparentBlock(String name) throws IOException
	{
		block(name, new FullBlock(name), new SimpleSpecial("transparent"));
		item(name, new ItemFromBlock(name), null, name);
	}

	/* Special Blocks */

	private void fallingFullBlock(String name) throws IOException
	{
		block(name, new FullBlock(name), new SimpleSpecial("gravel"));
		item(name, new ItemFromBlock(name), null, name);
	}

	/* Items */

	private void normalItem(String name) throws IOException
	{
		item(name, new ItemFromTexture(name), null, name);
	}

	private void normalItemFromBlock(String name) throws IOException
	{
		item(name, new ItemFromBlock(name), null, name);
	}

	private void specialItem(String name, ISpecial special) throws IOException
	{
		item(name, new ItemFromTexture(name), special, name);
	}

	private void block(String name, IModel model, ISpecial special) throws IOException
	{
		File block = new File(blocks, name + ".txt");
		block.createNewFile();

		SSS sss = new SSS(block);
		sss.clear();

		sss.add("model", "block/" + name);
		if (special != null)
		{
			sss.add("special", special.getName());
			special.generate(sss);
		}

		sss.save(block);

		save(new File(blockModels, name + ".json"), new JSONObject(model.build()).toString(4));
	}

	private void item(String name, IModel model, ISpecial special, String place) throws IOException
	{
		File item = new File(items, name + ".txt");
		item.createNewFile();

		SSS sss = new SSS(item);
		sss.clear();

		sss.add("model", "item/" + name);
		if (special != null)
		{
			sss.add("special", special.getName());
			special.generate(sss);
		}

		if (place != null)
			sss.add("place", place);

		sss.save(item);

		save(new File(itemModels, name + ".json"), new JSONObject(model.build()).toString(4));
	}


	private void save(File file, String s)
	{
		try (PrintWriter out = new PrintWriter(file))
		{
			out.println(s);

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

}
