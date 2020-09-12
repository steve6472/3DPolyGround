package steve6472.polyground.item.special;

import org.joml.Vector3f;
import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.commands.coms.TeleCommand;
import steve6472.polyground.entity.player.EnumSlot;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.events.WorldEvent;
import steve6472.polyground.item.Item;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.MouseEvent;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class TeleportPlacerItem extends Item
{
	private Vector3f pos;

	public TeleportPlacerItem(JSONObject json, int id)
	{
		super(json, id);
		pos = new Vector3f();
	}

	@Event
	public void renderPos(WorldEvent.PostRender e)
	{
		if (CaveGame.itemInHand != this)
			return;

		Player player = CaveGame.getInstance().getPlayer();

		float dis = 2.5f;
		pos.set(player.viewDir.x * dis + player.getX(), player.viewDir.y * dis + player.getY() + player.getEyeHeight(), player.viewDir.z * dis + player.getZ());

		if (CaveGame.getInstance().isKeyPressed(KeyList.C))
		{
			pos.set((float) Math.floor((pos.x + 0.25f) * 2.0f) / 2.0f, (float) Math.floor((pos.y + 0.25f) * 2.0f) / 2.0f, (float) Math.floor((pos.z + 0.25f) * 2.0f) / 2.0f);
		}

		CaveGame.getInstance().mainRender.particles.addBasicTickParticle(pos.x, pos.y, pos.z, 0.05f, 0.6f, 1f, 0.6f, 1f);
	}

	@Override
	public void onClick(Player player, EnumSlot slot, MouseEvent click)
	{
		if (TeleCommand.tele == null)
			return;

		if (click.getAction() == KeyList.PRESS)
		{
			if (click.getButton() == KeyList.LMB)
			{
				TeleCommand.tele.setAabb(TeleCommand.tele.getAabb().setMin(pos.x, pos.y, pos.z));
				player.processNextBlockBreak = false;
			}
			else if (click.getButton() == KeyList.RMB)
			{
				TeleCommand.tele.setAabb(TeleCommand.tele.getAabb().setMax(pos.x, pos.y, pos.z));
			}
		}
	}
}
