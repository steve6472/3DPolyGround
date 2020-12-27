package steve6472.polyground;

import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.player.Player;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.11.2020
 * Project: CaveGame
 *
 ***********************/
public class MouseClick
{
	private final BlockState state;
	private final int blockX, blockY, blockZ;
	private final EnumFace face;

	private final float hitX, hitY, hitZ;

	private final float dirX, dirY, dirZ;

	private final EnumMouseButton button;
	private final int mods;

	public MouseClick(MouseEvent event, HitResult hitResult, Player player)
	{
		button = EnumMouseButton.getButton(event.getButton());
		mods = event.getMods();

		if (hitResult != null && hitResult.isHit())
		{
			state = hitResult.getState();
			blockX = hitResult.getX();
			blockY = hitResult.getY();
			blockZ = hitResult.getZ();
			hitX = hitResult.getPx();
			hitY = hitResult.getPy();
			hitZ = hitResult.getPz();
			face = hitResult.getFace();
		} else
		{
			state = Block.AIR.getDefaultState();
			blockX = 0;
			blockY = 0;
			blockZ = 0;
			hitX = Float.NaN;
			hitY = Float.NaN;
			hitZ = Float.NaN;
			face = EnumFace.NONE;
		}

		dirX = player.viewDir.x;
		dirY = player.viewDir.y;
		dirZ = player.viewDir.z;
	}

	public MouseClick(int button, int mods, HitResult hitResult, Player player)
	{
		this.button = EnumMouseButton.getButton(button);
		this.mods = mods;

		if (hitResult.isHit())
		{
			state = hitResult.getState();
			blockX = hitResult.getX();
			blockY = hitResult.getY();
			blockZ = hitResult.getZ();
			hitX = hitResult.getPx();
			hitY = hitResult.getPy();
			hitZ = hitResult.getPz();
			face = hitResult.getFace();
		} else
		{
			state = Block.AIR.getDefaultState();
			blockX = 0;
			blockY = 0;
			blockZ = 0;
			hitX = Float.NaN;
			hitY = Float.NaN;
			hitZ = Float.NaN;
			face = EnumFace.NONE;
		}

		dirX = player.viewDir.x;
		dirY = player.viewDir.y;
		dirZ = player.viewDir.z;
	}

	public boolean hitBlock()
	{
		return !state.isAir();
	}

	public EnumMouseButton getButton()
	{
		return button;
	}

	public BlockState getState()
	{
		return state;
	}

	public int getBlockX()
	{
		return blockX;
	}

	public int getBlockY()
	{
		return blockY;
	}

	public int getBlockZ()
	{
		return blockZ;
	}

	public int getOffsetX()
	{
		return blockX + face.getXOffset();
	}

	public int getOffsetY()
	{
		return blockY + face.getYOffset();
	}

	public int getOffsetZ()
	{
		return blockZ + face.getZOffset();
	}

	public float getHitX()
	{
		return hitX;
	}

	public float getHitY()
	{
		return hitY;
	}

	public float getHitZ()
	{
		return hitZ;
	}

	public float getDirX()
	{
		return dirX;
	}

	public float getDirY()
	{
		return dirY;
	}

	public float getDirZ()
	{
		return dirZ;
	}

	public EnumFace getFace()
	{
		return face;
	}

	public boolean clickLMB()
	{
		return button == EnumMouseButton.LEFT;
	}

	public boolean clickMMB()
	{
		return button == EnumMouseButton.MIDDLE;
	}

	public boolean clickRMB()
	{
		return button == EnumMouseButton.RIGHT;
	}

	public int getMods()
	{
		return mods;
	}

	public boolean heldShift()
	{
		return (mods & KeyList.M_SHIFT) == KeyList.M_SHIFT;
	}

	public boolean heldControl()
	{
		return (mods & KeyList.M_CONTROL) == KeyList.M_CONTROL;
	}

	public boolean heldAlt()
	{
		return (mods & KeyList.M_ALT) == KeyList.M_ALT;
	}
}
