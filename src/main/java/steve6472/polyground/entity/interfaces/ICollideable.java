package steve6472.polyground.entity.interfaces;

import org.joml.AABBf;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 11.04.2020
 * Project: CaveGame
 *
 ***********************/
public interface ICollideable
{
	void collide(World world, int x, int y, int z);

	AABBf[] getHitbox();
}
