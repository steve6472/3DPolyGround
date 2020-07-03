package steve6472.polyground.item.special;

import steve6472.polyground.CaveGame;
import steve6472.polyground.commands.coms.RiftCommand;
import steve6472.polyground.entity.Player;
import steve6472.polyground.events.WorldEvent;
import steve6472.polyground.item.Item;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.MouseEvent;
import org.joml.Vector3f;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class RiftPlacerItem extends Item
{
	public RiftPlacerItem(File f, int id)
	{
		super(f, id);
		pos = new Vector3f();
	}

	private Vector3f pos, sel;

	@Event
	public void renderPos(WorldEvent.PostRender e)
	{
		if (CaveGame.itemInHand != this)
			return;

		Player player = CaveGame.getInstance().getPlayer();

		float dis = 3f;
		pos.set(player.viewDir.x * dis + player.getX(), player.viewDir.y * dis + player.getY() + player.getEyeHeight(), player.viewDir.z * dis + player.getZ());

		if (CaveGame.getInstance().isKeyPressed(KeyList.C))
		{
			pos.set((float) Math.floor((pos.x + 0.25f) * 2.0f) / 2.0f, (float) Math.floor((pos.y + 0.25f) * 2.0f) / 2.0f, (float) Math.floor((pos.z + 0.25f) * 2.0f) / 2.0f);
		}

		CaveGame.getInstance().mainRender.particles.addBasicTickParticle(pos.x, pos.y, pos.z, 0.05f, 0.6f, 1f, 0.6f, 1f);

		if (RiftCommand.rift != null)
		{
			for (Vector3f v : RiftCommand.rift.getModel().getVertices())
			{
				if (sel == null)
				{
					sel = v;
					continue;
				}
				if (v.distance(pos) <= sel.distance(pos))
				{
					sel = v;
				}
			}
		}

		if (sel != null)
			CaveGame.getInstance().mainRender.particles.addBasicTickParticle(sel.x, sel.y, sel.z, 0.09f, 1f, 0.6f, 0.6f, 1f);
	}

	@Override
	public void onClick(Player player, MouseEvent click)
	{
		if (click.getAction() == KeyList.PRESS)
		{
			if (click.getButton() == KeyList.RMB)
			{
				player.processNextBlockPlace = false;
				if (RiftCommand.rift != null)
				{
					RiftCommand.rift.getModel().addVertex(new Vector3f(pos));
					RiftCommand.rift.getModel().updateModel();
				}
			}

			if (click.getButton() == KeyList.MMB && click.getMods() != KeyList.M_SHIFT)
			{
				if (RiftCommand.rift != null)
				{
					RiftCommand.rift.setPosition(pos);
				}
			}

			if (click.getButton() == KeyList.MMB && click.getMods() == KeyList.M_SHIFT)
			{
				if (RiftCommand.rift != null)
				{
					RiftCommand.rift.getCorrection().set(pos).mul(-1.0f);
				}
			}

			if (click.getButton() == KeyList.LMB)
			{
				if (RiftCommand.rift != null)
				{
					Collections.reverse(RiftCommand.rift.getModel().getVertices());

					for (Iterator<Vector3f> iterator = RiftCommand.rift.getModel().getVertices().iterator(); iterator.hasNext(); )
					{
						Vector3f v = iterator.next();
						if (v == sel)
						{
							iterator.remove();
							sel = null;
							break;
						}
					}

					Collections.reverse(RiftCommand.rift.getModel().getVertices());

					RiftCommand.rift.getModel().updateModel();
				}
				player.processNextBlockBreak = false;
			}
		}
	}

	public Vector3f getPos()
	{
		return pos;
	}
}