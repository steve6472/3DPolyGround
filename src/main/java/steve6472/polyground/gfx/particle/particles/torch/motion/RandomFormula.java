package steve6472.polyground.gfx.particle.particles.torch.motion;

import org.joml.Vector3f;
import org.json.JSONObject;
import steve6472.sge.main.util.RandomUtil;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 11.09.2020
 * Project: CaveGame
 *
 ***********************/
public class RandomFormula extends Formula
{
	@Override
	public void calc(Vector3f position, float time)
	{
		// add 1 to time to prevent NaN
		position.add(
			RandomUtil.randomFloat(-1, 1) * 1f / ((time + 1) * 10f),
			RandomUtil.randomFloat(-1, 1) * 1f / ((time + 1) * 10f),
			RandomUtil.randomFloat(-1, 1) * 1f / ((time + 1) * 10f));
	}

	@Override
	public void loadFormulaData(JSONObject json)
	{

	}
}
