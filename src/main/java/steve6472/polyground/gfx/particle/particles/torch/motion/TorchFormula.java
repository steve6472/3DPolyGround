package steve6472.polyground.gfx.particle.particles.torch.motion;

import org.joml.Vector3f;
import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.03.2020
 * Project: CaveGame
 *
 ***********************/
public class TorchFormula extends Formula
{
	@Override
	public void calc(Vector3f position, float time)
	{
		position.add(0, 0.01f, 0);
	}

	@Override
	public void loadFormulaData(JSONObject json)
	{

	}
}
