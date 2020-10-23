package steve6472.polyground.block.blockdata;

import net.querz.nbt.tag.CompoundTag;
import steve6472.polyground.entity.model.CustomModel;
import steve6472.polyground.entity.model.loader.AnimController;
import steve6472.polyground.entity.model.loader.Animation;
import steve6472.polyground.gfx.stack.Stack;
import steve6472.polyground.registry.blockdata.BlockDataRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.10.2020
 * Project: CaveGame
 *
 ***********************/
public class AmethineCoreData extends BlockData
{
	private static final CustomModel MODEL = new CustomModel("amethine_core");
	private static final Animation placeAnimation = new Animation("amethine_core", "place", MODEL.getAnimElements());
	private static final Animation idleAnimation= new Animation("amethine_core", "idle", MODEL.getAnimElements());

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
		stack.translate(0.5f, 0, 0.5f);

		float c = (float) (Math.cos(Math.toRadians((System.currentTimeMillis() % (3600 * 2)) * 0.05f)) / 4f + 0.70f);
		stack.color(c, 0, c, 1);

		if (!isIdle)
		{
			placeAnimation.tick(controller);

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

			idleAnimation.tick(controller);
		}

		MODEL.render(stack);
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
