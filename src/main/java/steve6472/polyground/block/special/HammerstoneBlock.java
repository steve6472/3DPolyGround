package steve6472.polyground.block.special;

import org.joml.AABBf;
import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.model.CubeHitbox;
import steve6472.polyground.block.properties.EnumProperty;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.enums.EnumStoneType;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.item.ItemEntity;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;
import steve6472.sge.main.util.RandomUtil;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 05.10.2020
 * Project: CaveGame
 *
 ***********************/
public class HammerstoneBlock extends Block
{
	public static final EnumProperty<EnumStoneType> STONE_TYPE = States.STONE_TYPE;

	public HammerstoneBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public void onClick(BlockState state, World world, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getAction() == KeyList.PRESS && (click.getButton() == KeyList.LMB || click.getButton() == KeyList.MMB))
		{
			int ox = (int) (hash(world.getSeed(), -x, y, z * (1 << 4) + 1) % 12) - 6;
			int oz = (int) (hash(world.getSeed(), x * -((1 << 5) + 1), y, -z) % 12) - 6;

			world.setBlock(Block.air, x, y, z);
			ItemEntity e = new ItemEntity(null, item, null, x + 0.5f + ox / 16f, y + 0.25f, z + 0.5f + oz / 16f);
			world.getEntityManager().addEntity(e);
		}
	}

	@Override
	public int createModel(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		int ox = (int) (hash(world.getSeed(), -x, y, z * (1 << 4) + 1) % 12) - 6;
		int oz = (int) (hash(world.getSeed(), x * -((1 << 5) + 1), y, -z) % 12) - 6;

		buildHelper.setOffset(ox / 16f, 0, oz / 16f);
		return super.createModel(x, y, z, world, state, buildHelper, modelLayer);
	}

	@Override
	public CubeHitbox[] getHitbox(World world, BlockState state, int x, int y, int z)
	{
		CubeHitbox[] model = state.getBlockModel(world, x, y, z).getCubes();
		CubeHitbox[] copy = new CubeHitbox[model.length];
		for (int i = 0; i < copy.length; i++)
		{
			int ox = (int) (hash(world.getSeed(), -x, y, z * (1 << 4) + 1) % 12) - 6;
			int oz = (int) (hash(world.getSeed(), x * -((1 << 5) + 1), y, -z) % 12) - 6;

			CubeHitbox c = model[i];
			CubeHitbox cubeHitbox = new CubeHitbox(new AABBf(c.getAabb()));
			cubeHitbox.getAabb().translate(ox / 16f, 0, oz / 16f);
			cubeHitbox.setName(c.getName());
			cubeHitbox.setCollisionBox(c.isCollisionBox());
			cubeHitbox.setHitbox(c.isHitbox());
			cubeHitbox.setVisible(c.isVisible());
			copy[i] = cubeHitbox;
		}
		return copy;
	}

	private long hash(long seed, int x, int y, int z)
	{
		long h = seed + x * 668265263L + y * 2147483647L + z * 374761393L;
		h = (h ^ (h >> 14)) * 1274126177L;
		return h ^ (h >> 16);
	}

	@Override
	public BlockState getStateForPlacement(World world, Player player, EnumFace placedOn, int x, int y, int z)
	{
		return getDefaultState().with(STONE_TYPE, EnumStoneType.getValues()[RandomUtil.randomInt(0, 2)]).get();
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(STONE_TYPE);
	}
}
