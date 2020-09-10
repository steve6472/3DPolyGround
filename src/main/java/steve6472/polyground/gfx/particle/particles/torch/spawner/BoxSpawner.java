package steve6472.polyground.gfx.particle.particles.torch.spawner;

import org.joml.Vector3f;
import org.json.JSONObject;
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
	public void loadSpawnData(JSONObject json)
	{
		float sizeScale = json.getFloat("sizeScale");
		sizeX = json.getFloat("sizeX") * sizeScale;
		sizeY = json.getFloat("sizeY") * sizeScale;
		sizeZ = json.getFloat("sizeZ") * sizeScale;

		float offsetScale = json.getFloat("offsetScale");
		offsetX = json.getFloat("offsetX") * offsetScale;
		offsetY = json.getFloat("offsetY") * offsetScale;
		offsetZ = json.getFloat("offsetZ") * offsetScale;
	}
}
