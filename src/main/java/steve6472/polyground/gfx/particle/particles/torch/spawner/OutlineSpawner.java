package steve6472.polyground.gfx.particle.particles.torch.spawner;

import org.joml.Vector3f;
import org.json.JSONObject;
import steve6472.polyground.world.World;
import steve6472.sge.main.util.RandomUtil;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 11.09.2020
 * Project: CaveGame
 *
 ***********************/
public class OutlineSpawner extends Spawner
{
	private float sizeX, sizeY, sizeZ, offsetX, offsetY, offsetZ;

	public OutlineSpawner(float sizeX, float sizeY, float sizeZ, float offsetX, float offsetY, float offsetZ)
	{
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
	}

	@Override
	public void spawn(Vector3f position, World world, float x, float y, float z)
	{
		float sx = RandomUtil.randomFloat(0, sizeX) + x;
		float sy = RandomUtil.randomFloat(0, sizeY) + y;
		float sz = RandomUtil.randomFloat(0, sizeZ) + z;

		int r = RandomUtil.randomInt(0, 11);
		if (r == 0)  position.set(x, y, sz);
		if (r == 1)  position.set(sx, y, z + sizeZ);
		if (r == 2)  position.set(x + sizeX, y, sz);
		if (r == 3)  position.set(sx, y, z);

		if (r == 4)  position.set(x, sy, z);
		if (r == 5)  position.set(x, sy, z + sizeZ);
		if (r == 6)  position.set(x + sizeX, sy, z + sizeZ);
		if (r == 7)  position.set(x + sizeX, sy, z);

		if (r == 8)  position.set(x, y + sizeY, sz);
		if (r == 9)  position.set(sx, y + sizeY, z + sizeZ);
		if (r == 10)  position.set(x + sizeX, y + sizeY, sz);
		if (r == 11)  position.set(sx, y + sizeY, z);

		position.add(offsetX, offsetY, offsetZ);
	}

	@Override
	public void loadSpawnData(JSONObject json)
	{
		float sizeScale = json.getFloat("sizeScale");
		sizeX = json.getFloat("sizeX") * sizeScale;
		sizeY = json.getFloat("sizeY") * sizeScale;
		sizeZ = json.getFloat("sizeZ") * sizeScale;
	}
}
