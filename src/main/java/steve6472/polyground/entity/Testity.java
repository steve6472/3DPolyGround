package steve6472.polyground.entity;

import org.joml.Vector3f;
import steve6472.polyground.entity.interfaces.IAdvancedRender;
import steve6472.polyground.entity.interfaces.ITickable;
import steve6472.polyground.entity.model.CustomModel;
import steve6472.polyground.entity.model.loader.AnimController;
import steve6472.polyground.entity.model.loader.Animation;
import steve6472.polyground.gfx.stack.Stack;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2020
 * Project: CaveGame
 *
 ***********************/
public class Testity extends EntityBase implements IAdvancedRender, ITickable
{
	//	private static final ClaySolderModel MODEL = new ClaySolderModel();
	private static final CustomModel MODEL = new CustomModel("chest");
	private static final Animation animation = new Animation("chest", "open", MODEL.getAnimElements());

	private final AnimController controller;

	public Testity(Vector3f pos)
	{
		setPosition(pos);
		controller = new AnimController();
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

		if (controller.hasEnded())
			controller.start();
	}

	@Override
	public void render(Stack stack)
	{
		animation.tick(controller);
		stack.color(0.6f, 0.6f, 0.6f, 1f);
		MODEL.update();
		MODEL.render(stack);
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
