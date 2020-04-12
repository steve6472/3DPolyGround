package steve6472.polyground.entity.interfaces;

import org.joml.AABBf;
import steve6472.polyground.world.chunk.SubChunk;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 11.04.2020
 * Project: CaveGame
 *
 ***********************/
public interface ICollideable
{
	void collide(SubChunk subChunk, int x, int y, int z);

	AABBf[] getHitbox();
}
