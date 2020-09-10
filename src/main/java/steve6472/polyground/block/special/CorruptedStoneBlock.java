package steve6472.polyground.block.special;

import org.joml.Vector3f;
import org.json.JSONObject;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.Tags;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.IntProperty;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.gfx.particle.particles.GoodParticle;
import steve6472.polyground.gfx.particle.particles.torch.motion.AcidFormula;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.World;
import steve6472.sge.main.util.RandomUtil;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.08.2020
 * Project: CaveGame
 *
 ***********************/
public class CorruptedStoneBlock extends Block
{
	private static final IntProperty DISTANCE = States.DISTANCE_1_6;

	private BlockState spreadTo;

	public CorruptedStoneBlock(JSONObject json)
	{
		super(json);
		setDefaultState(getDefaultState().with(DISTANCE, 1).get());
	}

	@Override
	public void load(JSONObject json)
	{
		spreadTo = Blocks.getDefaultState("stone");
	}

//	@Override
//	public BlockState getStateForPlacement(World world, Player player, EnumFace placedOn, int x, int y, int z)
//	{
//		return getDefaultState().with(DISTANCE, 6).get();
//	}

	@Override
	public boolean randomTickable()
	{
		return true;
	}

	private static final AcidFormula MOTION = new AcidFormula();

	private float randomPos(World world)
	{
		return (world.getRandom().nextFloat() - 0.5f) * 1.3f + 0.5f;
	}

	private boolean vis(BlockState state)
	{
		return !state.getBlock().isFull || state.hasTag(Tags.TRANSPARENT);
	}

	private boolean isVisible(World world, int x, int y, int z)
	{
		return vis(world.getState(x, y + 1, z)) ||
			vis(world.getState(x, y - 1, z)) ||
			vis(world.getState(x + 1, y, z)) ||
			vis(world.getState(x - 1, y, z)) ||
			vis(world.getState(x, y, z + 1)) ||
			vis(world.getState(x, y, z - 1));
	}

	@Override
	public void randomTick(BlockState state, World world, int x, int y, int z)
	{
		if (isVisible(world, x, y, z))
		{
			for (int i = 0; i < 4; i++)
			{
				GoodParticle p = new GoodParticle(
					MOTION,
					new Vector3f(randomPos(world) + x, randomPos(world) + y, randomPos(world) + z),
					RandomUtil.randomFloat(1f / 48f, 1f / 24f),
					0.8f, 0.1f, 0.75f, 1f
				);

				world.getGame().mainRender.particles.addParticle(p);
			}
		}

		int currentDistance = state.get(DISTANCE);

		if (currentDistance < 6)
		{
			for (int i = 0; i < 2; ++i)
			{
				int X = x + world.getRandom().nextInt(6) - 3;
				int Y = y + world.getRandom().nextInt(3) - 1;
				int Z = z + world.getRandom().nextInt(6) - 3;
				if (world.getState(X, Y, Z) == spreadTo)
				{
					int add = currentDistance;
					if (world.getRandom().nextDouble() < 0.5d)
						add++;

					world.setState(state.with(DISTANCE, Math.min(add, 6)).get(), X, Y, Z);

					if (world.getRandom().nextDouble() < 0.75d)
					{
						world.setState(state.with(DISTANCE, Math.min(currentDistance + 1, 6)).get(), x, y, z);
					}
				}
			}
		}
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(DISTANCE);
	}
}
