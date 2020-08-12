package steve6472.polyground.block;

import org.joml.AABBf;
import org.json.JSONObject;
import steve6472.SSS;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.model.BlockModel;
import steve6472.polyground.block.model.BlockModelLoader;
import steve6472.polyground.block.model.Cube;
import steve6472.polyground.block.model.CubeFace;
import steve6472.polyground.block.model.faceProperty.LayerFaceProperty;
import steve6472.polyground.block.model.faceProperty.TextureFaceProperty;
import steve6472.polyground.block.model.faceProperty.UVFaceProperty;
import steve6472.polyground.block.model.faceProperty.condition.ConditionFaceProperty;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.special.SnapBlock;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.StateLoader;
import steve6472.polyground.entity.Player;
import steve6472.polyground.registry.face.FaceRegistry;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.Cull;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.sge.main.events.MouseEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.08.2019
 * Project: SJP
 *
 ***********************/
public class Block
{
	public static Block air, error;

	public boolean isFull;

	public String name;
	private BlockState defaultState;

	public static Block createAir()
	{
		return air = new Block("air", new BlockModel());
	}

	public static Block createError()
	{
		Cube cube = new Cube(new AABBf(0, 0, 0, 1, 1, 1));
		cube.setCollisionBox(true);
		cube.setHitbox(true);

		for (EnumFace face : EnumFace.getFaces())
		{
			CubeFace cubeFace = new CubeFace(cube, face);

			TextureFaceProperty texture = new TextureFaceProperty();
			texture.setTexture("null");
			BlockTextureHolder.putTexture("null");
			texture.setTextureId(BlockTextureHolder.getTextureId(texture.getTexture()));
			cubeFace.addProperty(texture);

			UVFaceProperty uv = new UVFaceProperty();
			uv.autoUV(cube, face);

			cubeFace.addProperty(uv);

			cube.setFace(face, cubeFace);
		}

		BlockModel model = new BlockModel(cube);

		error = new Block("error", model);
		error.getDefaultState().getBlockModel().addTag("error");
		error.getDefaultState().getBlockModel().addTag("solid");

		return error;
	}

	private Block(String name, BlockModel blockModel)
	{
		this.name = name;
		isFull = false;
		StateLoader.generateState(this, blockModel);
	}

	public Block(File f)
	{
		isFull = true;
		name = f.getName().substring(0, f.getName().length() - 4);
		generateStates(new SSS(f).getString("blockstate"));
	}

	public String getName()
	{
		return name;
	}

	/* States */

	public BlockState getDefaultState()
	{
		return defaultState;
	}

	public void setDefaultState(BlockState state)
	{
		this.defaultState = state;
	}

	public void fillStates(List<IProperty<?>> properties)
	{
	}

	private void generateStates(String blockState)
	{
		List<IProperty<?>> properties = new ArrayList<>();
		fillStates(properties);
		try
		{
			StateLoader.generateStates(this, properties, new JSONObject(BlockModelLoader.read(new File("game/objects/blockstates/" + blockState + ".json"))));
		} catch (Exception ex)
		{
			System.err.println("Error while loading blockstate " + blockState);
			ex.printStackTrace();
		}
	}

	/* Other Something */

	public void postLoad()
	{

	}

	public boolean isTickable()
	{
		return false;
	}

	public boolean isReplaceable(BlockState state)
	{
		return this == air;
	}

	public int createModel(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		int tris = 0;

		buildHelper.setSubChunk(world.getSubChunkFromBlockCoords(x, y, z));
		for (Cube c : state.getBlockModel().getCubes())
		{
			buildHelper.setCube(c);
			for (EnumFace face : EnumFace.getFaces())
			{
				/* Check if face is in correct (Chunk) Model Layer */
				if (LayerFaceProperty.getModelLayer(c.getFace(face)) == modelLayer)
				{
					CubeFace cubeFace = c.getFace(face);
					boolean flag = false;
					boolean hasCondTexture = false;
					if (cubeFace != null && cubeFace.hasProperty(FaceRegistry.conditionedTexture))
					{
						flag = ConditionFaceProperty.editProperties(cubeFace.getProperty(FaceRegistry.conditionedTexture), cubeFace, x, y, z, world);
						hasCondTexture = true;
					}

					if (hasCondTexture)
					{
						if (flag)
						{
							tris += buildHelper.face(face);
						}
					} else if (Cull.renderFace(x, y, z, c, face, this, world))
					{
						tris += buildHelper.face(face);
					}
				}
			}
		}

		return tris;
	}

	@Override
	public String toString()
	{
		return "Block{" + "isFull=" + isFull + ", id=" + -1 + ", name='" + name + '\'' + '}';
	}

	/**
	 *
	 * @param world world
	 * @param player player who placed the block
	 * @param placedOn side the block was placed on
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 * @return blockstate that will be placed if possible
	 */
	public BlockState getStateForPlacement(World world, Player player, EnumFace placedOn, int x, int y, int z)
	{
		return getDefaultState();
	}

	/**
	 * Runs every tick if {@code isTickable()} is true
	 *
	 * @param state State of this block
	 * @param world world
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 */
	public void tick(BlockState state, World world, int x, int y, int z)
	{
	}

	/**
	 *
	 * @param state State of this block
	 * @param world world
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 * @return true if block can be placed or sustain itself
	 */
	public boolean isValidPosition(BlockState state, World world, int x, int y, int z)
	{
		BlockState s = world.getState(x, y, z);
		return s.getBlock().isReplaceable(s) || state == s;
	}

	/**
	 *
	 * @param state State of this block
	 * @param world world
	 * @param oldState State the block is replacing
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 */
	public void onBlockAdded(BlockState state, World world, BlockState oldState, int x, int y, int z)
	{

	}

	/**
	 *
	 * @param state State of this block
	 * @param world world
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 */
//	public void updateAfterPlacement(BlockState state, World world, int x, int y, int z)
//	{
//
//	}

	/**
	 * @param state State of this block
	 * @param world world
	 * @param updateFrom which direction was the block updated
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 */
	/*public void updatePostPlacement(BlockState state, World world, EnumFace updateFrom, int x, int y, int z)
	{

	}*/

	/**
	 *
	 * @param state State of this block
	 * @param world world
	 * @param updateFrom which direction was the block updated
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 */
	public void neighbourChange(BlockState state, World world, EnumFace updateFrom, int x, int y, int z)
	{

	}

	/**
	 *
	 * @param state State of scheduled this block
	 * @param world world
	 * @param updateFrom which direction was the block updated
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 */
	public void scheduledUpdate(BlockState state, World world, EnumFace updateFrom, int x, int y, int z)
	{

	}

	/**
	 * @param state State of this block
	 * @param world world
	 * @param player player who broke the block
	 * @param breakedFrom face the block was broken from
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 */
	public void onPlayerBreak(BlockState state, World world, Player player, EnumFace breakedFrom, int x, int y, int z)
	{
		SnapBlock.activate(state, world, x, y, z, 1);
	}

	/**
	 * Runs on block in the world
	 * @param state State of this block
	 * @param world  World
	 * @param player    Player
	 * @param clickedOn Side the player has clicked on
	 * @param click     event
	 * @param x         x position of block
	 * @param y         y position of block
	 * @param z         z position of block
	 */
	public void onClick(BlockState state, World world, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
	}
}
