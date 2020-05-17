package steve6472.polyground.gfx.particle.particles;

import org.joml.Vector3f;
import org.joml.Vector4f;
import steve6472.polyground.CaveGame;
import steve6472.polyground.Particle;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.gfx.particle.particles.torch.motion.Formula;
import steve6472.polyground.world.light.Light;
import steve6472.sge.main.game.Tag;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 29.03.2020
 * Project: CaveGame
 *
 ***********************/
public class LightParticle extends Particle
{
	private final List<Tag> tags;
	private final Light light;
	private final long birth;
	private final Formula formula;

	public LightParticle(Formula formula, Vector3f position, float size, float r, float g, float b, float a, Light light)
	{
		super(new Vector3f(), position, size, new Vector4f(r, g, b, a), -1);
		this.formula = formula;
		tags = new ArrayList<>();
		this.light = light;
		birth = System.currentTimeMillis();
	}

	@Override
	public void tick()
	{
		formula.calc(getPosition(), System.currentTimeMillis() - birth);
//		getMotion().mul(1f - 0.02f);
//		getPosition().add(getMotion());
		setSize(getSize() - 0.0002f);

		long now = System.currentTimeMillis();
		float mul;

		long lightUpTime = 500;

		if (birth + lightUpTime < now)
		{
			getColor().w -= 0.011f;
			mul = getColor().w() - 0.2f;
		} else
		{
			long delta = birth + lightUpTime - now;
			mul = (lightUpTime - delta) / (float) lightUpTime;
		}

		light.setPosition(getPosition());
		light.setColor(getColor().x() * mul, getColor().y() * mul, getColor().z() * mul);
	}

	@Override
	public void die()
	{
		light.setInactive(true);
	}

	@Override
	public boolean sort()
	{
		return true;
	}

	@Override
	public boolean shouldDie()
	{
		return /*getSize() <= 0.01f || */getColor().w <= 0.2 || light.isInactive();
	}

	@Override
	public void applyShader()
	{
		MainRender.shaders.basicParticleShader.bind(CaveGame.getInstance().getCamera().getViewMatrix());
	}

	@Override
	public void applyInvidualShader()
	{

	}

	@Override
	public List<Tag> getTags()
	{
		return tags;
	}
}
