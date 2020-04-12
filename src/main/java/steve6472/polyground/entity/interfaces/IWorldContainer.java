package steve6472.polyground.entity.interfaces;

import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 11.04.2020
 * Project: CaveGame
 *
 ***********************/
public interface IWorldContainer
{
	void setWorld(World world);

	World getWorld();
}
