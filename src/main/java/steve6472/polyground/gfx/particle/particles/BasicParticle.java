package steve6472.polyground.gfx.particle.particles;

import org.joml.Vector3f;
import org.joml.Vector4f;
import steve6472.polyground.CaveGame;
import steve6472.polyground.Particle;
import steve6472.polyground.gfx.MainRender;
import steve6472.sge.main.game.Tag;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.07.2019
 * Project: SJP
 *
 ***********************/
public class BasicParticle extends Particle
{
	private final List<Tag> tags;

	public BasicParticle(Vector3f motion, Vector3f position, float size, Vector4f color, long lifeTime)
	{
		super(motion, position, size, color, lifeTime);
		tags = new ArrayList<>();
	}

	@Override
	public void tick()
	{
		getPosition().add(getMotion());
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
	public boolean sort()
	{
		return false;
	}

	@Override
	public List<Tag> getTags()
	{
		return tags;
	}
}
