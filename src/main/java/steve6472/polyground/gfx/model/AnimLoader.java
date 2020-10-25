package steve6472.polyground.gfx.model;

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
		List<IKey> pos = loadKeys(boneJson, "position");
		List<IKey> rot = loadKeys(boneJson, "rotation");
		List<IKey> siz = loadKeys(boneJson, "scale");

		Bone bone = new Bone(boneName, pos, rot, siz);
		bones.add(bone);
	}

	private static List<IKey> loadKeys(JSONObject json, String keyType)
	{
		List<IKey> list = new ArrayList<>();

		if (!json.has(keyType))
			return list;

		// type has only one value
		if (json.get(keyType) instanceof JSONArray arr)
		{
			list.add(
				new Key(
					0,
					loadValue(arr.get(0)),
					loadValue(arr.get(1)),
					loadValue(arr.get(2)),
					EnumKeyType.LINEAR
				)
			);
			return list;
		}

		JSONObject type = json.getJSONObject(keyType);

		for (String s : type.keySet())
		{
			double time = Double.parseDouble(s);
			if (type.get(s) instanceof JSONObject o)
			{
				JSONArray post = o.getJSONArray("post");
				list.add(
					new Key(
						time,
						loadValue(post.get(0)),
						loadValue(post.get(1)),
						loadValue(post.get(2)),
						EnumKeyType.CATMULL_ROM
					)
				);

			} else if (type.get(s) instanceof JSONArray arr)
			{
				list.add(
					new Key(
						time,
						loadValue(arr.get(0)),
						loadValue(arr.get(1)),
						loadValue(arr.get(2)),
						EnumKeyType.LINEAR
					)
				);
			}
		}

		list.sort(Comparator.comparingDouble(IKey::time));

		return list;
	}

	private static IKeyValue loadValue(Object o)
	{
		if (o instanceof Number n)
		{
			return new NumericValue(n.floatValue());
		} else if (o instanceof String s)
		{
			return new ExpressionValue(s);
		}

		throw new IllegalArgumentException(o.getClass().getCanonicalName());
	}

	public record Bone(String name, List<IKey> positions, List<IKey> rotations, List<IKey> scales)
	{}
}
