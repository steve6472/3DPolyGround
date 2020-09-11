package steve6472.polyground.gfx.particle.particles.torch.motion;

import org.joml.Vector3f;
import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 11.09.2020
 * Project: CaveGame
 *
 ***********************/
public class InwardsMotion extends Formula
{
	private final Vector3f pos;
	private final float dist;
	private final float speed;

	public InwardsMotion(Vector3f pos, float dist, float speed)
	{
		this.pos = pos;
		this.dist = dist;
		this.speed = speed;
	}

	@Override
	public void calc(Vector3f position, float time)
	{
		Vector3f v = new Vector3f(pos.x - position.x, pos.y - position.y, pos.z - position.z);
		v.normalize();
		v.mul(0.0065f * dist * 2.5f * speed);
		position.add(v);
	}

	@Override
	public void loadFormulaData(JSONObject json)
	{

	}
}
