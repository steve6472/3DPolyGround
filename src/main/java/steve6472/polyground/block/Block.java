package steve6472.polyground.block;

import org.joml.AABBf;
import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.model.BlockModel;
import steve6472.polyground.block.model.CubeHitbox;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.block.model.ModelLoader;
import steve6472.polyground.block.model.elements.CubeElement;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.special.CustomBlock;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.StateLoader;
import steve6472.polyground.entity.EntityBase;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.item.Item;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.sge.main.events.MouseEvent;
import steve6472.sge.main.util.Pair;

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
	public Item item;

	public static Block createAir()
	{
		air = new Block("air", new BlockModel(), Tags.TRANSPARENT);
		air.item = new ItemPlaceholder("air");
		return air;
	}

	public static Block createError()
	{
		CubeHitbox cube = new CubeHitbox(new AABBf(0, 0, 0, 1, 1, 1));
		cube.setCollisionBox(true);
		cube.setHitbox(true);
		cube.setVisible(true);

		CubeElement c = new CubeElement(true);
		JSONObject j = new JSONObject("""
			{
			    "from": [0, 0, 0],
			    "to": [16, 16, 16],
			    "faces": {
			        "east":  {"texture": "null"},
			        "south": {"texture": "null"},
			        "north": {"texture": "null"},
			        "west":  {"texture": "null"},
			        "up":    {"texture": "null"},
			        "down":  {"texture": "null"}
			    }
			}
			""");
		c.load(j);

		BlockModel model = new BlockModel(new IElement[]{c}, cube);

		error = new Block("error", model, Tags.ERROR, Tags.SOLID);
		error.item = new ItemPlaceholder("air");
		error.isFull = true;

		return error;
	}

	private Block(String name, BlockModel blockModel, String... tags)
	{
		this.name = name;
		isFull = false;
		StateLoader.generateState(this, blockModel, tags);
	}

	public Block(JSONObject json)
	{
		isFull = true;
		name = json.getString("name");
		item = new ItemPlaceholder(json.optString("item", "air"));
		generateStates(json.getString("blockstate"));
	}

	public String getName()
	{
		return name;
	}

	public CubeHitbox[] getHitbox(World world, BlockState state, int x, int y, int z)
	{
		return state.getBlockModel(world, x, y, z).getCubes();
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
			StateLoader.generateStates(this, properties, new JSONObject(ModelLoader.read(new File("game/objects/blockstates/" + blockState + ".json"))));
		} catch (Exception ex)
		{
			System.err.println("Error while loading blockstate " + blockState);
			ex.printStackTrace();
		}
	}

	/* Other Something */

	public void load(JSONObject json)
	{

	}

	public boolean isTickable()
	{
		return false;
	}

	public boolean randomTickable()
	{
		return false;
	}

	public boolean isReplaceable(BlockState state)
	{
		return this == air;
	}

	public int createModel(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		if (state.isCustom())
		{
			return CustomBlock.model(x, y, z, world, state, buildHelper, modelLayer);
		}

		int tris = 0;

		BlockModel model = state.getBlockModel(world, x, y, z);

		buildHelper.setSubChunk(world.getSubChunkFromBlockCoords(x, y, z));

		if (model.getElements() != null)
		{
			for (IElement c : model.getElements())
			{
				tris += c.build(buildHelper, modelLayer, world, state, x, y, z);
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

	/*
	public Pair<BlockState, BlockState> getStateForPlacement(World world, BlockState heldState, Player player, EnumFace placedOn, int x, int y, int z)
	{
		return new Pair<>(Block.air.getDefaultState(), heldState);
	}*/

	/**
	 *
	 * @param world world
	 * @param heldState state in players hand
	 * @param player player who placed the block
	 * @param placedOn side the block was placed on
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 * @return blockstate that will be placed if possible
	 */
	public Pair<BlockState, BlockState> getStateForPlacement(World world, BlockState heldState, Player player, EnumFace placedOn, int x, int y, int z)
	{
		return new Pair<>(Block.air.getDefaultState(), getStateForPlacement(world, player, placedOn, x, y, z));
	}

	/**
	 * This function gets run twice,
	 * once for the block player directly clicked on
	 * and once for the offseted block opposite of the face player clicked on
	 * <br>
	 * One should return null!
	 * <br>
	 * If the first one return non-null value this function will not be called for the second time
	 *
	 * @param world world
	 * @param worldState state of placed block the player clicked on
	 * @param heldState state of held block, {@code Block.air} if nothing is held
	 * @param player player
	 * @param placedOn side the player placed the block on
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 * @return Pair of BlockStates <br> A = State to be held<br> B = State to be placed in world<br>Return null if no change should be made!
	 */
	public Pair<BlockState, BlockState> merge(World world, BlockState worldState, BlockState heldState, Player player, EnumFace placedOn, int x, int y, int z)
	{
		return new Pair<>(Block.air.getDefaultState(), heldState.getBlock().getStateForPlacement(world, player, placedOn, x, y, z));
	}

	public boolean canMerge(BlockState holdedState, BlockState worldState, Player player, EnumFace clickedOn, int x, int y, int z)
	{
		return false;
	}

	/**
	 *
	 * @param world world
	 * @param worldState state of placed block
	 * @param heldState state of held block, {@code Block.air} if nothing is held
	 * @param player player
	 * @param clickedOn side the player clicked on
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 * @return Pair of BlockStates <br> A = State to be held<br>    B = State to be placed in world<br>Return null if no change should be made!
	 */
	public Pair<BlockState, BlockState> getStatesForPickup(World world, BlockState worldState, BlockState heldState, Player player, EnumFace clickedOn, int x, int y, int z)
	{
		if (heldState.isAir())
			return new Pair<>(worldState, Block.air.getDefaultState());
		else
			return null;
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
	 * Runs randomly if {@code isRandomTickable()} is true
	 *
	 * @param state State of this block
	 * @param world world
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 */
	public void randomTick(BlockState state, World world, int x, int y, int z)
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

	/*
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

	/*
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
	 * Default spawns loot, see {@link #spawnLoot(BlockState, World, int, int, int)}
	 *
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
		if (player.getGamemode().spawBlockLoot)
			spawnLoot(state, world, x, y, z);
	}

	/**
	 * Spawns loot at the center of the block
	 * @param state state of block
	 * @param world world
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 */
	public void spawnLoot(BlockState state, World world, int x, int y, int z)
	{
//		world.getEntityManager().addEntity(new BlockItemEntity(state.getBlock().item, x, y, z));
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

	public void entityCollision(EntityBase entity, World world, BlockState state, int x, int y, int z)
	{

	}

	public void entityOnBlockCollision(EntityBase entity, World world, BlockState state, int x, int y, int z)
	{

	}
}
