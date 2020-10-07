package steve6472.polyground;

import org.joml.AABBf;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.block.model.ModelLoader;
import steve6472.polyground.block.model.elements.ElUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
		JSONObject json = new JSONObject(ModelLoader.read(new File("model.bbmodel")));
		json = convert(json, true);
		System.out.println(PrettyJson.prettify(json));
	}

	private static void outlinerRecursive(List<OutlinerTransformation> outliner, JSONObject el)
	{
		UUID uuid = UUID.fromString(el.getString("uuid"));
		List<UUID> children = new ArrayList<>();

		if (el.has("children"))
		{
			if (el.has("rotation"))
			{
				JSONArray ch = el.getJSONArray("children");
				for (int j = 0; j < ch.length(); j++)
				{
					if (ch.get(j) instanceof JSONObject e)
					{
						outlinerRecursive(outliner, e);
					} else
					{
						children.add(UUID.fromString(ch.getString(j)));
					}
				}
				outliner.add(
					new OutlinerTransformation(
						ElUtil.loadVertex3("origin", el),
						ElUtil.loadVertex3("rotation", el),
						uuid, children));
			}
		}
	}

	public static JSONObject convert(JSONObject json, boolean move)
	{
		JSONObject out = new JSONObject();
		JSONArray cubes = new JSONArray();

		JSONArray textures = json.getJSONArray("textures");
		JSONArray elements = json.getJSONArray("elements");

		List<OutlinerTransformation> outliner = new ArrayList<>();

		if (json.has("outliner"))
		{
			JSONArray ou = json.getJSONArray("outliner");
			for (int i = 0; i < ou.length(); i++)
			{
				if (ou.get(i) instanceof JSONObject el)
				{
					outlinerRecursive(outliner, el);
				}
			}
		}

		for (int i = 0; i < elements.length(); i++)
		{
			JSONObject c = new JSONObject();
			JSONObject el = elements.getJSONObject(i);

			JSONArray from = el.getJSONArray("from");
			JSONArray to = el.getJSONArray("to");
			AABBf box = new AABBf(from.getFloat(0), from.getFloat(1), from.getFloat(2), to.getFloat(0), to.getFloat(1), to.getFloat(2));

			Matrix4f rotMat = new Matrix4f();
			rotMat.rotate((float) Math.toRadians(270), 0, 1, 0);
			if (move) rotMat.translate(8f, 0, -8f);

			if (el.has("uuid"))
			{
				UUID uuid = UUID.fromString(el.getString("uuid"));

				for (OutlinerTransformation ot : outliner)
				{
					if (ot.has(uuid))
					{
						Vector3f or = new Vector3f(ot.origin);
						ElUtil.rot(rotMat, or);
						c.put("outliner_origin", new JSONArray().put(or.x).put(or.y).put(or.z));
						c.put("outliner_rot", new JSONArray().put(-ot.rotation.z).put(ot.rotation.y).put(ot.rotation.x));
						break;
					}
				}
			}
			box.transform(rotMat);
			Vector3f point = ElUtil.loadVertex3("origin", el);
			ElUtil.rot(rotMat, point);
			if (el.has("rotation"))
			{
				Vector3f rot = ElUtil.loadVertex3("rotation", el);
				// Swap X and Z cause I rotate the WHOLE model by 270° so the axes swap
				c.put("rotation", new JSONArray().put(-rot.z).put(rot.y).put(rot.x));
			}

			c.put("from", new JSONArray().put(box.minX).put(box.minY).put(box.minZ));
			c.put("to", new JSONArray().put(box.maxX).put(box.maxY).put(box.maxZ));
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
				c.put("isHitbox", c.getString("name").contains("hit"));
				c.put("isCollisionBox", c.getString("name").contains("coll"));
				c.put("hitboxvisible", !c.getString("name").contains("invis"));
			}
			cubes.put(c);
		}
		out.put("cubes", cubes);
		out.put("blockbench", true);
		return out;
	}

	private static String findTexture(JSONArray textures, int id)
	{
		for (int i = 0; i < textures.length(); i++)
		{
			JSONObject texture = textures.getJSONObject(i);
			if (texture.getInt("id") == id)
			{
				String path = texture.getString("path");
				path = path.replace("\\", "/");
				path = path.substring(path.indexOf("textures") + 9);
				path = path.substring(0, path.length() - 4);
				return path;
			}
		}
		return "block/null";
	}

	private record OutlinerTransformation(Vector3f origin, Vector3f rotation, UUID uuid, List<UUID> children)
	{
		public boolean has(UUID uuid)
		{
			return children.contains(uuid);
		}
	}
}
