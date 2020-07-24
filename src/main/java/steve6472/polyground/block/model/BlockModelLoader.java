package steve6472.polyground.block.model;

import org.joml.AABBf;
import org.joml.Matrix4f;
import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.BlockTextureHolder;
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

	private Cube[] loadCubes(JSONObject json, int rot)
	{
		if (!json.has("cubes"))
			return new Cube[0];

		JSONArray array = json.getJSONArray("cubes");

		Cube[] cubes = new Cube[array.length()];

		for (int i = 0; i < array.length(); i++)
		{
			JSONObject c = array.getJSONObject(i);
			AABBf aabb = JsonHelper.createAABB(c);

			// Rotate cube
			Matrix4f rotMat = new Matrix4f();
			rotMat.translate(0.5f, 0, 0.5f);
			//noinspection IntegerDivisionInFloatingPointContext
			rotMat.rotate((float) Math.toRadians((rot / 90) * 90), 0, 1, 0);
			rotMat.translate(-0.5f, 0, -0.5f);
			aabb.transform(rotMat);

			Cube cube = new Cube(aabb);

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

			cube.loadFromJson(c);
			cubes[i] = cube;
		}

		return cubes;
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

	private List<Cube> createFromParent(JSONObject json)
	{
//		List<Cube> parents = loadModel(json.getString("parent"), 0);
//
//		for (Cube cube : parents)
//		{
//			cube.loadFromParent(json, cube);
//		}
//
//		return parents;
		return null;
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
