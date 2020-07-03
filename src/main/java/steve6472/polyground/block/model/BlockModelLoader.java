package steve6472.polyground.block.model;

import org.joml.AABBf;
import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.BlockTextureHolder;
import steve6472.polyground.block.model.faceProperty.AutoUVFaceProperty;
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
	public List<Cube> loadModel(String name)
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
			return createFromParent(json);
		}

		try
		{
			return loadCubes(json);
		} catch (Exception e)
		{
			System.err.println("Loading cubes failed while loading  " + name);
			e.printStackTrace();
			CaveGame.getInstance().exit();
			System.exit(0);
			return null;
		}
	}

	private List<Cube> loadCubes(JSONObject json)
	{
		List<Cube> cubeList = new ArrayList<>();

		if (!json.has("cubes"))
			return cubeList;

		JSONArray array = json.getJSONArray("cubes");
		for (int i = 0; i < array.length(); i++)
		{
			JSONObject c = array.getJSONObject(i);
			AABBf aabb = JsonHelper.createAABB(c);
			Cube cube = new Cube(aabb);

			if (c.has("faces"))
			{
				JSONObject faces = c.getJSONObject("faces");
				for (EnumFace ef : EnumFace.getFaces())
				{
					face(faces, ef, cube);
				}
			}

			cube.loadFromJson(c);
			cubeList.add(cube);
		}

		return cubeList;
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

	private static void face(JSONObject json, EnumFace face, Cube cube)
	{
		if (json.has(face.getName()))
		{
			JSONObject faceJson = json.getJSONObject(face.getName());
			CubeFace cf = new CubeFace(cube, face);
			cf.loadFromJSON(faceJson);

			if (!cf.hasProperty(FaceRegistry.uv))
				cf.addProperty(new UVFaceProperty());

			if (AutoUVFaceProperty.check(cf))
			{
				cf.getProperty(FaceRegistry.uv).autoUV(cube, face);
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
	}

	private List<Cube> createFromParent(JSONObject json)
	{
		List<Cube> parents = loadModel(json.getString("parent"));

		for (Cube cube : parents)
		{
			cube.loadFromParent(json, cube);
		}

		return parents;
	}

	private String read(File f)
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
