package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.model.BlockModel;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.IntProperty;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.item.ItemEntity;
import steve6472.polyground.entity.player.EnumGameMode;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.item.Item;
import steve6472.polyground.registry.Items;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;
import steve6472.sge.main.util.RandomUtil;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 07.10.2020
 * Project: CaveGame
 *
 ***********************/
public class HempBlock extends Block
{
	public static final IntProperty STAGE = States.STAGE_1_3;

	public HempBlock(JSONObject json)
	{
		super(json);
		isFull = false;

		setDefaultState(getDefaultState().with(STAGE, 3).get());
	}

	@Override
	public void onClick(BlockState state, World world, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (player.getGamemode() == EnumGameMode.CREATIVE || click.getAction() != KeyList.PRESS || click.getButton() != KeyList.LMB)
			return;

		if (state.get(STAGE) > 1)
		{
			world.setState(state.with(STAGE, state.get(STAGE) - 1).get(), x, y, z);
			spawnLoot(world, state, x, y, z);
		} else
		{
			world.setBlock(Block.AIR, x, y, z);
		}
		player.processNextBlockBreak = false;
	}

	@Override
	public void spawnLoot(World world, BlockState state, int x, int y, int z)
	{
		float distance = RandomUtil.randomFloat(0.5f, 0.8f);
		double rRads = RandomUtil.randomRadian();
		float sin = (float) Math.sin(rRads) * distance;
		float cos = (float) Math.cos(rRads) * distance;

		Item hemp = Items.getItemByName("hemp_string");
		ItemEntity entity = new ItemEntity(null, hemp, null, x + sin + 0.5f, y + RandomUtil.randomFloat(0, 1f), z + cos + 0.5f);
		entity.setYaw((float) RandomUtil.randomRadian());
		world.getEntityManager().addEntity(entity);
	}

	@Override
	public void randomTick(BlockState state, World world, int x, int y, int z)
	{
		if (world.getRandom().nextInt(5) != 2)
			return;

		if (state.get(STAGE) < 3)
		{
			world.setState(state.with(STAGE, state.get(STAGE) + 1).get(), x, y, z);
		}
	}

	@Override
	public int createModel(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		int tris = 0;
		BlockModel model = state.getBlockModel(world, x, y, z);

		buildHelper.setSubChunk(world.getSubChunkFromBlockCoords(x, y, z));

		if (model.getElements() != null)
		{
			for (IElement c : model.getElements())
			{
				tris += c.build(buildHelper, modelLayer, null, state, x, y, z);
			}
		}

		return tris;
	}

	@Override
	public boolean randomTickable()
	{
		return true;
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(STAGE);
	}
}
