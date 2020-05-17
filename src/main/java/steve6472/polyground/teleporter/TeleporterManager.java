package steve6472.polyground.teleporter;

import org.joml.AABBf;
import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.entity.Player;
import steve6472.polyground.world.World;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.11.2019
 * Project: SJP
 *
 ***********************/
public class TeleporterManager
{
	private final List<Teleporter> teleporters;
	private final World world;

	public TeleporterManager(World world)
	{
		this.world = world;
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

	public void loadTeleporters()
	{
		if (world.worldName == null || world.worldName.isEmpty())
		{
			System.err.println("Can not load teleporters, world name is invalid");
			return;
		}

		JSONObject main;

		File f = new File("game/worlds/" + world.worldName + "/teleporters.json");
		if (!f.exists())
		{
			System.err.println("World has no teleporters!");
			return;
		} else
		{
			try
			{
				BufferedReader reader = new BufferedReader(new FileReader(f));
				String line;
				StringBuilder builder = new StringBuilder();
				while ((line = reader.readLine()) != null)
				{
					builder.append(line);
				}

				main = new JSONObject(builder.toString());

			} catch (IOException e)
			{
				e.printStackTrace();
				return;
			}
		}

		JSONArray array = main.getJSONArray("teleporters");

		Teleporter[] tempTeleporters = new Teleporter[array.length()];
		UUID[] tempUuids = new UUID[array.length()];

		for (int i = 0; i < array.length(); i++)
		{
			JSONObject json = array.getJSONObject(i);
			JSONArray box = json.getJSONArray("aabb");

			UUID uuid = UUID.fromString(json.getString("uuid"));
			UUID otherUuid = UUID.fromString(json.getString("other"));
			AABBf aabb = new AABBf(box.getFloat(0), box.getFloat(1), box.getFloat(2), box.getFloat(3), box.getFloat(4), box.getFloat(5));

			Teleporter teleporter = new Teleporter(uuid);
			teleporter.setAabb(aabb);
			tempTeleporters[i] = teleporter;

			tempUuids[i] = otherUuid;
		}

		for (int i = 0; i < array.length(); i++)
		{
			for (int j = 0; j < array.length(); j++)
			{
				if (tempTeleporters[i].uuid.equals(tempUuids[j]))
				{
					tempTeleporters[i].setOther(tempTeleporters[j]);
				}
			}
		}

		teleporters.addAll(Arrays.asList(tempTeleporters));
	}

	public void saveTeleporters()
	{
		if (world.worldName == null || world.worldName.isEmpty())
		{
			System.err.println("Can not save teleporters, world name is invalid");
			return;
		}

		JSONObject main = new JSONObject();
		JSONArray array = new JSONArray();

		for (Teleporter t : teleporters)
		{
			JSONObject json = new JSONObject();
			JSONArray box = new JSONArray();
			box.put(t.getAabb().minX).put(t.getAabb().minY).put(t.getAabb().minZ).put(t.getAabb().maxX).put(t.getAabb().maxY).put(t.getAabb().maxZ);
			json.put("aabb", box);
			json.put("uuid", t.getUuid());
			json.put("other", t.getOther().getUuid());
			array.put(json);
		}
		main.put("teleporters", array);

		File f = new File("game/worlds/" + world.worldName + "/teleporters.json");
		try
		{
			FileWriter writer = new FileWriter(f);
			main.write(writer, 4, 4);
			writer.flush();
			writer.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
