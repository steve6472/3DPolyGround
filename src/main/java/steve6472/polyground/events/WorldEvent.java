package steve6472.polyground.events;

import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.11.2019
 * Project: SJP
 *
 ***********************/
public class WorldEvent extends CancellableEvent
{
	private World world;

	public WorldEvent(World world)
	{
		this.world = world;
	}

	public World getWorld()
	{
		return world;
	}

	public static class PreRender extends WorldEvent
	{
		public PreRender(World world)
		{
			super(world);
		}
	}

	public static class PostRender extends WorldEvent
	{
		public PostRender(World world)
		{
			super(world);
		}
	}
}
