package steve6472.polyground.events;

import steve6472.polyground.EnumFace;
import steve6472.polyground.EnumMouseButton;
import steve6472.polyground.MouseClick;
import steve6472.polyground.block.states.BlockState;
import steve6472.sge.main.events.AbstractEvent;
import steve6472.sge.main.events.MouseEvent;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 27.12.2020
 * Project: CaveGame
 *
 ***********************/
public class MouseClickEvent extends AbstractEvent
{
	private final MouseClick mouseClick;
	private final MouseEvent event;

	public MouseClickEvent(MouseClick mouseClick, MouseEvent event)
	{
		this.mouseClick = mouseClick;
		this.event = event;
	}

	public MouseClick getClick()
	{
		return mouseClick;
	}

	public int getAction()
	{
		return event.getAction();
	}

	public MouseEvent getEvent()
	{
		return event;
	}

	public boolean hitBlock()
	{
		return mouseClick.hitBlock();
	}

	public EnumMouseButton getButton()
	{
		return mouseClick.getButton();
	}

	public BlockState getState()
	{
		return mouseClick.getState();
	}

	public int getBlockX()
	{
		return mouseClick.getBlockX();
	}

	public int getBlockY()
	{
		return mouseClick.getBlockY();
	}

	public int getBlockZ()
	{
		return mouseClick.getBlockZ();
	}

	public int getOffsetX()
	{
		return mouseClick.getOffsetX();
	}

	public int getOffsetY()
	{
		return mouseClick.getOffsetY();
	}

	public int getOffsetZ()
	{
		return mouseClick.getOffsetZ();
	}

	public float getHitX()
	{
		return mouseClick.getHitX();
	}

	public float getHitY()
	{
		return mouseClick.getHitY();
	}

	public float getHitZ()
	{
		return mouseClick.getHitZ();
	}

	public float getDirX()
	{
		return mouseClick.getDirX();
	}

	public float getDirY()
	{
		return mouseClick.getDirY();
	}

	public float getDirZ()
	{
		return mouseClick.getDirZ();
	}

	public EnumFace getFace()
	{
		return mouseClick.getFace();
	}

	public boolean clickLMB()
	{
		return mouseClick.clickLMB();
	}

	public boolean clickMMB()
	{
		return mouseClick.clickMMB();
	}

	public boolean clickRMB()
	{
		return mouseClick.clickRMB();
	}

	public int getMods()
	{
		return mouseClick.getMods();
	}

	public boolean heldShift()
	{
		return mouseClick.heldShift();
	}

	public boolean heldControl()
	{
		return mouseClick.heldControl();
	}

	public boolean heldAlt()
	{
		return mouseClick.heldAlt();
	}
}
