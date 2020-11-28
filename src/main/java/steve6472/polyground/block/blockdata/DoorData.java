package steve6472.polyground.block.blockdata;

import net.querz.nbt.tag.CompoundTag;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.properties.enums.EnumLR;
import steve6472.polyground.block.special.DoorBlock;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.gfx.model.AnimController;
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

	public DoorData()
	{
		controller = new AnimController();
	}

	public void render(Stack stack, BlockState state)
	{
		stack.getEntityTess().color(1, 1, 1, 1);

		EnumFace facing = state.get(DoorBlock.FACING);
		EnumLR hinge = state.get(DoorBlock.HINGE);

		boolean invertAnimation = false;

		switch (facing)
		{
			case NORTH ->
			{
				if (hinge == EnumLR.RIGHT)
				{
					stack.rotateY((float) Math.toRadians(90));
					invertAnimation = true;
				}
			}
			case EAST ->
			{
				if (hinge == EnumLR.RIGHT)
				{
					invertAnimation = true;
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
					invertAnimation = true;
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
					invertAnimation = true;
				} else
				{
					stack.rotateY((float) Math.toRadians(90));
				}
			}
		}

		if (invertAnimation)
		{
			if (!isOpen)
				AnimationRegistry.DOOR_OPEN.tick(controller);
			else
				AnimationRegistry.DOOR_CLOSE.tick(controller);
		} else
		{
			if (isOpen)
				AnimationRegistry.DOOR_OPEN.tick(controller);
			else
				AnimationRegistry.DOOR_CLOSE.tick(controller);
		}

		ModelRegistry.DOOR.render(stack);

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
		return tag;
	}

	@Override
	public void read(CompoundTag tag)
	{
		isOpen = tag.getBoolean("open");
	}

	@Override
	public String getId()
	{
		return BlockDataRegistry.door.id();
	}
}
