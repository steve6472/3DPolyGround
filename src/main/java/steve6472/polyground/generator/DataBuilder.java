package steve6472.polyground.generator;

import org.json.JSONObject;
import steve6472.SSS;
import steve6472.polyground.EnumFace;
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

	private final List<String> blockTags;

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

	public DataBuilder plusBlock(String name, boolean biomeTint)
	{
		blockModel = BlockModelBuilder.create()
			.addCube(CubeBuilder.create()
				.fullBlock()
				.collisionBox(false)
			).addCube(CubeBuilder.create()
				.collisionBox(false)
				.hitbox(false)
				.min(8, 0, 0)
				.max(8, 16, 16)
				.face(FaceBuilder.create().texture(name).biomeTint(biomeTint), EnumFace.SOUTH, EnumFace.NORTH)
			).addCube(CubeBuilder.create()
				.collisionBox(false)
				.hitbox(false)
				.min(0, 0, 8)
				.max(16, 16, 8)
				.face(FaceBuilder.create().texture(name).biomeTint(biomeTint), EnumFace.EAST, EnumFace.WEST)
			).build();
		blockSpecial = new SimpleSpecial("custom");
		itemModel = new ItemFromTexture(name);
		blockName = name;
		itemName = name;
		blockToPlace = name;
		return this;
	}

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

	public DataBuilder oreBlock(String name, String baseTexture, String overlayTexture)
	{
		return oreBlock(name, baseTexture, overlayTexture, 255, 255, 255);
	}

	public DataBuilder oreBlock(String name, String baseTexture, String overlayTexture, float red, float green, float blue)
	{
		return DataBuilder.create().fullBlock(name)
			.blockModel(BlockModelBuilder.create()
				.addCube(CubeBuilder.create()
					.fullBlock()
					.face(FaceBuilder.create().texture(baseTexture))
				).addCube(CubeBuilder.create()
					.fullBlock()
					.face(FaceBuilder.create()
						.texture(overlayTexture)
						.tint(red, green, blue)
						.modelLayer(ModelLayer.OVERLAY_0))
				).build());
	}

	public DataBuilder lightOreBlock(String name, String baseTexture, String overlayTexture, float red, float green, float blue, String lightColor, float constant, float linear, float quadratic)
	{
		return DataBuilder.create().fullBlock(name)
			.blockSpecial(SpecialBuilder.create("light")
				.addValue("color", lightColor)
				.addValue("constant", constant)
				.addValue("linear", linear)
				.addValue("quadratic", quadratic))
			.blockModel(BlockModelBuilder.create()
				.addCube(CubeBuilder.create()
					.fullBlock()
					.face(FaceBuilder.create()
						.texture(baseTexture))
				).addCube(CubeBuilder.create()
					.fullBlock()
					.face(FaceBuilder.create()
						.texture(overlayTexture)
						.tint(red, green, blue)
						.modelLayer(ModelLayer.EMISSION_OVERLAY))
				).build());
	}

	public DataBuilder logBlock(String name, String sideTexture, String topTexture)
	{
		return DataBuilder.create().fullBlock(name)
			.blockModel(BlockModelBuilder.create()
				.addCube(CubeBuilder.create()
					.fullBlock()
					.face(FaceBuilder.create().texture(sideTexture), CubeBuilder.SIDE)
					.face(FaceBuilder.create().texture(topTexture), CubeBuilder.TOP_BOTTOM)).build());
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

	public void stairs(String name, String texture)
	{
		DataBuilder.create().stairBlock(name + "_stairs_north", texture, EnumFace.NORTH).blockModelPath("stairs/" + name).generate();
		DataBuilder.create().stairBlock(name + "_stairs_east", texture, EnumFace.EAST).blockModelPath("stairs/" + name).generate();
		DataBuilder.create().stairBlock(name + "_stairs_south", texture, EnumFace.SOUTH).blockModelPath("stairs/" + name).generate();
		DataBuilder.create().stairBlock(name + "_stairs_west", texture, EnumFace.WEST).blockModelPath("stairs/" + name).generate();
		DataBuilder.create().orientableItem(name + "_stairs", name + "_stairs_north", name + "_stairs_east", name + "_stairs_south", name + "_stairs_west").itemModel(new ItemFromBlock(name + "_stairs_north")).generate();
	}

	public void slabs(String name, String texture)
	{
		DataBuilder.create().doubleSlabBlock(name, name + "_top", name + "_bottom").generate();
		DataBuilder.create().slabBlock(name + "_bottom", texture, true).generate();
		DataBuilder.create().slabBlock(name + "_top", texture, false).generate();
		DataBuilder.create().slabItem(name + "_slab", name + "_top", name + "_bottom", name).generate();
	}

	public DataBuilder stairBlock(String name, String texture, EnumFace face)
	{
		FaceBuilder tex = FaceBuilder.create().texture(texture);
		CubeBuilder top = switch (face)
			{
				case NORTH -> CubeBuilder.create().northStairTop().face(tex);
				case EAST -> CubeBuilder.create().eastStairTop().face(tex);
				case SOUTH -> CubeBuilder.create().southStairTop().face(tex);
				case WEST -> CubeBuilder.create().westStairTop().face(tex);
				default -> throw new IllegalStateException("Unexpected value: " + face);
			};
		blockModel = BlockModelBuilder.create()
			.addCube(CubeBuilder.create()
				.bottomSlab()
				.face(tex)
			)
			.addCube(top)
			.build();
		blockName = name;
		blockSpecial = new SimpleSpecial("stair");

		return this;
	}

	public DataBuilder orientableItem(String name, String north, String east, String south, String west)
	{
		itemName = name;
		itemSpecial = SpecialBuilder.create("orientable")
			.addValue("north", north)
			.addValue("east", east)
			.addValue("south", south)
			.addValue("west", west);
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

	public DataBuilder doubleSlabBlock(String name, String topTexture, String sideTexture, String top, String bottom)
	{
		blockModel = BlockModelBuilder.create()
			.addCube(CubeBuilder.create()
				.fullBlock()
				.face(FaceBuilder.create().texture(sideTexture), CubeBuilder.SIDE)
				.face(FaceBuilder.create().texture(topTexture), CubeBuilder.TOP_BOTTOM)).build();
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

	public DataBuilder slabBlock(String name, String topTexture, String sideTexture, boolean isBottom)
	{
		blockModel = BlockModelBuilder.create()
			.addCube(
				CubeBuilder.create()
					.slab(isBottom)
					.face(FaceBuilder.create().texture(topTexture), CubeBuilder.TOP_BOTTOM)
					.face(FaceBuilder.create().texture(sideTexture), CubeBuilder.SIDE))
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
