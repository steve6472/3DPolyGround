package steve6472.polyground.gfx.model;

import org.json.JSONObject;
import steve6472.polyground.block.model.ModelLoader;
import steve6472.polyground.entity.model.Model;
import steve6472.polyground.gfx.model.AnimLoader.Bone;
import steve6472.sge.main.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static steve6472.polyground.gfx.model.AnimLoader.Key;
import static steve6472.polyground.gfx.model.AnimLoader.load;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.10.2020
 * Project: CaveGame
 *
 ***********************/
public class Animation
{
	private final String path, name;
	private final Model model;

	private final List<Bone> bones;
	private double dLength;

	public Animation(String path, String name, Model model)
	{
		this.path = path;
		this.name = name;
		this.model = model;
		this.bones = new ArrayList<>();
		reload();
	}

	public void reload()
	{
		bones.clear();
		JSONObject jsonObject = new JSONObject(ModelLoader.read(new File("custom_models/entity/" + path + ".animation.json")));
		dLength = load(jsonObject.getJSONObject("animations").getJSONObject(name), bones);
	}

	public void tick(AnimController controller)
	{
		double time;

		if (controller.isPaused())
			time = controller.getPauseTime();
		else
			time = controller.calculateTime(System.currentTimeMillis());

		time *= controller.getSpeed();

		for (Bone bones : bones)
		{
			Pair<Key, Key> pos = getKeys(bones.positions(), time);
			Pair<Key, Key> rot = getKeys(bones.rotations(), time);
			Pair<Key, Key> sca = getKeys(bones.scales(), time);

			if (rot != null)
			{
				Key a = rot.getA();
				Key b = rot.getB();
				double t = time(a.time(), b.time(), time);

				float vx = (float) Math.toRadians(-lerp(a.pos().x, b.pos().x, t));
				float vy = (float) Math.toRadians(lerp(a.pos().y, b.pos().y, t));
				float vz = (float) Math.toRadians(lerp(a.pos().z, b.pos().z, t));

//				CaveGame.getInstance().inGameGui.chat.addText(String.format("%.3f %.2f %.2f, time: %.2f -> %.2f", time, rot.getA().pos().y, rot.getB().pos().y, t, lerp(a.pos().y, b.pos().y, t)));

				OutlinerElement element = model.getAnimElements().get(bones.name());
				element.rotationX = vx;
				element.rotationY = vy;
				element.rotationZ = vz;
			}

			if (pos != null)
			{
				Key a = pos.getA();
				Key b = pos.getB();
				double t = time(a.time(), b.time(), time);

				float vx = (float) lerp(a.pos().x, b.pos().x, t);
				float vy = (float) lerp(a.pos().y, b.pos().y, t);
				float vz = (float) lerp(a.pos().z, b.pos().z, t);

				OutlinerElement element = model.getAnimElements().get(bones.name());
				element.positionX = vx;
				element.positionY = vy;
				element.positionZ = vz;
			}

			if (sca != null)
			{
				Key a = sca.getA();
				Key b = sca.getB();
				double t = time(a.time(), b.time(), time);

				float vx = (float) lerp(a.pos().x, b.pos().x, t);
				float vy = (float) lerp(a.pos().y, b.pos().y, t);
				float vz = (float) lerp(a.pos().z, b.pos().z, t);

				OutlinerElement element = model.getAnimElements().get(bones.name());
				element.scaleX = vx;
				element.scaleY = vy;
				element.scaleZ = vz;
			}
		}

		if (time >= dLength)
		{
			controller.setRunning(false);
		}
	}

	private int binarySearch(List<Key> keys, double target)
	{
		if (keys.size() == 0)
			return -1;
		else if (keys.size() == 1)
			return 1;

		if (target <= keys.get(0).time())
			return 0;
		else if (target > keys.get(keys.size() - 1).time())
			return keys.size() - 1;

		int min = 0;
		int max = keys.size() - 1;

		while (min <= max)
		{
			int mid = (min + max) / 2;

			if (target < keys.get(mid).time())
			{
				max = mid - 1;
			} else if (target > keys.get(mid).time())
			{
				min = mid + 1;
			} else
			{
				return mid;
			}
		}

		// Returns closest double, that I do not want
//		return (keys.get(min).time() - target) < (target - keys.get(max).time()) ? min : max;
		// Returns the lower closest value
		return max;
	}

	private Pair<Key, Key> getKeys(List<Key> keys, double time)
	{
		int index = binarySearch(keys, time);
		if (index == -1)
			return null;

		if (index == keys.size())
			index -= 1;

		if (index + 1 >= keys.size())
			return new Pair<>(keys.get(index), keys.get(index));

		return new Pair<>(keys.get(index), keys.get(index + 1));
	}

	public void print()
	{
		for (Bone b : bones)
		{
			System.out.println(b.name() + ":");
			System.out.println("\tPositions:");
			for (Key p : b.positions())
			{
				System.out.println("\t\tTime: " + p.time() + " : " + p.pos());
			}
			System.out.println("\tRotations:");
			for (Key p : b.rotations())
			{
				System.out.println("\t\tTime: " + p.time() + " : " + p.pos());
			}
			System.out.println("\tScales:");
			for (Key p : b.scales())
			{
				System.out.println("\t\tTime: " + p.time() + " : " + p.pos());
			}
		}
	}

	private double time(double start, double end, double time)
	{
		// Prevent NaN
		if (time - start == 0 || end - start == 0)
			return 0;

		return 1.0 / ((end - start) / (time - start));
	}

	private static double lerp(double start, double end, double value)
	{
		return start + value * (end - start);
	}
}
