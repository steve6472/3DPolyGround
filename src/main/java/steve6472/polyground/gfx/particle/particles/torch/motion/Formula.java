package steve6472.polyground.gfx.particle.particles.torch.motion;

import org.joml.Vector3f;
import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.03.2020
 * Project: CaveGame
 *
 ***********************/
public abstract class Formula
{
	public abstract void calc(Vector3f position, float time);

	public abstract void loadFormulaData(JSONObject json);
}
