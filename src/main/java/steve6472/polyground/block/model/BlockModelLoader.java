package steve6472.polyground.block.model;

import org.joml.AABBf;
import org.joml.Matrix4f;
import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.PrettyJson;
import steve6472.polyground.block.BlockTextureHolder;
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
public class BlockModelLoader
{
	private static AABBf createAABB(JSONObject json)
	{
		JSONArray from = json.getJSONArray("from");
		JSONArray to = json.getJSONArray("to");

		return new AABBf(from.getFloat(0) / 16f, from.getFloat(1) / 16f, from.getFloat(2) / 16f, to.getFloat(0) / 16f, to.getFloat(1) / 16f, to.getFloat(2) / 16f);
	}

	public CubeHitbox[] loadCubes(JSONObject json, int rotX, int rotY, int rotZ)
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

	public IElement[] loadElements(JSONObject json, int rotX, int rotY, int rotZ)
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
					BlockTextureHolder.putTexture(texture);
					el.setTexture(BlockTextureHolder.getTextureId(texture));
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
					BlockTextureHolder.putTexture(texture);
					el.setTexture(BlockTextureHolder.getTextureId(texture));
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
					ex.printStackTrace();
					System.err.println(PrettyJson.prettify(cubeObj));
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

	private void addRot(JSONObject json, int rotX, int rotY, int rotZ)
	{
		json.put("point_type", "origin");
		json.put("rotation", new JSONArray().put(rotX).put(rotY).put(rotZ));
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
