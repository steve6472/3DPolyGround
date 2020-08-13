package steve6472.polyground.gfx.particle.particles;

import org.joml.Vector3f;
import org.joml.Vector4f;
import steve6472.polyground.CaveGame;
import steve6472.polyground.Particle;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.gfx.particle.particles.torch.motion.Formula;
import steve6472.sge.main.game.Tag;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 29.03.2020
 * Project: CaveGame
 *
 ***********************/
public class GoodParticle extends Particle
{
	private final List<Tag> tags;
	private final long birth;
	private final Formula formula;

	public GoodParticle(Formula formula, Vector3f position, float size, float r, float g, float b, float a)
	{
		super(new Vector3f(), position, size, new Vector4f(r, g, b, a), -1);
		this.formula = formula;
		tags = new ArrayList<>();
		birth = System.currentTimeMillis();
	}

	@Override
	public void tick()
	{
		formula.calc(getPosition(), System.currentTimeMillis() - birth);
		setSize(getSize() - 0.0002f);
	}

	@Override
	public void die()
	{

	}

	@Override
	public boolean sort()
	{
		return false;
	}

	@Override
	public boolean shouldDie()
	{
		return getSize() <= 0.01f;
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
