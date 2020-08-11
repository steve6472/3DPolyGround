package steve6472.polyground.world.chunk;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.World;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 11.08.2020
 * Project: CaveGame
 *
 ***********************/
public class TickScheduler
{
	private final World world;
	private final List<ScheduledTick> ticks;
	private final List<ScheduledTick> newTicks;
	private int ticksScheduled = 0;

	public TickScheduler(World world)
	{
		this.world = world;
		ticks = new ArrayList<>();
		newTicks = new ArrayList<>();
	}

	public void tick()
	{
		if (ticksScheduled > 4096)
			System.err.println("Too many ticks scheduled");
		ticksScheduled = 0;
		ticks.addAll(newTicks);
		newTicks.clear();
		ticks.removeIf(p -> {
			if (--p.tickIn == 0)
			{
				for (EnumFace f : EnumFace.getFaces())
					p.state.getBlock().scheduledUpdate(p.state, world, f, p.x, p.y, p.z);
				return true;
			}
			return false;
		});
	}

	public void scheduleTick(BlockState state, int x, int y, int z, int tickIn)
	{
		if (state == null || state.getBlock() == Block.air)
			return;

		ticksScheduled++;
		if (ticksScheduled < 4096)
		{
			boolean add = true;
			for (ScheduledTick t : newTicks)
			{
				if (t.x == x && t.y == y && t.z == z)
				{
					add = false;
					break;
				}
			}
			if (add)
				newTicks.add(new ScheduledTick(x, y, z, state, tickIn));
		}
	}

	private static class ScheduledTick
	{
		final int x;
		final int y;
		final int z;
		final BlockState state;
		int tickIn;

		public ScheduledTick(int x, int y, int z, BlockState state, int tickIn)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.state = state;
			this.tickIn = tickIn;
		}
	}
}
