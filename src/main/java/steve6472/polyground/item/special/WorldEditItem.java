package steve6472.polyground.item.special;

import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.PolyUtil;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.entity.Player;
import steve6472.polyground.events.InGameGuiEvent;
import steve6472.polyground.item.Item;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gfx.font.Font;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.MouseEvent;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class WorldEditItem extends Item
{
	public WorldEditItem(File f, int id)
	{
		super(f, id);
		firstPos = new Vector3f();
		secondPos = new Vector3f();
		firstPosScreen = new Vector2f();
		secondPosScreen = new Vector2f();
	}

	private Vector3f firstPos, secondPos;
	private Vector2f firstPosScreen, secondPosScreen;

	@Event
	public void renderPos(InGameGuiEvent.PostRender e)
	{
		if (CaveGame.itemInHand != this)
			return;

		renderPosition(firstPos, firstPosScreen);
		renderPosition(secondPos, secondPosScreen);
	}

	private void renderPosition(Vector3f worldPos, Vector2f screenPos)
	{
		String t = String.format("[%.0f, %.0f, %.0f]", worldPos.x, worldPos.y, worldPos.z);
		int w = Font.getTextWidth(t, 1);

		SpriteRender.fillRect((int) screenPos.x - w / 2, (int) screenPos.y - 2, w + 3, 12, 0.2f, 0.2f, 0.2f, 0.6f);

		Font.render(t, (int) screenPos.x - Font.getTextWidth(t, 1) / 2, (int) screenPos.y);
	}

	@Override
	public void onClick(SubChunk subChunk, BlockData blockData, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getAction() == KeyList.PRESS)
		{
			if (click.getButton() == KeyList.LMB)
			{
				if (click.getMods() == KeyList.M_SHIFT)
					firstPos.set(player.getHitResult().getX() + clickedOn.getXOffset(), player.getHitResult().getY() + clickedOn.getYOffset(), player.getHitResult().getZ() + clickedOn.getZOffset());
				else
					firstPos.set(player.getHitResult().getX(), player.getHitResult().getY(), player.getHitResult().getZ());

				player.processNextBlockBreak = false;
			}
			if (click.getButton() == KeyList.RMB)
			{
				if (click.getMods() == KeyList.M_SHIFT)
					secondPos.set(player.getHitResult().getX() + clickedOn.getXOffset(), player.getHitResult().getY() + clickedOn.getYOffset(), player.getHitResult().getZ() + clickedOn.getZOffset());
				else
					secondPos.set(player.getHitResult().getX(), player.getHitResult().getY(), player.getHitResult().getZ());
				player.processNextBlockPlace = false;
			}
		}
	}

	public Vector3f getFirstPos()
	{
		return firstPos;
	}

	public Vector3f getSecondPos()
	{
		return secondPos;
	}

	@Override
	public void onTickInItemBar(Player player)
	{
		PolyUtil.toScreenPos(new Vector3f(firstPos).add(0.5f, 0.5f, 0.5f), firstPosScreen);
		PolyUtil.toScreenPos(new Vector3f(secondPos).add(0.5f, 0.5f, 0.5f), secondPosScreen);
	}
}
