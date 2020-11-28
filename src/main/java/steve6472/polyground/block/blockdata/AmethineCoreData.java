package steve6472.polyground.block.blockdata;

import net.querz.nbt.tag.CompoundTag;
import steve6472.polyground.gfx.model.AnimController;
import steve6472.polyground.gfx.stack.Stack;
import steve6472.polyground.registry.blockdata.BlockDataRegistry;
import steve6472.polyground.registry.model.AnimationRegistry;
import steve6472.polyground.registry.model.ModelRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.10.2020
 * Project: CaveGame
 *
 ***********************/
public class AmethineCoreData extends BlockData
{
	private final AnimController controller;
	private boolean isIdle;

	public AmethineCoreData()
	{
		controller = new AnimController();
		controller.start();
		isIdle = false;
	}

	public void render(Stack stack)
	{
		float c = (float) (Math.cos(Math.toRadians((System.currentTimeMillis() % (3600 * 2)) * 0.05f)) / 4f + 0.70f);
		stack.getEntityTess().color(c, 0, c, 1);

		if (!isIdle)
		{
			AnimationRegistry.AMETHINE_CORE_PLACE.tick(controller);

			if (controller.hasEnded())
			{
				isIdle = true;
				controller.start();
			}
		}

		if (isIdle)
		{
			if (controller.hasEnded())
				controller.start();

			AnimationRegistry.AMETHINE_CORE_IDLE.tick(controller);
		}

		ModelRegistry.AMETHINE_CORE.render(stack);
	}

	@Override
	public CompoundTag write()
	{
		return new CompoundTag();
	}

	@Override
	public void read(CompoundTag tag)
	{
	}

	@Override
	public String getId()
	{
		return BlockDataRegistry.amethineCore.id();
	}
}
