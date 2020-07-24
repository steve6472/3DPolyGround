package steve6472.polyground.gfx.particle.particles.torch.spawner;

import org.joml.Vector3f;
import steve6472.SSS;
import steve6472.polyground.world.World;
import steve6472.sge.main.util.RandomUtil;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.03.2020
 * Project: CaveGame
 *
 ***********************/
public class BoxSpawner extends Spawner
{
	private float sizeX, sizeY, sizeZ;
	private float offsetX, offsetY, offsetZ;

	@Override
	public void spawn(Vector3f position, World world, float x, float y, float z)
	{
		float sx = RandomUtil.randomFloat(-sizeX, sizeX) + x + offsetX;
		float sy = RandomUtil.randomFloat(-sizeY, sizeY) + y + offsetY;
		float sz = RandomUtil.randomFloat(-sizeZ, sizeZ) + z + offsetZ;

		position.set(sx, sy, sz);
	}

	@Override
	public void loadSpawnData(SSS data)
	{
		float sizeScale = data.getFloat("sizeScale");
		sizeX = data.getFloat("sizeX") * sizeScale;
		sizeY = data.getFloat("sizeY") * sizeScale;
		sizeZ = data.getFloat("sizeZ") * sizeScale;

		float offsetScale = data.getFloat("offsetScale");
		offsetX = data.getFloat("offsetX") * offsetScale;
		offsetY = data.getFloat("offsetY") * offsetScale;
		offsetZ = data.getFloat("offsetZ") * offsetScale;
	}
}
