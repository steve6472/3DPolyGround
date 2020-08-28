package steve6472.polyground.block.model;

import org.joml.AABBf;
import org.joml.Matrix4f;
import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.BlockTextureHolder;
import steve6472.polyground.block.model.elements.CubeElement;
import steve6472.polyground.block.model.elements.PlaneElement;
import steve6472.polyground.block.model.elements.TriangleElement;
import steve6472.polyground.block.properties.enums.EnumAxis;

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
	public CubeHitbox[] loadModel(String name, int rot_y)
	{
		JSONObject json = null;

		try
		{
//			json = new JSONObject(read(new File(MainApp.class.getResource("/models/" + name + ".json").getFile())));
			json = new JSONObject(read(new File("game/objects/models/" + name + ".json")));
		} catch (Exception e)
		{
			System.err.println("Could not load block model " + name);
			e.printStackTrace();
			CaveGame.getInstance().exit();
			System.exit(0);
		}

		if (json.has("parent"))
		{
			throw new IllegalArgumentException("Parent is currently not supported!");
//			return createFromParent(json);
		}

		try
		{
			return loadCubes(json, rot_y);
		} catch (Exception e)
		{
			System.err.println("Loading cubes failed while loading  " + name);
			e.printStackTrace();
			CaveGame.getInstance().exit();
			System.exit(0);
			return null;
		}
	}

	private static AABBf createAABB(JSONObject json)
	{
		JSONArray from = json.getJSONArray("from");
		JSONArray to = json.getJSONArray("to");

		return new AABBf(from.getFloat(0) / 16f, from.getFloat(1) / 16f, from.getFloat(2) / 16f, to.getFloat(0) / 16f, to.getFloat(1) / 16f, to.getFloat(2) / 16f);
	}

	private CubeHitbox[] loadCubes(JSONObject json, int rot_y)
	{
		if (!json.has("cubes"))
			return new CubeHitbox[0];

		JSONArray array = json.getJSONArray("cubes");

		CubeHitbox[] cubes = new CubeHitbox[array.length()];

		for (int i = 0; i < array.length(); i++)
		{
			JSONObject c = array.getJSONObject(i);
			AABBf aabb = createAABB(c);

			// Rotate cube
			Matrix4f rotMat = new Matrix4f();
			rotMat.translate(0.5f, 0, 0.5f);
			//noinspection IntegerDivisionInFloatingPointContext
			rotMat.rotate((float) Math.toRadians((rot_y / 90) * 90), 0, 1, 0);
			rotMat.translate(-0.5f, 0, -0.5f);
			aabb.transform(rotMat);

			CubeHitbox cube = new CubeHitbox(aabb);

			cube.loadFromJson(c);
			cubes[i] = cube;
		}

		return cubes;
	}

	public IElement[] loadElements(String path, int rot_y, boolean uvLock)
	{
		JSONObject json = null;

		try
		{
			json = new JSONObject(read(new File("game/objects/models/" + path + ".json")));
		} catch (Exception e)
		{
			System.err.println("Could not load block model (triangles) " + path);
			e.printStackTrace();
			CaveGame.getInstance().exit();
			System.exit(0);
		}

		List<IElement> elements = new ArrayList<>();

		if (json.has("triangles"))
		{
			JSONArray tris = json.getJSONArray("triangles");
			for (int i = 0; i < tris.length(); i++)
			{
				JSONObject triObj = tris.getJSONObject(i);
				addRot(triObj, rot_y);
				TriangleElement el = new TriangleElement();
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
				addRot(planeObj, rot_y);
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
				if (rot_y != 0)
				{
					addRot(cubeObj, rot_y);
					if (uvLock && rot_y % 90 == 0)
					{
						if (cubeObj.has("faces"))
						{
							for (EnumFace f : EnumFace.getFaces())
							{
								if (f.getAxis() == EnumAxis.Y)
								{
									if (cubeObj.getJSONObject("faces").has(f.getName()))
									{
										JSONObject face = cubeObj.getJSONObject("faces").getJSONObject(f.getName());
										float r = face.optFloat("rotation", 0);
										face.put("rotation", r - rot_y);
									}
								}
							}
						}
					}
				}
				CubeElement el = new CubeElement();
				el.load(cubeObj);
				if (rot_y != 0 && rot_y % 90 == 0)
				{
					for (int j = 0; j < (rot_y % 360) / 90; j++)
					{
						el.cycleFaces();
					}
				}
				elements.add(el);
			}
		}

		return elements.isEmpty() ? null : elements.toArray(IElement[]::new);
	}

	private void addRot(JSONObject json, int rot)
	{
		if (!json.has("rot_y"))
		{
			json.put("point_type", "origin");
			json.put("rot_y", rot % 360.0f);
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
