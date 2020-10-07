package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.block.model.ModelLoader;
import steve6472.polyground.block.properties.BooleanProperty;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.item.ItemEntity;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.item.Item;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.registry.Items;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.sge.main.events.MouseEvent;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 07.10.2020
 * Project: CaveGame
 *
 ***********************/
public class AxeBlock extends CustomBlock
{
	public static final BooleanProperty HEAD = States.HAS_HEAD;
	public static final BooleanProperty STICK = States.HAS_STICK;
	public static final BooleanProperty STRING = States.HAS_STRING;

	private static IElement[] MODEL_HEAD, MODEL_STICK, MODEL_STRING;

	public AxeBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public void load(JSONObject json)
	{
		super.load(json);
		MODEL_HEAD = ModelLoader.loadElements(ModelLoader.load("custom_models/items/axe/head.bbmodel", true), 0, 0, 0);
		MODEL_STICK = ModelLoader.loadElements(ModelLoader.load("custom_models/items/axe/stick.bbmodel", true), 0, 0, 0);
		MODEL_STRING = ModelLoader.loadElements(ModelLoader.load("custom_models/items/axe/string.bbmodel", true), 0, 0, 0);

		Blocks.elements.add(MODEL_HEAD);
		Blocks.elements.add(MODEL_STICK);
		Blocks.elements.add(MODEL_STRING);
	}

	@Override
	public void onClick(BlockState state, World world, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		boolean holdsHead = player.holdsItem() && player.getItemInHand().getName().equals("flint_axe_head");
		boolean holdsStick = player.holdsBlock() && player.getBlockInHand().getBlock().getName().equals("stick");
		boolean holdsString = player.holdsItem() && player.getItemInHand().getName().equals("hemp_string");

		boolean worldHead = state.get(HEAD);
		boolean worldStick = state.get(STICK);
		boolean worldString = state.get(STRING);

		if (!worldHead && holdsHead)
		{
			world.setState(state(true, worldStick, worldString), x, y, z);
			world.getEntityManager().removeEntity(player.heldItem);
			player.heldItem = null;
		}
		else if (!worldStick && holdsStick)
		{
			world.setState(state(worldHead, true, worldString), x, y, z);
			world.getEntityManager().removeEntity(player.heldBlock);
			player.heldBlock = null;
			player.processNextBlockPlace = false;
		}
		else if (!worldString && holdsString && (worldHead || worldStick))
		{
			world.setState(state(worldHead, worldStick, true), x, y, z);
			world.getEntityManager().removeEntity(player.heldItem);
			player.heldItem = null;
		}

		if ((worldStick && worldHead && holdsString) || (worldStick && worldString && holdsHead) || (worldHead && worldString && holdsStick))
		{
			world.setBlock(Block.air, x, y, z);

			spawnLoot(world, state, x, y, z);
		}
	}

	@Override
	public void spawnLoot(World world, BlockState state, int x, int y, int z)
	{
		Item item = Items.getItemByName("flint_axe");
		ItemEntity entity = new ItemEntity(null, item, null, x + 0.5f, y, z + 0.5f);

		world.getEntityManager().addEntity(entity);
	}

	private BlockState state(boolean head, boolean stick, boolean string)
	{
		return getDefaultState().with(HEAD, head).with(STICK, stick).with(STRING, string).get();
	}

	@Override
	public int createModel(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		int tris = 0;

		if (state.get(HEAD))
			tris += CustomBlock.model(MODEL_HEAD, x, y, z, world, buildHelper, modelLayer);
		if (state.get(STICK))
			tris += CustomBlock.model(MODEL_STICK, x, y, z, world, buildHelper, modelLayer);
		if (state.get(STRING))
			tris += CustomBlock.model(MODEL_STRING, x, y, z, world, buildHelper, modelLayer);

		return tris;
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(HEAD);
		properties.add(STICK);
		properties.add(STRING);
	}
}
