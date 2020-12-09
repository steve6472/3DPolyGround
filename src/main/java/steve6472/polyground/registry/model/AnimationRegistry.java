package steve6472.polyground.registry.model;

import steve6472.polyground.entity.model.Model;
import steve6472.polyground.gfx.model.Animation;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class AnimationRegistry
{
	private static final List<Animation> animations = new ArrayList<>();

	public static final Animation CHEST_OPEN = register(ModelRegistry.CHEST, "open");
	public static final Animation AMETHINE_CORE_PLACE = register(ModelRegistry.AMETHINE_CORE, "place");
	public static final Animation AMETHINE_CORE_IDLE = register(ModelRegistry.AMETHINE_CORE, "idle");
	public static final Animation TEST = register(ModelRegistry.TEST, "test");

	public static Animation register(Model model, String name)
	{
		Animation animation = new Animation(model.getName(), name, model);
		animations.add(animation);
		return animation;
	}

	public static Animation getAnimation(Model model, String name)
	{
		for (Animation animation : animations)
		{
			if (animation.getModel() == model && animation.getName().equals(name))
				return animation;
		}

		return null;
	}

	public static void init()
	{

	}

	public static void reload()
	{
		animations.forEach(Animation::reload);
	}
}
