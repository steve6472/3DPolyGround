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
public class ConveyorBeltData extends BlockData
{
	private final AnimController controller;

	public ConveyorBeltData()
	{
		controller = new AnimController();
		controller.start();
		controller.setLoop(true);
	}

	public void render(Stack stack)
	{
		stack.getEntityTess().color(1, 1, 1, 1);

		AnimationRegistry.CONVEYOR_BELT_MOVE.tick(controller);

		ModelRegistry.CONVEYOR_BELT.render(stack);
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
		return BlockDataRegistry.conveyorBelt.id();
	}
}
