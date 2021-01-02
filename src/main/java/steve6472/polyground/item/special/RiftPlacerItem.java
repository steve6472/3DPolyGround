package steve6472.polyground.item.special;

import org.joml.Vector3f;
import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.MouseClick;
import steve6472.polyground.commands.coms.RiftCommand;
import steve6472.polyground.entity.item.ItemEntity;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.item.Item;
import steve6472.polyground.world.World;
import steve6472.sge.main.KeyList;

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
	public RiftPlacerItem(JSONObject json, int id)
	{
		super(json, id);
		pos = new Vector3f();
	}

	private Vector3f pos, sel;

	@Override
	public void tickInHand(Player player, ItemEntity entity)
	{
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
	public void onClick(World world, Player player, MouseClick click)
	{
		onClick(player, click);
	}

	@Override
	public boolean canBePlaced()
	{
		if (RiftCommand.rift == null)
			return true;
		return RiftCommand.rift.isFinished();
	}

	@Override
	public void onClick(Player player, MouseClick click)
	{
		if (click.clickRMB())
		{
			player.processNextBlockPlace = false;
			if (RiftCommand.rift != null)
			{
				RiftCommand.rift.getModel().addVertex(new Vector3f(pos));
				RiftCommand.rift.getModel().updateModel();
			}
		}

		if (click.clickMMB() && click.getMods() != KeyList.M_SHIFT)
		{
			if (RiftCommand.rift != null)
			{
				RiftCommand.rift.setPosition(pos);
			}
		}

		if (click.clickMMB() && click.getMods() == KeyList.M_SHIFT)
		{
			if (RiftCommand.rift != null)
			{
				RiftCommand.rift.getCorrection().set(pos).mul(-1.0f);
			}
		}

		if (click.clickLMB())
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

	public Vector3f getPos()
	{
		return pos;
	}
}
