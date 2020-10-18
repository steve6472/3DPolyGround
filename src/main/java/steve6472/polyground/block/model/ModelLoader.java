package steve6472.polyground.block.model;

import org.joml.AABBf;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.BlockBenchTranslator;
import steve6472.polyground.CaveGame;
import steve6472.polyground.PrettyJson;
import steve6472.polyground.block.BlockAtlas;
import steve6472.polyground.block.model.elements.CubeElement;
import steve6472.polyground.block.model.elements.ElUtil;
import steve6472.polyground.block.model.elements.PlaneElement;
import steve6472.polyground.block.model.elements.TriangleElement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.09.2019
 * Project: SJP
 *
 ***********************/
public class ModelLoader
{
	public static JSONObject loadJSONModel(String path)
	{
		return load("game/objects/models/" + path + ".json", true);
	}

	public static boolean t = false;

	public static JSONObject load(String path, boolean move)
	{
		JSONObject json = null;

		try
		{
			json = new JSONObject(ModelLoader.read(new File(path)));

			if (json.has("meta"))
			{
				json = BlockBenchTranslator.convert(json, move);
			}
		} catch (Exception e)
		{
			System.err.println("Could not load model " + path);
			e.printStackTrace();
			CaveGame.getInstance().exit();
			System.exit(0);
		}

		return json;
	}

	private static AABBf createAABB(JSONObject json)
	{
		JSONArray from = json.getJSONArray("from");
		JSONArray to = json.getJSONArray("to");

		return new AABBf(from.getFloat(0) / 16f, from.getFloat(1) / 16f, from.getFloat(2) / 16f, to.getFloat(0) / 16f, to.getFloat(1) / 16f, to.getFloat(2) / 16f);
	}

	public static CubeHitbox[] loadCubes(JSONObject json, int rotX, int rotY, int rotZ)
	{
		if (!json.has("cubes"))
			return new CubeHitbox[0];

		JSONArray array = json.getJSONArray("cubes");

		List<CubeHitbox> cubes = new ArrayList<>(array.length());

		for (int i = 0; i < array.length(); i++)
		{
			JSONObject c = array.getJSONObject(i);
			if (c.optBoolean("isHitbox", true) || c.optBoolean("isCollisionBox", true))
			{
				AABBf aabb = createAABB(c);

				// Rotate cube
				Matrix4f rotMat = ElUtil.rotMat(0.5f, 0.5f, 0.5f, rotX, rotY, rotZ);
				aabb.transform(rotMat);

				CubeHitbox cube = new CubeHitbox(aabb);

				cube.loadFromJson(c);
				cubes.add(cube);
			}
		}

		return cubes.toArray(new CubeHitbox[0]);
	}

	public static IElement[] loadElements(JSONObject json, int rotX, int rotY, int rotZ)
	{
		List<IElement> elements = new ArrayList<>();

		if (json.has("triangles"))
		{
			JSONArray tris = json.getJSONArray("triangles");
			for (int i = 0; i < tris.length(); i++)
			{
				JSONObject triObj = tris.getJSONObject(i);
				addRot(triObj, rotX, rotY, rotZ);
				TriangleElement el = new TriangleElement(triObj.optString("name", "triangle"));
				el.load(triObj);
				elements.add(el);

				if (triObj.has("texture"))
				{
					String texture = triObj.getString("texture");
					BlockAtlas.putTexture(texture);
					el.setTexture(BlockAtlas.getTextureId(texture));
				}
			}
		}

		if (json.has("planes"))
		{
			JSONArray planes = json.getJSONArray("planes");
			for (int i = 0; i < planes.length(); i++)
			{
				JSONObject planeObj = planes.getJSONObject(i);
				addRot(planeObj, rotX, rotY, rotZ);
				PlaneElement el = new PlaneElement();
				el.load(planeObj);
				elements.add(el);

				if (planeObj.has("texture"))
				{
					String texture = planeObj.getString("texture");
					BlockAtlas.putTexture(texture);
					el.setTexture(BlockAtlas.getTextureId(texture));
				}
			}
		}

		if (json.has("cubes"))
		{
			JSONArray cubes = json.getJSONArray("cubes");
			for (int i = 0; i < cubes.length(); i++)
			{
				JSONObject cubeObj = cubes.getJSONObject(i);
				if (rotX != 0 || rotY != 0 || rotZ != 0)
				{
					addRot(cubeObj, rotX, rotY, rotZ);
				}
				CubeElement el = new CubeElement();
				try
				{
					el.load(cubeObj);
				} catch (Exception ex)
				{
					System.err.println(PrettyJson.prettify(cubeObj));
					ex.printStackTrace();
				}
				if (rotY != 0 && rotY % 90 == 0)
				{
					for (int j = 0; j < (rotY % 360) / 90; j++)
					{
						el.cycleFacesY();
					}
				}
				elements.add(el);
			}
		}

		return elements.isEmpty() ? null : elements.toArray(IElement[]::new);
	}

	private static void addRot(JSONObject json, int rotX, int rotY, int rotZ)
	{
		json.put("point_type", "origin");
		if (json.has("rotation"))
		{
			JSONArray r = json.getJSONArray("rotation");
			json.put("rotation", new JSONArray().put(r.getFloat(0) + rotX).put(r.getFloat(1) + rotY).put(r.getFloat(2) + rotZ));

			if (json.has("point"))
			{
				Vector3f point = ElUtil.loadVertex3("point", json);
				Matrix4f rotMat = new Matrix4f();
				rotMat.translate(8f, 8f, 8f);
				rotMat.rotate(new Quaternionf().rotateXYZ(rotX, rotY, rotZ));
				rotMat.translate(-8f, -8f, -8f);
				rotMat.transformPosition(point);
				json.put("point", new JSONArray().put(point.x).put(point.y).put(point.z));
				json.put("point_type", "point");
			}
		} else
		{
			json.put("rotation", new JSONArray().put(rotX).put(rotY).put(rotZ));
		}
	}

	public static String read(File f)
	{
		StringBuilder sb = new StringBuilder();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(f));

			boolean endOfTheFile = false;
			while (!endOfTheFile)
			{
				String line = br.readLine();

				if (line == null)
				{
					endOfTheFile = true;
				} else
				{
					sb.append(line);
				}
			}

			br.close();

		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return sb.toString();
	}
}
