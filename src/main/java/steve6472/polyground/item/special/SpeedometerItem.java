package steve6472.polyground.item.special;

import steve6472.polyground.CaveGame;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.events.InGameGuiEvent;
import steve6472.polyground.item.Item;
import steve6472.sge.gfx.font.Font;
import steve6472.sge.main.events.Event;
import org.joml.Vector2f;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class SpeedometerItem extends Item
{
	public SpeedometerItem(File f, int id)
	{
		super(f, id);
		lastPosition = new Vector2f();
	}

	private Vector2f lastPosition;
	private float distance;

	private byte t;

	@Event
	public void renderSpeed(InGameGuiEvent.PostRender e)
	{
		if (CaveGame.itemInHand == this)
			Font.render(String.format("Speed: %.3f", distance * 60f), CaveGame.getInstance().getWidth() - 96, CaveGame.getInstance().getHeight() - 15);
	}

	@Override
	public void onTickInItemBar(Player player)
	{
		t++;
		if (t == 2)
		{
			distance = lastPosition.distance(player.getPosition().x, player.getPosition().z);
			t = 0;
		}

		lastPosition.set(player.getPosition().x, player.getPosition().z);

	}
}
