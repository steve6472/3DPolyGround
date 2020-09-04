package steve6472.polyground;

import org.joml.AABBf;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.block.model.BlockModelLoader;
import steve6472.polyground.block.model.elements.ElUtil;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 02.09.2020
 * Project: CaveGame
 *
 ***********************/
public class BlockBenchTranslator
{
	public static void main(String[] args)
	{
		JSONObject json = new JSONObject(BlockModelLoader.read(new File("model.bbmodel")));
		json = convert(json);
		System.out.println(PrettyJson.prettify(json));
	}

	public static JSONObject convert(JSONObject json)
	{
		JSONObject out = new JSONObject();
		JSONArray cubes = new JSONArray();

		JSONArray textures = json.getJSONArray("textures");
		JSONArray elements = json.getJSONArray("elements");
		for (int i = 0; i < elements.length(); i++)
		{
			JSONObject c = new JSONObject();
			JSONObject el = elements.getJSONObject(i);

			JSONArray from = el.getJSONArray("from");
			JSONArray to = el.getJSONArray("to");
			AABBf box = new AABBf(from.getFloat(0), from.getFloat(1), from.getFloat(2), to.getFloat(0), to.getFloat(1), to.getFloat(2));

			Matrix4f rotMat = new Matrix4f();
			rotMat.rotate((float) Math.toRadians(270), 0, 1, 0);
			rotMat.translate(8f, 0, -8f);
			box.transform(rotMat);
			Vector3f point = ElUtil.loadVertex3("origin", el);
			ElUtil.rot(rotMat, point);

			c.put("from", new JSONArray().put(box.minX).put(box.minY).put(box.minZ));
			c.put("to", new JSONArray().put(box.maxX).put(box.maxY).put(box.maxZ));
			if (el.has("rotation")) c.put("rotation", el.get("rotation"));
			c.put("point", new JSONArray().put(point.x).put(point.y).put(point.z));
			if (el.has("faces"))
			{
				JSONObject faces = new JSONObject();
				for (String s : el.getJSONObject("faces").keySet())
				{
					JSONObject face = el.getJSONObject("faces").getJSONObject(s);
					if (face.has("texture") && !face.isNull("texture"))
					{
						face.put("texture", findTexture(textures, face.getInt("texture")));
						if (face.has("rotation") && EnumFace.get(s).isSide())
							face.put("rotation", face.getInt("rotation"));
						if (face.has("tint"))
						{
							int data = face.getInt("tint");
							face.remove("tint");

							face.put("biometint", (data & 1) == 1);
						}
						faces.put(s, face);
					}
				}
				c.put("faces", faces);
			}
			c.put("name", el.get("name"));
			if (!c.getString("name").contains("hitbox") || !c.getString("name").contains("collision"))
			{
				c.put("isHitbox", c.getString("name").contains("hitbox"));
				c.put("isCollisionBox", c.getString("name").contains("collision"));
			}
			cubes.put(c);
		}
		out.put("cubes", cubes);
		return out;
	}

	private static String findTexture(JSONArray textures, int id)
	{
		for (int i = 0; i < textures.length(); i++)
		{
			JSONObject texture = textures.getJSONObject(i);
			if (texture.getInt("id") == id)
			{
				String name = texture.getString("name");
				if (name.endsWith(".png"))
					name = name.substring(0, name.length() - 4);
				return name;
			}
		}
		return "null";
	}
}
