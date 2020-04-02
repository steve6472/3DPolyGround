package steve6472.polyground.teleporter;

import steve6472.polyground.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.11.2019
 * Project: SJP
 *
 ***********************/
public class TeleporterManager
{
	private List<Teleporter> teleporters;

	public TeleporterManager()
	{
		teleporters = new ArrayList<>();
	}

	public List<Teleporter> getTeleporters()
	{
		return teleporters;
	}

	public void tick(Player player)
	{
		for (Teleporter t : teleporters)
		{
			t.test(player);
		}
	}
}
