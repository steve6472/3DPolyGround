package steve6472.polyground.generator;

import org.json.JSONObject;
import steve6472.SSS;
import steve6472.polyground.generator.models.*;
import steve6472.polyground.generator.special.ISpecial;
import steve6472.polyground.generator.special.SimpleSpecial;
import steve6472.polyground.generator.special.SpecialBuilder;
import steve6472.polyground.world.chunk.ModelLayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.07.2020
 * Project: CaveGame
 *
 ***********************/
public class DataBuilder
{
	private IModel blockModel, itemModel;
	private ISpecial blockSpecial, itemSpecial;
	private String blockToPlace;
	private String blockName, itemName;
	private String blockModelPath, itemModelPath;

	private List<String> blockTags;

	public static DataBuilder create()
	{
		return new DataBuilder();
	}

	private DataBuilder()
	{
		blockModelPath = "";
		itemModelPath = "";
		blockTags = new ArrayList<>();
	}

	public DataBuilder blockModel(IModel model)
	{
		blockModel = model;
		return this;
	}

	public DataBuilder itemModel(IModel model)
	{
		itemModel = model;
		return this;
	}

	public DataBuilder blockName(String name)
	{
		blockName = name;
		return this;
	}

	public DataBuilder itemName(String name)
	{
		itemName = name;
		return this;
	}

	public DataBuilder blockToPlace(String block)
	{
		blockToPlace = block;
		return this;
	}

	public DataBuilder blockSpecial(ISpecial special)
	{
		blockSpecial = special;
		return this;
	}

	public DataBuilder itemSpecial(ISpecial special)
	{
		itemSpecial = special;
		return this;
	}

	public DataBuilder blockModelPath(String path)
	{
		blockModelPath = path + "/";
		return this;
	}

	public DataBuilder itemModelPath(String path)
	{
		itemModelPath = path + "/";
		return this;
	}

	public DataBuilder noItem()
	{
		itemModelPath = "";
		itemSpecial = null;
		itemName = null;
		blockToPlace = null;
		itemModel = null;
		return this;
	}

	public DataBuilder addTag(String tag)
	{
		blockTags.add(tag);
		return this;
	}

	public DataBuilder addTags(String... tags)
	{
		Collections.addAll(blockTags, tags);
		return this;
	}

	/*
	 * Presets
	 */

	public DataBuilder fullLightBlock(String name, String color, float constant, float linear, float quadratic)
	{
		blockModel = BlockModelBuilder.create()
			.addCube(CubeBuilder.create()
				.fullBlock()
				.face(FaceBuilder.create()
					.texture(name)
					.modelLayer(ModelLayer.LIGHT))
			).build();
		blockSpecial(SpecialBuilder.create("light")
			.addValue("color", color)
			.addValue("constant", constant)
			.addValue("linear", linear)
			.addValue("quadratic", quadratic));
		itemModel = new ItemFromBlock(name);
		blockName = name;
		itemName = name;
		blockToPlace = name;
		return this;
	}

	public DataBuilder fullBlock(String name)
	{
		blockModel = new FullBlock(name);
		itemModel = new ItemFromBlock(name);
		blockName = name;
		itemName = name;
		blockToPlace = name;
		return this;
	}

	public DataBuilder transparentFullBlock(String name)
	{
		blockModel = new FullBlock(name);
		itemModel = new ItemFromBlock(name);
		blockName = name;
		itemName = name;
		blockToPlace = name;
		blockSpecial = new SimpleSpecial("transparent");
		addTag("transparent");
		return this;
	}

	public DataBuilder doubleSlabBlock(String name, String top, String bottom)
	{
		blockModel = new FullBlock(name);
		itemModel = new ItemFromBlock(name);
		blockName = name;
		itemName = name;
		blockToPlace = name;
		blockSpecial = SpecialBuilder.create("double_slab").addValue("top", top).addValue("bottom", bottom);
		return this;
	}

	public DataBuilder slabBlock(String name, String texture, boolean isBottom)
	{
		blockModel = BlockModelBuilder.create()
			.addCube(
				CubeBuilder.create()
					.slab(isBottom)
					.face(FaceBuilder.create()
						.texture(texture)
						.autoUv(true)))
			.build();
		blockName = name;
		blockSpecial = SpecialBuilder.create("slab").addValue("type", isBottom ? "bottom" : "top");
		addTags("slab", isBottom ? "slab_bottom" : "slab_top");
		return this;
	}

	public DataBuilder slabItem(String name, String top, String bottom, String both)
	{
		itemSpecial = SpecialBuilder.create("slab")
			.addValue("top", top)
			.addValue("bottom", bottom)
			.addValue("both", both)
			.build();

		itemModel = new ItemFromBlock(bottom);
		itemName = name;
		blockToPlace = bottom;

		return this;
	}

	public DataBuilder stalaBlock(String name, String texture, float width)
	{
		width /= 2f;
		blockModel = BlockModelBuilder.create()
			.addCube(
				CubeBuilder.create()
					.min(8 - width, 0, 8 - width)
					.max(8 + width, 16, 8 + width)
					.face(FaceBuilder.create()
						.texture(texture)
						.autoUv(true))
			).build();
		blockSpecial = new SimpleSpecial("custom");

		blockToPlace = name;
		blockName = name;
		itemName = name;
		itemModel = new ItemFromBlock(name);

		return this;
	}

	/*
	 * Generation
	 */

	public void generate()
	{
		try
		{
			if (blockName != null && !blockName.isBlank())
				block();
		} catch (IOException e)
		{
			System.err.println("Error at generating block " + blockName);
			e.printStackTrace();
		}
		try
		{
			if (itemName != null && !itemName.isBlank())
				item();
		} catch (IOException e)
		{
			System.err.println("Error at generating item " + itemName);
			e.printStackTrace();
		}
		System.out.println();
	}

	private void item() throws IOException
	{
		System.out.println("Generating item " + itemName);
		File item = new File(DataGenerator.items, itemName + ".txt");
		item.createNewFile();

		SSS sss = new SSS(item);
		sss.clear();

		sss.add("model", "item/" + itemModelPath + itemName);
		if (itemSpecial != null)
		{
			System.out.println("\tWith Special \"" + itemSpecial.getName() + "\"");
			sss.add("special", itemSpecial.getName());
			itemSpecial.generate(sss);
		}

		if (blockToPlace != null)
		{
			System.out.println("\tPlaces " + blockToPlace);
			sss.add("place", blockToPlace);
		}

		if (!itemModelPath.isBlank())
		{
			System.out.println("\tWith path " + itemModelPath.substring(0, itemModelPath.length() - 1));
			File f = new File(DataGenerator.itemModels, itemModelPath.substring(0, itemModelPath.length() - 1));
			if (!f.exists())
			{
				if (f.mkdirs())
				{
					System.out.println("\tCreated new directory");
				}
			}
		}

		sss.save(item);

		save(new File(DataGenerator.itemModels, itemModelPath + itemName + ".json"), new JSONObject(itemModel.build()).toString(4));
	}

	private void block() throws IOException
	{
		System.out.println("Generating block " + blockName);
		File block = new File(DataGenerator.blocks, blockName + ".txt");
		block.createNewFile();

		SSS sss = new SSS(block);
		sss.clear();

		sss.add("model", "block/" + blockModelPath + blockName);
		if (blockSpecial != null)
		{
			System.out.println("\tWith Special \"" + blockSpecial.getName() + "\"");
			sss.add("special", blockSpecial.getName());
			blockSpecial.generate(sss);
		}

		if (!blockTags.isEmpty())
		{
			System.out.println("\tWith tags: ");
			for (String tag : blockTags)
			{
				System.out.println("\t\t" + tag);
			}
			sss.addArray("tags", blockTags);
		}

		sss.save(block);

		if (!blockModelPath.isBlank())
		{
			System.out.println("\tWith path " + blockModelPath.substring(0, blockModelPath.length() - 1));
			File f = new File(DataGenerator.blockModels, blockModelPath.substring(0, blockModelPath.length() - 1));
			if (!f.exists())
			{
				if (f.mkdirs())
				{
					System.out.println("\tCreated new directory");
				}
			}
		}

		save(new File(DataGenerator.blockModels, blockModelPath + blockName + ".json"), new JSONObject(blockModel.build()).toString(4));
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
