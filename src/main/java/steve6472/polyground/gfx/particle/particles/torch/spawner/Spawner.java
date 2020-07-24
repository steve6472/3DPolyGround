package steve6472.polyground.gfx.particle.particles.torch.spawner;

import org.joml.Vector3f;
import steve6472.SSS;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.03.2020
 * Project: CaveGame
 *
 ***********************/
public abstract class Spawner
{
	public abstract void spawn(Vector3f position, World world, float x, float y, float z);

	public abstract void loadSpawnData(SSS data);
}
