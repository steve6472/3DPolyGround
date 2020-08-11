package steve6472.polyground.generator;

import org.json.JSONObject;
import steve6472.SSS;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.properties.enums.EnumAxis;
import steve6472.polyground.block.properties.enums.EnumSlabType;
import steve6472.polyground.block.special.PilliarBlock;
import steve6472.polyground.block.special.SlabBlock;
import steve6472.polyground.block.special.StairBlock;
import steve6472.polyground.generator.models.*;
import steve6472.polyground.generator.special.ISpecial;
import steve6472.polyground.generator.special.SimpleSpecial;
import steve6472.polyground.generator.special.SpecialBuilder;
import steve6472.polyground.generator.state.PropertyBuilder;
import steve6472.polyground.generator.state.StateBuilder;
import steve6472.polyground.world.chunk.ModelLayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.07.2020
 * Project: CaveGame
 *
 ***********************/
public class DataBuilder
{
	private IModel itemModel;
	private ISpecial blockSpecial, itemSpecial;
	private String blockToPlace;
	private String blockName, itemName;
	private String itemModelPath;
	public StateBuilder blockState;

	public static DataBuilder create()
	{
		return new DataBuilder();
	}

	private DataBuilder()
	{
		itemModelPath = "";
	}

	public DataBuilder blockState(StateBuilder state)
	{
		this.blockState = state;
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

	/*
	 * Post-gen additions
	 */


	public StateGetter addTagToState()
	{
		return new StateGetter(this);
	}

	public DataBuilder addTag(String tag)
	{
		blockState.tag(tag);
		return this;
	}

	public DataBuilder addTags(String... tags)
	{
		blockState.tags(tags);
		return this;
	}

	/*
	 * Presets
	 */
/*
	public DataBuilder torch(String name, boolean light)
	{
		blockName = name;
		blockModel = BlockModelBuilder.create()
			.addCube(CubeBuilder.create()
				.torch()
				.collisionBox(false)
				.face(FaceBuilder.create()
						.texture(name)
						.modelLayer(light ? ModelLayer.LIGHT : ModelLayer.NORMAL),
					CubeBuilder.SIDE)
				.face(FaceBuilder.create()
						.texture(name)
						.modelLayer(light ? ModelLayer.LIGHT : ModelLayer.NORMAL)
						.uv(7, 6, 9, 8)
					, EnumFace.UP)
				.face(FaceBuilder.create()
						.texture(name)
						.modelLayer(light ? ModelLayer.LIGHT : ModelLayer.NORMAL)
						.uv(7, 11, 9, 13)
					,EnumFace.DOWN)
			).build();
		itemName = name;
		itemModel = new ItemFromBlock(name);
		blockToPlace = name;
		blockSpecial = new SimpleSpecial("custom");

		return this;
	}*/

	public DataBuilder plusBlock(String name, boolean biomeTint)
	{
		blockState = StateBuilder.create()
			.singleModel(BlockModelBuilder.create(name)
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
				)
			);
		blockSpecial = new SimpleSpecial("custom");
		itemModel = new ItemFromTexture(name);
		blockName = name;
		itemName = name;
		blockToPlace = name;
		return this;
	}

	public DataBuilder fullLightBlock(String name, String color, float constant, float linear, float quadratic)
	{
		blockState = StateBuilder.create()
			.singleModel(BlockModelBuilder.create(name)
					.addCube(CubeBuilder.create()
						.fullBlock()
						.face(FaceBuilder.create()
							.texture(name)
							.modelLayer(ModelLayer.LIGHT))));
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
			.blockState(StateBuilder.create()
				.singleModel(BlockModelBuilder.create(name)
					.addCube(CubeBuilder.create()
						.fullBlock()
						.face(FaceBuilder.create().texture(baseTexture))
					).addCube(CubeBuilder.create()
						.fullBlock()
						.face(FaceBuilder.create()
							.texture(overlayTexture)
							.tint(red, green, blue)
							.modelLayer(ModelLayer.OVERLAY_0))
					)
				)
			);
	}

	public DataBuilder lightOreBlock(String name, String baseTexture, String overlayTexture, float red, float green, float blue, String lightColor, float constant, float linear, float quadratic)
	{
		return DataBuilder.create().fullBlock(name)
			.blockSpecial(SpecialBuilder.create("light")
				.addValue("color", lightColor)
				.addValue("constant", constant)
				.addValue("linear", linear)
				.addValue("quadratic", quadratic))
			.blockState(StateBuilder.create()
				.singleModel(BlockModelBuilder.create(name)
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
					)
				)
			);
	}

	public DataBuilder pillarBlock(String name, String sideTexture, String topTexture)
	{
		return DataBuilder.create().fullBlock(name)
			.blockSpecial(new SimpleSpecial("pilliar"))
			.blockState(StateBuilder.create()
				.addState(PropertyBuilder.create()
						.addProperty(PilliarBlock.AXIS, EnumAxis.Y),
					BlockModelBuilder.create(name)
						.addCube(CubeBuilder.create()
							.fullBlock()
							.face(FaceBuilder.create().texture(sideTexture), CubeBuilder.SIDE)
							.face(FaceBuilder.create().texture(topTexture), CubeBuilder.TOP_BOTTOM)
						)
				)
				.addState(PropertyBuilder.create()
					.addProperty(PilliarBlock.AXIS, EnumAxis.X),
					BlockModelBuilder.create(name + "_side")
						.addCube(CubeBuilder.create()
							.fullBlock()
							.face(FaceBuilder.create().texture(sideTexture), EnumFace.UP, EnumFace.DOWN)
							.face(FaceBuilder.create().texture(sideTexture).rotation(90), EnumFace.EAST, EnumFace.WEST)
							.face(FaceBuilder.create().texture(topTexture), EnumFace.NORTH, EnumFace.SOUTH)
						)
				).addState(PropertyBuilder.create().addProperty(PilliarBlock.AXIS, EnumAxis.Z).rotation(90), BlockModelBuilder.noGen(name + "_side"))
			);
	}

	public DataBuilder fullBlock(String name)
	{
		blockState = StateBuilder.create()
			.singleModel(BlockModelBuilder.create(name)
				.addCube(CubeBuilder.create()
					.fullBlock().face(FaceBuilder.create()
						.texture(name))));
		itemModel = new ItemFromBlock(name);
		blockName = name;
		itemName = name;
		blockToPlace = name;
		return this;
	}

	public DataBuilder fullBlock(String name, String texture)
	{
		blockState = StateBuilder.create()
			.singleModel(BlockModelBuilder.create(name)
				.addCube(CubeBuilder.create()
					.fullBlock().face(FaceBuilder.create()
						.texture(texture))));
		itemModel = new ItemFromBlock(name);
		blockName = name;
		itemName = name;
		blockToPlace = name;
		return this;
	}

	public DataBuilder fullBlock(String name, String textureTop, String textureSide, String textureBottom)
	{
		blockState = StateBuilder.create()
			.singleModel(BlockModelBuilder.create(name)
				.addCube(CubeBuilder.create()
					.fullBlock()
					.face(FaceBuilder.create()
						.texture(textureTop), EnumFace.UP)
					.face(FaceBuilder.create()
						.texture(textureSide), CubeBuilder.SIDE)
					.face(FaceBuilder.create()
						.texture(textureBottom), EnumFace.DOWN)
				));
		itemModel = new ItemFromBlock(name);
		blockName = name;
		itemName = name;
		blockToPlace = name;
		return this;
	}

	public DataBuilder leaves(String name)
	{
		return leaves(name, true);
	}

	public DataBuilder leaves(String name, boolean biomeTint)
	{
		return DataBuilder.create().fullBlock(name)
			.blockSpecial(new SimpleSpecial("leaves"))
			.blockState(StateBuilder.create().singleModel(
				BlockModelBuilder.create(name)
					.addCube(CubeBuilder.create()
						.fullBlock()
						.face(FaceBuilder.create().texture(name).biomeTint(biomeTint))
					)
				)
			);
	}

	public DataBuilder slab(String name, String texture)
	{
		return DataBuilder.create()
			.blockName(name)
			.itemName(name)
			.blockToPlace(name)
			.blockSpecial(new SimpleSpecial("slab"))
			.itemModel(new ItemFromBlock(name))
			.itemSpecial(new SimpleSpecial("slab"))
			.blockState(StateBuilder.create()
				.addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.BOTTOM)
						.addProperty(SlabBlock.AXIS, EnumAxis.Y),
					BlockModelBuilder.create(name + "_bottom")
						.modelPath("slab")
						.addCube(CubeBuilder.create()
							.bottomSlab()
							.face(FaceBuilder.create()
								.texture(texture)
								.uvlock(true))
						)
				).addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.TOP)
						.addProperty(SlabBlock.AXIS, EnumAxis.Y),
					BlockModelBuilder.create(name + "_top")
						.modelPath("slab")
						.addCube(CubeBuilder.create()
							.topSlab()
							.face(FaceBuilder.create()
								.texture(texture)
								.uvlock(true))
						)
				).addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.DOUBLE)
						.addProperty(SlabBlock.AXIS, EnumAxis.Y),
					BlockModelBuilder.create(name + "_double")
						.modelPath("slab")
						.addCube(CubeBuilder.create()
							.fullBlock()
							.face(FaceBuilder.create()
								.texture(texture)
								.uvlock(true))
						)
				).addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.BOTTOM)
						.addProperty(SlabBlock.AXIS, EnumAxis.X),
					BlockModelBuilder.create(name + "_side")
						.modelPath("slab")
						.addCube(CubeBuilder.create()
							.min(0, 0, 0)
							.max(8, 16, 16)
							.face(FaceBuilder.create()
								.texture(texture)
								.uvlock(true))
						)
				).addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.TOP)
						.addProperty(SlabBlock.AXIS, EnumAxis.X).rotation(180),
					BlockModelBuilder.noGen(name + "_side").modelPath("slab")
				).addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.BOTTOM)
						.addProperty(SlabBlock.AXIS, EnumAxis.Z).rotation(90),
					BlockModelBuilder.noGen(name + "_side").modelPath("slab")
				).addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.TOP)
						.addProperty(SlabBlock.AXIS, EnumAxis.Z).rotation(270),
					BlockModelBuilder.noGen(name + "_side").modelPath("slab")
				).addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.DOUBLE)
						.addProperty(SlabBlock.AXIS, EnumAxis.Z),
					BlockModelBuilder.noGen(name + "_double").modelPath("slab")
				).addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.DOUBLE)
						.addProperty(SlabBlock.AXIS, EnumAxis.X),
					BlockModelBuilder.noGen(name + "_double").modelPath("slab")
				)
			);
	}

	public DataBuilder stairs(String name, String texture)
	{
		return DataBuilder.create()
			.blockName(name)
			.itemName(name)
			.blockToPlace(name)
			.blockSpecial(new SimpleSpecial("stairs"))
			.itemModel(new ItemFromBlock(name))
			.blockState(StateBuilder.create()
				.addState(PropertyBuilder.create().addProperty(StairBlock.FACING, EnumFace.NORTH), BlockModelBuilder.create(name)
					.addCube(CubeBuilder.create()
						.bottomSlab()
						.face(FaceBuilder.create()
							.texture(texture)
							.uvlock(true)
						)
					)
					.addCube(CubeBuilder.create()
						.stairTop()
						.face(FaceBuilder.create()
							.texture(texture)
							.uvlock(true)
							, EnumFace.UP, EnumFace.NORTH, EnumFace.EAST, EnumFace.SOUTH, EnumFace.WEST
						)
					)
				)
				.addState(PropertyBuilder.create().addProperty(StairBlock.FACING, EnumFace.EAST).rotation(270), BlockModelBuilder.noGen(name))
				.addState(PropertyBuilder.create().addProperty(StairBlock.FACING, EnumFace.SOUTH).rotation(180), BlockModelBuilder.noGen(name))
				.addState(PropertyBuilder.create().addProperty(StairBlock.FACING, EnumFace.WEST).rotation(90), BlockModelBuilder.noGen(name))
			);
	}

	public DataBuilder transparentFullBlock(String name)
	{
		fullBlock(name);
		itemModel = new ItemFromBlock(name);
		blockName = name;
		itemName = name;
		blockToPlace = name;
		blockSpecial = new SimpleSpecial("transparent");
		addTag("transparent");
		return this;
	}
/*
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
	}*/

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
		File item = new File(DataGenerator.ITEMS, itemName + ".txt");
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
			File f = new File(DataGenerator.ITEM_MODELS, itemModelPath.substring(0, itemModelPath.length() - 1));
			if (!f.exists())
			{
				if (f.mkdirs())
				{
					System.out.println("\tCreated new directory");
				}
			}
		}

		sss.save(item);

		save(new File(DataGenerator.ITEM_MODELS, itemModelPath + itemName + ".json"), new JSONObject(itemModel.build()).toString(4));
	}

	private void block() throws IOException
	{
		System.out.println("Generating block " + blockName);
		File block = new File(DataGenerator.BLOCKS, blockName + ".txt");
		block.createNewFile();

		SSS sss = new SSS(block);
		sss.clear();

		sss.add("blockstate", blockName);
		if (blockSpecial != null)
		{
			System.out.println("\tWith Special \"" + blockSpecial.getName() + "\"");
			sss.add("special", blockSpecial.getName());
			blockSpecial.generate(sss);
		}

		sss.save(block);

		blockState.generate(blockName);
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
