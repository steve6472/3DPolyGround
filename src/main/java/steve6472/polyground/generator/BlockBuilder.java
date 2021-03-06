package steve6472.polyground.generator;

import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.PrettyJson;
import steve6472.polyground.block.Tags;
import steve6472.polyground.block.model.CubeTags;
import steve6472.polyground.block.properties.enums.EnumAxis;
import steve6472.polyground.block.properties.enums.EnumHalf;
import steve6472.polyground.block.properties.enums.EnumSlabType;
import steve6472.polyground.block.special.PillarBlock;
import steve6472.polyground.block.special.SlabBlock;
import steve6472.polyground.block.special.StairBlock;
import steve6472.polyground.block.special.StalaBlock;
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
import java.util.function.Function;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.07.2020
 * Project: CaveGame
 *
 ***********************/
public class BlockBuilder
{
	private ISpecial blockSpecial;
	private String blockName;
	private JSONArray placerGroupPaths;
	public StateBuilder blockState;

	public static BlockBuilder create()
	{
		return new BlockBuilder();
	}

	private BlockBuilder()
	{
		placerGroupPaths = new JSONArray();
	}

	public BlockBuilder blockState(StateBuilder state)
	{
		this.blockState = state;
		return this;
	}

	public BlockBuilder blockName(String name)
	{
		blockName = name;
		return this;
	}

	public BlockBuilder placerGroupPath(String groupPath)
	{
		placerGroupPaths.put(groupPath);
		return this;
	}

	public BlockBuilder blockSpecial(ISpecial special)
	{
		blockSpecial = special;
		return this;
	}

	/*
	 * Post-gen additions
	 */


	public StateGetter addTagToState()
	{
		return new StateGetter(this);
	}

	public BlockBuilder addTag(String tag)
	{
		blockState.tag(tag);
		return this;
	}

	public BlockBuilder addTags(String... tags)
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

	public BlockBuilder plusBlock(String name, boolean biomeTint)
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
					.face(FaceBuilder.create().texture("block/" + name).biomeTint(biomeTint), EnumFace.SOUTH, EnumFace.NORTH)
				).addCube(CubeBuilder.create()
					.collisionBox(false)
					.hitbox(false)
					.min(0, 0, 8)
					.max(16, 16, 8)
					.face(FaceBuilder.create().texture("block/" + name).biomeTint(biomeTint), EnumFace.EAST, EnumFace.WEST)
				)
			);
		blockSpecial = new SimpleSpecial("custom");
		blockName = name;
		return this;
	}

	public BlockBuilder fullLightBlock(String name, String color, float constant, float linear, float quadratic)
	{
		return fullLightBlock(name, color, constant, linear, quadratic, 0, -1, 0, -60);
	}

	public BlockBuilder fullLightBlock(String name, String color, float constant, float linear, float quadratic, float dirX, float dirY, float dirZ, float cutOff)
	{
		blockState = StateBuilder.create()
			.singleModel(BlockModelBuilder.create(name)
					.addCube(CubeBuilder.create()
						.fullBlock()
						.face(FaceBuilder.create()
							.texture("block/" + name)
							.modelLayer(ModelLayer.LIGHT))));
		blockSpecial(SpecialBuilder.create("light")
			.addValue("color", color)
			.addValue("constant", constant)
			.addValue("linear", linear)
			.addValue("quadratic", quadratic)
			.addValue("dirX", dirX)
			.addValue("dirY", dirY)
			.addValue("dirZ", dirZ)
			.addValue("cutOff", cutOff));
		blockName = name;
		return this;
	}

	public BlockBuilder oreBlock(String name, String baseTexture, String overlayTexture)
	{
		return oreBlock(name, baseTexture, overlayTexture, 255, 255, 255);
	}

	public BlockBuilder oreBlock(String name, String baseTexture, String overlayTexture, float red, float green, float blue)
	{
		return BlockBuilder.create().fullBlock(name)
			.blockState(StateBuilder.create()
				.singleModel(BlockModelBuilder.create(name)
					.addCube(CubeBuilder.create()
						.fullBlock()
						.face(FaceBuilder.create().texture("block/" + baseTexture))
					).addCube(CubeBuilder.create()
						.fullBlock()
						.face(FaceBuilder.create()
							.texture("block/" + overlayTexture)
							.tint(red, green, blue)
							.modelLayer(ModelLayer.OVERLAY_0))
					)
				)
			);
	}

	public BlockBuilder lightOreBlock(String name, String baseTexture, String overlayTexture, float red, float green, float blue, String lightColor, float constant, float linear, float quadratic)
	{
		baseTexture = "block/" + baseTexture;
		overlayTexture = "block/" + overlayTexture;

		return BlockBuilder.create().fullBlock(name)
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

	public BlockBuilder pillarBlock(String name, String sideTexture, String topTexture)
	{
		sideTexture = "block/" + sideTexture;
		topTexture = "block/" + topTexture;

		return BlockBuilder.create().fullBlock(name)
			.blockSpecial(new SimpleSpecial("pilliar"))
			.blockState(StateBuilder.create()
				.addState(PropertyBuilder.create()
						.addProperty(PillarBlock.AXIS, EnumAxis.Y),
					BlockModelBuilder.create(name)
						.addCube(CubeBuilder.create()
							.fullBlock()
							.face(FaceBuilder.create().texture(sideTexture), CubeBuilder.SIDE)
							.face(FaceBuilder.create().texture(topTexture), CubeBuilder.TOP_BOTTOM)
						)
				)
				.addState(PropertyBuilder.create()
					.addProperty(PillarBlock.AXIS, EnumAxis.X),
					BlockModelBuilder.create(name + "_side")
						.addCube(CubeBuilder.create()
							.fullBlock()
							.face(FaceBuilder.create().texture(sideTexture), EnumFace.UP, EnumFace.DOWN)
							.face(FaceBuilder.create().texture(sideTexture).rotation(90), EnumFace.EAST, EnumFace.WEST)
							.face(FaceBuilder.create().texture(topTexture), EnumFace.NORTH, EnumFace.SOUTH)
						)
				).addState(PropertyBuilder.create().addProperty(PillarBlock.AXIS, EnumAxis.Z).rot(0, 90, 0), BlockModelBuilder.noGen(name + "_side"))
			);
	}

	public BlockBuilder fullBlock(String name)
	{
		blockState = StateBuilder.create()
			.singleModel(BlockModelBuilder.create(name)
				.addCube(CubeBuilder.create()
					.fullBlock().face(FaceBuilder.create()
						.texture("block/" + name))));
		blockName = name;
		return this;
	}

	public BlockBuilder fullBlock(String name, String texture)
	{
		blockState = StateBuilder.create()
			.singleModel(BlockModelBuilder.create(name)
				.addCube(CubeBuilder.create()
					.fullBlock().face(FaceBuilder.create()
						.texture("block/" + texture))));
		blockName = name;
		return this;
	}

	public BlockBuilder fullBlock(String name, String textureTop, String textureSide, String textureBottom)
	{
		blockState = StateBuilder.create()
			.singleModel(BlockModelBuilder.create(name)
				.addCube(CubeBuilder.create()
					.fullBlock()
					.face(FaceBuilder.create()
						.texture("block/" + textureTop), EnumFace.UP)
					.face(FaceBuilder.create()
						.texture("block/" + textureSide), CubeBuilder.SIDE)
					.face(FaceBuilder.create()
						.texture("block/" + textureBottom), EnumFace.DOWN)
				));
		blockName = name;
		return this;
	}

	public BlockBuilder leaves(String name)
	{
		return leaves(name, name, true);
	}

	public BlockBuilder leaves(String name, String texture)
	{
		return leaves(name, texture, true);
	}

	public BlockBuilder leaves(String name, String texture, boolean biomeTint)
	{
		return BlockBuilder.create().fullBlock(name)
			.blockSpecial(new SimpleSpecial("leaves"))
			.blockState(StateBuilder.create().singleModel(
				BlockModelBuilder.create(name)
					.addCube(CubeBuilder.create()
						.fullBlock()
						.face(FaceBuilder.create().texture("block/" + texture).biomeTint(biomeTint))
					)
				)
			);
	}

	public BlockBuilder slab(String name, String texture)
	{
		return BlockBuilder.create()
			.blockName(name)
			.blockSpecial(new SimpleSpecial("slab"))
//			.itemSpecial(SpecialBuilder.create("slab").addValue("block", name))
			.blockState(StateBuilder.create()
				.addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.BOTTOM)
						.addProperty(SlabBlock.AXIS, EnumAxis.Y)
						.tag(Tags.KILL_SPREAD_BOTTOM),
					BlockModelBuilder.create(name + "_bottom")
						.modelPath("slab")
						.addCube(CubeBuilder.create()
							.bottomSlab()
							.face(FaceBuilder.create()
								.texture("block/" + texture)
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
								.texture("block/" + texture)
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
								.texture("block/" + texture)
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
								.texture("block/" + texture)
								.uvlock(true))
						)
				).addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.TOP)
						.addProperty(SlabBlock.AXIS, EnumAxis.X).rot(0, 180, 0),
					BlockModelBuilder.noGen(name + "_side").modelPath("slab")
				).addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.BOTTOM)
						.addProperty(SlabBlock.AXIS, EnumAxis.Z).rot(0, 90, 0),
					BlockModelBuilder.noGen(name + "_side").modelPath("slab")
				).addState(PropertyBuilder.create()
						.addProperty(SlabBlock.TYPE, EnumSlabType.TOP)
						.addProperty(SlabBlock.AXIS, EnumAxis.Z).rot(0, 270, 0),
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

	public BlockBuilder stairs(String name, String texture)
	{
		return BlockBuilder.create()
			.blockName(name)
			.blockSpecial(new SimpleSpecial("stairs"))
			.blockState(StateBuilder.create()
				.addState(PropertyBuilder.create()
						.addProperty(StairBlock.FACING, EnumFace.NORTH)
						.addProperty(StairBlock.HALF, EnumHalf.BOTTOM)
						.uvLock(true)
						.tag(Tags.KILL_SPREAD_BOTTOM)
					, BlockModelBuilder.create(name)
						.addCube(CubeBuilder.create()
							.bottomSlab()
							.name(CubeTags.STAIR_BOTTOM)
							.face(FaceBuilder.create()
								.texture("block/" + texture)
								.uvlock(true)
							)
						)
						.addCube(CubeBuilder.create()
							.stairTop()
							.face(FaceBuilder.create()
									.texture("block/" + texture)
									.uvlock(true)
								, EnumFace.UP, EnumFace.NORTH, EnumFace.EAST, EnumFace.SOUTH, EnumFace.WEST
							)
						)
				)
				.addState(PropertyBuilder.create().addProperty(StairBlock.FACING, EnumFace.EAST).addProperty(StairBlock.HALF, EnumHalf.BOTTOM).tag(Tags.KILL_SPREAD_BOTTOM).uvLock(true).rot(0, 270, 0), BlockModelBuilder.noGen(name))
				.addState(PropertyBuilder.create().addProperty(StairBlock.FACING, EnumFace.SOUTH).addProperty(StairBlock.HALF, EnumHalf.BOTTOM).tag(Tags.KILL_SPREAD_BOTTOM).uvLock(true).rot(0, 180, 0), BlockModelBuilder.noGen(name))
				.addState(PropertyBuilder.create().addProperty(StairBlock.FACING, EnumFace.WEST).addProperty(StairBlock.HALF, EnumHalf.BOTTOM).tag(Tags.KILL_SPREAD_BOTTOM).uvLock(true).rot(0, 90, 0), BlockModelBuilder.noGen(name))
				.addState(PropertyBuilder.create().addProperty(StairBlock.FACING, EnumFace.NORTH).addProperty(StairBlock.HALF, EnumHalf.TOP).uvLock(true).rot(0, 180, 180), BlockModelBuilder.noGen(name))
				.addState(PropertyBuilder.create().addProperty(StairBlock.FACING, EnumFace.EAST).addProperty(StairBlock.HALF, EnumHalf.TOP).uvLock(true).rot(0, 90, 180), BlockModelBuilder.noGen(name))
				.addState(PropertyBuilder.create().addProperty(StairBlock.FACING, EnumFace.SOUTH).addProperty(StairBlock.HALF, EnumHalf.TOP).uvLock(true).rot(0, 0, 180), BlockModelBuilder.noGen(name))
				.addState(PropertyBuilder.create().addProperty(StairBlock.FACING, EnumFace.WEST).addProperty(StairBlock.HALF, EnumHalf.TOP).uvLock(true).rot(0, 270, 180), BlockModelBuilder.noGen(name))
			);
	}

	public BlockBuilder transparentFullBlock(String name)
	{
		fullBlock(name);
		blockName = name;
		blockSpecial = new SimpleSpecial("transparent");
		addTag(Tags.TRANSPARENT);
		return this;
	}

	public BlockBuilder stala(String name, String texture, String path)
	{
		Function<Integer, PropertyBuilder> state = width -> PropertyBuilder.create().addProperty(StalaBlock.WIDTH, width);
		Function<Integer, BlockModelBuilder> model = width -> BlockModelBuilder.create("stala_" + width)
		.modelPath("stala/" + path)
		.addCube(CubeBuilder.create()
			.min(8 - width, 0, 8 - width)
			.max(8 + width, 16, 8 + width)
			.face(FaceBuilder.create()
				.texture("block/" + texture)
				.autoUv(true))
		);

		return BlockBuilder.create()
			.blockName(name)
			.blockSpecial(new SimpleSpecial("stala"))
			.blockState(StateBuilder.create()
				.addState(state.apply(1), model.apply(1))
				.addState(state.apply(2), model.apply(2))
				.addState(state.apply(3), model.apply(3))
				.addState(state.apply(4), model.apply(4))
				.addState(state.apply(5), model.apply(5))
				.addState(state.apply(6), model.apply(6))
				.addState(state.apply(7), model.apply(7))
			);
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
		System.out.println();
	}

	private void block() throws IOException
	{
		System.out.println("Generating block " + blockName);
		File block = new File(DataGenerator.BLOCKS, blockName + ".json");
		if (block.createNewFile())
		{
			System.out.println("Created block " + block.getPath());
		}

		JSONObject json = new JSONObject();
		json.put("blockstate", blockName);
		json.put("name", blockName);

		if (!placerGroupPaths.isEmpty())
			json.put("groups", placerGroupPaths);

		if (blockSpecial != null)
		{
			System.out.println("\tWith Special \"" + blockSpecial.getName() + "\"");
			JSONObject special = blockSpecial.generate();
			if (special != null)
			{
				json.put("special", special);
			}
			System.out.println(PrettyJson.prettify(special));
		}

		save(block, json);

		blockState.generate(blockName);
	}

	public static void save(File file, JSONObject json)
	{
		try (PrintWriter out = new PrintWriter(file))
		{
			out.println(PrettyJson.prettify(json));

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
}
