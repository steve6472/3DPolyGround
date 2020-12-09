package steve6472.polyground.block.blockdata;

import net.querz.nbt.tag.CompoundTag;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.properties.enums.EnumLR;
import steve6472.polyground.block.special.DoorBlock;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.model.Model;
import steve6472.polyground.gfx.model.AnimController;
import steve6472.polyground.gfx.model.Animation;
import steve6472.polyground.gfx.stack.Stack;
import steve6472.polyground.registry.blockdata.BlockDataRegistry;
import steve6472.polyground.registry.model.AnimationRegistry;
import steve6472.polyground.registry.model.ModelRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 24.10.2020
 * Project: CaveGame
 *
 ***********************/
public class DoorData extends BlockData
{
	private final AnimController controller;
	private boolean isOpen;
	private Animation open, close;
	private Model model;

	public DoorData()
	{
		controller = new AnimController();
		model = ModelRegistry.getModel("door/oak");
		open = AnimationRegistry.getAnimation(model, "open");
		close = AnimationRegistry.getAnimation(model, "close");
	}

	public DoorData(Model model, Animation open, Animation close)
	{
		controller = new AnimController();
		this.open = open;
		this.close = close;
		this.model = model;
	}

	public void render(Stack stack, BlockState state)
	{
		stack.getEntityTess().color(1, 1, 1, 1);

		EnumFace facing = state.get(DoorBlock.FACING);
		EnumLR hinge = state.get(DoorBlock.HINGE);

		boolean reverseAnimation = false;

		switch (facing)
		{
			case NORTH ->
			{
				if (hinge == EnumLR.RIGHT)
				{
					stack.rotateY((float) Math.toRadians(90));
					reverseAnimation = true;
				}
			}
			case EAST ->
			{
				if (hinge == EnumLR.RIGHT)
				{
					reverseAnimation = true;
				} else
				{
					stack.rotateY((float) Math.toRadians(270));
				}
			}
			case SOUTH ->
			{
				if (hinge == EnumLR.RIGHT)
				{
					stack.rotateY((float) Math.toRadians(270));
					reverseAnimation = true;
				} else
				{
					stack.rotateY((float) Math.toRadians(180));
				}
			}
			case WEST ->
			{
				if (hinge == EnumLR.RIGHT)
				{
					stack.rotateY((float) Math.toRadians(180));
					reverseAnimation = true;
				} else
				{
					stack.rotateY((float) Math.toRadians(90));
				}
			}
		}

		controller.setReverse(reverseAnimation);

		if (isOpen)
			open.tick(controller);
		else
			close.tick(controller);

		model.render(stack);
	}

	public void open()
	{
		isOpen = true;
		controller.start();
	}

	public void close()
	{
		isOpen = false;
		controller.start();
	}

	@Override
	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		tag.putBoolean("open", isOpen);
		tag.putString("model", model.getName());
		return tag;
	}

	@Override
	public void read(CompoundTag tag)
	{
		isOpen = tag.getBoolean("open");
		if (tag.containsKey("model"))
		{
			model = ModelRegistry.getModel(tag.getString("model"));
			open = AnimationRegistry.getAnimation(model, "open");
			close = AnimationRegistry.getAnimation(model, "close");
		}
	}

	@Override
	public String getId()
	{
		return BlockDataRegistry.door.id();
	}
}
