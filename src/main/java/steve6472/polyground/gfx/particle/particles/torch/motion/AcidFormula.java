package steve6472.polyground.gfx.particle.particles.torch.motion;

import org.joml.Vector3f;
import steve6472.SSS;
import steve6472.sge.main.util.RandomUtil;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.03.2020
 * Project: CaveGame
 *
 ***********************/
public class AcidFormula extends Formula
{
	@Override
	public void calc(Vector3f position, float time)
	{
		position.add(
			(float) Math.sin(time / 100f) * RandomUtil.randomFloat(-0.005f, 0.005f),
			RandomUtil.randomFloat(0.005f, 0.02f),
			(float) Math.cos(time / 100f) * RandomUtil.randomFloat(-0.005f, 0.005f));
	}

	@Override
	public void loadFormulaData(SSS data)
	{

	}
}
