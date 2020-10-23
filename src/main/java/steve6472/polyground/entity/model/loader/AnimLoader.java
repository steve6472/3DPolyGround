package steve6472.polyground.entity.model.loader;

import org.joml.Vector3f;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.10.2020
 * Project: CaveGame
 *
 ***********************/
public class AnimLoader
{
	public static double load(JSONObject animation, List<Bone> boneList)
	{
		double length = animation.getDouble("animation_length");
		JSONObject bones = animation.getJSONObject("bones");

		for (String s : bones.keySet())
		{
			loadBone(bones.getJSONObject(s), s, boneList);
		}

		return length;
	}

	private static void loadBone(JSONObject boneJson, String boneName, List<Bone> bones)
	{
		List<Key> pos = loadKeys(boneJson, "position");
		List<Key> rot = loadKeys(boneJson, "rotation");
		List<Key> siz = loadKeys(boneJson, "scale");

		Bone bone = new Bone(boneName, pos, rot, siz);
		bones.add(bone);
	}

	private static List<Key> loadKeys(JSONObject json, String keyType)
	{
		List<Key> list = new ArrayList<>();

		if (!json.has(keyType))
			return list;

		if (json.get(keyType) instanceof JSONArray arr)
		{
			list.add(new Key(0, new Vector3f(arr.getFloat(0),
				arr.getFloat(1),
				arr.getFloat(2))));
			return list;
		}

		JSONObject type = json.getJSONObject(keyType);

		for (String s : type.keySet())
		{
			JSONArray arr = type.getJSONArray(s);
			list.add(new Key(Double.parseDouble(s),
				new Vector3f(arr.getFloat(0),
					arr.getFloat(1),
					arr.getFloat(2))));
		}

		list.sort(Comparator.comparingDouble(a -> a.time));

		return list;
	}

	public record Key(double time, Vector3f pos)
	{}

	public record Bone(String name, List<Key> positions, List<Key> rotations, List<Key> scales)
	{}
}
