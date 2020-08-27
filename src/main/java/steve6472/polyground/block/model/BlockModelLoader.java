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
import steve6472.polyground.block.model.faceProperty.AutoUVFaceProperty;
import steve6472.polyground.block.model.faceProperty.RotationFaceProperty;
import steve6472.polyground.block.model.faceProperty.TextureFaceProperty;
import steve6472.polyground.block.model.faceProperty.UVFaceProperty;
import steve6472.polyground.block.model.faceProperty.condition.ConditionFaceProperty;
import steve6472.polyground.registry.face.FaceRegistry;

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
	public Cube[] loadModel(String name, int rot)
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
			return loadCubes(json, rot);
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

	private Cube[] loadCubes(JSONObject json, int rot)
	{
		if (!json.has("cubes"))
			return new Cube[0];

		JSONArray array = json.getJSONArray("cubes");

		Cube[] cubes = new Cube[array.length()];

		for (int i = 0; i < array.length(); i++)
		{
			JSONObject c = array.getJSONObject(i);
			AABBf aabb = createAABB(c);

			// Rotate cube
			Matrix4f rotMat = new Matrix4f();
			rotMat.translate(0.5f, 0, 0.5f);
			//noinspection IntegerDivisionInFloatingPointContext
			rotMat.rotate((float) Math.toRadians((rot / 90) * 90), 0, 1, 0);
			rotMat.translate(-0.5f, 0, -0.5f);
			aabb.transform(rotMat);

			Cube cube = new Cube(aabb);
/*
			if (c.has("faces"))
			{
				JSONObject faces = c.getJSONObject("faces");

				switch ((rot / 90) * 90)
				{
					case 90 -> {
						if (faces.has(EnumFace.UP.getName())) face(faces.getJSONObject(EnumFace.UP.getName()), EnumFace.UP, cube, rot);
						if (faces.has(EnumFace.DOWN.getName())) face(faces.getJSONObject(EnumFace.DOWN.getName()), EnumFace.DOWN, cube, rot);
						if (faces.has(EnumFace.NORTH.getName())) face(faces.getJSONObject(EnumFace.NORTH.getName()), EnumFace.WEST, cube, rot);
						if (faces.has(EnumFace.EAST.getName())) face(faces.getJSONObject(EnumFace.EAST.getName()), EnumFace.NORTH, cube, rot);
						if (faces.has(EnumFace.SOUTH.getName())) face(faces.getJSONObject(EnumFace.SOUTH.getName()), EnumFace.EAST, cube, rot);
						if (faces.has(EnumFace.WEST.getName())) face(faces.getJSONObject(EnumFace.WEST.getName()), EnumFace.SOUTH, cube, rot);
					}
					case 180 -> {
						if (faces.has(EnumFace.UP.getName())) face(faces.getJSONObject(EnumFace.UP.getName()), EnumFace.UP, cube, rot);
						if (faces.has(EnumFace.DOWN.getName())) face(faces.getJSONObject(EnumFace.DOWN.getName()), EnumFace.DOWN, cube, rot);
						if (faces.has(EnumFace.NORTH.getName())) face(faces.getJSONObject(EnumFace.NORTH.getName()), EnumFace.SOUTH, cube, rot);
						if (faces.has(EnumFace.EAST.getName())) face(faces.getJSONObject(EnumFace.EAST.getName()), EnumFace.WEST, cube, rot);
						if (faces.has(EnumFace.SOUTH.getName())) face(faces.getJSONObject(EnumFace.SOUTH.getName()), EnumFace.NORTH, cube, rot);
						if (faces.has(EnumFace.WEST.getName())) face(faces.getJSONObject(EnumFace.WEST.getName()), EnumFace.EAST, cube, rot);
					}
					case 270 -> {
						if (faces.has(EnumFace.UP.getName())) face(faces.getJSONObject(EnumFace.UP.getName()), EnumFace.UP, cube, rot);
						if (faces.has(EnumFace.DOWN.getName())) face(faces.getJSONObject(EnumFace.DOWN.getName()), EnumFace.DOWN, cube, rot);
						if (faces.has(EnumFace.NORTH.getName())) face(faces.getJSONObject(EnumFace.NORTH.getName()), EnumFace.EAST, cube, rot);
						if (faces.has(EnumFace.EAST.getName())) face(faces.getJSONObject(EnumFace.EAST.getName()), EnumFace.SOUTH, cube, rot);
						if (faces.has(EnumFace.SOUTH.getName())) face(faces.getJSONObject(EnumFace.SOUTH.getName()), EnumFace.WEST, cube, rot);
						if (faces.has(EnumFace.WEST.getName())) face(faces.getJSONObject(EnumFace.WEST.getName()), EnumFace.NORTH, cube, rot);
					}
					default -> {
						for (EnumFace ef : EnumFace.getFaces())
						{
							if (faces.has(ef.getName()))
							{
								face(faces.getJSONObject(ef.getName()), ef, cube, rot);
							}
						}
					}
				}
			}
*/
			cube.loadFromJson(c);
			cubes[i] = cube;
		}

		return cubes;
	}

	public IElement[] loadElements(String path, int rot, boolean uvLock)
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
				addRot(triObj, rot);
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
				addRot(planeObj, rot);
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
				if (rot != 0)
				{
					addRot(cubeObj, rot);
					//TODO: add uvLock
//					if (rot % 90 == 0)
//					{
//						if (cubeObj.has("faces"))
//						{
//							for (EnumFace f : EnumFace.getFaces())
//							{
//								if (cubeObj.getJSONObject("faces").has(f.getName()))
//								{
//									addRot(cubeObj.getJSONObject("faces").getJSONObject(f.getName()), rot);
//								}
//							}
//						}
//					}
				}
				CubeElement el = new CubeElement();
				el.load(cubeObj);
				if (rot != 0 && rot % 90 == 0)
				{
					for (int j = 0; j < (rot % 360) / 90; j++)
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

	private static void fillMissingProperties(CubeFace face)
	{
		for (String key : FaceRegistry.getKeys())
		{
			if (!face.hasProperty(key))
			{
				face.addProperty(FaceRegistry.createProperty(key));
			}
		}
	}

	private static void face(JSONObject faceJson, EnumFace face, Cube cube, int baseRotation)
	{
		CubeFace cf = new CubeFace(cube, face);
		cf.loadFromJSON(faceJson);

		if (!cf.hasProperty(FaceRegistry.uv))
			cf.addProperty(new UVFaceProperty());

		if (AutoUVFaceProperty.check(cf))
		{
			cf.getProperty(FaceRegistry.uv).autoUV(cube, face);
		}

		if (!face.isSide() && !FaceRegistry.uvlock.getInstance().check(cf))
		{
			if (cf.hasProperty(FaceRegistry.rotation))
			{
				RotationFaceProperty rot = cf.getProperty(FaceRegistry.rotation);
				rot.setRotation(rot.getRotation() + baseRotation);
			} else
			{
				RotationFaceProperty rot = FaceRegistry.rotation.createNew();
				rot.setRotation(baseRotation);
				cf.addProperty(rot);
			}
		}

		if (cf.hasProperty(FaceRegistry.texture))
		{
			TextureFaceProperty texture = cf.getProperty(FaceRegistry.texture);
			if (!texture.isReference())
			{
				BlockTextureHolder.putTexture(texture.getTexture());
				texture.setTextureId(BlockTextureHolder.getTextureId(texture.getTexture()));
			}
		} else
		{
			if (cf.hasProperty(FaceRegistry.conditionedTexture))
			{
				ConditionFaceProperty cfp = cf.getProperty(FaceRegistry.conditionedTexture);
				cfp.loadTextures();
			}
			TextureFaceProperty texture = new TextureFaceProperty();
			texture.setTexture("null");
			BlockTextureHolder.putTexture("null");
			texture.setTextureId(BlockTextureHolder.getTextureId(texture.getTexture()));
			cf.addProperty(texture);
		}

		cube.setFace(face, cf);
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
