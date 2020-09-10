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
public class SphereSpawner extends Spawner
{
	private float radius;
	private float offsetX, offsetY, offsetZ;

	@Override
	public void spawn(Vector3f position, World world, float x, float y, float z)
	{
		float sx = x + offsetX;
		float sy = y + offsetY;
		float sz = z + offsetZ;

		getRandomSpherePos(position, radius);
		position.add(sx, sy, sz);
	}

	/**
	 * @author https://karthikkaranth.me/blog/generating-random-points-in-a-sphere/
	 */
	private void getRandomSpherePos(Vector3f target, float radius)
	{
		double u = Math.random();
		float x1 = RandomUtil.randomFloat(-radius, radius);
		float x2 = RandomUtil.randomFloat(-radius, radius);
		float x3 = RandomUtil.randomFloat(-radius, radius);

		float mag = (float) Math.sqrt(x1*x1 + x2*x2 + x3*x3);
		x1 /= mag; x2 /= mag; x3 /= mag;

		// Math.cbrt is cube root
		float c = (float) Math.cbrt(u);

		target.set(x1 * c, x2 * c, x3 * c);
	}

	@Override
	public void loadSpawnData(JSONObject json)
	{
		float sizeScale = json.getFloat("sizeScale");
		radius = json.getFloat("radius") * sizeScale;

		float offsetScale = json.getFloat("offsetScale");
		offsetX = json.getFloat("offsetX") * offsetScale;
		offsetY = json.getFloat("offsetY") * offsetScale;
		offsetZ = json.getFloat("offsetZ") * offsetScale;
	}
}
