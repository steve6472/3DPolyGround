package steve6472.polyground.entity.player;

import org.joml.AABBf;
import steve6472.polyground.world.World;
import steve6472.sge.main.game.mixable.IPosition3f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 07.09.2020
 * Project: CaveGame
 *
 ***********************/
public interface IHoldable extends IPosition3f
{
	IHoldable pickUp(World world, Player player);

	void place(World world, float x, float y, float z);

	void render(EnumSlot position);

	void tick(EnumSlot position);

	AABBf getHitbox();

	String getName();
}
