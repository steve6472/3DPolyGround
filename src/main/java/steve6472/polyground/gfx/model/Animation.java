package steve6472.polyground.gfx.model;

import org.joml.Vector3f;
import org.json.JSONObject;
import steve6472.polyground.block.model.ModelLoader;
import steve6472.polyground.entity.model.Model;
import steve6472.polyground.gfx.model.AnimLoader.Bone;
import steve6472.sge.main.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static steve6472.polyground.gfx.model.AnimLoader.load;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.10.2020
 * Project: CaveGame
 *
 ***********************/
public class Animation
{
	private static final Vector3f TEMP = new Vector3f();

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
			int posIndex = binarySearch(bones.positions(), time);
			int rotIndex = binarySearch(bones.rotations(), time);
			int scaIndex = binarySearch(bones.scales(), time);

			Pair<IKey, IKey> pos, rot, sca;

			if (controller.isReversed())
			{
				pos = getKeysReversed(bones.positions(), posIndex);
				rot = getKeysReversed(bones.rotations(), rotIndex);
				sca = getKeysReversed(bones.scales(), scaIndex);
			} else
			{
				pos = getKeys(bones.positions(), posIndex);
				rot = getKeys(bones.rotations(), rotIndex);
				sca = getKeys(bones.scales(), scaIndex);
			}

			if (rot != null)
			{
				animate(rot, time, posIndex, bones.positions(), controller.isReversed());

				OutlinerElement element = model.getAnimElements().get(bones.name());
				element.rotationX = (float) Math.toRadians(-TEMP.x());
				element.rotationY = (float) Math.toRadians(-TEMP.y());
				element.rotationZ = (float) Math.toRadians(TEMP.z());
			}

			if (pos != null)
			{
				animate(pos, time, posIndex, bones.positions(), controller.isReversed());

				OutlinerElement element = model.getAnimElements().get(bones.name());
				element.positionX = TEMP.x();
				element.positionY = TEMP.y();
				element.positionZ = TEMP.z();
			}

			if (sca != null)
			{
				animate(sca, time, posIndex, bones.scales(), controller.isReversed());

				OutlinerElement element = model.getAnimElements().get(bones.name());
				element.scaleX = TEMP.x();
				element.scaleY = TEMP.y();
				element.scaleZ = TEMP.z();
			}
		}

		if (time >= dLength)
		{
			if (controller.isLooping())
				controller.start();
			else
				controller.setRunning(false);
		}
	}

	private void animate(Pair<IKey, IKey> pair, double time, int index, List<IKey> keys, boolean reverse)
	{
		IKey before = pair.a();
		IKey after = pair.b();
		double t = AnimUtil.time(before.time(), after.time(), time);

		float vx;
		float vy;
		float vz;

		if (before.keyType() == EnumKeyType.CATMULL_ROM)
		{
			IKey past, future;

			if (index > 0)
				past = keys.get(index - 1);
			else
				past = before;

			if (index + 2 < keys.size())
				future = keys.get(index + 2);
			else
				future = after;

			if (reverse)
			{
				vx = (float) AnimUtil.catmullLerp(future.x(t), after.x(t), before.x(t), past.x(t), t);
				vy = (float) AnimUtil.catmullLerp(future.y(t), after.y(t), before.y(t), past.y(t), t);
				vz = (float) AnimUtil.catmullLerp(future.z(t), after.z(t), before.z(t), past.z(t), t);
			} else
			{
				vx = (float) AnimUtil.catmullLerp(past.x(t), before.x(t), after.x(t), future.x(t), t);
				vy = (float) AnimUtil.catmullLerp(past.y(t), before.y(t), after.y(t), future.y(t), t);
				vz = (float) AnimUtil.catmullLerp(past.z(t), before.z(t), after.z(t), future.z(t), t);
			}
		} else
		{
			if (reverse)
			{
				vx = (float) AnimUtil.lerp(after.x(t), before.x(t), t);
				vy = (float) AnimUtil.lerp(after.y(t), before.y(t), t);
				vz = (float) AnimUtil.lerp(after.z(t), before.z(t), t);
			} else
			{
				vx = (float) AnimUtil.lerp(before.x(t), after.x(t), t);
				vy = (float) AnimUtil.lerp(before.y(t), after.y(t), t);
				vz = (float) AnimUtil.lerp(before.z(t), after.z(t), t);
			}
		}

		TEMP.set(vx, vy, vz);
	}

	private int binarySearch(List<IKey> keys, double target)
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

	private Pair<IKey, IKey> getKeys(List<IKey> keys, int index)
	{
		if (index == -1)
			return null;

		if (index == keys.size())
			index -= 1;

		if (index + 1 >= keys.size())
			return new Pair<>(keys.get(index), keys.get(index));

		return new Pair<>(keys.get(index), keys.get(index + 1));
	}

	private Pair<IKey, IKey> getKeysReversed(List<IKey> keys, int index)
	{
		if (index == -1)
			return null;

		index = keys.size() - index;

		if (index == keys.size())
		{
			return new Pair<>(keys.get(index - 2), keys.get(index - 1));
		}

		if (index - 1 <= 0)
		{
			return new Pair<>(keys.get(0), keys.get(0));
		}

		return new Pair<>(keys.get(index), keys.get(index - 1));
	}

	public String getPath()
	{
		return path;
	}

	public String getName()
	{
		return name;
	}

	public Model getModel()
	{
		return model;
	}
}
