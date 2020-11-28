package steve6472.polyground.entity;

import org.joml.Vector3f;
import steve6472.polyground.entity.interfaces.IAdvancedRender;
import steve6472.polyground.entity.interfaces.ITickable;
import steve6472.polyground.gfx.model.AnimController;
import steve6472.polyground.gfx.stack.Stack;
import steve6472.polyground.registry.model.AnimationRegistry;
import steve6472.polyground.registry.model.ModelRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2020
 * Project: CaveGame
 *
 ***********************/
public class Testity extends EntityBase implements IAdvancedRender, ITickable
{
	private final AnimController controller;

	public Testity(Vector3f pos)
	{
		setPosition(pos);
		controller = new AnimController();
		controller.setLoop(true);
	}

	@Override
	public void tick()
	{
/*
		if (RandomUtil.decide(60 * 5))
		{
			getMotion().add(RandomUtil.randomFloat(-0.1f, 0.1f), 0, RandomUtil.randomFloat(-0.1f, 0.1f));

			setYaw((float) Math.atan2(getMotionX(), getMotionZ()));
		}

		addPosition(getMotion());
		getMotion().mul(0.95f);*/
	}

	@Override
	public void render(Stack stack)
	{
		position(stack);
		rotation(stack);

		AnimationRegistry.TEST.tick(controller);
		stack.getEntityTess().color(1, 1, 1, 1f);
		ModelRegistry.TEST.render(stack);
	}

	@Override
	public boolean isDead()
	{
		return false;
	}

	@Override
	public String getName()
	{
		return "Testity";
	}
}
